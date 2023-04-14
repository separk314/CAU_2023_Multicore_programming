public class JoinThr {
    public static void main(String[] args) {
        MyThread1 Thread_a;
        MyThread2 Thread_b;

        Thread_a = new MyThread1();
        Thread_b = new MyThread2(Thread_a);

        System.out.println("Starting the threads...");
        Thread_a.start();
        Thread_b.start();
    }
}

// 메세지를 4번 출력하는 스레드
class MyThread1 extends Thread {
    public void run() {
        System.out.println(getName() + " is running ...");
        for (int i=0; i<4; i++) {
            try {
                sleep(500);
            } catch (InterruptedException e) {}
            System.out.println("Hello there, from " + getName());
        }
    }
}

class MyThread2 extends Thread {
    private Thread wait4me; // Thread to wait for

    MyThread2(Thread target) {
        super();
        wait4me = target;
    }

    @Override
    public void run() {
        System.out.println(getName() + " is waiting for " + wait4me.getName() + "...");
        try {
            // target 스레드가 끝날 떄까지 기다려준다.
            wait4me.join();
        } catch (InterruptedException e) {}

        System.out.println(wait4me.getName() + " has finished...");

        // Print message 4 times
        for (int i=0; i<4; i++) {
            try {
                sleep(500);
            } catch (InterruptedException e) {}
            System.out.println("Hello there, from " + getName());
        }
    }
}