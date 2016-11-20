package extractors;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FacebookUserConnector {

	final static Logger logger = Logger.getLogger(FacebookUserConnector.class);
	private FirefoxDriver driver;

	public FacebookUserConnector() {
		String baseUrl = "https://www.facebook.com/";
		this.driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(baseUrl);

		// Logging in..
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("musicmapsproject@gmail.com");
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys("musicisgreat");
		driver.findElement(By.id("loginbutton")).click();
	}

	public Location getLocation(SpotifyUser user) {
		driver.get("https://www.facebook.com/search/people/?q="
				+ user.getDisplayName().replace(" ", "%20"));

		String searchHtmlContent = driver.getPageSource();
		Document document = Jsoup.parse(searchHtmlContent);
		Elements pics = document.select("img[src*=" + user.getImage() + "]");
		if (pics.isEmpty()) {
			logger.info("Facebook account of user " + user.getDisplayName()
					+ " is not found");
			return null;
		} else {
			String facebookPageOfUser = pics.first().parent().attr("href");
			logger.info("Facebook account of user " + user.getDisplayName()
					+ " is: " + facebookPageOfUser);
			Location location = null;
			driver.get(facebookPageOfUser);
			Document doc = Jsoup.parse(driver.getPageSource());

			Elements infoElements = doc.select("div._50f3");

			if (!infoElements.isEmpty()) {

				for (Element infoElement : infoElements) {
					String header = infoElement.ownText();

					if (header.contains("Studied")
							|| header.contains("Studies")||header.contains("okuyor")) {

						location = new Location(infoElement.select(
								"a.profileLink").text());
					}
				}
			}

			if (location != null)
				logger.info("User: " + facebookPageOfUser + " Location:"
						+ location.getUniversity());
			else
				logger.info("No address is found for " + facebookPageOfUser);
			return location;

		}

	}

}
