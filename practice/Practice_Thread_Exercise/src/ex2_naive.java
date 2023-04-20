public class ex2_naive {
    private static int NUM_END = 10000;
    private static int NUM_THREAD = 4;

    public static void main(String[] args) {
        // 배열 만들기
        int[] int_arr = new int[NUM_END];
        for(int i=0; i<NUM_END; i++)    int_arr[i] = i+1;

        int s = sum(int_arr);
        System.out.println("sum = " + s);
    }

    static int sum(int[] arr) {
        int len = arr.length;
        int ans = 0;

        SumThreadNaive[] ts = new SumThreadNaive[NUM_THREAD];

        for(int i=0; i<NUM_THREAD; i++) {
            ts[i] = new SumThreadNaive(arr, (i*len)/NUM_THREAD, ((i+1)*len)/NUM_THREAD);
            ts[i].start();
        }

        try {
            for(int i=0; i<NUM_THREAD; i++) {
                ts[i].join();
                ans += ts[i].ans;
            }
        } catch (InterruptedException e) {}

        return ans;
    }
}

class SumThreadNaive extends Thread {
    int lo, hi;
    int[] arr;
    int ans = 0;    // communicating inputs

    SumThreadNaive(int[] a, int l, int h) {
        lo = l;
        hi = h;
        arr = a;
    }

    @Override
    public void run() {
        for(int i=lo; i<hi; i++) {
            ans += arr[i];
        }
    }
}