# 05-1: Pthread Programming

# Thread

: OS에 의해 동시에 실행되는 Independent stream of instructions

![Untitled](05-1%20Pthread%20Programming%200cf73d40b8024bda9ca2d82c0e60d6e8/Untitled.png)

- 스레드는 process의 자원을 공유한다.
    - 포인터들이 같은 포인터를 가진다 → 한 스레드가 값을 바꾸면 다른 스레드에 영향.
    - 같은 memory location을 읽고 쓰기가 가능하기 때문에, 동기화가 필요하다.
- 프로세스는 자원을 공유하지 않는다.
    
    memory address값이 같아 보이더라도, 각 프로세스는 independent memory space를 가지기 때문에(즉, local address) 실제로는 physically 다른 공간에 저장한 것이다.
    

### Thread Properties

- 스레드는 독립적으로 스케쥴될 필요가 있는 필소 리소스들만 복제한다.
- parent process가 죽으면 자기도 죽는다.
- 대부분의 오버헤드가 프로세스 생성을 통해 이미 달성되었기 때문에 “lightweight”하다.
- 따라서, inter-process communication보다 inter-thread communication이 더 효율적이다.

# pthread

- Standardized C language threads for UNIX
- **shared memory multiprocessor**에서 작동된다.

**************************Why pthreads?**************************

- 성능 향상
- 프로세스에 비해 적은 시스템 리소스를 사용한다.
    
    `fork()`는 `pthread_create()`보다 10~50배 느리다.
    

## Thread Management

: Thread creation, thread destruction

### Thread Creation

<aside>
<img src="https://www.notion.so/icons/code_lightgray.svg" alt="https://www.notion.so/icons/code_lightgray.svg" width="40px" /> `pthread_create (thread, attr, start_routine, arg)`

- 더 자세한 API
    
    ```c
    int pthread_create (
    		pthread_t *thread,
    		const pthread_attr_t *attr,
    		void *(*start_routine)(void *),
    		void *restrict arg
    );
    ```
    
</aside>

`thread`: 함수의 결과. 함수 안에서 포인터가 가리키고 있는 값이 변함.

이 값은 나중에 pthread_join(pthread_t thread, …)에서 첫 번째 인자에 넣을 때 사용.

`start_routine`: 스레드가 실행할 함수의 이름

`arg`: arguments 포인터

- 새로운 스레드를 생성해서, 바로 실행한다.
- thread id를 저장할 장소를 제공한다.
- 스레드가 어떤 함수를 실행할지 지정해야 한다.
- 반환 타입: `pthread_t` 구조체
- default attribute를 사용하려면 NULL으로 지정해라.
- 작동할 인수가 없으면 NULL으로 지정해라.
- 에러 코드를 확인한다.

### Thread Termination

스레드가 종료되는 경우들

- 스레드가 스레드 함수를 정상적으로 완료하고 반환하는 경우
- 스레드 내에서 `pthread_exit()` 함수를 호출하는 경우
- 다른 스레드가 `pthread_canel` 으로 취소하는 경우
- `exec()`이나 `exit()`이 실행되어 전체 프로세스가 종료되는 경우
- main 함수가 먼저 종료되면서 `pthread_exit()` 함수를 명시적으로 호출하지 않을 때.
→ 다른 스레드들도 강제로 종료.

<aside>
<img src="https://www.notion.so/icons/code_lightgray.svg" alt="https://www.notion.so/icons/code_lightgray.svg" width="40px" /> `pthread_exit (status)`

- 더 자세한 API
    
    ```c
    void pthread_exit(void *value_ptr);
    ```
    
- 예제 코드
    
    ```c
    void *thread_function(void *arg) {
        printf("Thread is running. Argument was %s\n", (char*)arg);
        int i, sum = 0;
        for (i = 0; i < 100; i++) {
            sum += i;
        }
        pthread_exit((void*)sum);
    }
    ```
    
</aside>

`status`: void pointer: type이 지정되지 않았을 때. 사용할 때는 type casting해서 사용한다.

- `pthread_exit()` 함수가 main()에서 실행되는 경우, 스레드에 대한 영향은 다음과 같다.
    - main() 함수가 생성한 스레드들보다 먼저 끝났는데 main 안에서 `pthread_exit()`를 호출한 경우, **main 함수가 끝났더라도 다른 스레드들은 계속 실행**된다.
        
        ```c
        int main() {
          // 스레드 생성
          ...
          pthread_exit(NULL); 
        	// 메인 함수에서 pthread_exit()를 호출하므로, 
        	//생성된 스레드들이 작업을 완료할 때까지 기다립니다.
        }
        ```
        
    - main()이 `pthread_exit()`를 호출하지 않고 끝나면, 다른 스레드들은 모두 자동 종료.
        
        따라서 main 함수가 먼저 종료되면, 다른 스레드들은 일을 끝내지 못하고 강제 종료된다.
        
    
    따라서 main() 함수에서 `pthread_exit()`를 호출하여 → 프로그램이 종료되기 전에 생성된 스레드들이 끝날 때까지 기다리게 할 수 있다.
    

