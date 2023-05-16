import java.util.concurrent.atomic.AtomicInteger;

public class ex3 {
    public static void main(String[] args) {
        AtomicInteger atomic = new AtomicInteger(10);
        for (int i=1; i<=5; i++) {
            new AtomicIntegerThread(i, atomic);
        }
    }

    static class AtomicIntegerThread extends Thread {
        AtomicInteger atomic;
        int id;

        AtomicIntegerThread(int id, AtomicInteger atomic) {
            this.id = id;
            this.atomic = atomic;
            this.start();
        }

        @Override
        public void run() {
            try {
                sleep((int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            atomic.set(this.id);

            System.out.println("[Thread "+ this.id + "] get(): " + atomic.get() +
                    ", getAndAdd(" + this.id + "): " + atomic.getAndAdd(this.id));
            System.out.println("[Thread "+ this.id + "] get(): " + atomic.get() +
                    ", addAndGet(" + this.id + "): " + atomic.addAndGet(this.id));

        }
    }
}
