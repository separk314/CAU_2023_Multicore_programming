public class RunnableMain {
    public static void main(String[] args) {
        MyRunnable myrun = new MyRunnable();
        Thread t1 = new Thread(myrun);  // Thread의 target: myrun
        t1.start();
        System.out.println("InsideMain()");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("MyRunnable.run()");
    }
}