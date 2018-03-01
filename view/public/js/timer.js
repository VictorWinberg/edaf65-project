function Timer() {
  /* Create a timer to count the elapsed time in minutes, seconds */

  this.count = function() {
    /* Update the counter value when called */
    var now = new Date();
    var diff = Math.abs(now.getTime() - this.time.getTime());

    var minutes = Math.floor(diff / 60000);
    var seconds = ((diff % 60000) / 1000).toFixed(0);

    this.counter = minutes + ":" + seconds;

    if (seconds === 60) {
      minutes += 1;
      this.counter = ("0" + minutes).slice(-2) + ":00";
    } else {
      this.counter = ("0" + minutes).slice(-2) + ":" + (
      "0" + seconds).slice(-2);
    }
  };

  this.start = function(startTime) {
    /* Set the timer start time and start counting */
    var self = this;
    this.time = new Date(new Date().getTime() + startTime * 1000) || new Date();
    this.timer = setInterval(function() {
      self.count();
    }, 1000);
  };

  this.stop = function() {
    clearInterval(this.timer);
  }
}
