HEADERS = csv.h unit_tests.h

default: test

test:	csv.o unit_tests.o
	gcc -g -o test csv.o unit_tests.o
	
cpracticum.o: csv.c $(HEADERS)
	gcc -o csv -g -Wall csv.c unit_tests.c

unit_tests.o: unit_tests.c $(HEADERS)
	gcc -o test -g -Wall csv.c unit_tests.c

clean:
	-rm -f csv.o
	-rm -f unit_tests.o
	-rm -f test
	
