window.onload = function() {
  /* Main of the program, defines what is being done when the page loads */
  var body = document.getElementById("game");
  var canvas = createCanvas(body); //Used also in square.js and board.js to draw the game
  var ctx = canvas.getContext("2d");

  addListener(canvas, "mousemove");
  addListener(canvas, "click");
  addListener(canvas, "contextmenu");
  addExplodeListener(canvas);
  addCustomAlertListener(canvas);
  setTimer("timer");

  function startGame(size, mines) {
    document.getElementById("game").style.display = "block";
    document.getElementById("game-info").style.display = "block";
    document.getElementById("left").style.display = "block";
    document.getElementById("right").style.display = "block";
    document.getElementById("time").style.display = "block";

    setup(size, mines, canvas);
  }

  // Get references to elements on the page.
  var form = document.getElementById('message-form');
  var messageField = document.getElementById('message');
  var messagesList = document.getElementById('messages');
  var socketStatus = document.getElementById('status');
  var closeBtn = document.getElementById('close');

  // Fix messagesList height
  var offset = messagesList.offsetTop + form.offsetHeight;
  if (window.screen.width < 768) {
    messagesList.style.height = "calc(100vh - " + offset + "px - 7em)";
  } else {
    messagesList.style.height = "calc(100vh - " + offset + "px - 4em)";
  }

  // Create a new WebSocket.
  var wsurl = `${location.protocol === "http:" ? "ws" : "wss"}://${location.host}/websocket`;
  var ws = new WebSocket(wsurl);

  // Handle any errors that occur.
  ws.onerror = function(error) {
    console.log('WebSocket Error: ' + error);
  };

  // Show a connected message when the WebSocket is opened.
  ws.onopen = function(event) {
    socketStatus.innerHTML = 'Connected to: ' + event.currentTarget.url;
    socketStatus.className = 'open';
  };

  // Handle messages sent by the server.
  ws.onmessage = function(event) {
    var input = event.data;
    if (input.charAt(0) === '/') {
      var args = input.split(/ /);
      switch (args[0]) {
        case "/play":
          startGame(parseInt(args[1]), parseInt(args[2]));
          break;
      }
    }

    var message = input.replace(/\n/g, "<br />");
    messagesList.innerHTML += '<li class="received"><span>Received:</span>' + message + '</li>';
    messagesList.scrollTop = Math.max(messagesList.scrollHeight, messagesList.scrollTop);
  };

  // Show a disconnected message when the WebSocket is closed.
  ws.onclose = function(event) {
    socketStatus.innerHTML = 'Disconnected from WebSocket.';
    socketStatus.className = 'closed';
  };

  // Send a message when the form is submitted.
  form.onsubmit = function(e) {
    e.preventDefault();

    // Retrieve the message from the input.
    var message = messageField.value;

    // Send the message through the WebSocket.
    ws.send(message);

    // Add the message to the messages list.
    messagesList.innerHTML += '<li class="sent"><span>Sent:</span>' + message + '</li>';

    // Scroll down
    messagesList.scrollTop = Math.max(messagesList.scrollHeight, messagesList.scrollTop);

    // Clear out the message field.
    messageField.value = '';

    return false;
  };

  // Close the WebSocket connection when the close button is clicked.
  closeBtn.onclick = function(e) {
    e.preventDefault();

    // Close the WebSocket.
    ws.close();

    return false;
  };
};
