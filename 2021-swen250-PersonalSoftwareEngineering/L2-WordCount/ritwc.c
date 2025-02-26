#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define FALSE (0)
#define TRUE  (1)
#define MAXSTR 80


int main(int argc, char *argv[]) {
	int tot_chars = 0 ;	/* total characters */
	int tot_lines = 0 ;	/* total lines */
	int tot_words = 0 ;	/* total words */

	/* REPLACE WITH ADDITIONAL VARIABLE DECLARATIONS YOU NEED */
	char str[MAXSTR + 1] = " ";
  

	/* REPLACE WITH THE CODE FOR WORD COUNT */
  while(*str != EOF && !(*str == '\n' && feof(stdin))){
    int tot_char_pline = 0;
	  fgets(str, MAXSTR, stdin);

    for(int i = 0; str[i] != EOF && str[i] != '\0'; i++){
      if(isspace(str[i]) && tot_char_pline > 0){
        tot_words++;
      }
      else if(str[i] != '\n') {
        tot_char_pline ++;
      }
    }

    if(tot_char_pline > 0){
      tot_lines++;
    }

    tot_chars += tot_char_pline;

    }
  printf( "\t%d\t%d\t%d\n", tot_lines, tot_words, tot_chars);

	return 0 ;
}
