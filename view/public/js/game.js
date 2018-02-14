var board;
var timer = new Timer();
var WIN = {
  name: "win",
  img: "../img/trophy.svg",
  text: ["Congratulation!", "br", "you beat the game!"]
};
var LOSE = {
  name: "lose",
  img: "../img/skull.svg",
  text: ["BOOOM !!", "br", "you've exploded!"]
};

/* DOM Modifications */

function createCanvas(body, id, width, height) {
  /* Create a canvas and but it in "body" can be any tagnamed element, id, width and height of teh canvas can be configured */
  var canvas = document.createElement("canvas");

  canvas.id = id || "board";
  canvas.width = width || 516;
  canvas.height = height || 516;
  //canvas.oncontextmenu = "javascript:return false;";

  body.appendChild(canvas);

  return document.getElementById(canvas.id);
}

function updateTextNode(id, text) {
  /* Update text node of the defined id with text */
  var node = document.getElementById(id);
  node.textContent = text || "error"; //Firefox
  node.innerText = text || "error"; //IE
}

function addTextNode(parentId, text, id) {
  /* Add a text node under a parentId with a defined id in the HTML */
  var element = document.createElement("span");
  var p = text || "error";
  var node = document.createTextNode(p);
  var parent = document.getElementById(parentId);

  element.appendChild(node);
  element.id = id || "info";
  // element.classList.add("info");

  parent.appendChild(element);
  //parent.insertBefore(element, parent.childNodes[0]);
}

function removeChildren(parent) {
  /* Remove all children from the id node */
  var p = document.getElementById(parent) || parent;

  while (p.firstChild) {
    p.removeChild(p.firstChild);
  }
}

function addCustomAlert(type) {
  /* Add a custom alert with a text as the message and type win or lose */
  var text;
  var content = document.getElementById("customAlert-content");

  timer.stop();
  removeChildren(content);

  for (var i = 0; i < type.text.length; i++) {
    text = type.text[i];
    if (text === "br") {
      content.appendChild(document.createElement(text));
    } else {
      content.appendChild(document.createTextNode(text));
    }
  }
  document.getElementById("alertContainer").style.visibility = "visible";
  document.getElementById("alert-img").src = type.img;
}

function removeCustomAlert() {
  /* Remove the custom alert */
  document.getElementById("alertContainer").style.visibility = "hidden";
  timer.counter = "00:00";
  setup(document.getElementById("board")); // restart the game, /!\ "board" is default value
}

/* Listeners */

function getMousePos(canvas, evt) {
  /* Return the position of the mouse within the canvas */
  var rect = canvas.getBoundingClientRect();
  return {
    x: Math.round(evt.clientX - rect.left),
    y: Math.round(evt.clientY - rect.top)
  };
}

function addListener(canvas, event) {
  /* Creates the listener to catch events in the canvas */

  canvas.addEventListener(event, function(evt) {
    var pos = getMousePos(canvas, evt);
    evt.preventDefault(); // Prevent the link from updating the URL
    board.update(pos.x, pos.y, evt.type, canvas);
  });
}

function addExplodeListener(canvas) {
  /* Creates the listener and handler for the explode event in the board */

  canvas.addEventListener("explode", function(evt) {
    var explosion = new Explosion(evt.detail.x, evt.detail.y);
    var exploding = setInterval(function() {
      board.draw(canvas);
      explosion.update(canvas);
    }, 1000 / 15);
    // So we don't have too many running
    setTimeout(function() {
      clearInterval(exploding);
    }, 900);
  }, false);
}

function addCustomAlertListener(canvas) {
  canvas.addEventListener(WIN.name, function(evt) {
    addCustomAlert(WIN);
  }, false);

  canvas.addEventListener(LOSE.name, function(evt) {
    setTimeout(function() {
      addCustomAlert(LOSE);
    }, 500);

  }, false);

}

/* Game start up */

function setTimer(id) {
  /* Set the timer and update the html with the specified id */
  setInterval(function() {
    updateTextNode(id, timer.counter);
  }, 1000);
}

function setup(canvas) {
  /* Setup the minesweeper game */
  timer.start();
  board = new Board(12, 20, canvas);
  updateTextNode("mines", "x" + board.mineNumber);
  board.draw(canvas);
}