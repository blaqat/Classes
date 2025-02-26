#include <stdio.h>

// converts degrees from Fahrenheit to Celsius
int fromFtoC(int fahr){
	return (fahr - 32) * 5 / 9;
}

int main(){
	printf("Fahrenheit\tCelsius\n");

	for(int fahr = 0; fahr <= 300; fahr += 20){
		int cel = fromFtoC(fahr);
		printf("%d\t\t%d\n", fahr, cel);
	}

	return 0;
}
