import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ex4 {
    public static void main(String[] args) {
        BarrierAction action = new BarrierAction();
        CyclicBarrier barrier = new CyclicBarrier(3, action);

        for(int i=1; i<=6; i++) {
            new CyclicBarrierThread("Thread "+i, barrier);
        }
    }

    static class BarrierAction implements Runnable {
        @Override
        public void run() {
            System.out.println(" ! BarrierAction is executed ! ");
        }
    }

    static class CyclicBarrierThread extends Thread {
        String name;
        CyclicBarrier barrier;

        CyclicBarrierThread(String name, CyclicBarrier barrier) {
            this.name = name;
            this.barrier = barrier;
            this.start();
        }

        @Override
        public void run() {
            try {
                sleep((int)(Math.random() * 10000));
                System.out.println(this.name + ": barrier.await()");
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}