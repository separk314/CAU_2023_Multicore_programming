#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#define NUM_THREADS 4
#define NUM_STEPS 100000
#define STEP 1.0/NUM_STEPS

double pi[NUM_THREADS];

void* run(void* thread_id) {
	double x, sum = 0.0;

	int my_id = (int*)thread_id;	
	// thread 0은 0~25000, thread1은 25000~50000 담당...
	// -> my_id로 offset 계산
	int i_start = my_id * (NUM_STEPS / NUM_THREADS);
	int i_end = i_start + (NUM_STEPS / NUM_THREADS);

	for (int i = i_start; i < i_end; i++) {
		x = (i + 0.5) * STEP;
		sum = sum + 4.0 / (1.0 + x * x);
	}

	printf("my_id: %d, sum=%.8lf\n", my_id, sum * sum);
	pi[my_id] = sum * STEP;
	pthread_exit(NULL);
}

int main(int argc, char* argv[]) {

	pthread_t threads[NUM_THREADS];
	int err_code, status;

	for (int t = 0; t < NUM_THREADS; t++) {
		err_code = pthread_create(&threads[t], NULL, run, (void*)t);

		if (err_code) {
			printf("ERROR code is %d\n", err_code);
			exit(-1);
		}
	}

	for (int i = 0; i < NUM_THREADS; i++)
		pthread_join(threads[i], (void**)&status);	
		// 이 코드에서는 status에 NULL값이 들어감

	// 원래는 for문 안에서 sum값을 다 더하지만, parallel한 동작을 위해서
	// sum값을 pi 배열에 저장해두었다가 main thread에서 한번에 더한다.
	double pi_sum = 0;
	for (int i = 0; i < NUM_THREADS; i++) 
		pi_sum = pi_sum + pi[i];

	printf("integration result=%.8lf\n", pi_sum);
	pthread_exit(NULL);
}