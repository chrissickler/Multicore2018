#include <stdio.h>

#define NUM_BLOCKS 10
#define BLOCK_WIDTH 1

__global__ void minA()
{
    
}

__global__ void computeB() {

}


int main(int argc,char **argv)
{
    // launch the kernel
    minA<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    cudaDeviceSynchronize();

    //printf("That's all!\n");

    return 0;
}