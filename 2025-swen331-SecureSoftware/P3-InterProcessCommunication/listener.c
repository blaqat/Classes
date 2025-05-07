#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

#define SUCCESS 0
#define FAILURE 1

int return_code;

void siguser1_handler(int _sig) {
		printf("SIGUSR1 received!\n");
		return_code = SUCCESS;
}

void sigint_handler(int _sig) {
		printf("SIGINT received!\n");
		return_code = FAILURE;
}

int main(){
	// printf("Listener PID: %i\n", getpid());

	signal(SIGINT, sigint_handler);
	signal(SIGUSR1, siguser1_handler);

	pause();

	return return_code;
}
