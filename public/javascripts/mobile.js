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
        var pageUser = $('#name').attr('name');
        console.log( "Update : " + data.command);

        switch( data.command ) {
            case "setWaiting":
                var userName = data.userName;
                if (userName == pageUser) {
                    var r = jsRoutes.controllers.Mobile.waitingButton();
                    $.get(r.url, function( data ) {
                        $('#ask-speech').replaceWith( data );
                        enableNoSpeechButton();
                    });
                    setWaitingSymbole();
                }
                break;

            case "setPassive":
                var userName = data.userName;
                if (userName == pageUser) {
                    var r = jsRoutes.controllers.Mobile.passiveButton();
                    $.get(r.url, function( data ) {
                        $('#ask-speech').replaceWith(data);
                        enableAskSpeechButton();
                    });
                    setPassiveSymbole();
                }
                break;

            case "setSpeaker":
                var userName = data.userName;
                if (userName == pageUser) {
                    var r = jsRoutes.controllers.Mobile.speakingButton();
                    $.get(r.url, function( data ) {
                        $('#ask-speech').replaceWith( data );
                        enableStopSpeakingButton();
                    });
                    setSpeakingSymbole();
                }
                break;

            case "removeSpeaker":
                var userName = data.userName;
                if (userName == pageUser) {
                    var r = jsRoutes.controllers.Mobile.passiveButton();
                    $.get(r.url, function( data ) {
                        $('#ask-speech').replaceWith(data);
                        enableAskSpeechButton();
                    });
                    setPassivePostSpeakingSymbole();
                }
                break;
        }
    }
}

function setPassiveSymbole() {
    var waiting = $('#speak-waiting');
    waiting.toggleClass('speak-symbole-on', false);
    waiting.toggleClass('speak-symbole-off', true);
    $('#speak-passive').toggleClass('speak-symbole-on', true);
}

function setWaitingSymbole() {
    var passive = $('#speak-passive');
    passive.toggleClass('speak-symbole-on', false);
    passive.toggleClass('speak-symbole-off', true);
    $('#speak-waiting').toggleClass('speak-symbole-on', true);
}

function setSpeakingSymbole() {
    var passive = $('#speak-passive');
    passive.toggleClass('speak-symbole-on', false);
    passive.toggleClass('speak-symbole-off', true);
    var waiting = $('#speak-waiting');
    waiting.toggleClass('speak-symbole-on', false);
    waiting.toggleClass('speak-symbole-off', true);
    $('#speak-speaking').toggleClass('speak-symbole-on', true);
}

function setPassivePostSpeakingSymbole() {
    var waiting = $('#speak-waiting');
    waiting.toggleClass('speak-symbole-on', false);
    waiting.toggleClass('speak-symbole-off', true);
    var waiting = $('#speak-speaking');
    waiting.toggleClass('speak-symbole-on', false);
    waiting.toggleClass('speak-symbole-off', true);
    $('#speak-passive').toggleClass('speak-symbole-on', true);
}

function enableStopSpeakingButton() {
    $('#ask-speech').click( function() {
        $.ajax(jsRoutes.controllers.Mobile.askSpeechStop())
            .done()
            .fail();
    });
}

function enableNoSpeechButton() {
    $('#ask-speech').click( function() {
        $.ajax(jsRoutes.controllers.Mobile.askSpeechOff())
            .done()
            .fail();
    });
}

function enableAskSpeechButton() {
    $('#ask-speech').click( function () {
        $.ajax(jsRoutes.controllers.Mobile.askSpeechOn())
            .done()
            .fail();
    });
}

$(document).ready(function () {
    setTimeout( openMobileSSEConnection, 400 );
    enableAskSpeechButton();
});
