public class pc_static_cyclic {
    private static int NUM_END = 100; // default input
    private static int NUM_THREADS = 4; // default number of threads
    private static int TASK_SIZE = 10;  // default task size

    public static void main(String[] args) {
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        int totalCounter = 0;
        CyclicThread[] threads = new CyclicThread[NUM_THREADS];

        long startTime = System.currentTimeMillis();    // program execution time starts

        // 스레드 실행
        for(int i=0; i<NUM_THREADS; i++) {
            threads[i] = new CyclicThread(i, NUM_END, NUM_THREADS);
            threads[i].start();
        }

        // 스레드 join()
        for (int i=0; i<NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        // prime number 총 개수 구하기
        for (int i=0; i<NUM_THREADS; i++) {
            totalCounter += threads[i].counter;
        }

        long endTime = System.currentTimeMillis();      // program execution time ends
        long timeDiff = endTime - startTime;

        System.out.println("\n          < RESULT > ");
        for (int i=0; i<NUM_THREADS; i++) {
            System.out.println(i +" Thread: " + threads[i].timeDiff + " ms");
        }
        System.out.println("\nTotal Program Execution Time: " + timeDiff + "ms");
        System.out.println("1... " + (NUM_END - 1) + " prime# counter=" + totalCounter + "\n");
    }
}

class CyclicThread extends Thread {
    private static int TASK_SIZE = 10;  // default task size

    int threadIndex, NUM_END, NUM_THREADS, counter;
    long timeDiff;

    CyclicThread(int threadIndex, int NUM_END, int NUM_THREADS) {
        this.threadIndex = threadIndex;
        this.NUM_END = NUM_END;
        this.NUM_THREADS = NUM_THREADS;
        this.counter = 0;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        int num = threadIndex * 10;
        while (num <= NUM_END) {
            System.out.println("Thread " + threadIndex + " : num " + num);
            for(int i=num; i<num+10; i++) {
                if(isPrime(i))    counter++;
            }

            num += NUM_THREADS * 10;
        }

        long endTime = System.currentTimeMillis();
        timeDiff = endTime - startTime;
    } 

    private static boolean isPrime(int x) {
        if (x<=1)    return false;
        for(int i=2; i<x; i++) {
            if (x%i == 0)   return false;
        }
        return true;
    }
}