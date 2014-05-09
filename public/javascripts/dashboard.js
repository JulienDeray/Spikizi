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
                var userName = data.userName;
                var r = jsRoutes.controllers.Dashboard.userTemplate( userName );
                $.get(r.url, function( data ) {
                    $('#userList').append( data );
                    initCheckboxs( $('#' + userName + '-checkbox') );
                    refreshUserClassement();
                });
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
                break;
            case "setUserButtonToOff":
                $('#' + data.userName + '-checkbox').bootstrapSwitch("state", false);
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

function initCheckboxs( checkboxs ) {
    checkboxs.bootstrapSwitch("state", false);
    checkboxs.on('switchChange.bootstrapSwitch', function(event, state) {
        var userId = this.name.substr(0, this.name.length - 9);

        if ( state == true ) {
            $.ajax(jsRoutes.controllers.Dashboard.newSpeaker(userId))
                .done()
                .fail();
        }
        else {
            $.ajax(jsRoutes.controllers.Dashboard.removeSpeaker(userId))
                .done()
                .fail();
        }
    });
}

$(document).ready(function () {
    initCheckboxs( $("input[type='checkbox']") );
    refreshUserClassement();
    setTimeout( openDashboardSSEConnection, 400 );
});

