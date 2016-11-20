package db;

import org.junit.Test;

import artistmetadata.ArtistMetadataFiller;

public class TestArtistMetadatFiller {

	
	@Test
	public void genr() throws Exception {
		ArtistMetadataFiller artistMetadataFiller = new ArtistMetadataFiller();
		artistMetadataFiller.findArtistsWithoutAttribute(RelTypes.HAS_NATIONALITY_OF);
	}
}
