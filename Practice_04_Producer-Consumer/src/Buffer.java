class Buffer<E> {
    static int SIZE = 10;

    E[] array = (E[])new Object[SIZE];

    synchronized void enqueue(E elt) {
        while (isFull()) {
            try {
                this.wait();
            } catch (InterruptedException e) {}
        }

        // add to array
        if (buffer was empty)
            this.notify();
    }

    synchronized E dequeue() {
        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        // take away
        if(buffer was full)
            notify();
    }

    Boolean isFull() {  return true;    }
    Boolean isEmpty() { return true;    }
}
