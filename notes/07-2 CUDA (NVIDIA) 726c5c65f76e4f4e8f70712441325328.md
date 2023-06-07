# 07-2: CUDA (NVIDIA)

# Heterogeneous Computing

- Host: CPU와 CPU memory
- Device: GPU의 GPU memory

### Simple Processing Flow

1. CPU memory의 데이터를 GPU memory로 복사한다.
2. GPU program을 실행한다. (성능을 위해 데이터를 캐싱하면서)
3. 결과를 GPU에서 CPU memory로 복사한다.

### Hello world! with Device code

nvcc(NVIDIA compiler)는 소스코드를 host와 device로 분리한다.

- Device function은 nvcc가 실행한다.
- Host function(ex. main)은 standard host compiler가 실행한다.

<<<  >>>: host 코드가 device 코드를 부르는 표시이다.

- “kernel lanuch”라고도 불린다.

### Example: Vector Addition(단순한 숫자 덧셈)

```c
__global__ void add(int *a, int *b, int *c) {
    *c = *a + *b;
}
```

- add()는 host가 호출해서, device에서 실행된다.
- add()가 device에서 실행되기 때문에, a b c는 device memory를 가리켜야 하고,
따라서 GPU 메모리에 공간을 할당해주어야 한다.
    - `cudaMalloc()`, `cudaFree()`, `cudaMemcpy()`를 사용한다.

이제 main() 함수를 보자.

```c
int main(void) {
    int a, b, c;
    int *d_a, *d_b, *d_c;
		int size = sizeof(int);

		// GPU 메모리에 배열을 위한 메모리 할당
    cudaMalloc( (void**)&d_a, size );
    cudaMalloc( (void**)&d_b, size );
    cudaMalloc( (void**)&d_c, size );

		// input value 세팅
		a = 2;
		b = 7;    

		// CPU에서 GPU로 배열 복사
    cudaMemcpy( d_a, &a, size, cudaMemcpyHostToDevice );
    cudaMemcpy( d_b, &b, size, cudaMemcpyHostToDevice );

		// add() 함수를 GPU kernel에서 시작
    add<<<1, 1>>>(d_a, d_b, d_c);

		// GPU에서 CPU로 결과(배열 c)를 복사
    cudaMemcpy(&c, d_c, size, cudaMemcpyDeviceToHost);

		// GPU 메모리에 할당된 배열들을 해제 
    cudaFree (d_a);
    cudaFree (d_b);
    cudaFree (d_c);

    return 0;
}
```

# Blocks: Running in Parallel

이제 parallel하게 작동시키고 싶다.

→ add()를 한 번만 실행하지 말고, parallel하게 N번 실행해보자.

→ **********block********** 사용! 

`blockIdx.x`를 사용해서 block index에 접근하면, 각 block이 각각의 index를 맡는다.

```c
__global__ void add(int *a, int *b, int *c) {
    c[blockIdx.x] = a[blockIdx.x] + b[blockIdx.x];
}
```

![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled.png)

main() 함수

```c
int main(void) {
    int ***a, *b, *c**;
    int *d_a, *d_b, *d_c;    // Device(GPU)에서 사용되는 배열
		int size = **N *** sizeof(int)

		// GPU 메모리에 배열을 위한 메모리 할당
    cudaMalloc( (void**)&d_a, size );
    cudaMalloc( (void**)&d_b, size );
    cudaMalloc( (void**)&d_c, size );

    for (int i=0; i<N; i++) {
        a[i] = -i;
        b[i] = i*i;
    }

		// CPU에서 GPU로 배열 복사
    cudaMemcpy( dev_a, a, size, cudaMemcpyHostToDevice );
    cudaMemcpy( dev_b, b, size, cudaMemcpyHostToDevice );

    add<<<N, 1>>>(d_a, d_b, d_c);

		// GPU에서 CPU로 결과(배열 c)를 복사
    cudaMemcpy(c, d_c, size, cudaMemcpyDeviceToHost);

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

add<<<N, 1>>>: N개의 add() copy를 시작한다.

# Threads

- block 사용: `blockIdx.x`로 접근
    
    ![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%201.png)
    
    ```c
    __global__ void add(int *a, int *b, int *c) {
    		c[blockIdx.x] = a[blockIdx.x] + b[blockIdx.x];
    }
    ```
    
- thread 사용: `threadIdx.x`로 접근
    
    ![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%202.png)
    
    ```c
    __global__ void add(int *a, int *b, int *c) {
    		c[threadIdx.x] = a[threadIdx.x] + b[threadIdx.x];
    }
    ```
    

# Indexing: Combining threads and blocks

block 당 8개의 thread를 가지는 예시를 생각해보자.

![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%203.png)

block 당 M개의 thread를 가질 때, thread index는 

int index = **threadIdx.x + blockIdx.x * M**

이때, M 대신 built-in variable인 `blockDim.x`를 사용하면 된다.

int index = **threadIdx.x + blockIdx.x * blockDim.x**

따라서 add() 함수는 다음과 같이 수정할 수 있다.

```c
__global__ void add(int *a, int *b, int *c) {
		int index = threadIdx.x + blockIdx.x * blockDim.x;
		c[index] = a[index] + b[index];
}
```

main() 함수

```c
#define N **(2048*2048)**
#define THREADS_PER_BLOCK 512

