public class AutoRun_ThreadIdentity implements Runnable {
    private Thread _me;

    public AutoRun_ThreadIdentity() {
        _me = new Thread(this);
        _me.start();
    }

    @Override
    public void run() {
        if (_me == Thread.currentThread()) {
            System.out.println("AutoRun.run()");
        }
    }
}

class Main2 {
    public static void main(String[] args) {
        AutoRun_ThreadIdentity t1 = new AutoRun_ThreadIdentity(); // printout
        t1.run();   // no printout
        System.out.println("InsideMain()");
    }
}