import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class ParkingSemaphore {
    private static final int CAR_NUM = 10;
    private static final int PLACE_NUM = 7;

    public static void main(String[] args) {
        Semaphore parkingGarage = new Semaphore(PLACE_NUM);
        for (int i=1; i<=CAR_NUM; i++) {
            Car c = new Car("Car " + i, parkingGarage);
        }
    }
}

class Car extends Thread {
    private Semaphore parkingGarage;
    private String name;
    public Car(String name, Semaphore p) {
        super(name);
        this.name = name;
        this.parkingGarage = p;
        start();
    }

    private void tryingEnter()
    {
        System.out.println(getName()+": trying to enter");
    }
    private void justEntered() {    System.out.println(getName()+": just entered");}
    private void aboutToLeave() {   System.out.println(getName()+":         about to leave");}
    private void Left()
    {
        System.out.println(getName()+":         have been left");
    }

    public void run() {
        while (true) {
            try {
                sleep((int)(Math.random() * 10000)); // drive before parking
                tryingEnter();
                parkingGarage.acquire();
                justEntered();

                sleep((int)(Math.random() * 20000)); // stay within the parking garage
                aboutToLeave();
                parkingGarage.release();
                Left();
            } catch (InterruptedException e) {}
        }
    }
}