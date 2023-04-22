# 03: Performance of Parallel Programs

# Flynn’s Taxonomy on Parallel Computer(플린 분류)

- Classified with two independent dimension
    - Instruction stream (명령어 스트림의 개수)
    - Data stream (처리 가능한 데이터 스트림)
    
    ![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled.png)
    
    <aside>
    💡 Parallel computer는 크게 **SIMD와 MIMD**로 나눌 수 있다.
    
    </aside>
    

### 1️⃣ SISD

: Single Instruction, Single Data

- A serial (non-parallel) computer
- 가장 오래된 type

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%201.png)

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%202.png)

### 2️⃣ SIMD

: **Single Instruction, Multiple Data**

- All processing units execute the same instruction at any given clock cycle.
    
    모든 유닛들이 같은 instruction을 실행한다
    
- Best suited for specialized problems characterized by a high degree of regularity (such as graphics/image processing)
    
    고도의 규칙성을 특징으로 하는 전문적인 문제에 가장 적합하다 (처리할 데이터가 방대한 경우에 적합, 복잡한 구조를 가지는 parallel 문제에는 적합X)
    
- Ex. GPU

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%203.png)

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%204.png)

### 3️⃣ MISD

: Multiple Instruction, Single Data

- Each processing unit operates on the data independently via separate instruction streams
    
    (각 프로세서는 별도의 명령 스트림을 통해 독립적으로 데이터에서 작동한다)
    
- Few actual examples of this class of parallel computer have ever existed.
    
    (실제로 구현된 컴퓨터 X)
    

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%205.png)

### 4️⃣ MIMD

: **Multiple Instruction, Multiple Data**

- Every processor may be executing a different instruction system
- Every processor may be working with a different data stream
- the most common type of parallel computer
- Most modern **super-computers** fall into this category

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%206.png)

# Creating a Parallel Program

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%207.png)

1. **Decomposition**: task 분해
2. **Assignment**: 스레드에 task 할당
    
    (1~2: **Partitioning**)
    
3. **Orchestration**: 메모리 및 task 스케쥴링
4. **Mapping**: CPU(프로세서)에 thread 할당 

## 1️⃣ Decomposition

- Break up computation into tasks to be divided among processes
- identify concurrency and decide level at which to exploit

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%208.png)

### Domain Decomposition

- Data is decomposed
- Each parallel task then works on a portion of data

데이터를 분해해서, 각각의 parallel task이 데이터 위에서 일한다.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%209.png)

1. Load balancing
2. per-workout overhead
- **Block**: processor 하나 당 thread 1개 → **less overhead**
- **Cyclic**: task가 라운드로빈 방식으로 공평하게 분배된다 → **************************************good load balancing**************************************

⇒ 상황에 따라 맞는 방식을 선택해서 사용한다.

### Functional Decomposition

- The focus is on the computation that is to be performed rather than on the data.
- Problem is decomposed according to the work that must be done.
- Each task then performs a portion of the overall work.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2010.png)

<aside>
💡 - Domain Decomposition: data와 task를 분해.
- Functional Decomposition: 기능을 기준으로 task를 분해. data을 분리하지 않는다.

</aside>

## 2️⃣ Assignment

- 스레드에 task를 할당한다.
    - Balance workload, reduce communication and management cost
    - Together with decomposition, also called partitioning.
- static 또는 dynamically Assign.
- Assignment의 목표
    - **Balance workload**
    - **Reduced communication costs**

### Partitioning: Decomposition + Assignment

- Static partitioning: something occurs at **compile time**
- Dynamic partitioning: somthing occurs at **runtime**

## 3️⃣ Orchestration

- Structing communication and synchronization
- **Organizing data structures in memory and scheduling tasks** temporally.

- Orchestration의 목표
    - Reduce cost of communication and synchronization as seen by processors
    - Reserve locality of data reference (including data structure organization)

## 4️⃣ Mapping

- **Mapping threads to execution units** (CPU cores)
- Parallel application tries to use the entire machine.
- Usually a job for OS
- Mapping decision
    - Place **related threads** (cooperating threads) **on the same processor**
    - **maximize locality, data sharing**
    - **minimize costs of communication, synchronization**

# Performance of Parallel Programs

- performance에 영향을 끼치는 요인들
    - **Decomposition**: **Coverage** of parallelism in algorithm
    - **Assignment**: **Granularity** of partitioning among processors
    - **Orchestration/Mapping**: **Locality** of computation and communication

