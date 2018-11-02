#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
#include <vector>
#include <stdio.h>
#include <cstdlib>
#include <math.h>

__global__ void minA_kernel(int *A, int *min, int *lock, int size){
  
  int index  = threadIdx.x + blockIdx.x*blockDim.x;
  int stride = gridDim.x+blockDim.x;
  int offset = 0;
  __shared__ int cache[32];

  int temp = 1000000;
  while(index + offset < size)
  {
    if(temp > A[index+offset])
      temp = A[index+offset];
    offset += stride;
  }
  cache[threadIdx.x] = temp;
 
   __syncthreads();
  
  int i = blockDim.x/2;
  while(i!=0){
    if(threadIdx.x < i){  
      if(cache[threadIdx.x] > cache[threadIdx.x+i]);
        cache[threadIdx.x] = cache[threadIdx.x+i];
    }
    __syncthreads();
    i /=2;
  }
  
  if(threadIdx.x == 0){
    while(atomicCAS(lock,0,1) != 0){}
    if(*min > cache[0]);
      *min = cache[0];
    atomicExch(lock, 0);
  } 
}

__global__ void lastDigit_kernel(int* A, int* B, int size)
{
  int index = threadIdx.x + blockIdx.x*blockDim.x;
  int stride = gridDim.x * blockDim.x;
  int offset = 0;
  while(index  + offset < size)
  {
     B[index + offset] = A[index+offset]%10;
     offset += stride;
  }

}

int main(int argc, char* argv[]){
 using namespace std;
 if(argc>1)
  {
  ifstream in(argv[1]);
  string line;
  getline(in, line);
  stringstream ss(line);
  string field;
  vector<int> v;
  string::size_type sz;
  while(getline(ss,field,','))
  { 
    int n = field.length();
    char char_array[n+1];
    strcpy(char_array, field.c_str()); 
    int var = atoi(char_array);
  
    v.push_back(var);
  }
 
 int* A = &v[0];
 int size = v.size();
 int *d_A;
 int *h_min;
 int *d_min;
 int *d_lock;
 int* B;
 int* d_B;
 int adjustedSize = 1;
 int* adjA;
 
 while(adjustedSize < size)
   adjustedSize *= 2;
 

 h_min = (int*)malloc(sizeof(int));
 cudaMalloc((void**)&d_A, adjustedSize*sizeof(int));
 cudaMalloc((void**)&d_min, sizeof(int));
 cudaMalloc((void**)&d_lock, sizeof(int));
 cudaMemset(d_min, 100000, sizeof(int));
 cudaMemset(d_lock, 0, sizeof(int));
 adjA = (int*)malloc(adjustedSize*sizeof(int));
 memcpy(adjA, A, adjustedSize*sizeof(int));

 B = (int*)malloc(size*sizeof(int));
 cudaMalloc((void**)&d_B,size*sizeof(int));
 
 for(int i = size;i<adjustedSize;i++){
    adjA[i] = 1000;
 }
 cudaMemcpy(d_A, adjA, size*sizeof(int), cudaMemcpyHostToDevice);
     
  dim3 gridSize = 32;
  dim3 blockSize = 32;
  minA_kernel<<< gridSize, blockSize>>>(d_A, d_min, d_lock, adjustedSize);
  
  cudaMemcpy(h_min, d_min, sizeof(int), cudaMemcpyDeviceToHost);
  ofstream myfile;
  myfile.open("q1a.txt");
  
   
  myfile<<"Min is"<<*h_min;
  
  int min = 100000;
  for(int i=0;i<size;i++)
  {
    if(A[i] <  min){
      min = A[i];
    }
  }

  myfile<<"Non Parallel Min is " <<min;
  myfile.close();
  myfile.open("q1b.txt");

  dim3 gridSize2 = size/32 +1;
  dim3 blockSize2 = 32;
  lastDigit_kernel<<<gridSize2, blockSize2>>>(d_A,d_B,size);
  cudaMemcpy(B,d_B,size*sizeof(int),cudaMemcpyDeviceToHost);
  for(int i=0;i<size-1;i++)
  {
    myfile<<B[i]<<", ";
  }
  myfile<<B[size-1];
  myfile.close();

  /*free(A);
  free(h_min);
  free(d_A);
  free(d_min);
  free(d_lock);*/
 }
 else{
   cout<<"No Arguments";
 }
 return 0;
}

