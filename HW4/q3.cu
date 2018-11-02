#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
#include <vector>
#include <stdio.h>
#include <cstdlib>
#include <math.h>

__global__ void countodd_kernel(int *A, int *count, int *values, int size){
  
  int index  = threadIdx.x + blockIdx.x*(size/gridDim.x);
  int stride = 32;
  int offset = 0;
  __shared__ int cache[32];
  cache[threadIdx.x] = 0;

  int temp = 0;
  while(index + offset <(blockIdx.x+1)*(size/gridDim.x))
  {
    if(A[index + offset]%2==1)
      temp+=1;
    offset += stride;
  }
  cache[threadIdx.x] = temp;
 
   __syncthreads();
  
  int i = blockDim.x/2;
  while(i!=0){
    if(threadIdx.x < i){  
      cache[threadIdx.x] += cache[threadIdx.x + i];
    }
    __syncthreads();
    i /= 2; 
  }

  if(threadIdx.x==0){
    atomicAdd(count,cache[0]);
    values[blockIdx.x] = cache[0];
  }
}

__global__ void getodd_kernel(int *A, int *D, int sizeA, int *sizeD){
   
   int index = threadIdx.x + blockIdx.x*(sizeA/gridDim.x);
   int stride = 1;
   int offset = 0;
   int count = 0;
   int startIndex = 0;
   for(int i=0;i<blockIdx.x;i++)
   {
     startIndex+=sizeD[i];
   }
   while(count<sizeD[blockIdx.x])
   {
     if(A[index+offset]%2==1)
     {
       D[startIndex+count] = A[index+offset];
       count++;  
     }
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
 int* d_A;
 int *d_count;
 int *h_count;
 int *d_lock;
 int adjustedSize = 1;
 int* adjA;
 int* d_values;
 int* h_values;
 
 while(adjustedSize < size)
   adjustedSize *= 2;
 cout<<adjustedSize<<endl;

 h_values = (int*)malloc(32*sizeof(int));
 cudaMalloc((void**)&d_values, 32*sizeof(int));
 h_count = (int*)malloc(sizeof(int));
 cudaMalloc((void**)&d_A, adjustedSize*sizeof(int));
 cudaMalloc((void**)&d_count, sizeof(int));
 cudaMalloc((void**)&d_lock, sizeof(int));
 cudaMemset(d_count, 0, sizeof(int));
 cudaMemset(d_lock, 0, sizeof(int));
 cudaMemset(d_values, 0, 32*sizeof(int));
 adjA = (int*)malloc(adjustedSize*sizeof(int));
 memcpy(adjA, A, size*sizeof(int));

 // B = (int*)malloc(size*sizeof(int));
// cudaMalloc((void**)&d_B,size*sizeof(int));
 
 for(int i = size;i<adjustedSize;i++){
    adjA[i] = 8;
 }
 cudaMemcpy(d_A, adjA, adjustedSize*sizeof(int), cudaMemcpyHostToDevice);
     
  dim3 gridSize = 32;
  dim3 blockSize = 32;
  countodd_kernel<<< 32, 32>>>(d_A, d_count, d_values, adjustedSize);
  
  cudaMemcpy(h_count, d_count, sizeof(int), cudaMemcpyDeviceToHost);
  cudaMemcpy(h_values, d_values, 32*sizeof(int), cudaMemcpyDeviceToHost);
  cudaMemcpy(d_values, h_values, 32*sizeof(int), cudaMemcpyHostToDevice);  

  for(int i=0;i<32;i++)
     cout<<i<<": "<<h_values[i]<<endl;
 /* int* h_D[32];
  int* d_D[32];
  for(int i=0;i<32;i++)
     h_D[i] = (int*)malloc(h_values[i]*sizeof(int));
  for(int i=0;i<32;i++)
     cudaMalloc((void**)&d_D[i],h_values[i]*sizeof(int));

  getodd_kernel<<<32,1>>>(d_A, d_D, adjustedSize, d_values);
  for(int i=0;i<32;i++)
    cudaMemcpy(h_D[i],d_D[i],h_values[i]*sizeof(int),cudaMemcpyDeviceToHost);
  for(int i=0;i<32;i++)
    for(int j=0;j<h_values[i];j++)
      cout<<h_D[i][j]<<", ";*/
  
  ofstream(myfile);
  myfile.open("q3.txt");  

  int* d_D;
  int* h_D;
  h_D = (int*)malloc(*h_count*sizeof(int));
  cudaMalloc((void**)&d_D,*h_count*sizeof(int));
  getodd_kernel<<<32,1>>>(d_A, d_D, adjustedSize, d_values);
  
  cudaMemcpy(h_D, d_D, *h_count*sizeof(int), cudaMemcpyDeviceToHost);
  for(int i=0;i<*h_count-1;i++)
     myfile<<h_D[i]<<", ";
  myfile<<h_D[*h_count-1];
  
      
  cout<<"Count is "<<*h_count;
  
  int count = 0;
  for(int i=0;i<size;i++)
  {
    if(A[i] %2==1){
      count += 1;
    }

  }

  cout<<" Seq Count is "<<count;


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

