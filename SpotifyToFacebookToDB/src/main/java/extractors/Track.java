package extractors;

public class Track {

	private String artist;
	private String artistId;
	private String trackName;
	private String trackId;
	private String genre;

	public Track(String artist, String artistId, String trackName,
			String trackId, String genre) {
		super();
		this.artist = artist;
		this.artistId = artistId;
		this.trackName = trackName;
		this.trackId = trackId;
		this.genre = genre;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "Track [artist=" + artist + ", artistId=" + artistId
				+ ", trackName=" + trackName + ", trackId=" + trackId
				+ ", genre=" + genre + "]";
	}
	
	

}
