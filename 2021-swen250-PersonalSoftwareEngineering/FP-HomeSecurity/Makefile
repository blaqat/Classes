hs: hs.o hs_util.o
	gcc -g -o hs hs.o hs_util.o

hs.o: hs.c
	gcc -g -c hs.c

hs_util.o: hs_util.c hs_util.h
	gcc -g -c hs_util.c
	
clean:
	rm -f *.o hs
	
level_1:
	@echo "\n*** Level 1 Test ***"
	./hs < hs_l1_in.txt > test1.txt
	diff test1.txt hs_l1_out.txt
	
level_2:
	@echo "\n*** Level 2 Test ***"
	./hs < hs_l2_in.txt > test2.txt
	diff test2.txt hs_l2_out.txt
	
level_3:
	@echo "\n*** Level 3 Test ***"
	./hs < hs_l3_in.txt > test3.txt
	diff test3.txt hs_l3_out.txt
	
testall: level_1 level_2 level_3

all: clean hs
