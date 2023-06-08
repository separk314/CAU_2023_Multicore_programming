
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

#define CUDA 0
#define OPENMP 1
#define SPHERES 20

#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048


// Indicates 3D sphere shape: sphere location, radius, and color information
struct Sphere {
    float   r,b,g;
    float   radius;
    float   x,y,z;
};

__global__ void CUDA_kernel(Sphere* s, unsigned char* ptr) {
  int x = threadIdx.x + blockIdx.x * blockDim.x;
  int y = threadIdx.y + blockIdx.y * blockDim.y;
  int offset = x + y*DIM;
  float ox = (x - DIM/2);
	float oy = (y - DIM/2);

  float r=0, g=0, b=0;
	float   maxz = -INF;

	for(int i=0; i<SPHERES; i++) {
		float dx = ox - s[i].x;
    float dy = oy - s[i].y;
		float t, n;

		// hit() function
    if (dx * dx + dy * dy < s[i].radius * s[i].radius) {
    	float dz = sqrtf(s[i].radius * s[i].radius - dx * dx - dy * dy);
    	n = dz / sqrtf(s[i].radius * s[i].radius);
    	t = dz + s[i].z;
    } else {
			t = -INF;
		}

		if (t > maxz) {
			float fscale = n;
			r = s[i].r * fscale;
			g = s[i].g * fscale;
			b = s[i].b * fscale;
			maxz = t;
		}
	}

	ptr[offset*4 + 0] = (int)(r * 255);
	ptr[offset*4 + 1] = (int)(g * 255);
	ptr[offset*4 + 2] = (int)(b * 255);
	ptr[offset*4 + 3] = 255;
}

// Function to store images in PPM file format
void ppm_write(unsigned char* bitmap, int xdim,int ydim, FILE* fp)
{
	int i,x,y;
	fprintf(fp,"P3\n");
	fprintf(fp,"%d %d\n",xdim, ydim);
	fprintf(fp,"255\n");
	for (y=0;y<ydim;y++) {
		for (x=0;x<xdim;x++) {
			i=x+y*xdim;
			fprintf(fp,"%d %d %d ",bitmap[4*i],bitmap[4*i+1],bitmap[4*i+2]);
		}
		fprintf(fp,"\n");
	}
	printf("[result.ppm] was generated. \n");
}

int main(int argc, char* argv[])
{
	srand(time(NULL));
  FILE* fp = fopen("result.ppm", "w");

	// temp_s: Sphere used by the CPU
	Sphere *temp_s = (Sphere*)malloc( sizeof(Sphere) * SPHERES );
	for (int i=0; i<SPHERES; i++) {
		temp_s[i].r = rnd( 1.0f );
		temp_s[i].g = rnd( 1.0f );
		temp_s[i].b = rnd( 1.0f );
		temp_s[i].x = rnd( 2000.0f ) - 1000;
		temp_s[i].y = rnd( 2000.0f ) - 1000;
		temp_s[i].z = rnd( 2000.0f ) - 1000;
		temp_s[i].radius = rnd( 200.0f ) + 40;
	}

	// cuda_s: Sphere used by the GPU
	Sphere *cuda_s;
	cudaMalloc((void**)&cuda_s, sizeof(Sphere) * SPHERES);
	cudaMemcpy(cuda_s, temp_s, sizeof(Sphere) * SPHERES, cudaMemcpyHostToDevice);

	// bitmap: Bitmap used by CPU
	unsigned char* bitmap;
	bitmap = (unsigned char*)malloc(sizeof(unsigned char) * DIM*DIM*4);

	// cuda_bitmap: Bitmap used by GPU
	unsigned char* cuda_bitmap;
	cudaMalloc((void**)&cuda_bitmap, sizeof(unsigned char) *DIM*DIM*4);
	cudaMemcpy(cuda_bitmap, bitmap, sizeof(unsigned char)*DIM*DIM*4, cudaMemcpyHostToDevice);

  dim3 gridDims(DIM / 16, DIM / 16);
  dim3 blockDims(16, 16);

	clock_t start = clock();
  CUDA_kernel<<<gridDims, blockDims>>>(cuda_s, cuda_bitmap);
	clock_t end = clock();

  cudaDeviceSynchronize();
	cudaMemcpy(bitmap, cuda_bitmap, sizeof(unsigned char)*DIM*DIM*4, cudaMemcpyDeviceToHost);
	
	clock_t exe_time = end - start;
	double exe_time_ms = ((double)exe_time / CLOCKS_PER_SEC) * 1000.0;
	printf("CUDA ray tracing: %f ms \n", exe_time_ms);

	ppm_write(bitmap,DIM,DIM,fp);

	fclose(fp);
	free(bitmap);
	free(temp_s);
	free(cuda_bitmap);
	free(cuda_s);

	return 0;
}
