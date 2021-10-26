import state from './status.js';

export default function sendData( target, data, okFunc, options={} ) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 ) {
			var response = this.responseText;
			if (this.status == 200 || this.status == 204) {
				okFunc( response, options['param'] );
			} else if (options['error']!=undefined) {
				if ( response=='' ){
					response='An unexpected server error occurred!';
				}
				options['error'].err(response);
			}
		}
	};
	xhttp.open('POST', target, true);
	if ( options['id']!=undefined ) {
		xhttp.setRequestHeader('x-id',state['user']);
	}
	if ( options['session']!=undefined ) {
		xhttp.setRequestHeader('x-session-id',state['session']);
	}
	if ( options['conv']!=undefined ) {
		xhttp.setRequestHeader('x-conv-id',options['conv']);
	}
	if ( options['json']===true ) {
		xhttp.setRequestHeader('Content-type', 'application/json');
		if ( data instanceof Object ) {
			data = JSON.stringify(data);
		}
	}
 //xhttp.onerror = function () {
 //   if (options['error']!=undefined) {
 //	    options['error'](this.responseText);
 //	 }
 //};
	xhttp.send(data);
}
