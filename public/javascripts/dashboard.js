function openDashboardSSEConnection() {
    var pushSource = new WebSocket( 'ws://localhost:9000/admin/dashboard/sse' );

    pushSource.onopen = function(e) {
        console.log("[INFO] Dashboard's SSE channel opened !");
    };

    pushSource.onerror = function(e) {
        if (pushSource.readyState == EventSource.CLOSED) {
            console.log("[INFO] Dashboard's SSE channel closed !");
        } else {
            console.log("[ERROR] Dashboard's SSE channel error : %s", e);
        }
    };

    pushSource.onmessage = function ( event ) {
        var data = JSON.parse( event.data );
        console.log( "Update : " + data.command);

        switch( data.command ) {
            case "newUser":
                $('#userList').append('<tr class="user" id="' + data.userName + '"><td></td><td>' + data.userName + '</td></tr>');
                refreshUserClassement();
                break;
            case "delUser":
                $('#' + data.userName).remove();
                refreshUserClassement();
                break;
        }
    }
}

function refreshUserClassement() {
    $('.user').each(
        function(id, user){
            user.children[0].textContent = id + 1;
        }
    )
}

$(document).ready(function () {
    setTimeout( openDashboardSSEConnection, 400 );
    refreshUserClassement();
});

