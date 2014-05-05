function openMobileSSEConnection() {
    var pushSource = new WebSocket('ws://localhost:9000/m/sse')

    pushSource.onopen = function (e) {
        console.log("[INFO] Mobile's SSE channel opened !")
    };

    pushSource.onerror = function (e) {
        if (pushSource.readyState == EventSource.CLOSED) {
            console.log("[INFO] Mobile's SSE channel closed !")
        } else {
            console.log("[ERROR] Mobile's SSE channel error : %s", e)
        }
    };

    pushSource.onmessage = function ( event ) {
        var data = JSON.parse( event.data );
        console.log( "Update : " + data.command);

        switch( data.command ) {

        }
    }
}


$(document).ready(function () {
    setTimeout( openMobileSSEConnection, 400 );
});
