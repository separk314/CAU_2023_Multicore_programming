// Promblem 1

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParkingBlockingQueue {
    private static final int CAR_NUM = 10;
    private static final int PLACE_NUM = 7;

    public static void main(String[] args) {
        BlockingQueue parkingGarage = new ArrayBlockingQueue(PLACE_NUM);
        for (int i=1; i<=CAR_NUM; i++) {
            Car c = new Car("Car " + i, parkingGarage);
        }
    }
}

class Car extends Thread {
    private BlockingQueue parkingGarage;
    private String name;
    public Car(String name, BlockingQueue p) {
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
                parkingGarage.put(this.name);
                justEntered();

                sleep((int)(Math.random() * 20000)); // stay within the parking garage
                aboutToLeave();
                parkingGarage.remove(this.name);
                Left();
            } catch (InterruptedException e) {}
        }
    }
}
