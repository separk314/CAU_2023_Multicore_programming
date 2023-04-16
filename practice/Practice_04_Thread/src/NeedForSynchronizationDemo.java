public class NeedForSynchronizationDemo {
    public static void main(String[] args) {
        FinTrans ft = new FinTrans();
        TransThread tt1 = new TransThread(ft, "Deposit Thread");
        TransThread tt2 = new TransThread(ft, "Withdrawal Thread");

        // deposit과 withdrawal 동시에 실행 -> critical section에 2개의 스레드
        tt1.start();
        tt2.start();
    }
}

class FinTrans {
    public static String transName;
    public static double amount;
}

class TransThread extends Thread {
    private FinTrans ft;
    TransThread(FinTrans ft, String name) {
        super(name);    // 스레드 이름 저장
        this.ft = ft;   // financial transaction 객체 reference 저장
    }

    @Override
    public void run() {
        for (int i=0; i<100; i++) {
            if (getName().equals("Deposit Thread")) {
                // Deposit: critical code section 시작
                ft.transName = "Deposit";
                try {
                    Thread.sleep((int)(Math.random() * 1000));
                } catch (InterruptedException e) {}

                ft.amount = 2000.0;
                System.out.println(ft.transName + " " + ft.amount);
            } else {
                // Withdrawal: critical code section 시작
                ft.transName = "Withdrawal";
                try {
                    Thread.sleep((int)(Math.random() * 1000));
                } catch (InterruptedException e) {}

                ft.amount = 250.0;
                System.out.println(ft.transName + " " + ft.amount);
            }
        }
    }
}