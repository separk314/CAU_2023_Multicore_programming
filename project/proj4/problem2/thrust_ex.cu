
#include <stdio.h>
#include <thrust/device_vector.h>
#include <thrust/sequence.h>
#include <thrust/transform.h>
#include <thrust/reduce.h>

#define NUM_STEPS 200000
#define STEP 1.0/NUM_STEPS

struct calculation {
  __host__ __device__
  double operator()(double i) {
    double x = (i+0.5)*STEP;
    return 4.0/(1.0+x*x);
  }
};

int main ()
{ 
	clock_t start_time = clock();

  // value "i" initialization
  thrust::device_vector<double> i(NUM_STEPS); // same with "i" in the original code
  thrust::sequence(i.begin(), i.end());    // 0 to NUM_STEPS in "i" vector

  thrust::device_vector<double> sum(NUM_STEPS); // vector to store the "sum"
  // same with the "for" statement in the original code
  thrust::transform(i.begin(), i.end(), sum.begin(), calculation());    

  double result = thrust::reduce(sum.begin(), sum.end());    // summation
  double pi = STEP * result;

  // Calulate the total execution time
	clock_t end_time = clock();
  clock_t exe_time = end_time - start_time;
  double exe_time_sec = (double)(exe_time) / CLOCKS_PER_SEC;
  printf("Execution Time : %.10lf sec \n", exe_time_sec);

	printf("pi = %.10lf \n",pi);
}
