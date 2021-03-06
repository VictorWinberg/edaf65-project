function random(min, max) {
  /* Generates a random number between max and min */
  return min + Math.random() * (max - min);
}

function randomColor() {
  /* Return a random color */

  var red = "#CC1600";
  var darkOrange = "#D33407";
  var orange = "#E17016";
  var yellow = "#FFA318";
  var gray = "#525252";

  var i = random(0, 100);

  switch (true) {
    case i <= 10:
      return red;
    case i <= 20:
      return darkOrange;
    case i <= 30:
      return orange;
    case i <= 50:
      return yellow;
    default:
      return gray;
  }
}

function randomDirection() {
  /* Return a vector pointing to a random direction */
  var angle = random(0, 360) * Math.PI / 180;
  var direction = {
    x: Math.cos(angle),
    y: Math.sin(angle)
  };

  return direction;
}

function Particle() {
  this.x;
  this.y;
  this.radius;
  this.scale; // for particle reduction
  this.direction = {
    x: 0,
    y: 0
  }; //vector
  this.speed; //speed at which they go to a direction
  this.color;

  this.update = function() {
    /* Update the particle */

    // Particle radius being scaled down
    this.radius -= this.scale / 5;

    if (this.radius < 0) {
      this.radius = 0;
    }

    // moving away from explosion center
    this.x += this.direction.x * this.speed;
    this.y += this.direction.y * this.speed;

  };

  this.draw = function(canvas) {
    /* Draw the particles on the canvas */

    var ctx = canvas.getContext("2d");

    // translates the particle from previous place to new one
    ctx.save();
    ctx.translate(this.x, this.y);
    ctx.scale(this.scale, this.scale);

    // drawing a filled circle in the particle's local space
    ctx.beginPath();
    // arc(x, y, radius, startAngle, endAngle, anticlockwise)
    ctx.arc(0, 0, this.radius, 0, Math.PI * 2, true);
    ctx.closePath();

    ctx.fillStyle = this.color;
    ctx.fill();

    ctx.restore();
  };
}

function Explosion(x, y, number, size, speed) {
  var sizeMax = size || 9;
  var sizeMin = 4;
  var speedMax = speed || 15;
  var speedMin = 7;
  var n = number || 20;
  this.particles = [];

  this.initiate = function() {
    /* Initiate the explosion by creating all particles */

    for (var i = 0; i < n; i++) {
      var p = new Particle();

      p.x = x;
      p.y = y;
      p.radius = random(sizeMin, sizeMax);
      p.scale = random(4, 10);
      p.color = randomColor();
      p.speed = random(speedMin, speedMax);
      p.direction = randomDirection();

      this.particles.push(p);

    }
  };

  this.update = function(canvas) {
    for (var i = 0; i < this.particles.length; i++) {
      this.particles[i].update();
      this.particles[i].draw(canvas);
    }
  };

  //Initiate the explosion when the function is called
  this.initiate();
}
