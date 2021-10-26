export default class ContactsSelection{
   constructor(){
       this.helpTextVisible=true;
       this.list={};
       this.newConversationBtn = document.getElementById("new-conversation-btn");
       this.delContactBtn = document.getElementById("del-contact-btn");
   }

   toggle(id, obj){
      if( this.list[id]==undefined ) {
          obj.classList.add("contact-selected");
          this.list[id]=obj;
          if( Object.keys(this.list).length==1 ) {
               this._enableContactButtons();
          }
      } else {
          obj.classList.remove("contact-selected");
		  this.remove(id);
      }
   }

   remove(id) {
	  delete this.list[id];
      if(  Object.keys(this.list).length==0 ) {
          this._disableContactButtons();
      }
   }

   _enableContactButtons() {
      this.newConversationBtn.classList.remove("disabled");
      this.delContactBtn.classList.remove("disabled");
   }

   _disableContactButtons() {
      this.newConversationBtn.classList.add("disabled");
      this.delContactBtn.classList.add("disabled");
   }
   
   clear() {
	  for( let key in this.list ) {
		  this.list[key].classList.remove("contact-selected");
	  }
	  if(  Object.keys(this.list).length>0 ) {
          this._disableContactButtons();
      }
	  this.list={};
   }
   
}