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

![image](https://file.notion.so/f/s/486157ad-2eff-4bf3-9185-4e4e3d28c59f/Untitled.png?id=e9381d8f-870d-4a5e-a9ae-a2e3ddef0527&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682962474360&signature=HLFFcqRrVw8OUzNMJJE6u4xkbMzRHBvyzvd68f7Il_8&downloadName=Untitled.png)

- 각각 L1 cache를 가진다
- L2 cache는 공유
- Fast communication through shared L2
- Coherent shared memory (일관된 공유 메모리)

### AMD Dual Core

![image](https://file.notion.so/f/s/96a6f14e-67fe-4505-b3cf-fab1098a3c04/Untitled.png?id=dc6bc5b9-e340-43c9-a63c-4b2ccad9f290&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682962494231&signature=VXwgIEuYee7QizCjqskshREQ8EMEuPto8U-NeRKd4Dg&downloadName=Untitled.png)

- 각각 L1, L2 cache를 가짐
- Coherent shared memory

## 2️⃣ Symmetric Multi-Processor (SMP)

Ex. Sun Fire E25K

![image](https://file.notion.so/f/s/d001fd4f-8bb1-4ad6-9e2e-415021f47851/Untitled.png?id=d04c7bbb-704f-4332-a50a-df8d288ad49b&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682962517102&signature=0eCnR6M6jBujexiiUdSDqOO1KA9FW0ZhYxC_g5HWNAc&downloadName=Untitled.png)

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

![image](https://file.notion.so/f/s/a6498681-3eb4-4efa-8d06-5d18fef5e3ad/Untitled.png?id=723b7d44-0e6b-42f1-a6ef-677500ffb862&table=block&spaceId=f33e0516-75c1-4b3e-b02d-479e88e873e0&expirationTimestamp=1682962532804&signature=DTolAEMlG3E5bcsGIGcfAZgcr88M2wijh06Sp0g1lp4&downloadName=Untitled.png)

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