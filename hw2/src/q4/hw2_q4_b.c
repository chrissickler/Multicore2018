#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

double MonteCarlo(int s)
{
  //Choose R=1 so square is from 0-2 on both axis and cicrle is centered at 1,1
  double tot = 0.0;
  #pragma omp parallel num_threads(s)
  { 
    double x = (double)rand()/RAND_MAX*2.0;//0-2
    double y = (double)rand()/RAND_MAX*2.0;//0-2
    if(((x*x)+(y*y)) < 4)
    {
      #pragma omp critical
          tot++;
    }
    
  }
  return (tot/s)*4 ;
}

void main()
{
  srand(time(NULL));
  double pi;
  pi = MonteCarlo(100000);
  printf("Value of pi is: %lf\n", pi);


}
