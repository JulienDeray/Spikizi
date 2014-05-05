$(document).ready(function () {

    setTimeout( openSSEConnection, 400 );
    putUserIds();

});

function putUserIds() {
    $('.user').each(
        function(id, user){
            user.children[0].textContent = id + 1;
        }
    )
}