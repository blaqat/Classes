HEADERS = read_data.h

test_pointers: main.o read_data.o
	gcc -o test_pointers -g main.o read_data.o

main.o: main.c read_data.c $(HEADERS)
	gcc -c -Wall -g main.c

read_data.o: read_data.c $(HEADERS)
	gcc -c -Wall -g read_data.c

all: test_pointers main.o read_data.o