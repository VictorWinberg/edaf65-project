package server;

public class Mailbox {
  
  private String value;
  
  public Mailbox() {
    value = "Initialized";
  }
  
  public synchronized void set(String value) throws InterruptedException {
    while(this.value != null) {
      wait();
    }
    this.value = value;
    notifyAll();
  }
  
  public synchronized String get() throws InterruptedException {
    while(value == null) {
      wait();
    }
    String temp = value;
    value = null;
    notifyAll();
    return temp;
  }
}