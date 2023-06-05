# 07-1: CUDA Introduction

- Moore’s Law
    
    : 트랜지스터 개수가 2년마다 2배로 늘어난다.
    
- Multicore Architecture의 필요성
    
    : high clock speed(frequency)를 구현하기 어렵다
    
    왜? power consumption, heat generation이 너무 크기 때문에.
    

## Many-core GPUs

- Many core가 왜 필요한가?
    
    ⇒ 실시간 고화질 3D 그래픽 구현을 위하여.
    
- parallel, multi-threaded, many-core 프로세서가 발전했다.
- **************GPGPU**************: General Purpose computing on Graphical Processing Unit
    
    전통적으로는 CPU에서 처리되던 general purpose 계산을 수행하기 위하여 GPU를 사용하는 것.
    

### Processor: Multicore vs Many-core

🟠 **Multicore: CPU: 2~8 cores**

- 일반적으로 general purpose 계산
- sequential 프로그램의 속도를 향상시키기 위해 노력한다
- 여러 코어들로 이동하는 과정이 복잡하다: out-of order 문제, instruction issue, 분기 예측(branch prediction), pipelining, 대용량 캐시…

🟠 ******Many-core: GPU: 100~3000 cores******

- parallel application의 실행 처리량(execution throughput)에 집중
- 단순하다: in-order, single instruction issue
- 다수의 작은 코어들
- Ex. Nnvidia GPU

# GPU

고도의 parallel application을 위해 만들어짐.

- high level language(C/C++) 사용
- 높은 GFLOPS(컴퓨터 성능 수치)
- 빠른 처리에는 높은 대역폭(bandwidth)가 필요하다.
- simpler memory model, 적은 제약 → 높은 대역폭 허용
- Memory bandwidth: 프로세서로 데이터를 읽거나 메모리에 저장하는 속도

### GPU is specialized for

- Compute-intensive(컴퓨팅 집약적)
- Highly **data parallel computation**
    
    같은 프로그램이 여러 데이터에서 parallel하게 실행된다.
    
    Ex. 행렬 계산
    
- Data caching, flow control보다 데이터를 처리하는 트랜지스터 수가 많아진다.

- Graphics rendering이 필요한 것은?
    
    Geometry + Pixel processing
    
- 많은 애플리케이션 개발자들이 집약적인(intensive) SW부분을 GPU로 이동했다.

### Application

- 3D rendering
    
    pixel, 정점(vertex) 세트들은 parallel thread에 매핑된다.
    
- 렌더링된 이미지의 후처리, 비디오 인코딩 및 디코딩, 이미지 스케일링, stereo vision, 패턴 인식: 이미지 블록과 픽셀을 parallel processing thread에 매핑할 수 있다.
- 많은 다른 알고리즘들은 data-parellel processing에 의해 빨라진다.
    
    일반적인 신호 처리/물리학 시뮬레이션부터 컴퓨터 금융, 컴퓨터 생물학까지.
    

# CPU vs GPU

<aside>
🔹 CPU와 GPU는 본질적으로 다른 design philosophy를 가진다.

</aside>

### CPU

: sequntial code 성능에 최적화되어 있다.

- 정교한 control logic
    - single thread의 명령어를 parallel하게 실행하기 위해.
    - 심지어 out-of-order의 경우에도 실행하기 위해.
    - branch prediction
- 대용량 캐시 메모리
    
    instruction과 data access latency를 줄이기 위해
    
- 강력한 ALU(산술 논리장치)
    
    operation latency를 줄이기 위해.
    
- latency를 최소화한다. (time to complete a task)

![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled.png)

### GPU

: multiple thread의 실행 처리량에 최적화되어 있다.

- 원래는 3D video game을 위해 만들어짐.
    
    (프레임 당 엄청난 수의 부동 소수점 계산이 필요)
    
- control logic, 캐시 메모리 최소화
    - chip area가 대부분 floating-point 계산 전용으로 사용된다.
    - 메모리 처리량(memory throughput) 향상
- 에너지 효율적인 ALU
- numeric computing engine으로 설계되었다.(데이터 병렬화)
- simple task를 처리하기 위해 디자인되었다.
- throughput을 최대화한다. (number of tasks in fixed time)

### Winning Applications Use Both CPU and GPU

- CPU가 성능이 더 좋은 부분들도 있을 것.
    
    그러니까 CPU와 GPU 둘 다 쓰자.
    
- sequential part는 CPU, 수적으로 많은 부분(numerically intensive)은 GPU 사용
- CUDA
    
    프로그래머들은 C/C++ programming tool을 사용할 수 있다.
    
    복잡한 그래픽 인터페이스를 사용하지 않아도 된다.
    

# GPU Architecture

![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%201.png)

