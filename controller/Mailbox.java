package controller;

public class Mailbox {

    private Message msg;

    public synchronized void set(Message msg) throws InterruptedException {
        while (this.msg != null) {
            wait();
        }
        this.msg = msg;
        notifyAll();
    }

    public synchronized Message get() throws InterruptedException {
        while (msg == null) {
            wait();
        }
        Message temp = msg;
        msg = null;
        notifyAll();
        return temp;
    }
}

class Message {

    public final User user;
    public final String message;

    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }
}