/*
#include <stdio.h>
#include <omp.h>
#include <stdbool.h>
#include <stdlib.h>
//#include <pthread.h>
#define MAX 200000

bool isPrime(int num) {
	if (num <= 1)	return false;
	for (int i = 2; i < num; i++) {
		if (num % i == 0)	return false;
	}
	return true;
}

int static_default(int num) {
	int count = 0;
	double start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:count) schedule(static)
	for (int i = 0; i < num; i++) {
		if (isPrime(i))
			count = count + 1;
	}

	double end_time = omp_get_wtime();
	printf("Execution time: %f seconds \n", end_time - start_time);
	return count;
}

int main(int argc, char* argv[]) {
	if (argc != 3) {
		printf("Please enter 2 commands: \n");
		printf("1. scheduling type number \n");
		printf("2. number of threads \n");
		return 0;
	}

	int type = atoi(argv[1]);
	int thread_num = atoi(argv[2]);
	omp_set_num_threads(thread_num);

	printf("%d \n", static_default(MAX));


	return 0;
}
*/