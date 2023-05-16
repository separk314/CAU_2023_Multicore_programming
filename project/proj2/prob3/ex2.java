import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        ReadThread readThread1 = new ReadThread(lock, 1);
        readThread1.start();

        ReadThread readThread2 = new ReadThread(lock, 2);
        readThread2.start();

        WriteThread writeThread3 = new WriteThread(lock, 3);
        writeThread3.start();

        WriteThread writeThread4 = new WriteThread(lock, 4);
        writeThread4.start();
    }

    static class WriteThread extends Thread {
        ReadWriteLock lock;
        int id;

        WriteThread(ReadWriteLock lock, int id) {
            this.lock = lock;
            this.id = id;
        }

        @Override
        public void run() {
            // writeLock().lock()
            System.out.println("Thread #" +id + ": Try to lock writeLock()");
            lock.writeLock().lock();
            System.out.println("Thread #" +id + ": Locked writeLock()");

            // writeLock().unlock()
            System.out.println("Thread #" +id + ": Try to unlock writeLock()");
            lock.writeLock().unlock();
        }
    }

    static class ReadThread extends Thread {
        ReadWriteLock lock;
        int id;

        ReadThread(ReadWriteLock lock, int id) {
            this.lock = lock;
            this.id = id;
        }

        @Override
        public void run() {
            // readLock().lock()
            System.out.println("Thread #" +id + ": Try to lock readLock()");
            lock.readLock().lock();
            System.out.println("Thread #" +id + ": Locked readLock()");

            // Thread sleeps
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // readLock().unlock()
            System.out.println("Thread #" +id + ": Try to unlock readLock()");
            lock.readLock().unlock();
        }
    }
}
