import sendData from './sender.js';
import state from './status.js';

export default class MessageListener{
	
	constructor( getNameFunction, createFunction ){
		this.convIds={};
		this.registered=false;
		this.evtSource=undefined;
		this.getName = getNameFunction;
		this.create = createFunction;
	}
	
	register(){
		if( this.registered===false) {
		  sendData( "../message/request", '', this.registerOk.bind(this), {'id':true});
		  this.registered=true;
		}
	}
	
	registerOk( token ) {
      this.evtSource = new EventSource("../message/register");
      //evtSource.onmessage = function(event) {}
      this.evtSource.onerror = function(err) {
        console.error("EventSource failed:", err);
        this.close();
      };

	  var self = this;
	  this.evtSource.addEventListener("message-"+state['user'], function(e) {
          var data = JSON.parse(e.data);
		  var name = self.getName(data['sender']);
          var content = name+": "+data['content']+"\n";
		  var convId = data['convId'];
		  var textArea = self.convIds[convId];
		  if ( textArea == undefined ) {
			  sendData( "../message/contacts", '', self.createNewConversationOk.bind(self), {'id':true,'conv':convId, 
			  'param':{'convId':convId,'content':content}});
		  } else {
			  self.addContent( textArea, content ); 
		  }
      });
      this.evtSource.addEventListener("token", function(e) {
        let obj = {'requestToken':token,
	               'responseToken':JSON.parse(e.data)['token']};
        sendData( "../message/accept", obj, self.acceptOk.bind(self), {'id':true,'session':true,'json':true} );
      });
    }
	
	createNewConversationOk( ids, data ) {
		var ids = JSON.parse( ids );
		var convId = data['convId'];
		var names = [];
		for( let id of ids['contactIds'] ) {
			names.push( this.getName(id) );
		}
		this.create( convId, names );
		var textArea = this.convIds[convId];
		this.addContent( textArea, data['content'] ); 
	}
	
	addContent( textArea, content ) {
		textArea.value+=content;
		textArea.scrollTop=textArea.scrollHeight;
	}

    acceptOk( result ) {
    }
	
	addConversation( convId, textArea ) {
	    this.convIds[convId] = textArea;
	}
	
	remove( convId ) {
		delete this.convIds[convId];
	}
	
	stop() {
		if (this.evtSource!=undefined) {
			this.evtSource.close();
			this.convIds={};
			this.registered=false;
		}
	}
}