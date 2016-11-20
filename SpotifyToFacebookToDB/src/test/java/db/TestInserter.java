package db;
import java.util.ArrayList;

import org.junit.Test;

import extractors.SongFinder;
import extractors.Track;


public class TestInserter {
	
   @Test
public void testName() throws Exception {
//Track track = new Track("Adele", "5487", "Hello", "656565", "pop");
//Track track2 = new Track("Rihanna", "46464", "Work", "46464774", "RnB");
//Track track3 = new Track("Adele", "5487", "Rolling in the Deep", "777777", "pop");
//
//ArrayList<Track> trackList = new ArrayList<Track>();
//trackList.add(track);
//trackList.add(track2);
//trackList.add(track3);
//
//new Inserter().insertUser("77777", "Izel Cavusoglu", "Ege University", trackList);
//new Inserter().insertUser("88888", "Ece Ozkan", "Ege University", trackList);
	   
	   ArrayList<Track> playlist = new SongFinder().getPlaylist("11124760075");
	   new Inserter().insertUser("11124760075", "sarp turk", "Ozyegin university", playlist);
   
   }
   
}
