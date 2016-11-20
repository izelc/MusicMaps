package extractors;

public class SpotifyUser {

	private String displayName;
	private String image;

	public SpotifyUser(String displayName, String image) {
		this.displayName = displayName;
		this.image = image;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
