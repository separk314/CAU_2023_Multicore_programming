/*

#include <stdio.h>
#include <omp.h>

#define NUM_THREADS 4

#define NX 5
#define NM NX
#define NY NX

// 1000 * 1000 ���
int a[NX * NM];
int b[NM * NY];
int m[NX * NY];

#define A(i, n) a[(i) + NX * (n)]	// a[n][i] ����
#define B(n, j) b[(n) + NM * (j)]	// b[j][n] ����
#define M(i, j) m[(i) + NX * (j)]	// m[j][i] ����

void printMatrix(int* mat, int X, int Y)
{
	int i, j;
	for (j = 0; j < Y; j++) {
		for (i = 0; i < X; i++) {
			printf("%4d ", mat[i + j * X]);
		}
		printf("\n");
	}
}

int main() {
	int i, j, n;

	omp_set_num_threads(NUM_THREADS);

	// ��ü �迭 �� �ʿ��� ���ҵ鸸 �ʱ�ȭ �صд�.
	double start_time = omp_get_wtime();

#pragma omp parallel for default(shared) private(n, i)
	for (n = 0; n < NM; n++) {
		for (i = 0; i < NX; i++) {
			A(i, n) = 3;
		}
	}
#pragma omp parallel for default(shared) private(n, j)
	for (j = 0; j < NY; j++) {
		for (n = 0; n < NM; n++) {
			B(n, j) = 2;
		}
	}

#pragma omp parallel for default(shared) private(i, j)
	for (j = 0; j < NY; j++) {
		for (i = 0; i < NX; i++) {
			M(i, j) = 0;
		}
	}

#pragma omp parallel for default(shared) private(i, j, n)
	for (j = 0; j < NY; j++) {
		for (i = 0; i < NX; i++) {
			for (n = 0; n < NM; n++) {
				M(i, j) += A(i, n) * B(n, j);
			}
		}
	}

	double end_time = omp_get_wtime();

	printMatrix(m, NX, NY);
	printf("computation time:%lf, using %d threads\n",
		end_time - start_time, NUM_THREADS);
	return 0;
}

*/