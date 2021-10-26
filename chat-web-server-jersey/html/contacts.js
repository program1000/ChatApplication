import ContactsSelection from './contacts-selection.js';
import ConversationDialogs from './conversation-dialogs.js';
import sendData from './sender.js';
import validate from './validate.js';
import ErrorBox from './error-box.js';

export default class Contacts{
   constructor(){
      this.tokenInviteInput = document.getElementById('contact-token-text');
      this.newContactTokenInput = document.getElementById('add-token-text');
      this.contactList = document.getElementById('contact-list');
      this.contactListDefaultItem = this.addHelpContactItem();

      document.getElementById('contact-token-btn').onclick=this.generateInvite.bind(this);
      document.getElementById('add-token-btn').onclick=this.addContactFromToken.bind(this);
      document.getElementById('new-conversation-btn').onclick=this.newConversation.bind(this);
      document.getElementById('del-contact-btn').onclick=this.removeContacts.bind(this);
	  
	  this.errorBox = new ErrorBox( document.getElementById('error-div'), document.getElementById('error-txt') );
	  this.contactsSelection = new ContactsSelection();
	  this.convDlgs = new ConversationDialogs( this.getName.bind(this), this.errorBox );
	  this.contactErrorBox = new ErrorBox( document.getElementById('contact-error-div'), document.getElementById('contact-error-txt') );
	  
	  this.idMap = {};
   }
   
   addHelpContactItem() {
	   var listItem = document.createElement('li');
	   listItem.classList.add('list-group-item');
	   listItem.id='contact-default-item';
	   var small = document.createElement('small');
       small.appendChild(document.createTextNode("Click 'Modify contacts' button to add contact"));
	   listItem.appendChild(small);
	   this.contactList.appendChild(listItem);
	   return listItem;
   }

   addContacts(list) {
	   for( let contact of list ) {
		   this.addContact(contact);
	   }
   }

   generateInvite(event) {
     event.preventDefault();
     sendData( '../contact/invite', '', this.showInvite.bind(this), {'session':true, 'error':this.contactErrorBox} );
   }

   showInvite( data ) {
     this.tokenInviteInput.value=data;
   }

   addContactFromToken(event) {
     event.preventDefault(); 
     var token = this.newContactTokenInput.value;
     sendData( '../contact/new',token, this.addContactOk.bind(this), {'id':true,'session':true,'error':this.contactErrorBox} );
   }
   
   addContactOk(result) {
	   result = validate( result, this.contactErrorBox, {'test':'id'} );
	   if( result!=undefined ) {
	       this.addContact(result);
		   this.contactErrorBox.ok('Contact added');
	   }
	   this.newContactTokenInput.value='';
   }

   addContact(result) {
     if( this.contactsSelection.helpTextVisible ) {
       this.contactListDefaultItem.remove();
       this.contactsSelection.helpTextVisible=false;
     }

     var listItem = document.createElement('li');
     listItem.classList.add('list-group-item');
     var rowDiv = document.createElement('div');
     rowDiv.classList.add('row');
     rowDiv.classList.add('align-items-center');
     var leftColDiv = document.createElement('div');
     leftColDiv.classList.add('col-md-1');
     var rightColDiv = document.createElement('div');
     rightColDiv.classList.add('col-md-auto');
     rightColDiv.classList.add('contact-item');

     var statusDiv = document.createElement('div');
     statusDiv.classList.add('border');
     statusDiv.classList.add('rounded-3');
     statusDiv.style['width']='1rem';
     statusDiv.style['height']='1rem';
     var linkNode = document.createElement('a');
     linkNode.href='#';
     var textNode = document.createTextNode(result['name']);
     linkNode.appendChild(textNode);
     rightColDiv.appendChild(linkNode);
     leftColDiv.appendChild(statusDiv);
     rowDiv.appendChild(leftColDiv);
     rowDiv.appendChild(rightColDiv);
     listItem.appendChild(rowDiv);
	 var self = this;
     listItem.onclick=function(e) {
        e.preventDefault();
        self.contactsSelection.toggle(result['id'], listItem );
     };
     this.contactList.appendChild(listItem);
	 this.idMap[result['id']]=result['name'];
  }

  removeContacts(event) {
    event.preventDefault();
	for (let key in this.contactsSelection.list) {
       sendData( '../contact/del', key, this.removeContactsOk.bind(this), {'param':{'key':key,
	                                                                                'obj':this.contactsSelection.list[key]},
																			'id':true,
																			'session':true,
																			'error':this.errorBox} );
    }
  }

  removeContactsOk(result, param) {
    param['obj'].onclick=null;
    this.contactList.removeChild(param['obj']);
    this.contactsSelection.remove(param['key']);
	delete this.idMap[param['key']];
  }

  newConversation(event) {
    event.preventDefault();
	if ( this.convDlgs.canCreateNew()==false ) {
		return;
	}
	var ids = Object.keys(this.contactsSelection.list);
	var names = [];
	for (let id of ids) {
		names.push(this.idMap[id]);
	}
	sendData( '../conversation/new',{'contactIds':ids}, this.newConversationOk.bind(this), {'param':names,
	                                                                                        'id':true,
																							'json':true,
																							'error':this.errorBox } );
  }
  
  getName( id ) {
	return this.idMap[id];
  }
  
  newConversationOk( data, names ) {
    this.convDlgs.create( data, names );
  }
  
  listenToServer() {
	this.convDlgs.listenToServer();
  }
  
  clear() {
	 this.convDlgs.clear();
	 this.contactsSelection.clear();
	 while ( this.contactList.children.length > 0 ) {
	    let listItem = this.contactList.children[0];
	 	listItem.onclick=null;
		listItem.remove();
	 }
	 this.idMap = {};
  }
}