- 다수의 simple core로 구성되어 있다.
- 고도로 스레드화된 SM(Streaming Multiprocessor) 배열
- 두 개 이상의 SM이 building block을 형성한다.

### GPU chip design

- GPU core는 stream processor이다.
- SM: Stream processor로 이루어진 그룹.
    
    SM는 기본적으로 SIMD 프로세서이다.
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%202.png)
    

# Why more parallelism?

- 애플리케이션은 계속해서 속도 향상을 요구한다.
- GPU를 잘 구현하면 sequential execution에 비해 100배 이상의  속도 향상을 달성할 수 있다.
- SuperComputing 애플리케이션
- 행렬 계산과 같이 데이터 병렬 계산이 필요한 모든 애플리케이션

# CUDA (Computer Unified Device Architecture)

: Parallel Computing Framework (NVIDIA에서 개발)

- GPGPU (General Purpose GPU)
- 병렬화를 transparent하게 확장하여 코어를 많이 사용한다.

### Compute Capability

- 컴퓨팅 장치의 일반 사양 및 특징
- major revision number과 mainor revision number로 정의된다.
    
    Ex. 1.3, 2.1 
    
    - 5: maxwell architecture
    - 3: Kepler architecture
    - 2: Fermi architecture
    - 1: Tesla architecture

### CUDA: main features

![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%203.png)

- Heterogeneous(이질적) 프로그래밍 모델
- CPU: host
- GPU: device

### CUDA Device and Threads

- Device
    - CPU(host)의 보조 프로세서.
    - DRAM(device memory)에 엑세스할 수 있다.
    - 스레드를 병렬로 실행한다.
    - 일반적으로 GPU이지만 다른 유형의 parallel processing device일 수도 있다.
- Data parallel 부분은 device kernel로 표현된다.
    
    device kernel은 많은 스레드 위에서 실행된다.
    
- GPU thread v.s. CPU thread
    
    GPU: lightweight (생성 overhead 매우 적다)
    
    full efficiency를 위해서는 1000개 스레드 필요하다.
    

### Processing Flow

1. Main memory의 데이터를 GPU memory로 복사
2. CPU: processing을 지시한다.
3. GPU가 각각의 코어를 병렬로 실행한다.
4. Main memory는 GPU memory에서 결과를 복사한다.

### Example 1: CUDA Hello world

```c
%%cu
#include <stdio.h>

__global__ void hello_world(void) {
    printf("Hello World \n");
}

int main(void) {
    hello_world<<<1, 5>>>();
    cudaDeviceSynchronize();
    return 0;
}
```

- 결과
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%204.png)
    

### C Language Extensions

- **Function Type Qualifiers: `__global__`**
    - device(GPU)로 실행되는 부분이다.
    - host(CPU)에서 부를 수 있다.
    - 함수는 반드시 void type을 반환해야 한다.
    - __global__함수 호출은 해당 호출에 대한 execution configuration을 지정해야 한다.
    
- **Execution configuration**
    
    <<<Grid 당 block 개수, Block 당 Thread 개수>>>
    
    방법 1. <<<int, int>>>
    
    x값만 바뀐다.
    
    방법 2. <<<dim3, dim3>>>
    
    ```c
    dim3 blocks(65535,65535,1)
    dim3 threads(1024,1,1)
    <<<blocks,threads>>>
    ```
    
- **Built-in Variables**
    
    blockIdx = (blockIdx.x, blockIdx.y, blockIdx.z)
    
    threadIdx = (threadIdx.x, threadIdx.y, threadIdx.z)
    
- **Built-in Vector types**
    
    dim3: 차원을 표기하기 위한 Integer vector type
    

### Grid, Block, Thread

![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%205.png)

- Grid 당 최대 Block size
: 65535 * 65535 * 1
- Block 당 최대 Thread 사이즈
: 1024 * 1024 * 64개
- Block 당 최대 Thread 수
: 1024개

### Example 2-1: dim3를 사용하지 않은 blockIdx, threadIdx

```c
%%cu
#include <stdio.h>

__global__ void exec_conf(void) {
    int ix = threadIdx.x + blockIdx.x * blockDim.x;
    printf("gridDim = (%d, %d, %d), blockDim = (%d, %d, %d) \n",
           gridDim.x, gridDim.y, gridDim.z,
           blockDim.x, blockDim.y, blockDim.z);
    
    printf("blockIdx = (%d, %d, %d), threadIdx = (%d, %d, %d), arrayIdx: %d \n",
           blockIdx.x, blockIdx.y, blockIdx.z,
           threadIdx.x, threadIdx.y, threadIdx.z, ix);
}

int main(void) {
    exec_conf<<<2, 3>>>();
    cudaDeviceSynchronize();
    return 0;
}
```

