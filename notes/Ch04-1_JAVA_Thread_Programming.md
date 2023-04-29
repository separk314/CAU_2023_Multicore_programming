# 04-1: JAVA Thread Programming

# Process

- **Program in execution**
    
    즉, running program.
    
    All the related information is included in the process
    (**own independent space**를 가진다. thread는 memory 공유.)
    

- PCB(Process Control Block)에 프로세스에 관련된 정보들을 저장


- 프로세서 1개에는 프로세스 1개만 실행 가능
    
    → context switch가 일어난다.
    

### Unix process

process 0을 제외하고, 모든 프로세스는 `fork()` 시스템 call로 만들어진다.

- `fork()` : allocates entry in process table and assigns a unique PID to the child process.
프로세스 테이블의 항목을 할당, PID 할당
- 자식 프로세스는 부모의 process image를 복제한다
- 자식/부모는 `fork()`에 따라 같은 코드를 실행한다.
- 참고: [https://codetravel.tistory.com/23](https://codetravel.tistory.com/23)

### Process Creation

```c
main() {
		int pid;    // 1) only parent process
		pid = fork();    // 2) two independent processes

		if (pid == 0)
			cout << "i am the child" << endl;
		else if (pid > 0)
			cout << "i am the parent" << endl;
		else
			cout << "fork failed" << endl;
}
```


# Threads

: single sequential flow of control within a program

- A thread runs with the context of a program’s process and ******************************************************************************************************takes advantage of the resources allocated for that process****************************************************************************************************** and it’s environment.

- Each thread is comprised of
    - ******************************Program counter****************************** (PC)
    - ********Register set********
    - **********Stack**********


- Thread가 Process와 공유하는 부분
    - 코드
    - 데이터 영역(메모리)
    - OS resources(opened fiMles)
    

### Multi-process vs Multi-thread

- Process
    - 자식 프로세스: parent’s variable의 **복사본**을 갖는다
    - expensive to start
    - **Don’t** have to worry about **concurrent access** to variables.

- Thread
    - 자식 프로세스: parent’s variable을 공유한다
    - cheap to start
    - ************************************************************Concurrent access to variables************************************************************ is an issue

# Programming JAVA threads

- Java에는 built-in thread가 있다 (`java.lang.thread`)
- Application은 최소 1개의 스레드로 이루어져 있다(`main` 스레드)
- Java Virtual Machine: 메인 메서드를 실행시킬 initial thread를 생성한다
- main thread는 다른 스레드들을 생성할 수 있다.

### Creating Threads 방법 1: Thread 클래스 상속

`Thread` 클래스를 상속받는 MyThread 클래스 생성

- `start()` 실행 → `run()` 자동 실행

```java
class MyThread extends Thread {
		public void run() {
				// 해야 하는 일...
		}
}

********MyThread t = new MyThread();
t.start()********
```

⇒ 메인 스레드가 만들어낸 `t`스레드의 `run()` 메소드가 실행된다.

- Example 1
    
    ```java
    class ThreadDemo {
        public static void main(String[] args) {
            MyThread mt = new MyThread();
            mt.start();
    
            // mt랑 동시에 실행됨!
            for (int i=0; i<50; i++)
                System.out.println("i = " + i + ", i*i = " + i*i);
        }
    }
    
    class MyThread extends Thread {
        @Override
        public void run() {
            for (int count=1, row=1; row<20; row++, count++) {
                for (int i=0; i<count; i++)
                    System.out.print("*");
                System.out.print("\n");
            }
        }
    }
    ```
    
    - 결과
        
        ```java
        *
        **
        ***
        ****
        *****
        ******
        *******
        ********
        *********
        **********
        ***********
        ************
        *************
        **************
        ***************
        ****************
        *****************
        ******************
        *******************
        i = 0, i*i = 0
        i = 1, i*i = 1
        i = 2, i*i = 4
        i = 3, i*i = 9
        i = 4, i*i = 16
        i = 5, i*i = 25
        i = 6, i*i = 36
        i = 7, i*i = 49
        i = 8, i*i = 64
        i = 9, i*i = 81
        i = 10, i*i = 100
        i = 11, i*i = 121
        i = 12, i*i = 144
        i = 13, i*i = 169
        i = 14, i*i = 196
        i = 15, i*i = 225
        i = 16, i*i = 256
        i = 17, i*i = 289
        i = 18, i*i = 324
        i = 19, i*i = 361
        i = 20, i*i = 400
        i = 21, i*i = 441
        i = 22, i*i = 484
        i = 23, i*i = 529
        i = 24, i*i = 576
        i = 25, i*i = 625
        i = 26, i*i = 676
        i = 27, i*i = 729
        i = 28, i*i = 784
        i = 29, i*i = 841
        i = 30, i*i = 900
        i = 31, i*i = 961
        i = 32, i*i = 1024
        i = 33, i*i = 1089
        i = 34, i*i = 1156
        i = 35, i*i = 1225
        i = 36, i*i = 1296
        i = 37, i*i = 1369
        i = 38, i*i = 1444
        i = 39, i*i = 1521
        i = 40, i*i = 1600
        i = 41, i*i = 1681
        i = 42, i*i = 1764
        i = 43, i*i = 1849
        i = 44, i*i = 1936
        i = 45, i*i = 2025
        i = 46, i*i = 2116
        i = 47, i*i = 2209
        i = 48, i*i = 2304
        i = 49, i*i = 2401
        ```
        
    
    메인 스레드와 MyThread가 동시에 시행된다.
    
    i의 값이 작아서 MyThread가 끝나고 → 메인 스레드가 동작하는 것처럼 보이지만, i의 값을 500으로 늘려서 찍어보니 동시에 실행되는 것을 확인할 수 있었다. 
    

<Thread Names>

- default name: `Thread-Number`
- Thread 클래스의 `getName()`으로 이름을 가져올 수 있다.

방법 1. User-defined names through constructor

```java
Thread myThread = new Thread("HappyThread");
```

방법 2. `setName()` 메서드 사용해서 초기화

- Example 2
    
    ```java
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
    ```
    
    결과
    
    ```
    ...
    Thread 1(1319)
    Thread 3(1318)
    Thread 1(1320)
    Thread 2(1319)
    Thread 3(1319)
    ...
    ```
    

### Creating Threads 방법 2: Runnable 인터페이스 구현

Java에서는 다중 상속이 안 되기 때문에, `Thread` 클래스가 아닌 `Runnable` 인터페이스의 `run()`을 구현하기도 한다.

![image](https://file.notion.so/f/s/f69a0c50-bbb9-47bf-a591-868660b671f4/Untitled.png?id=51f083ef-99e2-4ea2-8edf-91cc2e09138e&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682963986747&signature=icNSb1c3fnd9_5brDXzhiZKDl_snKv2KAtFzVNjV3Fw&downloadName=Untitled.png)

- Runnable 인터페이스에는 `run()` 메소드 1개만 있다.
    
    ```java
    public void run()
    ```
    
- Thread의 `run()` 메소드가 Runnable의 `run()` 메소드를 실행시킨다.
    - Thread의 target이 Runnable이 되도록 → Thread 생성자 인자에 Runnable을 넣는다!
    - Example Code
        
        ```java
        public class RunnableMain {
            public static void main(String[] args) {
                MyRunnable myrun = new MyRunnable();
                Thread t1 = new Thread(myrun);  // Thread의 target: myrun
                t1.start();
                System.out.println("InsideMain()");
            }
        }
        
        class MyRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("MyRunnable.run()");
            }
        }
        ```
        
        결과
        
        ```java
        InsideMain()
        MyRunnable.run()    // Thread의 run이 Runnable의 run을 실행시킨다
        ```
        

- Example: Self-starting Threads
    
    ```java
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
            AutoRun AutoRun_t1 = new AutoRun(); 
    					// main에서 start하지 않아도 run이 자동 시작
            System.out.println("InsideMain()");
        }
    }
    ```
    

## Thread Life-Cycle

![image](https://file.notion.so/f/s/e6946a06-244c-4df1-9fd5-4e68ff2278c4/Untitled.png?id=c178bba6-4ecf-4f8f-94e0-37a164ba979f&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682964000070&signature=MkjCy87bJjuagY4UdFOBFpCHnmVHlTDSDsR_3sLyl_o&downloadName=Untitled.png)

- 스레드 생성 → `start()` → 자동으로 `run()` 실행
- 스레드는 `stop()` 또는 `run()`을 return한다.
- `isAlive()` : 스레드가 시작됐는데 아직 안 끝났는지 테스트할 때 사용.
    
    한번 끝났으면 다시 재시작할 수 없다.
    

### Alive States

Alive state에는 sub-state들이 많다.

![image](https://file.notion.so/f/s/d84625e0-cfc8-4815-bffa-c05f0b42aa48/Untitled.png?id=9872829c-1f21-4aa0-a378-e20ae46f1c07&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682964017196&signature=NucqEpxShwPyIYuZ7kk6GfMdetq_nF2mNcIEATD775w&downloadName=Untitled.png)

- Thread Priority(딱히 중요하진 않다)
    - 1에서 10까지 중요도를 매길 수 있다
    - `setPriority(int newPriority)`로 변경 가능
    - Preemptive scheduling(권장 X)

- `yield()`
    - 스케쥴러가 다른 runnable thread를 선택하도록 한다.
    - 어떤 스레드가 선택될지는 모른다
    - Example
        
        ```java
        public class YieldThread extends Thread {
            @Override
            public void run() {
                for (int count=0; count<4; count++) {
                    System.out.println(count + " From: " + getName());
                    yield();    // cooperative multi-tasking
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
        ```
        
        결과
        
        ```java
        End    // End가 맨 앞에 출력됨
        0 From: Thread-1
        0 From: Thread-0
        1 From: Thread-1
        1 From: Thread-0
        2 From: Thread-1
        2 From: Thread-0
        3 From: Thread-1
        3 From: Thread-0
        ```
        
        스레드가 번갈아 가면서 실행된다.
        
        `yield()`를 없애보니
        
        ```java
        End
        0 From: Thread-0
        1 From: Thread-0
        0 From: Thread-1
        1 From: Thread-1
        2 From: Thread-1
        3 From: Thread-1
        2 From: Thread-0
        3 From: Thread-0
        ```
        
        그냥 동시에 실행된다.
        

- Thread identity
    
    `Thread.currentThread()`: running thread의 reference를 반환한다.
    
    - Compare running thread with created thread
        - Example code
            
            ```java
            public class AutoRun_ThreadIdentity implements Runnable {
                private Thread _me;
            
                public AutoRun_ThreadIdentity() {
                    _me = new Thread(this);
                    _me.start();
                }
            
                @Override
                public void run() {
                    if (_me == Thread.currentThread()) {
                        System.out.println("AutoRun.run()");
                    }
                }
            }
            
            class Main2 {
                public static void main(String[] args) {
                    AutoRun_ThreadIdentity t1 = new AutoRun_ThreadIdentity(); // printout
                    t1.run();   // no printout
                    System.out.println("InsideMain()");
                }
            }
            ```
            
            - AutoRun_ThreadIdentity이 생성될 때: 생성자에서 _me 실행
                
                지금 실행되고 있는 스레드가 _me 이므로 printout
                
            - t1.run(): 지금 실행되고 있는 스레드는 t1이므로 no printout
            - Main thread, _me thread는 동시에 실행된다.
            

- `sleep(long millis)`: 해당 스레드를 Block한다.
- `stop()`, `suspend()`, `resume()`: Deprecated(사용 거의 안 씀)

- `join()`: Thread A can wait for Thread B to end
    
    ```java
    // in thread A
    threadB.join()
    ```
    
    - Example code
        
        ```java
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
                    // target 스레드가 끝날 때까지 기다려준다.
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
        ```
        
        결과
        
        ```java
        Starting the threads...
        Thread-0 is running ...
        Thread-1 is waiting for Thread-0...
        Hello there, from Thread-0
        Hello there, from Thread-0
        Hello there, from Thread-0
        Hello there, from Thread-0
        Thread-0 has finished...    // Thread-0이 끝나는 걸 기다려준다
        Hello there, from Thread-1
        Hello there, from Thread-1
        Hello there, from Thread-1
        Hello there, from Thread-1
        ```
        

- `isAlive()`: thread가 시작된 뒤로 stop하지 않았으면 true를 반환

# Thread synchronization

- Thread 장점: 동시에 여러 task 처리 가능
- Thread 단점: Race condition 발생
    
    → **********************************Synchronization으로 race condition 방지**********************************
    
- Race condition Example: 은행 계좌
    
    두 명 이상이 balance에 동시에 접근하는 경우
    

```java
class Account {
		int balance;

		public void deposit(int val) {
				balance = balance + val;
		}
}
```

- shared data에 동시에 접근하는 경우 문제 발생
- 동시 접근을 제한해야 한다 ⇒ ************************************************************************Mutual Exclusion of critical section************************************************************************
    
    최대 1개의 thread만 critical seciton에 진입할 수 있도록 제한
    
    - object에 ********lock********을 걸어놓는다
    - 스레드는 lock을 가져야 메소드를 실행할 수 있다.
    - lock이 불가능한 경우 → **thread will block**

### Synchronized Java methods

`synchronized` 키워드를 사용해서 접근을 제한한다.

- 수정한 은행 계좌 Example
    
    ```java
    class Account {
    		**private** int balance;
    
    		public **synchronized** void deposit(int val) {
    				// synchronized -> 하나의 thread만 들어갈 수 있다.
    				balance = balance + val;
    		}
    
    		public **synchronized** void withdraw(int val) {
    				balance = balance - val;
    		}
    }
    ```
    

- 은행 계좌 Example 1: Need for Sync
    
    ```java
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
    ```
    
    결과
    
    ```java
    Deposit 2000.0
    Deposit 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 2000.0
    Deposit 250.0
    Withdrawal 2000.0
    ```
    
    코드는 무조건 Withdrawal 250.0, Deposit 2000.0으로 출력되게 하였는데
    
    실제로 출력해보니 뒤죽박죽이다.
    
    → 한 스레드가 출력하기 직전에 다른 스레드가 critical section에서 transName, amount를 바꿔버리기 때문.
    
    → `synchronize`가 필요하다!
    
- 은행 계좌 Example 2: Synchronized
    - FinTrans의 변수들을 `private`으로 변경하고,
        
        변수 값을 바꿀 때는 FinTrans의 `update` 메소드를 사용하여 바꾸도록.
        
    - `update()` 메소드는 `synchronized` 키워드로 critical section으로 만든다.
    
    ```java
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
    }-
    
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
    ```
    
    결과
    
    ```java
    Deposit 2000.0
    Deposit 2000.0
    Deposit 2000.0
    Deposit 2000.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    ```
    

### Synchronized Lock Object: synchronized를 쓰는 다른 방법

- 모든 Java object는 lock을 걸 수 있다.
- synchronized statements (block)
    
    ```java
    synchronized statements(anObject) {
    		// execute code while holding anObject's lock.
    }
    ```
    
- Lock granularity: ************************************concurrency object************************************에는 small critical section이 좋다.

- 은행 계좌 Example 3: Synchronized lock object
    - Example 1이랑 바뀐 점: if-else문의 critical section을 `synchronized(ft)`로 감싸주었다.
    - 나머지는 그대로!
    - `ft` 객체가 sync object → 하나의 스레드만 허용한다.
    
    ```java
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
    ```
    
    결과
    
    ```java
    Deposit 2000.0
    Deposit 2000.0
    Deposit 2000.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    Withdrawal 250.0
    ```
    

## Condition Variables

- `lock(synchronized)`
    - data에 대한 thread 접근을 제어한다

- condition variable(`wait`, `notify`, `notifyall`)
    - 특정 조건이 발생할 때까지 thread를 대기하도록 하는 동기화 기본요소
    - enable threads to **atomically release a lock** and **enter the sleeping state**
    - without condition variables
        - 프로그래머는 조건이 충족되었는지 확인하기 위해서 → 스레드를 지속적으로 polling 해야한다.
        - condition variable은 **polling 없이** 동일한 목표를 달성할 수 있다.
        
        (polling: 동기화 처리를 위해서 다른 프로그램의 상태를 주기적으로 검사하여 일정한 조건을 만족할 때를 찾는 )
        
    - A condition variable는 항상 mutex lock과 함께 사용된다.

### `wait()` and `notify()`

```java
public class Object {
		public final void wait() throws InterruptedException { ... }
		public final void notify() { ... }
		public final void notifyall() { ... }
}
```

- `wait()`
    - 일반적인 경우(no interrupt): **현재 스레드가 blocked** 된다.
    - 그 객체에 대해서 스레드는 **wait 상태**로 들어간다 (Non-runnable state)
    - 그 객체의 **synchronization lock이 release** 된다.
- `notify()`
    - T라는 스레드가 wait 상태였다면, wait 상태에서 벗어난다.
    - T는 object의 lock을 다시 가져간다.
    - T는 waiting 상태에서부터 재개된다.
    

### Example: Garage Parking

- 자동차: thread로 표현
- 자동차 `enter()`: free space 개수가 감소
- 자동차 `leave()`: free space 개수가 증가
- 여러 자동차가 주차장을 이용하므로 `enter()`과 `leave()`는 동기화 필요

```java
public class ParkingGarage {
    private int places;

    public ParkingGarage(int spaces) {
        if (spaces < 0)
            this.places = 0;
        else
            this.places = spaces;

    }

    **public synchronized void enter() {
        while (places == 0) {
            // garage full -> car should wait, lock 반납
            try {
                wait(); // 자리가 생길 때까지 기다림
									// 다른 차가 나갈 때 notify()로 깨워줌 -> try to acquire lock -> lock을 얻으면 continue
            } catch (InterruptedException e) {}
        }
        places--;
    }

    public synchronized void leave() {
        places++;
        notify();    // 하지만 notify()가 끝나는게 빠를지, waiting car이 시그널을 받는게 더 빠를지는 모른다.
    }**

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
```

결과

```java
Car24: entered
Car26: entered
Car10: entered
Car30: entered
Car11: entered
Car18: entered
Car30: left
Car3: entered
```