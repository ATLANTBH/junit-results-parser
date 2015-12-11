(function($) {
	            $(document).ready( function() {
				    $( '.tree li' ).each( function() {
				        if( $( this ).children( 'ul' ).length > 0 ) {
				            $( this ).addClass( 'parent' );     
				        }
				    });
				 
				    $( '.tree li.parent > a' ).click( function( ) {
				        $( this ).parent().toggleClass( 'active' );
				        $( this ).parent().children( 'ul' ).slideToggle( 'fast' );
				    });
				    
				})
})(jQuery)