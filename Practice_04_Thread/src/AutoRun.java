public class AutoRun  implements Runnable {

    public AutoRun() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("AutoRun.run()");
    }
}

class Main {
    public static void main(String[] args) {
        AutoRun AutoRun_t1 = new AutoRun(); // main에서 start하지 않아도 자동 시작
        System.out.println("InsideMain()");
    }
}