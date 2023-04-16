public class Loop3  extends Thread {

    // Constructor
    public Loop3(String name) {
        super(name);    // 부모클래스에 name 전해주기
    }

    @Override
    public void run() {
        for (int i=1; i<=10000; i++) {
            System.out.println(getName() + "(" + i + ")");

            try {
                sleep(10);
            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) {
        Loop3 t1 = new Loop3("Thread 1");
        Loop3 t2 = new Loop3("Thread 2");
        Loop3 t3 = new Loop3("Thread 3");
        t1.start();
        t2.start();
        t3.start();
    }
}
