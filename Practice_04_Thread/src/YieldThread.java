public class YieldThread extends Thread {
    @Override
    public void run() {
        for (int count=0; count<4; count++) {
            System.out.println(count + " From: " + getName());
//            yield();    // cooperative multi-tasking
        }
    }
}

class TestThread {
    public static void main(String[] args) {
        YieldThread first = new YieldThread();
        YieldThread second = new YieldThread();

        first.start();
        second.start();

        System.out.println("End");
    }
}
