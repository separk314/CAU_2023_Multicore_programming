/*
#include <stdio.h>
#include <omp.h>
#include <stdbool.h>
#include <stdlib.h>
#define NUM_STEPS 10000000

double static_scheduling(int chunck_size) {
	long i; double x, pi, sum = 0.0;
	double start_time, end_time, step;
	step = 1.0 / (double)NUM_STEPS;
	start_time = omp_get_wtime();

#pragma omp parallel for private(x) reduction(+:sum) schedule(static, chunck_size)
	for (i = 0; i < NUM_STEPS; i++) {
		x = (i + 0.5) * step;
		sum = sum + 4.0 / (1.0 + x * x);
	}

	pi = step * sum;
	end_time = omp_get_wtime();
	double timeDiff = end_time - start_time;
	printf("Execution Time : %lfms\n", timeDiff);

	return pi;
}

double dynamic_scheduling(int chunck_size) {
	long i; double x, pi, sum = 0.0;
	double start_time, end_time, step;
	step = 1.0 / (double)NUM_STEPS;
	start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:sum) private(x) schedule(dynamic, chunck_size)
	for (i = 0; i < NUM_STEPS; i++) {
		x = (i + 0.5) * step;
		sum = sum + 4.0 / (1.0 + x * x);
	}

	pi = step * sum;
	end_time = omp_get_wtime();
	double timeDiff = end_time - start_time;
	printf("Execution Time : %lfms\n", timeDiff);

	return pi;
}

double guided_scheduling(int chunck_size) {
	long i; double x, pi, sum = 0.0;
	double start_time, end_time, step;
	step = 1.0 / (double)NUM_STEPS;
	start_time = omp_get_wtime();

#pragma omp parallel for reduction(+:sum) private(x) schedule(guided, chunck_size)
	for (i = 0; i < NUM_STEPS; i++) {
		x = (i + 0.5) * step;
		sum = sum + 4.0 / (1.0 + x * x);
	}

	pi = step * sum;
	end_time = omp_get_wtime();
	double timeDiff = end_time - start_time;
	printf("Execution Time : %lfms\n", timeDiff);

	return pi;
}

int main(int argc, char* argv[]) {
	if (argc != 4) {
		printf("Please enter 3 commands: \n");
		printf("1. scheduling type number \n");
		printf("2. chunk size \n");
		printf("3. number of threads \n");
		return 0;
	}

	int type = atoi(argv[1]);
	int chunk_size = atoi(argv[2]);
	int thread_num = atoi(argv[3]);
	omp_set_num_threads(thread_num);

	if (type == 1) {
		printf("<< Static >> \n");
		printf("pi=%.24lf \n", static_scheduling(chunk_size));
	} 
	else if (type == 2) {
		printf("<< Dynamic >> \n");
		printf("pi=%.24lf \n", dynamic_scheduling(chunk_size));
	}
	else if (type == 3) {
		printf("<< Guided >> \n");
		printf("pi=%.24lf \n", guided_scheduling(chunk_size));
	}
	else {
		printf("Invalid type: You should enter between 1~4");
		return 0;
	}
	printf("Number of threads: %d \n", thread_num);
	printf("Chunck size: %d \n", chunk_size);

	return 0;
}
*/