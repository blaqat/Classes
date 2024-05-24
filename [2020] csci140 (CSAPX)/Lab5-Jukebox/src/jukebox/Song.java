package jukebox;


public class Song implements Comparable{
    private String artist;
    private String song;
    public Song(String artist, String song){
        this.artist = artist;
        this.song = song;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getSong(){
        return this.song;
    }

    @Override
    public String toString(){
        return "Artist: " + getArtist() + ", Song: " + getSong();
    }

    @Override
    public boolean equals(Object o){
        Song other = (Song)o;
        return getArtist().equals(other.getArtist()) && getSong().equals(other.getSong());
    }

    @Override
    public int hashCode(){
        return getSong().hashCode() + getArtist().hashCode();
    }

    @Override
    public int compareTo(Object o){
        Song other = (Song)o;
        int compared = getArtist().compareTo(other.getArtist());
        return compared==0?getSong().compareTo(other.getSong()):compared;
    }
}
