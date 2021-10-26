export default class ErrorBox {
	constructor( targetDiv, targetText ) {
		this.div = targetDiv;
		this.txt = targetText;
	}
	
	err( message ) {
		this.show( message, 'btn-outline-danger' );
	}
	
	warning( message ) {
		this.show( message, 'btn-outline-warning' );
	}
	
	ok( message ) {
		this.show( message, 'btn-outline-success' );
	}

	show( message, type ) {
		this.div.classList.remove('invisible');
		this.txt.value=message;
		this.txt.classList.add(type);
		this.fadeOut(type);
	}
	
	fadeOut( type ){
		var obj = {'d':this.div,'t':this.txt};
		window.setTimeout( function(){
		   var f = function ( op ) {
			   obj['d'].style['opacity']=op;
				op-= 0.1;
				if ( op>0 ) {
					window.setTimeout( function(){
						f( op );
					}, 200);
				} else {
					obj['t'].value='';
					delete obj['d'].style.removeProperty('opacity');
					obj['d'].classList.add('invisible');
					obj['t'].classList.remove(type);
				}
		   };
		   f(1);
	    }, 5000);
		
	}

}