package extractors;
import java.util.ArrayList;

import org.junit.Test;


public class TestSongFinder {
	
	
	@Test
	public void testName() throws Exception {
		
		SongFinder songFinder = new SongFinder();
		//songFinder.authorize();
 ArrayList<Track> playlist = songFinder.getPlaylist("11124760075");
 
 for (Track track : playlist) {
	System.out.println(track.toString());
}

	}

}
