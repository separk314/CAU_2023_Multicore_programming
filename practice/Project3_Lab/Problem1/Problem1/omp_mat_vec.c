/*

// Matrix와 Vector의 곱을 Result_Vec에 저장하는 프로그램

#include <omp.h>
#include <stdio.h>

#define NUM_THREADS 4
#define M 8
#define N 8

void mxv_row(int m, int n, double* a, double* b, double* c) {
	// a: Result_Vec, b: Matrix, c: Vector
	int i, j;
	double sum;

#pragma omp parallel for default(none) private(i,j,sum) shared(m,n,a,b,c)
	for (i = 0; i < m; i++) {
		sum = 0.0;

		for (j = 0; j < n; j++)
			sum += b[i*n + j] * c[j];

		a[i] = sum;
	}
}

int main()
{
	double* Matrix = (double*)malloc(M * N * sizeof(double));
	double* Vector = (double*)malloc(N * sizeof(double));
	double* Result_Vec = (double*)malloc(N * sizeof(double));

	// Matrix initialize:
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			Matrix[i * N + j] = 1.0;
		}
	}

	// Vector initialize
	for (int i = 0; i < N; i++)
		Vector[i] = 2.0;

	// mxv_row: parallel processing
	mxv_row(M, N, Result_Vec, Matrix, Vector);

	// Print result vector
	for (int j = 0; j < N; j++)
		printf("%.1lf ", Result_Vec[j]);

	printf("\n\n\n");

	return 0;
}*/