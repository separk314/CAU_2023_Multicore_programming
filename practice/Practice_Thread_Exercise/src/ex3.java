class IntThread extends Thread {
    int my_id, num_steps, num_threads;
    double sum;

    IntThread(int id, int numSteps, int numThreads){
        my_id = id;
        num_steps = numSteps;
        num_threads = numThreads;
        sum = 0.0;
    }

    public void run(){
        double x, step;
        int i, i_start, i_end;

        i_start = my_id * (num_steps/num_threads);
        i_end = (my_id + 1) * (num_steps/num_threads);
        step = 1.0 / (double) num_steps;

        for(i=i_start; i<i_end; i++) {
            x = (i+0.5) * step;
            sum += 4.0 / (1+x*x);
        }

        sum = step*sum;
    }
}


class ex3 {
    private static int NUM_STEPS = 100000;
    private static final int NUM_THREAD = 4;

    public static void main(String[] args){
        double sum = 0.0;

        ////////////////////////////////////////////////
        double step = 1/(double)NUM_STEPS;
        long gap = NUM_STEPS/NUM_THREAD;

        IntThread[] threads = new IntThread[NUM_THREAD];

        for(int i=0; i<NUM_THREAD; i++) {
            threads[i] = new IntThread(i, NUM_STEPS, NUM_THREAD);
            threads[i].start();
        }

        try {
            for(int i=0; i<NUM_THREAD; i++) {
                    threads[i].join();
                    sum += threads[i].sum;
            }
        } catch (InterruptedException e) {}

        sum = sum * step;
        ////////////////////////////////////////////////

        System.out.println("integration result = " + sum);
    }
}
