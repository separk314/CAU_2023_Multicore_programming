
public class ex2_divide_and_conquer {
    private static int NUM_END = 10000;

    public static void main(String[] args) {
        // 배열 만들기
        int[] int_arr = new int[NUM_END];
        for(int i=0; i<NUM_END; i++)    int_arr[i] = i+1;

        int s = sum(int_arr);
        System.out.println("sum = " + s);
    }

    static int sum(int[] arr) {
        SumThreadDNC t = new SumThreadDNC(arr, 0, arr.length);
        t.run();
        return t.ans;
    }
}

class SumThreadDNC extends Thread {
    private static int SEQUENTIAL_CUTOFF = 100;

    int lo, hi;
    int[] arr;
    int ans = 0;    // communicating inputs

    SumThreadDNC(int[] a, int l, int h) {
        lo = l;
        hi = h;
        arr = a;
    }

    @Override
    public void run() {
        if(hi - lo < SEQUENTIAL_CUTOFF) {
            for (int i = lo; i < hi; i++)
                ans += arr[i];
        } else {
            SumThreadDNC left = new SumThreadDNC(arr, lo, (hi+lo)/2);
            SumThreadDNC right = new SumThreadDNC(arr, (hi+lo)/2, hi);

            left.start();
            right.start();

            try {
                left.join();
                right.join();
                ans = left.ans + right.ans;
            } catch (InterruptedException e) {}
        }
    }
}