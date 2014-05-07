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
            case "updateUserState":
                var userName = data.userName;
                var r = jsRoutes.controllers.Dashboard.userIcon( userName );
                $.get(r.url, function( data ) {
                    $('#' + userName + ' > .iconState').html( data );
                });
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
    var checkboxs = $("input[type='checkbox']");

    checkboxs.bootstrapSwitch("state", false);
    checkboxs.on('switchChange.bootstrapSwitch', function(event, state) {
        var userId = this.name.substr(0, this.name.length - 9);

        $.ajax(jsRoutes.controllers.Dashboard.newSpeaker(userId))
            .done()
            .fail();

    });

    refreshUserClassement();
    setTimeout( openDashboardSSEConnection, 400 );
});

