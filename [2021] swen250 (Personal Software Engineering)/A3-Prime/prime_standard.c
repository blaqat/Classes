#include <stdio.h>

int is_prime(int num){
  /*
  Base Cases:
  1. number is greater than 2
  2. number is odd
  */
  if(num < 2 || num % 2 == 0){
    return num == 2;
  }
  else{
    //Looops through odd numbers from 3 to the sqrt(num)
    for(int i = 3; i * i <= num; i += 2){
      if(num % i == 0){
        return 0;
      }
    }
    
    return 1;
  }
}

int main(){
  int upper_bound;

  printf("Enter upper bound:\n");
  scanf("%d", &upper_bound);

  for(int i = 3; i <= upper_bound; i += 2) {
    if(is_prime(i) == 1) {
      printf("%d is prime\n", i);
    } 
  } 

  return 0;
}
