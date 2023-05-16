import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ex1 {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(2);

        ProducerThread producer = new ProducerThread(queue);
        producer.start();

        ConsumerThread consumer = new ConsumerThread(queue);
        consumer.start();
    }

    static class ConsumerThread extends Thread {
        BlockingQueue queue;

        ConsumerThread(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                Thread.sleep(500);
                System.out.println("\n< ConsumerThread Starts >");

                for(int i=0; i<4; i++) {
                    System.out.println("\n[TAKE] Current Queue: " + queue.toString());
                    System.out.println("[TAKE] Try to take an element");
                    System.out.println("[TAKE] Took an element: " + queue.take());
                    System.out.println("[TAKE] Queue remaining capacity: " + queue.remainingCapacity());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ProducerThread extends Thread {
        BlockingQueue queue;

        ProducerThread(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            System.out.println("< ProducerThread Starts >");
            try {
                System.out.println("\n[PUT] Queue remaining capacity: " + queue.remainingCapacity());
                System.out.println("[PUT] Try to insert 1");
                queue.put(1);
                System.out.println("[PUT] Current Queue: " + queue.toString());

                System.out.println("\n[PUT] Queue remaining capacity: " + queue.remainingCapacity());
                System.out.println("[PUT] Try to insert 2");
                queue.put(2);
                System.out.println("[PUT] Current Queue: " + queue.toString());

                System.out.println("\n[PUT] Queue remaining capacity: " + queue.remainingCapacity());
                System.out.println("[PUT] Try to insert 3");
                queue.put(3);
                System.out.println("[PUT] Current Queue: " + queue.toString());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
