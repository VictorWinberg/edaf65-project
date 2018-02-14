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

function Board(size, mineNumber, canvas) {
  this.column = size;
  this.row = size;
  this.boardSize = this.column * this.row || 64;
  this.numberNotUnveiled = this.boardSize;
  this.mineNumber = mineNumber || this.column;
  this.zones = [];
  this.mines = [];
  this.values = Array.apply(null, new Array(this.boardSize)).map(Number.prototype.valueOf, 0); // Array of zeros

  this.autoFit = function(canvas) {
    /* Try to fit the squares in the Canvas */
    this.padding = ( 1 / 15 ) * canvas.width / ( this.column + 1 );
    this.zoneSize = canvas.width - (this.column + 1) * this.padding;
    this.zoneSize /= this.column;
  };

  this.hasMine = function(n) {
    /* Check if an integer n is a zone where there's a mine */
    return this.mines.indexOf(n) > -1;
  };

  this.north = function(z) {
    /* Return the zone north to the "z" one unless we're at the top row */
    if ((0 <= z && z < this.column) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z - this.column;
    }
  };

  this.south = function(z) {
    /* Return the zone south to the "z" one unless we're at the bottom row */
    if ((this.boardSize - this.column <= z && z < this.boardSize) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z + this.column;
    }
  };

  this.east = function(z) {
    /* Return the zone east to the "z" one unless we're at the eastern row */
    if ((z % this.column === this.column - 1) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z + 1;
    }
  };

  this.west = function(z) {
    /* Return the zone west to the "z" one unless we're at the western row */
    if ((z % this.column === 0) || z === this.boardSize) {
      return this.boardSize;
    } else {
      return z - 1;
    }
  };

  this.neighbour = function(z) {
    /* Return an array with all available surrounding zones of the *z* input one */
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
      //Increment the value for all surrounding zones
      coord = this.neighbour(this.mines[i]);

      for (j = 0; j < coord.length; j++) {
        this.values[coord[j]] += 1;
      }

    }
  };

  this.addZone = function() {
    /* Create a zone and add it to the board */
    var mine = false;
    var value = this.values[this.zones.length];
    var x = (this.zoneSize + this.padding) * (this.zones.length % this.column) + this.padding;
    var y = (this.zoneSize + this.padding) * Math.floor(this.zones.length / this.column) + this.padding;

    //When using addZones, the length of the zones moves from 0 to this.boardSize
    if (this.hasMine(this.zones.length)) {
      mine = true;
    }

    this.zones.push(new Zone(x, y, mine, this.zoneSize, value));
  };

  this.setZone = function() {
    /* Initialise board values and create the zones */

    // Add the mines
    this.setMines();

    // Add all the values, the number that say how mines are around
    this.setValues();

    // Create all the zones
    while (this.zones.length < this.boardSize) {
      this.addZone();
    }

  };

  this.update = function(x, y, evt, canvas) {
    /* Action to perform based on event received and the coordinates of the mouse */
    var z = this.getZone(x, y);

    if (this.zones[z]) {
      switch (evt) {
        case "click":
          this.clicked(z, canvas);
          break;
        case "contextmenu":
          this.zones[z].switchFlag();
          break;
        case "mousemove":
          //this.zones[z].hover();
          break;
        default:
          //console.log("Unusual behaviour: " + evt);
      }
      this.draw(canvas);
    }
  };

  this.explode = function(zone, canvas) {
    /* Dispatch custom event "explode" */
    var explode = new CustomEvent("explode", {
      "detail": {
        "x": zone.x,
        "y": zone.y
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
      this.zones[this.mines[i]].unveil();
    }

    this.explode(this.zones[z], canvas);
    this.alertStatus(canvas, "lose");
  };

  this.checkWin = function(canvas) {
    if (this.mineNumber === this.numberNotUnveiled) {
      this.alertStatus(canvas, "win");
    }
  };

  this.unveil = function(z) {
    /* A zone can be unveiled if there's no flag, and it's not already unveiled */
    if (!this.zones[z].flag && !this.zones[z].isUnveiled) {
      this.zones[z].unveil();
      this.numberNotUnveiled--;
      return true;
    } else {
      return false;
    }
  };

  this.expand = function(z) {
    var coord,
      j;

    //Unveil the zone, if it fits the requirement, we continue with the possible neighbours
    if (this.unveil(z)) {
      if (!this.zones[z].value) {
        coord = this.neighbour(z);

        for (j = 0; j < coord.length; j++) {
          if (!this.zones[coord[j]].isUnveiled) {
            this.expand(coord[j]);
          }

        }
      }
    }
  };

  this.clicked = function(z, canvas) {
    /* Define how the board reacts when it's clicked */

    this.expand(z);

    if (this.zones[z].hasMine()) {
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

    //The board will ask each zone to draw itself
    for (var i = 0; i < this.zones.length; i++) {
      this.zones[i].draw(canvas);
    }
  };

  this.getZone = function(x, y) {
    /* Give the zone number of a given position (x, y) */
    var column,
      row;
    var zone = null;

    if (x <= (this.zoneSize + this.padding) * this.column && y <= (this.zoneSize + this.padding) * this.column) {
      column = Math.floor(x / (this.zoneSize + this.padding));
      row = Math.floor(y / (this.zoneSize + this.padding));
      zone = row * this.column + column;
    }

    return zone;
  };

  this.autoFit(canvas);
  this.setZone();
}
