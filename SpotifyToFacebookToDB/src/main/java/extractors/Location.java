package extractors;

import org.apache.commons.lang.StringUtils;

public class Location {

	private String cityLive;
	private String cityBorned;
	private String university;

	public String getCityLive() {
		return cityLive;
	}

	public Location(String university) {
		super();
		this.university = formatUniversity(university);
	}

	public String formatUniversity(String university) {
		university = university.toLowerCase();
		if (university.contains("university")
				|| university.contains("üniversitesi")) {
			university = convertToEnglishCharacters(university.replaceAll(
					"üniversitesi", "university"));
			university = StringUtils.substringBefore(university, "university");
			university = university + "university";
		}
		return university;

	}

	public String convertToEnglishCharacters(String string) {
		for (char c : string.toCharArray()) {
			switch (c) {
			case 'ş':
			case 'Ş':
				string = string.replace(c, 's');
				break;
			case 'ç':
			case 'Ç':
				string = string.replace(c, 'c');
				break;
			case 'ı':
			case 'İ':
				string = string.replace(c, 'i');
				break;
			case 'ğ':
			case 'Ğ':
				string = string.replace(c, 'g');
				break;
			case 'ü':
			case 'Ü':
				string = string.replace(c, 'u');
				break;
			case 'ö':
			case 'Ö':
				string = string.replace(c, 'o');
				break;
			}
		}
		return string;
	}

	public Location(String cityLive, String cityBorned, String university) {
		super();
		this.cityLive = cityLive;
		this.cityBorned = cityBorned;
		this.university = university;
	}

	public void setCityLive(String cityLive) {
		this.cityLive = cityLive;
	}

	public String getCityBorned() {
		return cityBorned;
	}

	public void setCityBorned(String cityBorned) {
		this.cityBorned = cityBorned;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

}
