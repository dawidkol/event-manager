let stompClient = null;
let currentUserName = '';

function setConnected(connected) {
    document.getElementById('messageInput').disabled = !connected;
}

function connect() {
    const socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        setConnected(true);
        document.getElementById('login').style.display = 'none'; // Ukryj formularz logowania
        document.getElementById('chat').style.display = 'block'; // Pokaż okno czatu

        stompClient.subscribe('/topic/messages', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

document.getElementById('connectButton').addEventListener('click', function () {
    currentUserName = document.getElementById('nameInput').value;
    if (currentUserName) {
        connect();
    } else {
        alert('Proszę podać imię.');
    }
});

function sendMessage(event) {
    event.preventDefault();
    const message = document.getElementById('messageInput').value;

    stompClient.send("/app/chat", {}, JSON.stringify({ 'name': currentUserName, 'message': message }));
    document.getElementById('messageInput').value = '';
}

function showMessage(message) {
    const messagesDiv = document.getElementById('messages');
    const messageElement = document.createElement('div');

    if (message.from === currentUserName) {
        messageElement.classList.add('my-message');
    } else {
        messageElement.classList.add('other-message');
    }

    messageElement.textContent = `${message.from}: ${message.message} (${message.date})`;
    messagesDiv.appendChild(messageElement);
    messagesDiv.scrollTop = messagesDiv.scrollHeight; // Scroll to the bottom
}

document.getElementById('messageForm').addEventListener('submit', sendMessage);
