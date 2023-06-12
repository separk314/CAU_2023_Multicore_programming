/*
// Example 1: Thread Creation
#include <pthread.h>
#include <stdio.h>
#define NUM_THREADS 5

void* PrintHello(void* thread_id) {
	int tid = (int)thread_id;
	printf("Hello World! It's me, thread #%d!\n", tid);
	pthread_exit(NULL);
}

int main(int argc, char* argv[]) {
	pthread_t threads[NUM_THREADS];
	int error_code;

	for (int i = 0; i < NUM_THREADS; i++) {
		printf("In main: creating thread %d\n", i);
		error_code = pthread_create(&threads[i], NULL, PrintHello, (void*)i);

		if (error_code) {
			printf("ERROR code is %d\n", error_code);
			exit(-1);
		}
	}

	pthread_exit(NULL);
}



// Example 2: Passing Parameters to Thread
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <Windows.h>

void* PrintHello(void* ptr)
{
	char* filename;
	filename = (char*)ptr;
	while (1) {
		printf("Hello World! It's me, thread %s!\n", filename);
		Sleep(1000);
	}
	pthread_exit(NULL);
}

int main(int argc, char* argv[])
{
	pthread_t thread[100];
	int err_code, i = 0;
	char* filename;

	printf("Enter thread name at any time to create thread\n");

	while (1) {
		filename = (char*)malloc(80 * sizeof(char));
		scanf("%s", filename);
		printf("In main: creating thread %d\n", i);
		err_code = pthread_create(&thread[i], NULL, PrintHello, (void*)filename);

		if (err_code) {
			printf("ERROR code is %d\n", err_code);
			exit(-1);
		}
		else i++;
	}

	pthread_exit(NULL);
}


// Example 3: Files
#include <stdio.h>

void* PrintHello(void* ptr)
{
	FILE* file;
	char* filename = (char*)ptr;
	char textline[100];

	printf("Hello World! Opening %s!\n", filename);
	file = fopen(filename, "r");

	if (file != NULL) {
		// file 내용을 출력
		while (fscanf(file, "%s\n", textline) != EOF) 
			printf("%s\n", textline);
	}

	fclose(file);
}


// Example 4: JOIN
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
		printf("Join with thread %ld, status: %ld\n",i,(long)status);
	}

	printf("Main: program completed. Exiting.\n");
	pthread_exit(NULL);
}


// Example 5: Mutexes: 백터 곱셈 예제
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
	double* a;	// 벡터 a
	double* b;	// 벡터 b
	double sum;	// 계산된 내적의 합
	int veclen;	// 벡터의 길이
} DOTDATA;

#define NUMTHRDS 4
#define VECLEN 100

DOTDATA dotstr;
pthread_t callThd[NUMTHRDS];
pthread_mutex_t mutexsum;	// mutex로 보호할 shared data


// 스레드 함수. 부분 벡터의 내적을 계산, 전역 sum에 결과 추가
// 결과를 누적할 때 mutex를 사용하여 race condition 방지.
void* dotprod(void* thread_idx)
{
	long offset = (long)thread_idx;
	int len = dotstr.veclen;
	int start = offset * len;
	int end = start + len;

	double *x = dotstr.a; 
	double *y = dotstr.b;
	double mysum = 0;

	for (int i = start; i < end; i++) 
		mysum += (x[i] * y[i]);

	pthread_mutex_lock(&mutexsum);
	
	dotstr.sum += mysum;
	printf("Thread %ld did %d to %d: mysum=%.2f global sum=%.2f\n", offset, start, end, mysum, dotstr.sum);
	
	pthread_mutex_unlock(&mutexsum);
	pthread_exit((void*)0);
}

int main(int argc, char* argv[])
{
	void* status;
	double* a = (double*)malloc(NUMTHRDS * VECLEN * sizeof(double));
	double* b = (double*)malloc(NUMTHRDS * VECLEN * sizeof(double));

	for (int i = 0; i < VECLEN * NUMTHRDS; i++) {
		a[i] = 1;
		b[i] = a[i];
	}

	dotstr.veclen = VECLEN;
	dotstr.a = a;
	dotstr.b = b;
	dotstr.sum = 0;

	pthread_mutex_init(&mutexsum, NULL);

	for (int i = 0; i < NUMTHRDS; i++) 
		pthread_create(&callThd[i], NULL, dotprod, (void*)i);

	// 동기화: 모든 스레드가 끝날 때까지 기다림
	for (int i = 0; i < NUMTHRDS; i++) {
		pthread_join(callThd[i], &status);
		printf("Thread %d: finished \n", i);
	}

	printf("Sum = %f \n", dotstr.sum);
	free(a);
	free(b);
	pthread_mutex_destroy(&mutexsum);
	pthread_exit(NULL);
}


// Example 6: Control Variables
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <Windows.h>
#include <thread>

#define NUM_THREADS 3
#define TCOUNT 10
#define COUNT_LIMIT 12

int count = 0;
int thread_ids[3] = { 0,1,2 };
pthread_mutex_t count_mutex;
pthread_cond_t count_threshold_cv;

void* inc_count(void* t)
{
	long my_id = (long)t;

	for (int i = 0; i < TCOUNT; i++) {	// 10번 반복
		pthread_mutex_lock(&count_mutex);
		count++;

		// COUNT_LIMIT에 도달했을 때 watch_count 스레드에게 알림
		if (count == COUNT_LIMIT) {
			pthread_cond_signal(&count_threshold_cv);
			printf("inc_count(): thread %ld, count = %d Threshold reached.\n", my_id, count);
		}

		printf("inc_count(): thread %ld, count = %d, unlocking mutex\n", my_id, count);
		pthread_mutex_unlock(&count_mutex);
		Sleep(1000);
	}

	pthread_exit(NULL);
}

void* watch_count(void* t)
{
	long my_id = (long)t;

	printf("Starting watch_count(): thread %ld\n", my_id);
	pthread_mutex_lock(&count_mutex);

	// 아직 count값이 한계점에 도달하지 않은 경우
	while (count < COUNT_LIMIT) {
		pthread_cond_wait(&count_threshold_cv, &count_mutex);

		printf("watch_count(): thread %ld Condition signal received.\n", my_id);
		// inc_count에서 count_threshold_cv 시그널을 보냄: count값이 다 찼음!
		// 자동으로 count_mutex는 다시 lock되었다.

		count += 125;
		printf("watch_count(): thread %ld count now = %d.\n", my_id, count);
	}

	pthread_mutex_unlock(&count_mutex);
	pthread_exit(NULL);
}

int main(int argc, char* argv[])
{
	long t1 = 1, t2 = 2, t3 = 3;
	pthread_t threads[3];

	pthread_mutex_init(&count_mutex, NULL);
	pthread_cond_init(&count_threshold_cv, NULL);

	pthread_create(&threads[0], NULL, watch_count, (void*)t1);
	pthread_create(&threads[1], NULL, inc_count, (void*)t2);
	pthread_create(&threads[2], NULL, inc_count, (void*)t3);

	for (int i = 0; i < NUM_THREADS; i++) 
		pthread_join(threads[i], NULL);

	printf("Main(): Waited on %d threads. Done.\n", NUM_THREADS);
	pthread_mutex_destroy(&count_mutex);
	pthread_cond_destroy(&count_threshold_cv);
	pthread_exit(NULL);
}
*/