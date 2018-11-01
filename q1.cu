#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
#include <vector>
#include <stdio.h>
#include <cstdlib>
int main(int argc, char* argv[]){
  using namespace std;
 if(argc>1)
  {
  ifstream in(argv[1]);
  string line;
  getline(in, line);
  stringstream ss(line);
  string field;
  vector<int> arr;
  string::size_type sz;
  while(getline(ss,field,','))
  { 
    int n = field.length();
    char char_array[n+1];
    strcpy(char_array, field.c_str()); 
    int var = atoi(char_array);

    arr.push_back(var);
  }
  for(int i =0;i<arr.size();i++)
  {
    cout << arr[i] << " ";
  }
 }
 else
 {
  std::cout << "No arguments." << '\n';
 }
 return 0;
}
