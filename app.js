const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws-stomp'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
	// subscribing to a broadcast channel
    stompClient.subscribe('/topic/status', (greeting) => {
        showGreeting(JSON.parse(greeting.body));
    });
	 // subscribing to a unicast channel 
	 stompClient.subscribe('/user/queue/status', (greeting) => {
        showGreeting(JSON.parse(greeting.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
	// publish to the channel bound to broadcast group
    stompClient.publish({
        destination: "/app/quote",
        body: JSON.stringify({'clientID': $("#name").val(),'clientCode': $("#code").val(),'model': $("#model").val()})
    });
	// publish to the channel bound to unicast group
	stompClient.publish({
        destination: "/app/message",
        body: JSON.stringify({'clientID': $("#name").val(),'clientCode': $("#code").val(),'model': $("#model").val()})
    });
}

function showGreeting(message) {
    console.log(message);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});