- 결과
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%206.png)
    
    모든 스레드는 같은 gridDim, blockDim을 가진다.
    
    - gridDim = (gridDim.x, gridDim.y, gridDim.x) = (2, 1, 1)
    - blockDim = (blockDim.x, blockDim.y, blockDim.z) = (3, 1, 1)
    
    첫 번째 blockIdx는 (0, 0, 0), 두 번째 blockIdx는 (1, 0, 0)
    
    각 block의 스레드의 threadIdx는 각각 (0, 0, 0), (1, 0, 0), (2, 0, 0)
    
    즉, x값만 바뀌었다.
    
    따라서 각 thread의 identical index는 ix = **threadIdx.x + blockIdx.x * blockDim.x**
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%207.png)
    

### Example 2-2: dim3 사용

```c
%%cu
#include <stdio.h>

__global__ void exec_conf(void) {
    int ix = threadIdx.x + blockIdx.x * blockDim.x;
    int iy = threadIdx.y + blockIdx.y * blockDim.y;

    printf("gridDim = (%d, %d, %d), blockDim = (%d, %d, %d) \n",
           gridDim.x, gridDim.y, gridDim.z,
           blockDim.x, blockDim.y, blockDim.z);
    
    printf("blockIdx = (%d, %d, %d), threadIdx = (%d, %d, %d), arrayIdx: (%d, %d) \n",
           blockIdx.x, blockIdx.y, blockIdx.z,
           threadIdx.x, threadIdx.y, threadIdx.z, ix, iy);
}

int main(void) {
    dim3 blocks(2, 2, 1);
    dim3 threads(2, 2, 2);
    exec_conf<<<blocks, threads>>>();
    cudaDeviceSynchronize();
    return 0;
}
```

- 결과
    
    ```c
    gridDim = (2, 2, 1), blockDim = (2, 2, 2) 
    gridDim = (2, 2, 1), blockDim = (2, 2, 2) 
    gridDim = (2, 2, 1), blockDim = (2, 2, 2) 
    ...
    blockIdx = (0, 1, 0), threadIdx = (0, 0, 0), arrayIdx: (0, 2) 
    blockIdx = (0, 1, 0), threadIdx = (1, 0, 0), arrayIdx: (1, 2) 
    blockIdx = (0, 1, 0), threadIdx = (0, 1, 0), arrayIdx: (0, 3) 
    blockIdx = (0, 1, 0), threadIdx = (1, 1, 0), arrayIdx: (1, 3) 
    blockIdx = (0, 1, 0), threadIdx = (0, 0, 1), arrayIdx: (0, 2) 
    blockIdx = (0, 1, 0), threadIdx = (1, 0, 1), arrayIdx: (1, 2) 
    blockIdx = (0, 1, 0), threadIdx = (0, 1, 1), arrayIdx: (0, 3) 
    blockIdx = (0, 1, 0), threadIdx = (1, 1, 1), arrayIdx: (1, 3) 
    blockIdx = (0, 0, 0), threadIdx = (0, 0, 0), arrayIdx: (0, 0) 
    blockIdx = (0, 0, 0), threadIdx = (1, 0, 0), arrayIdx: (1, 0) 
    blockIdx = (0, 0, 0), threadIdx = (0, 1, 0), arrayIdx: (0, 1) 
    blockIdx = (0, 0, 0), threadIdx = (1, 1, 0), arrayIdx: (1, 1) 
    blockIdx = (0, 0, 0), threadIdx = (0, 0, 1), arrayIdx: (0, 0) 
    blockIdx = (0, 0, 0), threadIdx = (1, 0, 1), arrayIdx: (1, 0) 
    blockIdx = (0, 0, 0), threadIdx = (0, 1, 1), arrayIdx: (0, 1) 
    blockIdx = (0, 0, 0), threadIdx = (1, 1, 1), arrayIdx: (1, 1) 
    blockIdx = (1, 1, 0), threadIdx = (0, 0, 0), arrayIdx: (2, 2) 
    blockIdx = (1, 1, 0), threadIdx = (1, 0, 0), arrayIdx: (3, 2) 
    blockIdx = (1, 1, 0), threadIdx = (0, 1, 0), arrayIdx: (2, 3) 
    blockIdx = (1, 1, 0), threadIdx = (1, 1, 0), arrayIdx: (3, 3) 
    blockIdx = (1, 1, 0), threadIdx = (0, 0, 1), arrayIdx: (2, 2) 
    blockIdx = (1, 1, 0), threadIdx = (1, 0, 1), arrayIdx: (3, 2) 
    blockIdx = (1, 1, 0), threadIdx = (0, 1, 1), arrayIdx: (2, 3) 
    blockIdx = (1, 1, 0), threadIdx = (1, 1, 1), arrayIdx: (3, 3) 
    blockIdx = (1, 0, 0), threadIdx = (0, 0, 0), arrayIdx: (2, 0) 
    blockIdx = (1, 0, 0), threadIdx = (1, 0, 0), arrayIdx: (3, 0) 
    blockIdx = (1, 0, 0), threadIdx = (0, 1, 0), arrayIdx: (2, 1) 
    blockIdx = (1, 0, 0), threadIdx = (1, 1, 0), arrayIdx: (3, 1) 
    blockIdx = (1, 0, 0), threadIdx = (0, 0, 1), arrayIdx: (2, 0) 
    blockIdx = (1, 0, 0), threadIdx = (1, 0, 1), arrayIdx: (3, 0) 
    blockIdx = (1, 0, 0), threadIdx = (0, 1, 1), arrayIdx: (2, 1) 
    blockIdx = (1, 0, 0), threadIdx = (1, 1, 1), arrayIdx: (3, 1)
    ```
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%208.png)
    

