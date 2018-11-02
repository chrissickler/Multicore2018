#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
#include <vector>
#include <stdio.h>
#include <cstdlib>

#define NUM_BLOCKS 32
#define BLOCK_WIDTH 32

__global__ void countG(int *a, int *b, int n, int *mutex) {
    int index = 0;
    while (index < n) {
        int val = a[index]/100;
        if (val == threadIdx.x) {
            b[val]++;
        }
        index++;
    }
}

__global__ void count(int *a, int *b, int n, int *mutex) {
    int index = threadIdx.x + blockIdx.x * blockDim.x;
    int stride = gridDim.x * blockDim.x;
    int offset = 0;

    int temp_array[10];
    //temp_array = (int*)malloc(10*sizeof(int));

    int i;
    for ( i=0;i<10;i++) {
        temp_array[i] = 0;
        b[i]= 0;
    }

    __shared__ int cache[BLOCK_WIDTH][10];
    for(int j=0;j<BLOCK_WIDTH;j++) {
        for (int k=0;k<10;k++) {
            cache[j][k] = 0;
        }
    }

    while (index + offset < n) {
        int comb = index + offset;
        int val = a[comb]/100;
        temp_array[val] ++;

        offset += stride;
    }

    for (i=0;i<10;i++){
        cache[threadIdx.x][i] = temp_array[i];
    }

    __syncthreads();

    i = blockDim.x/2;
    while (i!=0) {
        if (threadIdx.x < i) {
            for (int j = 0; j < 10;j++) {
                cache[threadIdx.x][j] += cache[threadIdx.x + i][j];
            }
        }
        __syncthreads();
        i/=2;
    }

    if (threadIdx.x == 0) {
        while (atomicCAS(mutex,0,1) != 0);
        for (int j =0; j < 10; j++) {   
            b[j] += cache[0][j];
        }
        atomicExch(mutex,0);
    }   
    
}

int main(int argc, char* argv[]) {
  using namespace std;
  vector<int> arr;
 if(argc>1) {
  ifstream in(argv[1]);
  string line;
  getline(in, line);
  stringstream ss(line);
  string field;
  //string::size_type sz;
  while(getline(ss,field,',')) { 
    int n = field.length();
    char char_array[n+1];
    strcpy(char_array, field.c_str()); 
    int var = atoi(char_array);

    arr.push_back(var);
  }
  
 } else {
  std::cout << "No arguments." << '\n';
 }

    int* countA = (int*)malloc(10*sizeof(int));
    for (int i = 0; i < 10; i++) {
        countA[i] = 0;
    }
    for(int i = 0; i<10000;i++) {
        int val = arr[i]/100;
        countA[val]++;
    }

    
    
    int *d_B;
    int *B;
    int *d_A;
    int *d_mutex;
    int *h_A;
    int *C;
    int *d_C;
    int N = arr.size();

    //allocate memory
    h_A = (int*)malloc(N*sizeof(int));
    B = (int*)malloc(10*sizeof(int));
    C = (int*)malloc(10*sizeof(int));
    cudaMalloc((void**)&d_A,N*sizeof(int));
    cudaMalloc((void**)&d_B, 10*sizeof(int));
    cudaMalloc((void**)&d_mutex,sizeof(int));
    cudaMalloc((void**)&d_C,10*sizeof(int));

    ofstream q2af;
    ofstream q2bf;
    ofstream q2cf;
    q2af.open("q2a.txt");
    q2bf.open("q2b.txt");
    q2cf.open("q2c.txt");



    //copy from host to device
    h_A = &arr[0];

    cudaMemcpy(d_A,h_A,N*sizeof(int),cudaMemcpyHostToDevice);
    cudaMemset(d_mutex, 0, sizeof(int));

    
    
    /*for (int i = 0; i < 10; i++) {
        B[i] = 0;
    }
    cudaMemcpy(d_B,B,10*sizeof(int),cudaMemcpyHostToDevice);*/

    countG<<<1,10>>>(d_A,d_B,N,d_mutex);

    cudaMemcpy(B,d_B,10*sizeof(int),cudaMemcpyDeviceToHost);

    q2af << B[0];
    for (int k = 1; k < 10; k++) {
        q2af << ", " << B[k];
    }



    count<<<NUM_BLOCKS,BLOCK_WIDTH>>>(d_A,d_B,N,d_mutex);

    cudaMemcpy(B,d_B,10*sizeof(int),cudaMemcpyDeviceToHost);

    //report results
    

    q2bf << B[0];
    for (int k = 1; k < 10; k++) {
        q2bf << ", " << B[k];
    }

    cudaMemcpy(d_B,B,10*sizeof(int),cudaMemcpyHostToDevice);
    cudaMemset(d_mutex, 0, sizeof(int));

    count<<<4,4>>>(d_B,d_C,10,d_mutex);

    cudaMemcpy(C,d_C,10*sizeof(int),cudaMemcpyDeviceToHost);

    q2cf << C[0];
    for (int k = 1; k < 10; k++) {
        q2cf << ", " << C[k];
    }



    //free(h_B);
    free(h_A);
    free(B);
    free(C);
    cudaFree(d_B);
    cudaFree(d_A);
    cudaFree(d_C);
    cudaFree(d_mutex);

    // force the printf()s to flush
    //cudaDeviceSynchronize();

    //printf("That's all!\n");
    //cudaFree(b);
    q2af.close();
    q2bf.close();
    q2cf.close();
 return 0;
}
