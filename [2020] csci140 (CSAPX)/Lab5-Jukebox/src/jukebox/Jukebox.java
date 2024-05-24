package jukebox;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class Jukebox {
    private ArrayList<Song> playedSongs;
    private HashMap<Song, Integer> tracks;
    private HashMap<String, Object> statistics;

    public Jukebox(String fileName){
        /**
         * Reads a file into a HashMap of Songs
         */
        try {
            Scanner songFile = new Scanner(new File(fileName));
            playedSongs = new ArrayList<Song>();
            tracks = new HashMap<Song, Integer>();
            statistics = new HashMap<String, Object>();

            while (songFile.hasNextLine()) {
                String[] line = songFile.nextLine().split("<SEP>", 4);
                Song newSong = new Song(line[2], line[3]);
                tracks.put(newSong, 0);
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("The file name provided was not correct.");
        }
        catch (Exception e) {
            System.out.println("CAUGHT\t"+e);
        }
    }

    public void playSong(Song song){
        /**
         * adds the given song into the playedSong history
         * and adds to the tall of how many times that song has been played
         * @param: Song song
         * @returns: void
         */
        tracks.put(song, tracks.get(song) + 1);
        playedSongs.add(song);
    }

    public void run(int amount, Random ran){
        /**
         * runs the simulation n (amount) of times
         * with the random seed wanted
         * @param: int simulationAmount, Random
         * @returns: void
         */
        System.out.println("Jukebox of "+tracks.size()+" song" + (tracks.size()==1?"":"s")+" starts rockin'...");
        Song[] songs = tracks.keySet().toArray(new Song[tracks.size()]);
        statistics.put("totalSimulations", amount);

        for(int i = 0; i < amount; i++){
            TreeSet<Song> played = new TreeSet<Song>();
            boolean duplicate = false;
            while(!duplicate){
                Song song = songs[ran.nextInt(songs.length)];
                duplicate = played.contains(song);
                if(!duplicate){
                    played.add(song);
                    playSong(song);
                }
            }
        }

    }

    public void genStats(long startTime){
        /**
         * generates the necessary stats based on the amount of songs in the songHistory
         * and the passed startTime
         * @param: long startTime (when program started)
         * @returns: void
         */
        statistics.put("duration", System.currentTimeMillis()-startTime);
        statistics.put("totalSongs", playedSongs.size());
        statistics.put("averageSongs", (int)(playedSongs.size()/(int)statistics.get("totalSimulations")));
        statistics.put("mostPlayed", tracks.entrySet().stream().max((entry1, entry2) -> Integer.compare(entry1.getValue(), entry2.getValue())).get().getKey());
        statistics.put("songsTally", tracks);
    }

    public void printSongHistory(){printSongHistory(playedSongs.size());}

    public void printSongHistory(int amount){
        /**
         * prints n (amount) of songs from the song history
         * @param: int amount
         * @returns: void
         */
        System.out.println("Printing first" + (amount==1?"":" "+amount) + " song" + (amount==1?"":"s") + " played");
        for(int i = 0; i < amount; i++){
            System.out.println("\t"+playedSongs.get(i));
        }
    }

    public void printStats(){
        /**
         * @precondition: BEFORE YOU RUN THIS, RUN genStats()!
         * prints out the generated statistics history
         * @returns: void
         */
        long seconds = (long)statistics.get("duration")/1000;
        Song mostPlayed = (Song)statistics.get("mostPlayed");
        printSongHistory(5);
        System.out.println("Simulation took " + seconds + " second" + (seconds==1?"":"s"));
        System.out.println("Number of simulations run: " + statistics.get("totalSimulations"));
        System.out.println("Total number of songs played: " + statistics.get("totalSongs"));
        System.out.println("Average number of songs played per simulation to get duplicate: " + statistics.get("averageSongs"));
        System.out.println("Most played song: \"" + mostPlayed.getSong() + "\" by \""+ mostPlayed.getArtist() + "\"");
        System.out.println("All songs alphabetically by \"" + mostPlayed.getArtist() + "\":");
        TreeSet<Song> organizedTracks = new TreeSet<Song>(tracks.keySet());

        organizedTracks.forEach(track -> {
            if(track.getArtist().equals(mostPlayed.getArtist()))
                System.out.println("\t\"" + track.getSong() + "\" with " + tracks.get(track) + " plays");
        });
    }

    public static void main(String[] args) {
        /**
         * The main method.
         * @param: String[] passed arguments (needs 2)
         * @returns: void
         */
        if(args.length != 2){
            System.out.println("Usage: java Jukebox filename seed");
        }
        else {
            Random ran = new Random(Integer.parseInt(args[1]));
            Jukebox jb = new Jukebox(args[0]);
            long startTime = System.currentTimeMillis();
            jb.run(50000, ran);

            jb.genStats(startTime);
            jb.printStats();
        }
    }
}