# 1️⃣ Coverage(Amdahl’s Law)

: Potential program speedup is defined by the fraction of code that can be parallelized.

(parallelized 될 수 있는 코드가 얼마나 있는가.)

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2011.png)

SpeedUp = old running time / new running time

= 100sec / 60sec

= 1.67배

즉, parallel version이 1.67배 더 빠르다.

### Amdahl’s Law

- p: fraction of work that can be parallelized
- n: the number of processor

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2012.png)

<Implications of Amdahl’s Law>

- Speedup tens to `1/(1-p)` as number of processors tends to infinity
    
    당연하지, 프로세서가 무한개면 parallel task에 걸리는 시간이 0초니까
    
- Parallel programming is worthwhile when **programs have a lot of work that is parallel in nature.**

<Performance Scalability>

- **********************Scalability**********************: the capability of a system to increase total throughput under an increased load when resources are added.
    
    ![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2013.png)
    
    Hardware가 추가되면 speedup이 linear하게 증가한다.
    

# 2️⃣ Granularity

: a qualitative mearsure of the ratio of computation to communication

(**communication에 필요한 연산의 개수**)

- **Coarse**: relatively **large amounts of computational work** are done between communication events
- **Fine**: relatively **small amounts of computational work** are done between communication events

- Computation stages are typically separated from periods of communication by synchronization events.

- Granularity (from wikipedia)
    - Granularity: the extent to which a system is broken down into small parts
    - Coarse-grained systems: regards large sub-components
    - Fine-grained systems: regards smaller components of which the larger ones are composed

### Fine vs Coarse Granularity

**Coarse-grain Parallelism**

- 큰 덩어리
    
    → communication 비율에 비해 high computation.
    
- More opportunity for performance increase due to **low communication overhead**
- Less opportunity for performance enhancement due to **worse load balance**.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2014.png)

**Fine-grain Parallelism**

- 작은 덩어리
    
    → communication 비율에 비해 low computation.
    
- Less opportunity for performance enhancement due to **high communication overhead**
- Better opportunity for performance enhancement due to ****************************better load balance****************************.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2015.png)

- 알고리즘과 하드웨어에 따라 효율적인 granularity를 선택해야 한다.
- 보통 오버헤드는 **communication, synchronization**에서 발생하므로, **Coarse-grain granularity가 더 유리**하다.
- **************Fine-grain parallelism**************은 ****************************load imbalance**************************** 오버헤드를 줄일 수 있다.

## 🔷 Load Balancing

- Distributing approximately **equal amounts of work** among tasks so that **all tasks are kept busy all the time**.
- **Minimization of task idle time**.

Ex. if all tasks are subject to a barrier synchronization point, the slowest task will determine the overall performance.

(가장 느린 thread가 끝날 때까지 다른 thread들은 idle 상태를 유지
→ execution time of slowest task를 줄여야 한다.)

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2016.png)

- General Load Balancing Problem
    - 전체 work는 최대한 빨리 끝나야 한다.
    - workers는 비싸니까 항상 바쁘게 일하도록 해야한다.
    - work는 ************************************distributed fairly************************************. 각자 같은 양의 일을 분배받아야 한다.
    - precedence constraints가 있으므로(벽을 완성한 다음 지붕을 올릴 수 있는 것처럼)
    → clever processing order를 찾아야 한다.

- Load Balancing Problem
    
    : 빨리 끝난 프로세서는 다른 프로세서가 끝날 때까지 기다려야 한다.
    
    → Leads to **********idle time, lowers utilization**********
    

### Static load balancing == Partitioning

- Programmers make decisions and **assign a fixed amount of work** to each processing core.
- **Runtime overhead ↓**
- Works well for homogeneous multicores (같은 코어에서 잘 작동)
    - All cores are same
    - Each core has an equal amount of work
- Not so well for heterogenous multicores
    - Some cores may be faster than others
    - Work distribution is uneven

### Dynamic load balancing

- **When one core finishes** its allocated work, **it takes work** from a work queue or a core with the heaviest workload
- Adapt partitioning at runtime to balance load
- **High runtime overhead**
- 또 다른 문제점: access to shared data → 병목 현상 발생
- Ideal for codes where work is uneven, unpredictable, and in heterogenous multicore.

### Granularity and Performance Tradeoffs

1. Load balancing: 코어 간에 작업이 얼마나 잘 분배되는가? 
2. Synchronization, Communication: Communication Overhead?

