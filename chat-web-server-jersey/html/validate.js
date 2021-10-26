export default function validate( response, errOutput, options={} ) {
	var result=undefined;
	try{
		result=JSON.parse( response );
	} catch ( e ) {
		errOutput.err('Cannot parse response from server' );
		return undefined;
	}
	var testKey = options['test'];
	if ( testKey!=undefined && result[testKey]==undefined ) {
		var warning = result['warning'];
		if ( warning !=undefined ) {
			errOutput.warning( warning );
		    return undefined;
		}
		var reason = result['reason'];
		if ( reason == undefined ) {
			reason = 'Unexpected response!';
		}
		errOutput.err( reason );
		return undefined;
	}
	return result;
}
