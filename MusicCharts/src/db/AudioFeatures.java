package db;
public class AudioFeatures {

	private double danceability, valence, energy, loudness;

	public AudioFeatures(double danceability, double valence, double energy,
			double loudness) {

		this.danceability = danceability;
		this.valence = valence;
		this.energy = energy;
		this.loudness = loudness;
	}

	@Override
	public String toString() {
		return "AudioFeatures [danceability=" + danceability + ", valence="
				+ valence + ", energy=" + energy + ", loudness=" + loudness
				+ "]";
	}

	public double getDanceability() {
		return danceability;
	}

	public void setDanceability(double danceability) {
		this.danceability = danceability;
	}

	public double getLoudness() {
		return loudness;
	}

	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getValence() {
		return valence;
	}

	public void setValence(double valence) {
		this.valence = valence;
	}
}
