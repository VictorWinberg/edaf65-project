function Board(size, mineNumber, canvas, ws) {
  this.column = size;
  this.row = size;
  this.boardSize = this.column * this.row || 64;
  this.numberNotUnveiled = this.boardSize;
  this.mineNumber = mineNumber || this.column;
  this.squares = [];
  this.mines = [];
  this.values = Array.apply(null, new Array(this.boardSize)).map(Number.prototype.valueOf, 0); // Array of zeros

  this.autoFit = function(canvas) {
    /* Try to fit the squares in the Canvas */
    this.padding = ( 1 / 15 ) * canvas.width / ( this.column + 1 );
    this.squareSize = canvas.width - (this.column + 1) * this.padding;
    this.squareSize /= this.column;
  };

  this.hasMine = function(n) {
    /* Check if an integer n is a square where there's a mine */
    return this.mines.indexOf(n) > -1;
  };

  this.addSquare = function() {
    /* Create a square and add it to the board */
    var mine = false;
    var value = 0;
    var x = (this.squareSize + this.padding) * (this.squares.length % this.column) + this.padding;
    var y = (this.squareSize + this.padding) * Math.floor(this.squares.length / this.column) + this.padding;

    this.squares.push(new Square(x, y, mine, this.squareSize, value));
  };

  this.setSquares = function() {
    /* Create all the squares */
    while (this.squares.length < this.boardSize) {
      this.addSquare();
    }
  };

  this.updateSquares = function(z) {
    for (var i = 0; i < this.squares.length; i++) {
      this.squares[i].value = this.values[i];
      this.squares[i].mine = this.hasMine(i);
      if (this.squares[i].value >= 0) {
        this.unveil(i);
      }
    }

    if (this.hasMine(z)) {
      this.gameOver(z, canvas);
    } else {
      this.checkWin(canvas);
    }

    this.draw(canvas);
  };

  this.update = function(x, y, evt, canvas) {
    /* Action to perform based on event received and the coordinates of the mouse */
    var [col, row, z] = this.getSquare(x, y);

    if (this.squares[z]) {
      switch (evt) {
        case "click":
          this.clicked(col, row, z, canvas);
          break;
        case "contextmenu":
          this.squares[z].switchFlag();
          break;
        case "mousemove":
          //this.squares[z].hover();
          break;
        default:
          //console.log("Unusual behaviour: " + evt);
      }
      this.draw(canvas);
    }
  };

  this.explode = function(square, canvas) {
    /* Dispatch custom event "explode" */
    var explode = new CustomEvent("explode", {
      "detail": {
        "x": square.x,
        "y": square.y
      }
    });
    canvas.dispatchEvent(explode);
  };

  this.alertStatus = function(canvas, type) {
    var status = new CustomEvent(type);
    canvas.dispatchEvent(status);
  };

  this.gameOver = function(z, canvas) {
    /* Unveil all the mines of the board */

    for (var i = 0; i < this.mines.length; i++) {
      this.squares[this.mines[i]].unveil();
    }

    this.explode(this.squares[z], canvas);
    this.alertStatus(canvas, "lose");
  };

  this.checkWin = function(canvas) {
    if (this.mineNumber === this.numberNotUnveiled) {
      this.alertStatus(canvas, "win");
    }
  };

  this.unveil = function(z) {
    /* A square can be unveiled if there's no flag, and it's not already unveiled */
    if (!this.squares[z].flag && !this.squares[z].isUnveiled) {
      this.squares[z].unveil();
      this.numberNotUnveiled--;
      return true;
    }

    return false;
  };

  this.clicked = function(col, row, z, canvas) {
    /* Define how the board reacts when it's clicked */
    this.unveil(z);
    ws.send('/pick ' + (col + 1) + ' ' + (row + 1));
  };

  this.draw = function(canvas) {
    /* Drawing the state of the board */
    var ctx = canvas.getContext("2d");

    //Clean up the board
    ctx.fillStyle = "#FFF";
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);

    //The board will ask each square to draw itself
    for (var i = 0; i < this.squares.length; i++) {
      this.squares[i].draw(canvas);
    }
  };

  this.getSquare = function(x, y) {
    /* Give the square number of a given position (x, y) */
    var column,
      row;
    var square = null;

    if (x <= (this.squareSize + this.padding) * this.column && y <= (this.squareSize + this.padding) * this.column) {
      column = Math.floor(x / (this.squareSize + this.padding));
      row = Math.floor(y / (this.squareSize + this.padding));
      square = row * this.column + column;
    }

    return [column, row, square];
  };

  this.autoFit(canvas);
  this.setSquares();
}
