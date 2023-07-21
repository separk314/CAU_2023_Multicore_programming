# 05-2: C++ Threads

# C++ Multi-Threading

C++ 17 이상부터 Parallel STL

# Threads Library: #include <thread>

- Creating thread: `std::thread`
    
    생성자: `thread(func, args...)`
    
    func 함수가 return하면 스레드가 종료된다.
    
    모든 파라미터들은 value로 전달된다.
    
    reference로 전달하고 싶으면, `std::ref`로 감싸서 전달한다.
    
- 스레드가 끝날 때까지 기다리고 싶으면: `join()`
- 독립적으로 돌리고 싶으면(No join): `detach()`
    
    detach() 호출 이후에는, 스레드는 non-joinable이 되고, 안전하게 종료될 수 있다.
    
- parent thread는 `join()` 또는 `detach()`를 호출해야 한다.

- thread 생성할 때
    - 일반 함수 넘기기: std::thread t1(funcName, args…): 그냥 함수 이름 넘기면 됨
    - 멤버 함수 넘기기: std::thread t2(&ClassName::funcName, &obj, args…): 멤버 함수의 주소와, 실행할 멤버의 주소까지 넘겨주어야 함.

- 헤더 파일 <mutex>를 추가하면 `std::mutex` 사용 가능
    
    ```cpp
    std::mutex m;
    m.lock();
    m.unlock();
    ```
    
    `std::lock_guard`: 시작될 때 mutex를 잠그고, 파괴될 때 unlock한다.
    
    `std::transform()`: 모든 element에 function 적용 (map 같은 역할)
    
    sorting example에서 (psort.cpp) std::sort(std::execution::par, vec.begin(), vec. …)
    
    std::execution::par으르 추가하면 sorting이 parallel하게 진행된다.
