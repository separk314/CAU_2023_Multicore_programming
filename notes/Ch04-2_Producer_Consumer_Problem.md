# 04-2: Producer-Consumer Problem

# Producer-Consumer Problem

- full → 생성자가 buffer에 추가 못하도록. 생성자를 sleep
- empty → 소비자가 remove 못하도록. 소비자를 sleep

![Untitled](04-2%20Producer-Consumer%20Problem%20b60454ad1623419491d080fcdfb7e721/Untitled.png)

- Bounded buffer 사용: A queue with a fixed size

- 기본 code
    
    ```java
    class Buffer<E> {
        static int SIZE = 10;
    
        E[] array = (E[])new Object[SIZE];
        
        synchronized void enqueue(E elt) {
            if (isFull()) {
                // ...
            } else {
                // ...
            }
        }
        
        synchronized E dequeue() {
            if (isEmpty()) {
                // ...
            } else {
                // ...
            }
            return null;
        }
    }
    ```
    

# Attempt 1: spinning

- enqueue인데 full일 때: 자리가 날 때까지 기다린다
- Bad approach!
    - 이유 1: wasted work
    - 이유 2: keep grabbing lock

```java
synchronized void enqueue(E elt) {
    while (true) {
        synchronized (this) {
            if (isFull()) continue;
            // add to array
            return;
        }
    }
}
```

# What we want

- 스레드가 실행될 수 있을 때까지 `wait()`
- Be notified when it should try again
- 그 사이에는 다른 스레드들이 실행됨

- OS에서 제공하는 Language나 library 없이 구현하기
    
    → Condition variable 사용
    

### Java approach: not quite right

```java
synchronized void enqueue(E elt) {
    if (isFull()) {
        try {
            this.wait();
        } catch (InterruptedException e) {}
    }
        
    // add to array
    if (buffer was empty)
        this.notify();
}

synchronized E dequeue() {
    if (isEmpty()) {
        try {
            wait();
        } catch (InterruptedException e) {}
    }
        
    // take away
    if(buffer was full)
        notify();
}
```

# Key ideas

- `wait`
    - wake up 스레드를 “register” 한다.
    - 원자적으로: lock을 반납하고 block
    - execution이 재개될 때: **스레드가 lock을 가진다**

- `notify`
    - **무작위의 스레드 1개를 골라서 깨운다**
    - 깨운 스레드가 바로 실행 될지는 모른다.
        
        **암튼 깨우기만 한다.** 
        
        **깨어난 스레드는 이제 lock을 기다린다.**
        
    - 깨어나길 기다리는 스레드가 없다면, 아무 일도 일어나지 않는다.

# Bug #1

- 문제점: (thread가 notified 된 시간) ~ (re-acquire lock) 사이에, 조건이 다시 `false`가 될 수 있다!

- 예시 상황
    
    Thread A: enqueue 하려고 했는데 full이라 waiting 중
    
    (다른 스레드가 dequeue 하고 notify 해줌)
    
    Thread B: 먼저 잽싸게 lock을 받아서 enqueue 해버림
    
    Thread A: 다시 full 상태인데 인지 못하고 wait() 다음 inst 실행해버림, 오류 발생
    
- `notify()`를 받으면 반드시 wake up 하는 건 맞다.
    
    하지만 lock을 잡을 수 있을지, 없을지는 알 수 없다(?) → condition을 충족하는지는 확실히 알 수 없다는 뜻인 듯??
    
    `notify`가 끝나면 lock이 풀리는데, 이때 다른 메소드가 lock을 먼저 가져갈 수 있다. (또는 내가(스레드가) `notify signal`을 받기 전에 lock이 풀려서 다른 스레드가 lock을 가져가는 경우)
    
    → 이 경우 full인데 full인 줄 모르고 `add to array` 진행했다가 오류 생긴다.
    

<Bug #1 수정 코드>

```java
synchronized void enqueue(E elt) {
    **while** (isFull()) {
				wait();
    }
}

synchronized E dequeue() {
    **while** (isEmpty()) {
        wait();
    }
}
```

- Guideline: **lock을 다시 받았을 때 항상 조건을 다시 체크하자**

# Bug #2

- 문제점: 스레드 여러 개가 waiting 하고 있을 때: 우리는 하나만 깨운다.

- 예시 상황: Thread A, B가 enqueue에서 waiting
    
    Thread C: dequeue한 뒤에 `notify()`
    
    → notify 시그널이 도착하기 전에 Thread D이 dequeue
    
    → not full 상태였으므로 **Thread D는 notify 하지 않음**
    
    → Thread C의 `notify` 시그널을 받은 스레드는 실행되지만, 나머지 하나는 wake up 되지 않는다.
    
- `notifyAll()`을 사용해서 모든 waiting 스레드들을 깨운다.

```java
synchronized void enqueue(E elt) {
    while (isFull()) {
        try {
            this.wait();
        } catch (InterruptedException e) {}
    }
        
    // add to array
    if (buffer was empty)
        **this.notifyAll();**
}

synchronized E dequeue() {
    while (isEmpty()) {
        try {
            wait();
        } catch (InterruptedException e) {}
    }
        
    // take away
    if(buffer was full)
        **notifyAll();**
}
```

- Guildline: 그냥 `notifyAll()`을 기본으로 사용하자.
    
    아예 안 깨우는 것보다는 wasteful waking이 낫다.
    

- 그럼 notify는 왜 있냐?
    
    → notify가 빠르긴 해…