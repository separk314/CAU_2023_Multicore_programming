#include <stdio.h>
#include <omp.h>
#include <stdbool.h>
#include <stdlib.h>
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
	printf("Execution time: %lf ms \n", end_time - start_time);
	return count;
}

int dynamic_default(int num) {
	int count = 0;
	double start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:count) schedule(dynamic)
	for (int i = 0; i < num; i++) {
		if (isPrime(i))
			count = count + 1;
	}

	double end_time = omp_get_wtime();
	printf("Execution time: %lf ms \n", end_time - start_time);
	return count;
}

int static_10(int num) {
	int count = 0;
	double start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:count) schedule(static, 10)
	for (int i = 0; i < num; i++) {
		if (isPrime(i))
			count = count + 1;
	}

	double end_time = omp_get_wtime();
	printf("Execution time: %lf ms \n", end_time - start_time);
	return count;
}

int dynamic_10(int num) {
	int count = 0;
	double start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:count) schedule(dynamic, 10)
	for (int i = 0; i < num; i++) {
		if (isPrime(i))
			count = count + 1;
	}

	double end_time = omp_get_wtime();
	printf("Execution time: %lf ms \n", end_time - start_time);
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

	if (type == 1) {
		printf("<< Static, default >> \n");
		printf("Number of prime numbers %d \n", static_default(MAX));
	}
	else if (type == 2) {
		printf("<< Dynamic, default >> \n");
		printf("Number of prime numbers %d \n", dynamic_default(MAX));
	}
	else if (type == 3) {
		printf("<< Static, chunk size 10 >> \n");
		printf("Number of prime numbers %d \n", static_10(MAX));
	}
	else if (type == 4) {
		printf("<< Dynamic, chunk size 10 >> \n");
		printf("Number of prime numbers %d \n", dynamic_10(MAX));
	}
	else {
		printf("Invalid type: You should enter between 1~4");
		return 0;
	}

	printf("Number of threads: %d \n", thread_num);
	return 0;
}
