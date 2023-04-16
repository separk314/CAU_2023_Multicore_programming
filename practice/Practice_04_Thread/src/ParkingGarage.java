public class ParkingGarage {
    private int places;

    public ParkingGarage(int spaces) {
        if (spaces < 0)
            this.places = 0;
        else
            this.places = spaces;

    }

    // enter, leave를 따로 synchronized 해도 된다.
    // 어차피 places==0인 경우는 while문으로 따로 처리하기 때문.
    // 동시에 enter, leave하는 건 문제되지 않는다.

    public synchronized void enter() {
        while (places == 0) {
            // garage full -> car should wait, lock 반납
            try {
                wait(); // 자리가 생길 때까지 기다림
            } catch (InterruptedException e) {}
        }
        places--;
    }

    public synchronized void leave() {
        places++;
        notify();
    }

}

class Car extends Thread {
    private ParkingGarage parkingGarage;

    public Car(String name, ParkingGarage p) {
        super(name);
        this.parkingGarage = p; // parkingGarage: shared
        start();    // self start
    }

    @Override
    public void run() {
        // random time wait -> enter -> stay -> leave 과정을 반복
        while (true) {
            try {
                sleep((int)(Math.random() * 10000));

            } catch (InterruptedException e) {}

            parkingGarage.enter();
            System.out.println(getName() + ": entered");

            try {
                sleep((int)(Math.random() * 20000));    // stay within the grage
            } catch (InterruptedException e) {}

            parkingGarage.leave();
            System.out.println(getName() + ": left");
        }
    }
}

class ParkingGarageOperation {
    public static void main(String[] args) {
        ParkingGarage parkingGarage = new ParkingGarage(10);
        for (int i=1; i<=40; i++) {
            Car c = new Car("Car" + i, parkingGarage);
        }
    }
}