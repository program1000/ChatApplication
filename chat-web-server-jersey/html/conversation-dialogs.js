import ConversationDialogMoveHandler from './conversation-dialog-move-handler.js';
import MessageListener from './message-listener.js';
import sendData from './sender.js';
import state from './status.js';

export default class ConversationDialogs{
   constructor( getNameFunction, errorBox ){
       this.seq=1;
       this.parent=document.getElementById('conversation-content');
	   this.convDlgHandler=new ConversationDialogMoveHandler();
	   this.messageListener=new MessageListener( getNameFunction, this.create.bind(this) );
	   this.dlgsSeq=[];
	   this.errorBox = errorBox;
	   this.maxDlgs=10;
   }

   getSeq(){
      return this.seq++;
   }

   create( convId, names ){
       var conDiv = document.createElement('div');
       var seq=this.getSeq();
       conDiv.classList.add('conv-div','p-3','bg-light','border','rounded-3');
       conDiv.id='conv-'+seq+'-div';
	   conDiv.style['z-index']='1';
       var closeDiv = document.createElement('div');
       closeDiv.classList.add('d-grid','d-sm-flex','justify-content-sm-end');
       var closeBtn = document.createElement('button');
       closeBtn.classList.add('btn','btn-outline-secondary','btn-sm');
       closeBtn.id='inp-'+seq+'-close';
       closeBtn.type='button';
       closeBtn.appendChild(document.createTextNode('X'));
       closeDiv.appendChild(closeBtn);
       conDiv.appendChild(closeDiv);
       var heading=document.createElement('p');
       heading.appendChild(document.createTextNode('With '+names.join(', ')));
       conDiv.appendChild(heading);
       var taDiv = document.createElement('div');
       taDiv.classList.add('mb-3');
       var textArea = document.createElement('textarea');
       textArea.classList.add('form-control','conv-textarea');
       textArea.rows=4;
       textArea.id='inp-'+seq+'-area';
       textArea.readOnly=true;
       taDiv.appendChild(textArea);
       conDiv.appendChild(taDiv);
       var msgDiv = document.createElement('div');
       msgDiv.classList.add('input-group');
       var textInput = document.createElement('input');
       textInput.classList.add('form-control');
       textInput.id='inp-'+seq+'-text';
       textInput.type='text';
       msgDiv.appendChild(textInput);
       var sendBtn = document.createElement('button');
       sendBtn.classList.add('btn','btn-outline-secondary');
       sendBtn.id='inp-'+seq+'-btn';
       sendBtn.appendChild(document.createTextNode('Send'));
       msgDiv.appendChild(sendBtn);
       conDiv.appendChild(msgDiv);
       this.parent.appendChild(conDiv);
	   this.dlgsSeq.push(seq);

       var self = this;
       conDiv.onmousedown = function(e) {
           self.convDlgHandler.dragMouseDown(e,conDiv,textArea);
       };
	   this.convDlgHandler.setCurrent(conDiv);
       this.convDlgHandler.closeDragElement(conDiv,textArea);
	   this.convDlgHandler.offset(conDiv, 2*seq);

       closeBtn.onclick = function(e) {
           e.preventDefault();
           sendBtn.onclick=null;
           closeBtn.onclick=null;
           conDiv.onmousedown=null;
           self.parent.removeChild(conDiv);  
		   var index = self.dlgsSeq.indexOf( seq );
		   self.dlgsSeq.splice(index,1);
		   self.messageListener.remove( convId );
   		   sendData( '../conversation/close', '', self.closeDiagOk.bind(self), {'id':true,
		                                                                        'conv':convId, 
																				'error':self.errorBox} );
       };

       sendBtn.onclick = function(e) {
           e.preventDefault();
           var message=textInput.value;
           sendData( '../message/new', message, self.okMessage.bind(self), {'id':true,
		                                                                    'conv':convId,
																			'error':self.errorBox,
		                                                                    'param':{'textArea':textArea,'message':message}} );
           textInput.value='';
       };
	   
	   this.messageListener.addConversation( convId, textArea );
   }
   
   canCreateNew() {
	   if ( this.dlgsSeq.length >= this.maxDlgs ){
		   this.errorBox.err( 'Too many open conversations, close a conversation dialog!' );
		   return false;
	   }
       return true;
   }
   
   closeDiagOk( result ) {
	   
   }
   
   okMessage( result, obj ) {
     var r = result;
	 var content = state['name']+': '+obj['message']+"\n";
     obj['textArea'].value+=content;
	 obj['textArea'].scrollTop=obj['textArea'].scrollHeight;
   }
   
   listenToServer() {
	 this.messageListener.register();
   }
   
   clear() {
	   this.messageListener.stop();
	   while( this.dlgsSeq.length>0 ) {
		   let seq = this.dlgsSeq[0];
	   	   var closeBtn = document.getElementById('inp-'+seq+'-close');
		   if ( closeBtn!=undefined ) {
			   closeBtn.click();
		   }
	   }
	   this.dlgsSeq=[];
	   this.convDlgHandler.clear();
	   this.seq=1;
   }

}