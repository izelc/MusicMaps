package db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestAnalyzer {

	
	
	private static boolean setUpIsDone = false;
	Analyzer analyzer;
	
	
	@Before
	public void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
	    analyzer =new Analyzer();
	    setUpIsDone = true;
	}
	@Test
	public void getTopArtists() throws Exception {

		analyzer.getTopTracksAnalyze("ozyegin university");
		

	}

	@Test
	public void getTurkishRate() {
		analyzer.getTurkishArtistsRate("bogazici university");
	}

}
