public class SynchronizationDemo1 {
    public static void main(String[] args) {
        FinTrans3 ft = new FinTrans3();
        TransThread3 tt1 = new TransThread3(ft, "Deposit Thread");
        TransThread3 tt2 = new TransThread3(ft, "Withdrawal Thread");

        // deposit과 withdrawal 동시에 실행 -> critical section에 2개의 스레드
        tt1.start();
        tt2.start();
    }
}

class FinTrans3 {
    public static String transName;
    public static double amount;
}

class TransThread3 extends Thread {
    private FinTrans3 ft;
    TransThread3(FinTrans3 ft, String name) {
        super(name);    // 스레드 이름 저장
        this.ft = ft;   // financial transaction 객체 reference 저장
    }

    @Override
    public void run() {
        for (int i=0; i<100; i++) {
            if (getName().equals("Deposit Thread")) {
                synchronized (ft) {
                    // ft: sync object -> 하나의 회문만 허락한다
                    ft.transName = "Deposit";
                    try {
                        Thread.sleep((int)(Math.random() * 1000));
                    } catch (InterruptedException e) {}

                    ft.amount = 2000.0;
                    System.out.println(ft.transName + " " + ft.amount);
                }

            } else {
                synchronized (ft) {
                    ft.transName = "Withdrawal";
                    try {
                        Thread.sleep((int) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                    }

                    ft.amount = 250.0;
                    System.out.println(ft.transName + " " + ft.amount);
                }
            }
        }
    }
}