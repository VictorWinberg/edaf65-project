function removeFromArray(value, array) {
  /* Return a new array without all of the occurences of the specified value */
  var results = [];

  for (var i = 0; i < array.length; i++) {
    // Coord with this.boardsize as value are not available and are removed
    if (array[i] !== value) {
      results.push(array[i]);
    }
  }

  return results;
}

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

  this.north = function(z) {
    /* Return the square north to the "z" one unless we're at the top row */
    if ((0 <= z && z < this.column) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z - this.column;
    }
  };

  this.south = function(z) {
    /* Return the square south to the "z" one unless we're at the bottom row */
    if ((this.boardSize - this.column <= z && z < this.boardSize) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z + this.column;
    }
  };

  this.east = function(z) {
    /* Return the square east to the "z" one unless we're at the eastern row */
    if ((z % this.column === this.column - 1) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z + 1;
    }
  };

  this.west = function(z) {
    /* Return the square west to the "z" one unless we're at the western row */
    if ((z % this.column === 0) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z - 1;
    }
  };

  this.neighbour = function(z) {
    /* Return an array with all available surrounding squares of the *z* input one */
    var coord = [
      this.north(z),
      this.north(this.east(z)),
      this.north(this.west(z)),
      this.south(z),
      this.south(this.east(z)),
      this.south(this.west(z)),
      this.east(z),
      this.west(z)
    ];

    // Coord with this.boardsize as value are not available and are removed
    return removeFromArray(this.boardSize, coord);

  };

  this.setMines = function() {
    /* Create the mines array */

    while (this.mines.length < this.mineNumber) {
      var n = Math.ceil(Math.random() * (this.boardSize - 1));

      if (!this.hasMine(n)) {
        this.mines.push(n);
      }
    }

    // Have the mines in the right order (not mandatory)
    this.mines.sort(function(a, b) {
      return a - b;
    });
  };

  this.setValues = function() {
    /* Generates this.values[] which stores the number that says how many mines are around */
    var coord,
      i,
      j;

    for (i = 0; i <= this.mines.length; i++) {
      //Increment the value for all surrounding squares
      coord = this.neighbour(this.mines[i]);

      for (j = 0; j < coord.length; j++) {
        this.values[coord[j]] += 1;
      }

    }
  };

  this.addSquare = function() {
    /* Create a square and add it to the board */
    var mine = false;
    var value = this.values[this.squares.length];
    var x = (this.squareSize + this.padding) * (this.squares.length % this.column) + this.padding;
    var y = (this.squareSize + this.padding) * Math.floor(this.squares.length / this.column) + this.padding;

    //When using addSquares, the length of the squares moves from 0 to this.boardSize
    if (this.hasMine(this.squares.length)) {
      mine = true;
    }

    this.squares.push(new Square(x, y, mine, this.squareSize, value));
  };

  this.setSquare = function() {
    /* Initialise board values and create the squares */

    // Add the mines
    // this.setMines();

    // Add all the values, the number that say how mines are around
    // this.setValues();

    // Create all the squares
    while (this.squares.length < this.boardSize) {
      this.addSquare();
    }

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
    } else {
      return false;
    }
  };

  this.expand = function(z) {
    var coord,
      j;

    //Unveil the square, if it fits the requirement, we continue with the possible neighbours
    if (this.unveil(z)) {
      if (!this.squares[z].value) {
        coord = this.neighbour(z);

        for (j = 0; j < coord.length; j++) {
          if (!this.squares[coord[j]].isUnveiled) {
            this.expand(coord[j]);
          }

        }
      }
    }
  };

  this.clicked = function(col, row, z, canvas) {
    /* Define how the board reacts when it's clicked */

    // this.expand(z);
    ws.send('/pick ' + (col + 1) + ' ' + (row + 1))

    if (this.squares[z].hasMine()) {
      this.gameOver(z, canvas);
    } else {
      this.checkWin(canvas);

    }
    //*/
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
  this.setSquare();
}
