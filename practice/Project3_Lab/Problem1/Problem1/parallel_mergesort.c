#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <omp.h>

#define SWAP(a,b) {int temp = a; a = b; b = temp;}

#define SIZE (1<<19)

void setUp(int a[], int size);
void tearDown(double start, double end, int a[], int size);
void merge(int a[], int size, int temp[]);
void mergesort_serial(int a[], int size, int temp[]);
void mergesort_parallel_omp(int a[], int size, int temp[], int threads);

int main() {
	int a[SIZE];
	int temp[SIZE];

	int num_threads = omp_get_num_threads();
	setUp(a, SIZE);		// 배열을 랜덤값으로 채우기

	double startTime = omp_get_wtime();
	mergesort_parallel_omp(a, SIZE, temp, num_threads);
	double endTime = omp_get_wtime();

	tearDown(startTime, endTime, a, SIZE);
}

void setUp(int a[], int size) {
	int i;

	srand(time(NULL));
	for (i = 0; i < size; ++i) {
		a[i] = rand() % size;
	}
	return;
}

void tearDown(double start, double end, int a[], int size) {
	int sorted = 1;
	int i;

	printf("Time to execute: %f\n", end - start);

	for (i = 0; i < size - 1; ++i) {
		sorted &= (a[i] <= a[i + 1]);
	}

	printf("Array sorted: %d\n", sorted);

#pragma omp parallel
	{
#pragma omp master
		{
			printf("Num threads: %d\n", omp_get_num_threads());
		}
	}
}

// 이미 정렬된 두 배열을 하나로 합쳐서 a에 저장하는 함수
void merge(int a[], int size, int temp[]) {
	int i1 = 0;		// a의 왼쪽 부분을 가리키는 index
	int i2 = size / 2;	// a의 오른쪽 부분
	int it = 0;		// temp 배열을 가리키는 index


	// a의 왼쪽 오른쪽에 포인터를 하나씩 두고
	// 작은 값을 temp에 복사한다.
	while (i1 < size / 2 && i2 < size) {
		if (a[i1] <= a[i2]) {
			temp[it] = a[i1];
			i1 += 1;
		}
		else {
			temp[it] = a[i2];
			i2 += 1;
		}
		it += 1;
	}

	// 왼쪽과 오른쪽 부분 배열의 나머지 요소를 temp에 복사한다.
	// (한쪽 배열의 모든 요소가 사용된 경우, 다른 쪽의 요소들도 마저 복사.)
	while (i1 < size / 2) {
		temp[it] = a[i1];
		i1++;
		it++;
	}
	while (i2 < size) {
		temp[it] = a[i2];
		i2++;
		it++;
	}

	memcpy(a, temp, size * sizeof(int));	// 정렬된 결과를 원래 a로 복사

}


// thread 개수가 1일 때 실행됨.
void mergesort_serial(int a[], int size, int temp[]) {
	int i;

	if (size == 2) {
		if (a[0] <= a[1])
			return;
		else {
			SWAP(a[0], a[1]);
			return;
		}
	}

	mergesort_serial(a, size / 2, temp);
	mergesort_serial(a + size / 2, size - size / 2, temp);
	merge(a, size, temp);
}


void mergesort_parallel_omp
(int a[], int size, int temp[], int threads) {

	if (threads == 1) {
		mergesort_serial(a, size, temp);

	} else if (threads > 1) {
#pragma omp parallel sections
		{
#pragma omp section
			mergesort_parallel_omp(a, size / 2, temp, threads / 2);
#pragma omp section
			mergesort_parallel_omp(a + size / 2, size - size / 2, temp + size / 2, threads - threads / 2);
		}

		merge(a, size, temp);	// 결과를 합침.
	} 
}