- 프로그래머는 선택적으로 종료 상태를 지정할 수 있으며, 
이 상태는 calling thread의 void pointer에 저장된다.
- 정리: pthread_exit() 루틴은 파일을 닫지 않는다;
스레드 안에서 오픈된 파일은 스레드가 종료된 후에도 열린 상태를 유지한다.

### Thread Cancellation

<aside>
<img src="https://www.notion.so/icons/code_lightgray.svg" alt="https://www.notion.so/icons/code_lightgray.svg" width="40px" /> `pthread_cancel (thread)`

- 더 자세한 API
    
    ```c
    int pthread_cancel (pthread_t thread);
    ```
    
</aside>

- 스레드는 다른 스레드를 끝내도록 request할 수 있다.
(request가 100% 수행되는 것은 아님.)
- `pthread_return`은 request를 수행한 뒤에 반환한다.

## Joining

<aside>
<img src="https://www.notion.so/icons/code_lightgray.svg" alt="https://www.notion.so/icons/code_lightgray.svg" width="40px" /> `pthread_join (thread, ptr)`

- 더 자세한 API
    
    ```c
    int pthread_join (pthread_t thread, void **value_ptr)
    ```
    

*************************************************************************************************************************************************The **pthread_join()** subroutine **blocks** the calling thread until the specific thread terminates.
The programmer is able to obtain the target thread’s termination return **status** if it was specified in the target thread’s call to **pthread_exit().***

</aside>

`thread`: pthread_create로 얻은 첫 번째 인자를 thread에 넣는다

`ptr`: void pointer pointer 타입이다. 처음에는 값이 없다가, 스레드에서 pthread_exit의 인자로 값을 넣어주면, 그 값이 ptr에 들어간다.

1. pthread_exit (**void*** ptr): 부모 스레드에게 전달할 termination status를 넣는다.
타입을 모르기 때문에 void pointer. 이 값은 pthread_join의 두번째 인자로 전달됨.
2. pthread_join (thread, **void**** vptr): 함수가 실행될 때는 값 없다가 끝나면 값이 들어간다.
원래 타입의 포인터 형태여야 값을 받을 수 있다!

- `pthread_join` 함수를 호출하면, 타겟 스레드가 종료되기 전까지 block된다. 일시중지.
- 타겟 스레드가 `pthread_exit()`을 호출한 경우, 개발자는 termination status를 받을 수 있다.
- 한 스레드에 대하여 `pthread_join` 함수를 한 번만 호출해야 된다. 
같은 스레드에 여러 번 호출하는 건 오류가 발생할 수 있다.

![Untitled](05-1%20Pthread%20Programming%200cf73d40b8024bda9ca2d82c0e60d6e8/Untitled%201.png)

## Mutexes (mutual exclusion)

- 스레드 동기화 구현
- 여러 번의 write가 생길 때, shared data를 보호한다.
- mutex 변수는 공유 데이터 리소스에 대한 엑세스를 관리하는 일종의 “lock” 이다.
하나의 스레드가 mutex 변수를 잠그면, 다른 스레드들은 못 잠근다.
- race condition을 방지하기 위해 사용된다.
- 여러 스레드가 뮤텍스를 경쟁할 때, 루저는 blocked 되어 기다려야 한다.
”lock” 대신 ”trylock”을 사용하면 block되지 않는다.

### Mutex Routines

- `pthread_mutex_init (mutex, attr)`: 사용하기 전에 반드시 initialize
- `pthread_mutex_destroy (mutex)`

### Locking/Unlocking Mutexes

- `pthread_mutex_lock (mutex)`: 이미 사용되고 있으면 block된다.
- `pthread_mutex_trylock (mutex)`: mutex가 이미 사용되고 있는 경우, block 하지 않고 “busy” 에러 코드를 반환한다.
    
    <aside>
    💡 pthread_mutex_trylock behaves identically to pthread_mutex_lock, except that it does not **block** the calling thread if the given mutex is already locked by another thread.
    Instead, pthread_mutex_trylock **returns** immediately with the error code EBUSY.
    
    </aside>
    
- `pthread_mutex_unlock (mutex)`

### Programmer’s Responsibility for Using Mutex

- shared data를 보호해야할 때, 모든 스레드가 mutex를 사용해야 한다.
    
    예를 들어, 스레드 3개가 데이터를 고치고 있는데 그 중 2개의 스레드만 mutex를 사용
    → 나머지 하나의 스레드 때문에 데이터가 손상될 수 있다.
    

## Conditional Variables

