public class SynchronizationDemo2 {
    public static void main(String[] args) {
        FinTrans2 ft = new FinTrans2();
        TransThread2 tt1 = new TransThread2(ft, "Deposit Thread");
        TransThread2 tt2 = new TransThread2(ft, "Withdrawal Thread");

        // deposit과 withdrawal 동시에 실행 -> critical section에 2개의 스레드
        tt1.start();
        tt2.start();
    }
}

class FinTrans2 {
    private String transName;
    private double amount;

    synchronized void update(String transName, double amount) {
        // critical section
        this.transName = transName;
        this.amount = amount;
        System.out.println(this.transName + " " + this.amount);
    }
}

class TransThread2 extends Thread {
    private FinTrans2 ft;
    TransThread2(FinTrans2 ft, String name) {
        super(name);    // 스레드 이름 저장
        this.ft = ft;   // financial transaction 객체 reference 저장
    }

    @Override
    public void run() {
        for (int i=0; i<100; i++) {
            if (getName().equals("Deposit Thread"))
                ft.update("Deposit", 2000.0);
            else
                ft.update("Withdrawal", 250.0);
        }
    }
}