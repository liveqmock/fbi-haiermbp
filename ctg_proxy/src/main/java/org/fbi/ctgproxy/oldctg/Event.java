package org.fbi.ctgproxy.oldctg;

public class Event {

    private boolean autoReset;
    private boolean raised;

    public Event() {
        this(true);
    }

    public Event(boolean flag) {
        autoReset = true;
        raised = false;
        autoReset = flag;
    }

    public synchronized void waitForEvent()
            throws InterruptedException {
        while (!raised)
            wait();
        if (autoReset)
            raised = false;
    }

    public synchronized void signalEvent() {
        raised = true;
        notifyAll();
    }

    public synchronized void reset() {
        raised = false;
    }
}
