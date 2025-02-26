#include <stdio.h>

// converts degrees from Fahrenheit to Celsius
float fromFtoC(float fahr){
	return (fahr - 32) * 5 / 9;
}

int main(){
	printf("Fahrenheit\tCelsius\n");

	for(int fahr = 0; fahr <= 300; fahr += 20){
		printf("%d\t\t%.1f\n", fahr, fromFtoC(fahr));
	}

	return 0;
}
