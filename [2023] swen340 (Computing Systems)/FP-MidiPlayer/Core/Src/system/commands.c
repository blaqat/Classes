/*   commands.c   */
#include <stdarg.h>
#include <stdio.h>
#include <stdint.h>
#include "LED.h"
#include "project.h"
#include "printf.h"
#include "string.h"
#include "interpreter.h"

SongInfo info;

int song_counter = -1;
int song_total = 5;
int is_playing = 0;
int state = 0;
int has_started = 0;
char wave = 's';

void play(){
	is_playing = 1;
	LED_On(PROJECT_LED);
}

void stop(){
	is_playing = 0;
	has_started = 0;
	state = 2;
	LED_Off(PROJECT_LED);
}

//Stars the Non Interrupt Tone Player
void run_song(){
	if(state == 1){
		stop();
	}
	if(is_playing && song_counter < 2){
		state = play_track_no_int(song_counter, &has_started, wave);
	}

}

void run_command(char command[], uint8_t* state){
	if(strcmp(command, "PLAY") == 0){
		if(song_counter == -1){
			printlf("! No Song Selected !");
		} else {
			printlf(". . . playing");
			play();
			*state = 2;
		}
	}
	else if(strcmp(command, "TOGGLE") == 0){ //Toggle pause and play commands
		if(*state == 2){
			run_command("PAUSE", state);
		} else {
			run_command("PLAY", state);
		}
	}
	else if(strcmp(command, "STOP") == 0){
		printlf(". . . stopped");
		stop();
		*state = 0;
	}
	else if(strcmp(command, "PAUSE") == 0){
		is_playing = 0;
		if(*state != 1){
			printlf(". . . pausing");
			*state = 1;
		}
		else {
			printlf(". . ! Already Paused !");
		}
	}
	else if(strcmp(command, "NEXT") == 0){
		song_counter++;
		if(song_counter >= song_total){
			song_counter = 0;
		}

		has_started = 0;
		info = get_song_info(song_counter);
		printlf(". . .  next song\n\r\n\rSONG NUMBER %d:\n\r-------------------------", song_counter+1);
		//printlf("MIDWAY");
		printlf("Song Title: %s", info.title);
		printlf("Copyright: %s", info.copyright);
		printlf("Tempo: %d", info.tempo);



		//printlf("FINISHED");
	}
	else if(strcmp(command, "HELP") == 0){
		printlf("\n\rAvailable User Commands\n\r-------------------------");
		printlf("HELP - Show list of commands");
		printlf("NEXT - Show next song info");
		printlf("PLAY - Play the song (LED on)");
		printlf("PAUSE - Pause the song (LED flash)");
		printlf("STOP - Stop the song (LED off)");
		printlf("WAVE - Changes the wave form");
		printlf("\t[s] - Square Wave");
		printlf("\t[t] - Triangle Wave");
		printlf("\t[w] - Saw Wave");
		printlf("\t[p] - Pulse Wave");
	}
	else if(strcmp(command, "WAVE") == 32){
		char c = command[5];
		if(c == 's' || c == 't' || c == 'w' || c == 'p'){
			char* wav = c == 's'?"-_":c == 't'?"/\\":c == 'w'?"/":"-__";
			printlf("%s changed wave form %s", wav, wav);
			wave = c;
		}
		else
			printlf("!Invalid Wave Form!");
	}
	else {
		printlf("!Invalid Command!");
	}
	printlf("");
}