## 🔷 Communication

Message passing할 때, 프로그래머는 다음 상황에 따라 communication을 orchestrate 해야한다.

- Point to Point
- One to All (Broadcast), All to One (Reduce)
- All to All
- One to Several (Scatter), Several to One (Gather)

### Factors to consider for communication

1. Cost of communications
    - Communication은 오버헤드가 있다.
    - Communications frequently require some type of **synchronization** between tasks, which can result in tasks spending time ‘************waiting************’ **instead of doing work**.

1. Latency vs Bandwidth
    
    — **Latency**: A부터 B까지 0바이트를 보낼 때 걸리는 시간
    
    — **Bandwidth**: 시간 당 소통할 수 있는 데이터의 양
    
    - 작은 메세지를 많이 보내기 → Latency, communication overheads
    - 작은 메세지들을 묶어서 하나의 큰 메세지로 보내는 게 더 효율적일 때도 있다.

1. Synchronous vs Asynchronous
    
    — **Synchronous**: require some type of ‘handshaking’ between tasks that share data
    
    — Asynchronous: transfer data independently from one another
    

1. Scope of communication
    - Point to Point
    - Collective
    
    (앞에서 나왔던 Broadcast, Reduce, Gather, Scatter)
    

### MPI: Message Passing Library

- MPI: portable specification
    - Not a language or compiler specification
    - Not a specific implementation or product
    - SPMD model (same program, multiple data)
- Multiple communication models allow precise buffer management
(다양한 통신 모델을 통해 정확한 버퍼 관리 가능)
- Extensive collective operations for scalable global communication
(확장 가능한 글로벌 커뮤니케이션을 위한 광범위한 집단 운영)

### ① Point-to-Point

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2017.png)

- Basic method of communication between two processors
    - Originating processor “sends” message to destination processor
    - Destination processor then “receives” the message
- The message commonly includes
    - Data
    - Length of the message
    - Destination address and possibly a tag

### Synchronous vs Asynchronous Messages

- **Asynchronous**: Sender는 메세지가 보내진 것만 안다 == **Non-Blocking**

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2018.png)

- **Synchronous**: 메세지가 도착하면 Sender가 알아차린다 == **Blocking**

### Blocking vs Non-Blocking Messages

- **Blocking messages**
    - Sender waits until message is transmitted: buffer is empty
    - Receiver waits until message is received: buffer is full
    - Potential for deadlock
    - 장점: stable, safer
    
    Ex. `read(buf, len, file)`, `write` 함수: blocking function이면 읽기 끝나기 전까지 기다린다. (보통 우리는 blocking 사용)
    
- **Non-Blocking messages**
    - Processing continues even if messages hasn’t been transmitted
    - Avoid idle time and deadlocks
    - 장점: faster
    
    Ex. `fread`, `fwrite`: non-blocking function에서는 read 함수 호출하자마자 return 되고 끝난다.
    

### ② Broadcast

- One processor sends the same information to many other processors

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2019.png)

### ③ Reduction

Ex. Every processor starts with a value and needs to know the sum of values stored on all processors

- A reduction combines data from all processors and returns it to a single process
- No processor can finish reduction before each processor has contributed a value.
- **Broadcast & Reduction can reduce programming complexity** and may be more efficient in some programs

### Example: Parallel Numerical Integration

적분 구간을 나눠서 계산하는 예제

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2020.png)

```c
static long num_steps = 10000

void main(int argc, char* argv[]) {
		int i_start, i_end, i, myid, numprocs;
		double pi, mypi, x, step, sum = 0.0;

		MPI_Init(&argc, &argv);
		MPI_Comm_size(MPI_COMM_WORLD, &numprocs);    // 프로세서 개수
		MPI_Comm_rank(MPI_COMM_WORLD, &myid);

		**MPI_BCAST**(&num_steps, 1, MPI, INIT, 0, MPI_COMM_WORLD);
		
		i_start = my_id * (num_steps/numprocs)
		i_end - i_start + (num_steps/numprocs)
		step = 1.0 / (double) num_steps;

		for (i = i_start; i < i_end; i++) {
			x = (i + 0.5) * step;
			sum = sum + 4.0 / (1.0 + x*x);
		}
		mypi = step * sum;    // 마지막에 한꺼번에 밑변을 곱한다

		**MPI_REDUCE**(&mypi, &pi, 1, MPI_DOUBLE, **MPI_SUM**, 0, MPI_COMM_WORLD);

		if (myid == 0)
			printf("Pi = %f\n", pi);
		MPI_Finalize();
	)
}
```

