# 02-2: Intro

# Moore’s Law

: **Doubling of the number of transistors** on integrated circuits roughly **every two years.**

(2년마다 회로의 트랜지스터 수가 2배씩 늘어난다.)

- Micro-processors have become smaller, denser, and more powerful
- processing speed, memory capacity, sensors and even the number of pixels
- All of these are improving at (roughly) exponential rates

### Computer Hardware Trend

- Clock speed는 빨라지지 않는다.
    
    In high clock speed, **power consumption and heat generation is too high to be tolerated.**
    
- No more hidden parallelism(ILP; instruction level parallelism to be found)
- Transistor # is still rising
- Clock speed flattening sharply

⇒ Need Multicore programming!

# Examples of Parallel Computer

## 1️⃣ Chip Multi-Processor (CMP)

### Intel Core Duo

![Untitled](02-2%20Intro%2035421e169c214337b0d276e532de98a0/Untitled.png)

- 각각 L1 cache를 가진다
- L2 cache는 공유
- Fast communication through shared L2
- Coherent shared memory (일관된 공유 메모리)

### AMD Dual Core

![Untitled](02-2%20Intro%2035421e169c214337b0d276e532de98a0/Untitled%201.png)

- 각각 L1, L2 cache를 가짐
- Coherent shared memory

## 2️⃣ Symmetric Multi-Processor (SMP)

Ex. Sun Fire E25K

![Untitled](02-2%20Intro%2035421e169c214337b0d276e532de98a0/Untitled%202.png)

- 여러 프로세서들이 single shared memory에 연결됨
- single OS
- 요즘 가장 널리 쓰이는 multi-processor system
- Shared bus에서 병목 현상

## 3️⃣ Heterogeneous Chips

Cell processor

- Main CPU performs hard to parallelize portion
(메인 CPU는 부분적으로 병렬화하기 어렵다)
- Attached processor(GPU) performs compute intensive parts

## 4️⃣ Clusters

## 5️⃣ Super-computers

# GPGPU: NVIDIA GPU

**General Purpose Graphical Processing Unit**

computation↑

# Summary

- 현재 모든 컴퓨터들은 parallel computers
- Multi-core processors를 사용한 새로운 트렌드
    - Decreased power consumption and heat generation
    - Minimized wire lengths and interconnect latencies
    
    → 진정한 thread-level parallelism 가능
    
- 성능을 위해서 applications은 multi-threaded model로 바꾸어야 한다.

# Why writing (fast) parallel programs is hard

### Principles of Parallel Computing

- Finding enough parallelism (Amdahl’s Law, 암달의 법칙)
- Granularity (세분화, 세분성)
- Locality
- Load balance
- Coordination and synchronization

### Finding Enough Parallelism

- application의 일부분만 parallel하다고 가정해보자.
- **Amdahl’s law**: Even if the parallel part speeds up perfectly, performance is limited by the sequential part.
    
    프로그램은 병렬처리가 가능한 부분과 불가능한 sequential 부분으로 구성되므로, 프로세서를 아무리 병렬화 시켜도 더 이상 성능이 향상되지 않는 한계가 존재한다.
    
    - s: sequentially 이루어지는 부분
    - 1-s: parallel 가능한 부분
    - P: # of processors
    - Speedup(P): 프로세서 p개를 썼을 때 성능이 몇 배 향상되는가
    
    Speedup(P) = Time(1) / Time(P)
    
    ≤ 1 / (s + (1-s))
    
    ≤ 1 / s
    

### Overhead of Parallelism

- Cost of **starting a thread or a process**
- Cost of **communicating shared data**
- Cost of **synchronizing**
- extra (redundant) computation

### Locality and Parallelism

![Untitled](02-2%20Intro%2035421e169c214337b0d276e532de98a0/Untitled%203.png)

- Parallel processors, collectively, have **large, fast cache**
    
    (Parallel 프로세서는 전체적으로 대용량의 빠른 캐시를 제공한다)
    
    - the **slow accesses to “remote” data** we call “**communication**”
        
        (원격 데이터에 대한 느린 엑세스를 “통신”이라고 한다)
        
- **Algorithm should do most work on local data**

### Load Imbalance

: the time that some processors in the system are idle due to

- insufficient parallelism (during that phase)
- unequal size tasks

→ Algorithm needs to balance load