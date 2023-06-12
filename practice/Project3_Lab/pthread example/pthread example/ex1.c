/*
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#define NUM_THREADS 4

void* BusyWork(void* t)
{
	long tid = (long)t;
	double result = 0.0;
	printf("Thread %ld starting...\n", tid);

	for (int i = 0; i < 1000000; i++)
	{
		result = result + sin(i) * tan(i);
	}

	printf("Thread %ld done. Result = %e\n", tid, result);
	pthread_exit((void*)t);
}

int main()
{
	pthread_t thread[NUM_THREADS];
	int err_code;
	void* status;

	for (long i = 0; i < NUM_THREADS; i++) {
		printf("Main: creating thread %ld\n", i);
		err_code = pthread_create(&thread[i], NULL, BusyWork, (void*)i);
		if (err_code) {
			printf("ERROR; return code is % d\n", err_code);
			exit(-1);
		}
	}

	for (long i = 0; i < NUM_THREADS; i++) {
		err_code = pthread_join(thread[i], &status);
		// main 함수가 thread[i]가 끝날 때까지 기다린다.
		// thread[0]이 끝나면 pthread_exit의 결과가 status에 전달된다.

		if (err_code) {
			printf("ERROR; return code is %d\n", err_code);
			exit(-1);
		}
		printf("Join with thread %ld, status: %ld\n", i, (long)status);
	}

	printf("Main: program completed. Exiting.\n");
	pthread_exit(NULL);
}

*/