int main(void) {

		int *a, *b, *c; // host copies of a, b, c
		int *d_a, *d_b, *d_c; // device copies of a, b, c
		int size = N * sizeof(int);

		// Alloc space for device copies of a, b, c
		cudaMalloc((void **)&d_a, size);
		cudaMalloc((void **)&d_b, size);
		cudaMalloc((void **)&d_c, size);

		// Alloc space for host copies of a, b, c and setup input values
		a = (int *)malloc(size); random_ints(a, N);
		b = (int *)malloc(size); random_ints(b, N);
		c = (int *)malloc(size);

		// Copy inputs to device
		cudaMemcpy(d_a, a, size, cudaMemcpyHostToDevice);
		cudaMemcpy(d_b, b, size, cudaMemcpyHostToDevice);

		// Launch add() kernel on GPU
		add<<<**N/THREADS_PER_BLOCK,THREADS_PER_BLOCK**>>>(d_a, d_b, d_c);

		// Copy result back to host
		cudaMemcpy(c, d_c, size, cudaMemcpyDeviceToHost);

		// Cleanup
		free(a); free(b); free(c);
		cudaFree(d_a); cudaFree(d_b); cudaFree(d_c);

		return 0;
}
```

- block 개수 = **gridDim.x** = 2048 * 2048 / 512개 = 8192개
- block 당 thread 개수 = **blockDim.x** = 512개
- 총 thread 개수: 2048 * 2048개

### 임의의 Vector size 설정하기

- 일반적인 문제는 blockDim의 배수가 아니다.
- Array의 끝을 넘어서 접근하지 않도록 하자. (??)

```c
__global__ void add(int *a, int *b, int *c, int n) {
		int index = threadIdx.x + blockIdx.x * blockDim.x;
		if (index < n)
			 c[index] = a[index] + b[index];
}
```

```c
add<<<**(N + M-1) / M**, M>>>(d_a, d_b, d_c, **N**);
```

Thread가 복잡해 보이지만, 우리는 다음과 같은 이유로 thread를 사용한다.

- Communicate
- Synchronize

# Shared memory, __syncthreads()

### New example: 1D Stencil

1D Stencil: 배열의 각 원소에 대하여, 해당 원소와 이웃한 원소들을 조합하여 값을 계산하는 알고리즘.

- 각 출력 원소가 입력 원소들 중 일정한 반경(radius) 내에 있는 원소들의 합으로 계산된다.
- radius가 3인 경우, 출력 원소는 자신과 좌우 3개의 입력 원소를 포함한, 총 7개의 원소들의 합으로 계산된다.

![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%204.png)

### Implementing Within a Block

![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%205.png)

- radius가 3일 때
    - Input element는 여러 번 읽힌다.
    - 각 thread는 하나의 output element를 계산한다.

### Sharing Data Between Threads

- Block 안에서 thread는 ****************************shared memory****************************를 통해 데이터를 공유한다.
- `__shared__` 키워드로 표시한다.

- 공유 메모리에 **데이터 캐시(Cache Data)**
    - global memory에서 (blockDim.x + 2*radius) 만큼의 input element를 읽어온다.
    - (blockDim.x) 개의 output element를 계산한다.
    - (blockDim.x) output element를 global memory에 쓴다.
- 각 블록은 각 경계에 반지름 만큼의 halo가 필요하다.
    
    ![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%206.png)
    

### Example: Stencil Kernel

```c
__global__ void stencil_1d(int *in, int *out) {
		**__shared__** int temp[BLOCK_SIZE + 2 * RADIUS];
		int gindex = threadIdx.x + blockIdx.x * blockDim.x;
		int lindex = threadIdx.x + RADIUS;
		// lindex: temp 배열에서 threadIdx.x의 위치.
		// 배열 앞에 radius만큼 더 읽어왔으므로 radius를 더해줘야 threadIdx.x의 위치이다.

		// Read input elements into shared memory(이 부분의 밑 그림 참고)
		temp[lindex] = in[gindex];    // 전체 배열(in)에서 현재 threadIdx.x값 읽어오기
		if (threadIdx.x < RADIUS) {   // radius보다 작으면 그 전의 값도 읽어와야 한다.
				temp[lindex - RADIUS] = in[gindex - RADIUS];
				temp[lindex + BLOCK_SIZE] = in[gindex + BLOCK_SIZE];    // 오른쪽값도 읽음.
		}

		// Apply the stencil(자기를 포함한 반경 radius만큼의 값을 더한다.)
		int result = 0;
		for (int offset = -RADIUS ; offset <= RADIUS ; offset++)
				result += temp[lindex + offset];

		// Store the result
		out[gindex] = result;
}
```

![Untitled](07-2%20CUDA%20(NVIDIA)%20726c5c65f76e4f4e8f70712441325328/Untitled%207.png)

- 각자(thread)가 자기 값을 global memory(in 배열)에서 읽어서 공유 메모리(temp 배열)에 복사해둔다.
- 각자가 복사한 값으로 만들어진 temp 배열은 다같이 공유해서 사용한다.
- Radius보다 작은 threadIdx.x를 갖는 스레드인 경우, 예를 들어 threadIdx.x == 1인 경우,
배열의 맨 왼쪽과 오른쪽에 radius만큼의 값을 더 읽어온다.

### __syncthreads()

이렇게 코드를 구성하면 ********************************안 돌아간다!!!********************************

15개의 스레드 중 몇 개의 스레드가 temp 배열에 값을 복사하는 과정이 늦었다고 생각해보자. 
다른 스레드들은 공유 데이터를 쓸 수가 없다.

⇒ `__syncthreads()` 사용

- block의 모든 스레드들을 동기화한다.
- 모든 스레드들은 이 barrier에 도달해야 한다.

```c
__global__ void stencil_1d(int *in, int *out) {
		__shared__ int temp[BLOCK_SIZE + 2 * RADIUS];
		int gindex = threadIdx.x + blockIdx.x * blockDim.x;
		int lindex = threadIdx.x + radius;

		// Read input elements into shared memory
		temp[lindex] = in[gindex];
		if (threadIdx.x < RADIUS) {
				temp[lindex – RADIUS] = in[gindex – RADIUS];
				temp[lindex + BLOCK_SIZE] = in[gindex + BLOCK_SIZE];
		}

		// Synchronize (ensure all the data is available)
		**__syncthreads();**

		// Apply the stencil
		int result = 0;
		for (int offset = -RADIUS ; offset <= RADIUS ; offset++)
		result += temp[lindex + offset];

		// Store the result
		out[gindex] = result;
}
```

# Managing the device

### Asynchronous operation

- Kernel launches are asynchronous. Control returns to the CPU immediately.
    
    즉, GPU에서 커널을 실행하는 동안 CPU는 기다리지 않고 다른 작업을 수행할 수 있다.
    
    이러한 비동기 실행은 CPU와 GPU 간의 작업을 효율적으로 분리하여 병렬 처리를 가능하게 하고, 시스템의 전체 성능을 향상시킬 수 있다.
    
    그러나 CPU와 GPU 사이의 동기화와 데이터 일관성을 관리해주어야 한다.
    
- CPU가 결과를 처리하기 전에 동기화를 해주어야 한다.
    - `cudaMemcpy()`: copy가 끝나기 전까지 CPU를 block한다. copy는 모든 CUDA가 끝나면 시작된다.
    - `cudaMemcpyAsync()`: Asynchronous하기 때문에 CPU를 block하지 않는다.
    - `cudaDeviceSynchronize()`: CUDA가 모두 끝날 때까지 CPU를 block한다.
    

### Handling errors

모든 CUDA API는 error code(`cudaError_t`)를 반환한다.

(API 그 자체의 에러 또는 kernel과 같이 asynchronous operation에서의 에러)

- `cuda Error_t cudaGetLastError(void)`: 마지막 에러를 가져온다
- `char *cudaGetErrorString(cudaError_t)`: error를 string으로 변

printf("%s\n", cudaGetErrorString(cudaGetLastError())); 의 형태로 사용한다.

### Managing devices

Application은 GPU를 질의하고 선택할 수 있다.

- `cudaGetDeviceCount(int *count)`
- `cudaSetDevice(int device)`
- `cudaGetDevice(int *device)`
- `cudaGetDeviceProperties(cudaDeviceProp *prop, int device)`

여러 CPU 스레드들이 device를 공유해서 사용할 수 있다.

하나의 CPU thread는 여러 개의 device를 관리할 수 있다.