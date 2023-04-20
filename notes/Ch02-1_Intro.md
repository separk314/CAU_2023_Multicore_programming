# 02-1: Intro

<aside>
💡 CPU == Processor == Core

</aside>

# Multicore Processor

: A single computing component with two or more independent cores.

- Core(CPU): computing unit that reads/executes program instructions.
- 여러 개의 코어들이 동시에 여러 instructions를 돌린다.
Multiple cores run multiple instructions at the same time.
- Multicore processor의 성능은 software algorithms와 implement에 의존한다.

### GPU

: CPU와 다르게, 매우 많은 코어들을 가진 multicore architectures

- CUDA: Compute Unified Device Architecture
    
    parallel computing platform and programming model created by NVIDIA
    
    **GPGPU**(General Purpose Graphics Processing Unit)
    
- OpenCL

# Parallel Computing

### Parallel computing

: **using multiple processors in parallel** to solve problems more quickly than with a single processor.

<Examples of parallel machines>

- A **cluster computer** that contains multiple PCs **combined together with a high speed network.**
- A ********************************************************shared memory multiprocessor******************************************************** by connecting multiple processors to a single memory system.
- A ****************Chip Multi-Processor**************** (******CMP******) contains multiple processors(called cores) on a single chip.

### Parallel Applications

- Image, video processing (encoding, decoding, filtering)
- 3D graphics (rendering, animation)
- 3D gaming
- Simulation (protein folding, climate modeling)
- Machine learning, deep learning

### Good Parallel Program

Writing good parallel programs should be

- Correct (result)
- Good performance
- Scalability
- Load Balance
- Portability
- Hardware Specific Utilization

### Thread/Process

둘 다 Independent sequence of execution

- Process = ************************************************************************An independent program in execution************************************************************************
    - run in separate memory address space
- Thread
    - run in shared memory space in process
    - One process may have multiple threads
- Multi-threaded Program
    
    : a program running with multiple threads that is executed simultaneously.
    
    ![Untitled](02-1%20Intro%2021b6d4e0f3fb4188b9ab26b39e90bf11/Untitled.png)
    

### 1️⃣ Parallelism vs Concurrency

- Parallel Programming
    
    : Using additional computational resources to produce an answer faster.
    
    - **Using multiple processors in parallel** to solve problems more quickly than with a single processor.
    - Ex. summing up all the numbers in an array with multiple n processors

- Concurrent Programming
    
    : Correctly and efficiently controlling access **by multiple threads to shared resources.**
    
    - Ex. Multiple threads access the same hash-table

### 2️⃣ Parallel Computing vs Distributed Computing

- Parallel Computing
    - all processors may have access to a shared memory to exchange information between processors.
- Distributed Computing
    
    : a collection of autonomous computer systems that are **physically separated, but are connected by a centralized computer network** that is equipped with distributed system software.
    
    - multiple computers communicate through network.
    - **each processor has its own private memory** (distributed memory)
    - executing sub-tasks on different machines and then merging the results.

<aside>
💡 - Cluster Computing: 여러 대의 컴퓨터가 하나의 컴퓨터처럼 동작
- Distributed Computing: 상대적으로 느슨히 결합. 여러 하위 서비스로 분할될 수 있다.

</aside>

![Untitled](02-1%20Intro%2021b6d4e0f3fb4188b9ab26b39e90bf11/Untitled%201.png)

<Distributed Computing 특징>

1. Heterogeneity: 컴퓨터들이 달라도 O
2. Transparency: invisible, 복잡성을 숨긴다.
3. Fault tolerance: 지진, 화재 등의 문제가 발생해도 operate properly.
4. Scalability: if we want to add more resources → increase scale
5. Concurrency
6. Openness

1. Resource sharing

### 3️⃣ Cluster Computing vs Grid Computing

- Cluster Computing
    
    : a set of **loosely connected** computers that work together so that in many respects they can be **viewed as a single system**
    
    - good price/performance
    - memory not shared
- **Grid Computing**
    
    : federation of computer resources from multiple locations to reach a common goal (a large scale distributed system)
    
    - Grids tend to be **more loosely coupled**, **heterogeneous**, and geographically dispersed.
    - Ex. protein folding
    

# Cloud Computing

: **shares networked computing resources** rather than having local servers or personal devices to handle applications.

- 반대말: local computing, local server
- Ex. AWS, Google cloud, Microsoft Azure
- “Cloud” == “internet” == “a type of Internet-based computing”
- different services(servers, storage, applications) are delivered to an user’s computers and smart phones through the Internet.

- **Why Cloud Computing?**
    
    IT service를 만들 때 physical space를 직접 사지 않아도 된다.
    
    즉, initial setup이 필요없다.