: Communication between threads that share a mutex

- 스레드 동기화의 또다른 방법이다.
어떤 상황이 일어났을 때 감지 → 알림 (JAVA의 `wait()`, `notify()`같은 역할)
- mutex는 data access를 컨트롤하며 동기화
- condition variables은 실제 데이터의 값에 기반한 동기화.
    - condition variable이 없으면 개발자는 어떤 상황이 일어났는지 계속 체크해야함(polling)
    Example 5에서: mutual lock을 얻는 것에 실패하면 block 상태가 됨. 다른 스레드가 lock을 해제하면 경쟁을 통해 mutual lock을 얻을 수 있다.
- 항상 뮤텍스 잠금과 함께 사용된다.
- 뮤텍스랑 뭐가 달라?
    - 뮤텍스: lock 시도했는데 누가 사용중이면 그대로 block 되었다가, lock이 풀리면 자동으로 block도 풀림.
    - Condition variable: 특정 상태가 되면 다른 스레드에게 알려주고 싶을 때 사용, 그 과정에서 mutex가 자동으로 풀렸다가 lock되었다가 한다.

### Condition Variables Routines

- `pthread_cond_init (condition, attr)`: Condition variables는 반드시 pthread_cond_t 타입으로 선언되어야 하며, 쓰기 전에 초기화를 해야 한다.
    
    attr: 세팅할 값을 넣는다, 디폴트로 NULL값.
    
- `pthread_cond_destroy (condition)`: 쓰지 않는 변수를 free

- `pthread_cond_wait (condition, mutex)` = wait()
    
    condition에 대해 signal이 올 때까지 스레드를 block한다.
    
    이 함수는 mutex가 locked되었을 때 호출되어야 한다.
    
    이 함수가 호출되었을 때, mutex는 lock이 풀려서 다른 스레드들이 mutex를 가질 수 있다.
    
    signal을 받으면 스레드가 깨어나고, mutex가 자동으로 locked된다.
    
- `pthread_cond_signal (condition)` = notify()
    
    condition variable을 기다리고 있는 다른 스레드를 깨운다.
    
    wait() 전에 이 함수를 호출하는건 논리적인 오류임.
    

- `pthread_cond_broadcast (condition)` = notifyAll()
    
    **기다리는 스레드가 둘 이상**일 때, pthread_cond_signal 대신 쓰이는 것.
    

- 생성자-소비자 문제 예시
    
    ```c
    #include <stdio.h>
    #include <stdlib.h>
    #include <pthread.h>
    
    #define BUFFER_SIZE 5
    
    int buffer[BUFFER_SIZE];
    int in = 0;
    int out = 0;
    
    pthread_mutex_t mutex;
    pthread_cond_t cond_not_full;
    pthread_cond_t cond_not_empty;
    
    void *producer(void *arg) {
        for (int i = 0; i < 10; i++) {
            pthread_mutex_lock(&mutex);
    
            while ((in + 1) % BUFFER_SIZE == out) { // 버퍼가 가득 찼을 때 기다림
                pthread_cond_wait(&cond_not_full, &mutex);
            }
    
            buffer[in] = i;
            printf("Producer produced item %d at index %d\n", buffer[in], in);
            in = (in + 1) % BUFFER_SIZE;
    
    				// 소비자에게 버퍼가 비어있지 않다고 신호 전송
            pthread_cond_signal(&cond_not_empty); 
            pthread_mutex_unlock(&mutex);
        }
        return NULL;
    }
    
    void *consumer(void *arg) {
        for (int i = 0; i < 10; i++) {
            pthread_mutex_lock(&mutex);
    
            while (in == out) { // 버퍼가 빈 경우 기다림
                pthread_cond_wait(&cond_not_empty, &mutex);
            }
    
            int item = buffer[out];
            printf("Consumer consumed item %d at index %d\n", item, out);
            out = (out + 1) % BUFFER_SIZE;
    
    				// 생산자에게 버퍼가 가득 차있지 않다고 신호 전송
            pthread_cond_signal(&cond_not_full); 
            pthread_mutex_unlock(&mutex);
        }
        return NULL;
    }
    
    int main() {
        pthread_t producer_thread, consumer_thread;
    
        pthread_mutex_init(&mutex, NULL);
        pthread_cond_init(&cond_not_full, NULL);
        pthread_cond_init(&cond_not_empty, NULL);
    
        pthread_create(&producer_thread, NULL, producer, NULL);
        pthread_create(&consumer_thread, NULL, consumer, NULL);
    
        pthread_join(producer_thread, NULL);
        pthread_join(consumer_thread, NULL);
    
        pthread_mutex_destroy(&mutex);
        pthread_cond_destroy(&cond_not_full);
        pthread_cond_destroy(&cond_not_empty);
    
        return 0;
    }
    ```