### Synchronization

: Coordination of simultaneous events(threads/processes) in order to obtain correct runtime order and avoid unexpected condition.
(동시 이벤트 조정)

<Types of synchronization>

- Barrier
    
    Any thread/process must stop at this point(barrier) and cannot proceed until all other threads/processes reach this barrier.
    
    ![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2021.png)
    

- Lock/semaphore
    - **Lock**: the first task acquires the lock. This task can then safely (serially) access the protected data or code.
        
        Other tasks can attempt to acquire the lock but must wait until the task that owns the lock releases it.
        
    - Semaphore: 세마포어 값을 5로 지정하면 5개의 task가 acquire lock.

# 3️⃣ Locality

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2022.png)

- communication: the slow accesses to “remote” data
- ******************************************************Algorithm should do most work on local data******************************************************
- Need to exploit spatial and temporal locality

### Locality of memory access (shared memory)

Parallel computation is ************************************************************************serialized due to memory contention************************************************************************ and lack of bandwidth.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2023.png)

A[0], A[4], A[8], A[12] 4개의 데이터가 같은 메모리 공간에 있다고 가정해보자.

이 경우 동시에 4개의 thread가 memory에 접근

→ 메모리 구조 상 한번에 접근 불가

→ serialized

⇒ 성능 증가를 위해서는 메모리 Distribution을 수정해야함.

Distribute data to relieve contention and increase effective bandwidth.

![기존의 memory interface](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2024.png)

기존의 memory interface

![수정한 memory interface](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2025.png)

수정한 memory interface

### Memory Access Latency in Shared Memory Architectures

- **UMA** (Uniform Memory Access)
    - processor(CPU) - Memory: 어떤 data에 접근하든 접근 시간 같다.
    - Centrally located memory
    
    ![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2026.png)
    

- **NUMA** (Non-Uniform Memory Access)
    - Memory가 physically partitioned, 하지만 Bus로 연결되어 있어 logically connected(accessible by all)
    - Processors have the same address space
    - How to organize data affects performance
    - CC-NUMA (**************************************Cache-Coherent NUMA**************************************)
    
    ![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2027.png)
    

### Cache Coherence(캐시 일관성)

: the uniformity of shared resouce data that ends up stored in multiple local caches.

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2028.png)

- Problem: When a processor modifies a shared variable in local cache,
different processors may have different value of the variable.
    - 여러 캐시에서 variable 복사본을 가질 수 있다
    - 한 프로세서가 수정하는 건 다른 프로세스가 알 수 없다
    - 수정된지 모르고 옛날 값을 계속 사용할 수 있다.
        
        → Visibility 또는 cache coherence를 유지하기 위한 대안이 필요하다. 
        

- **Snooping cache coherence**
    
    : 수정할 때마다 send all requests for data to all processors.
    
    - 작은 시스템에서는 사용 가능
    - expensive

- **Directory-based cache coherence**
    
    : directory에서 공유되는 변수는 kepp track
    
    - 모든 프로세서가 아닌, 특정 프로세서에게만 request 제공
    - Send point-to-point requests to processors

### 🌟 Shared Memory Architecture

: all processors to access all memory as global address space.

- 장점
    - Global address space provides a **user-friendly programming** perspective to memory. (제한이 없다)
    - Data sharing between tasks is both **fast and uniform** due to the proximity of memory to CPUs

- 단점
    - Lack of scalability between memory and CPUs
    - Programmer responsibility for ******************************synchronization****************************** (distributed system에서는 신경 안 써도 됨)
    - Expense: it becomes increasingly difficult and expensive to design and produce shared memory machines with ever increasing numbers of processors.
    

### 🌟 Distributed Memory Architecture

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2029.png)

- Only private(local) memory
- Independent
- require a communication network to connect inter-processor memory

- 장점
    - Scalable (확장하기 좋다)
    - Cost effective (싸다)
- 단점
    - Programmer responsibility of data communication
    - No global memory access
    - Non-uniform memory access time.

### 🌟 Hybrid Architecture

![Untitled](03%20Performance%20of%20Parallel%20Programs%20b7bfe451df434c159b351625a6de58e3/Untitled%2030.png)

- Combination of Shared/Distributed architecture
- Scalable
- Increased programmer complexity

### Example of Parallel Program: Ray tracing

각각의 픽셀에 대해 parallel program