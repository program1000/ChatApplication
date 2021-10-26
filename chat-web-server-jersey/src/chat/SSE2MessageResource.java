package chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import chat.message.model.ChatMessage;
import chat.message.model.ConversationMessage;
import chat.message.model.SSEMessage;
import chat.message.model.TokenCreateMessage;
import chat.message.model.TokenMessage;

//Requires JAX-RS 2.1 API


//Protocol
/*
To register for incoming messages:
request register token
-> id
<- token

register
<- (SSE send) tmp token

accept reg
-> tmp token + token
<- delayed messages

To listen for specific conversation:
request conversation token
-> id
<- token

list to conversation
-> token, conv id
< -delayed messages

*/
@Path( "message" )
@Singleton
public class SSE2MessageResource {
    
    private Sse sse;
    private final Map<String, SseEventSink> tmpSinks;
    private final Map<String, SseEventSink> sinks;
    private final Map<String, String> tmpIdMap;

    private final SecurityManagerInterface securityManager;
    private int tmpTokenSeq;
    
    public SSE2MessageResource(@Context final Sse sse) {
        this.sse = sse;
        tmpSinks = new HashMap<String, SseEventSink>();
        sinks = new HashMap<String, SseEventSink>();
        tmpIdMap = new HashMap<String, String>();
        tmpTokenSeq = 1;
        securityManager = SecurityManager.get();
    }
    
    @Path( "request" )
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response requestToken( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId ) {
        if ( viewId==null ) {
            //error
        }
        String token = generateToken( viewId );
        tmpIdMap.put(token,viewId);
        return Response.ok(token).build();
    }
    
    private String generateToken( String viewId ) {
        return viewId+"T";
    }
    
    @Path( "register" )
    @GET
    @Produces( MediaType.SERVER_SENT_EVENTS )
    public void registerServerSentEvents(  @Context SseEventSink eventSink ) {
        String tmpToken = generateTmpToken();
        tmpSinks.put( tmpToken, eventSink );
        new Thread(new Runnable() {
            @Override
            public void run() {
                eventSink.send( createTokenEvent( tmpToken ) );
            }
        }).start();
    }
    
    private String generateTmpToken() {
        String result = Integer.toString( tmpTokenSeq );
        tmpTokenSeq++;
        return result;
    }
    
    @Path( "accept" )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public void acceptRegister( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId, 
                                @HeaderParam( value = SecurityManagerInterface.HEADER_SESSION ) String sessionId, TokenMessage message ) {
        if ( message==null ) {
            //error
//            throw new WebApplicationException("Invalid message",Status.BAD_REQUEST);
        }
        if ( message.requestToken == null ) {
//            throw new WebApplicationException("Invalid message",Status.BAD_REQUEST);
        }
        String tmpId = tmpIdMap.remove( message.requestToken );
        SseEventSink eventSink = tmpSinks.remove( message.responseToken );
        if (viewId ==null || tmpId == null || !viewId.equals( tmpId ) ) {
            //error 
//            throw new WebApplicationException("Invalid message",Status.BAD_REQUEST);
          }
        // check eventSink
        // check already registered
        sinks.put(viewId, eventSink);
        List<String> messages = securityManager.getDelayedMessages( viewId );
        if ( messages !=null ) {
            int len = messages.size();
            for ( int i =0; i<len; i+=3 ) {
                String senderId = messages.get( i );
                String convId = messages.get( i+1 );
                String content = messages.get( i+2 );
                eventSink.send( ( createMessageEvent( content, viewId, senderId, convId ) ) );
            }
        }
    }
    
    @Path( "new" )
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendMessage( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId,
                                 @HeaderParam( value = SecurityManagerInterface.HEADER_CONV_ID ) String convId, String content ) {
        List<String> userViewIds = securityManager.getConversationUserIds( viewId, convId );
        if ( userViewIds == null ) {
            //error
            return Response.status( Status.BAD_REQUEST.getStatusCode(), "No conversation match" ).build();
        }

        String message = content;
        SseEventSink eventSink = null;
        for( String userViewId : userViewIds ) {
            eventSink = sinks.get( userViewId );
            if ( eventSink != null ) {
                eventSink.send( ( createMessageEvent( message, userViewId, securityManager.getContactViewId( userViewId, viewId ), convId ) ) );
            } else {
                securityManager.addDelayedMessage( convId, viewId, userViewId, message );
            }

        }
        securityManager.addMessageToConversation( convId, viewId, message );
        return Response.ok("Send message from key "+viewId).build();
    }
    
    @Path( "contacts")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ConversationMessage getContactIdsFromConversation( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId,
                                                              @HeaderParam( value = SecurityManagerInterface.HEADER_CONV_ID ) String convId ) {
        List<String> userViewIds = securityManager.getConversationContactIds( viewId, convId );
        if ( userViewIds == null ) {
            //error
            throw new WebApplicationException("No conversation match",Status.BAD_REQUEST);
        }
        return new ConversationMessage( userViewIds );
    }
    

    private OutboundSseEvent createMessageEvent(String message, String id, String senderId, String convId ) {
        return createEvent( "message-"+id, new ChatMessage( senderId, message, convId) );
    }
    
    private OutboundSseEvent createTokenEvent( String token ) {
        return createEvent( "token", new TokenCreateMessage( token ) );
    }
    
    private OutboundSseEvent createEvent(String type, SSEMessage message) {
        return sse.newEventBuilder().name( type ).mediaType( MediaType.APPLICATION_JSON_TYPE ).data( message ).build();
    }

    
    @Path( "close" )
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response close ( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId ) {
        int id = Integer.valueOf( viewId );
        SseEventSink eventSink = sinks.remove( viewId );
        String message=null;
        if ( eventSink!=null ) {
            eventSink.close();
            message = "Closed ";
        } else {
            message = "Already closed ";
        }
        return Response.ok(message+id).build();
    }

}
