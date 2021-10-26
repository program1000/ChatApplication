export default class ConversationDialogMoveHandler{
    constructor(){
        this.mx=0,this.my=0;
        this.contactDiv=document.getElementById("contact-div");
        this.mainElmnt=document.getElementsByTagName("main")[0];
		this.prevDiag=undefined;
    }
  
    dragMouseDown(e, element, textArea) {
        e = e || window.event;
        if ( e.target == undefined || e.target.id == undefined || e.target.id.startsWith('inp-') ) {
           return;
        }
        e.preventDefault();
		this.setCurrent( element );
        this.mx = e.clientX;
        this.my = e.clientY;
        var self = this;
        document.onmouseup = function(e){
            e = e || window.event;
            e.preventDefault();
            self.closeDragElement(element, textArea);
        };
        document.onmousemove = function(e){
            self.elementDrag(e, element);
        };
    }
	
	setCurrent( element ) {
		if ( this.prevDiag!=undefined ) {
			this.prevDiag.style['z-index']='1';
		}
		element.style['z-index']='2';
	}

    elementDrag(e, element) {
        e = e || window.event;
        e.preventDefault();
        var deltax = this.mx - e.clientX;
        var deltay = this.my - e.clientY;
        var newx = element.offsetLeft - deltax;
        var newy = element.offsetTop - deltay;

        if ( newx > 2 && 
             newy > 2 && 
             (newx+element.clientWidth+5 ) < this.contactDiv.getBoundingClientRect().x &&
             (newy+element.clientHeight+5 ) < this.mainElmnt.getBoundingClientRect().height ) {
          this.mx = e.clientX;
          this.my = e.clientY;
          element.style.top = (element.offsetTop - deltay) + "px";
          element.style.left = newx + "px";
        }
    }
	
	offset( element, offset ) {
	   element.style.top = (element.offsetTop + offset) + "px";
       element.style.left = (element.offsetLeft + offset) + "px";
	}

    closeDragElement(element, textArea) {
       this.setBounds(element, textArea);
	   this.prevDiag=element;
       document.onmouseup = null;
       document.onmousemove = null;
    }
	
	setBounds(element, textArea) {
	   textArea.style['max-height']=(this.mainElmnt.getBoundingClientRect().height-(element.offsetTop+167))+"px";
       textArea.style['max-width']=(this.contactDiv.getBoundingClientRect().x-(element.offsetLeft+37))+"px";
	}
	
	clear() {
		if ( document.onmouseup != null) {
			document.onmouseup = null;
		};
		if ( document.onmousemove != null) {
			document.onmousemove = null;
		};
	}
}