### Example 3: Vector Sum

```c
%%cu
#include <stdio.h>

const int N = 10;

__global__ void add(int *a, int *b, int *c) {
    int tid = threadIdx.x;
    c[tid] = a[tid] + b[tid];
}

int main(void) {
    int a[N], b[N], c[N];          // Host(CPU)에서 사용되는 배열
    int *dev_a, *dev_b, *dev_c;    // Device(GPU)에서 사용되는 배열

		// GPU 메모리에 배열을 위한 메모리 할당
    cudaMalloc( (void**)&dev_a, N * sizeof(int) );
    cudaMalloc( (void**)&dev_b, N * sizeof(int) );
    cudaMalloc( (void**)&dev_c, N * sizeof(int) );

    for (int i=0; i<N; i++) {
        a[i] = -i;
        b[i] = i*i;
    }

		// CPU에서 GPU로 배열 복사
    cudaMemcpy( dev_a, a, N*sizeof(int), cudaMemcpyHostToDevice );
    cudaMemcpy( dev_b, b, N*sizeof(int), cudaMemcpyHostToDevice );

    add<<<1, N>>>(dev_a, dev_b, dev_c);

		// GPU에서 CPU로 결과(배열 c)를 복사
    cudaMemcpy(c, dev_c, N*sizeof(int), cudaMemcpyDeviceToHost);

    for (int i=0; i<N; i++) {
        printf("%d + %d = %d \n", a[i], b[i], c[i]);
    }

		// GPU 메모리에 할당된 배열들을 해제 
    cudaFree (dev_a);
    cudaFree (dev_b);
    cudaFree (dev_c);

    return 0;
}
```

- 결과
    
    ![Untitled](07-1%20CUDA%20Introduction%20658e34426c4543369c326e3afeaa55c6/Untitled%209.png)
    
    - 왜 굳이 cpu, gpu 배열을 따로 만들어서 사용하는 건가?
        
        GPU가 병렬 처리에 특화되어 있기 때문이다.
        
        CUDA 프로그래밍에서는 CPU와 GPU 간에 데이터를 복사하고, GPU에서 연산을 수행한 후 결과를 다시 CPU로 복사해와야 된다.
        
    - 왜 gpu에서는 배열로 할당하지 않고 `cudaMalloc`, `cudaMemcpy`를 사용하는가?
        
        일반적으로 안전성과 효율성을 위해 `cudaMalloc`, `cudaMemcpy`를 사용한다.
        
    - 메모리 할당 부분이 이해가 잘 안 된다. 왜 `void**` 형태의 이중 포인터로 형변환?
        
        원래 cudaMalloc 함수의 첫 번째 인수는 `void**` 형식으로 전달되어야 한다.
        
        ```c
        cudaError_t cudaMalloc(void** devPtr, size_t size);
        ```
        
        왜냐하면 `cudaMalloc` 함수가 호출되고 나서, 할당된 디바이스 메모리 주소를 포인터로 반환하기 때문이다. 
        
        이를 위해 `devPtr` 매개변수를 `void**` 형식으로 전달하여, 해당 주소를 수정할 수 있게 해야 한다.
        
        참고로, `void*`는 어떤 type의 메모리 주소를 가리킬 수 있는 일반적인 포인터 type이다.
        (int*는 int 타입의 메모리 주소를 가리키고, float*은 float 타입을 가리키는 것처럼.)
        
        즉, `void*`는 메모리 주소에 대한 일종의 “일반 포인터”이다.
        
        따라서, `void**` 형식으로 전달하면, cudaMalloc 함수가 할당된 메모리 주소를 해당 포인터에 저장할 수 있다. (포인터의 포인터니까, 포인터값를 수정할 수 있다.)