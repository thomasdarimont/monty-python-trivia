package org.demo;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class WebClientTests {

	//	This is a use of the raw HTMLUnit WebClient.  It requires a server running and a
	//	URL that you can actually hit.  Good for debugging problems.
	@Test(expected = FailingHttpStatusCodeException.class)
	public void test() throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getPage("http://localhost:8080/mpt/dummy");
	}

	@Test
	public void testWebDriver() {
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://localhost:8080/mpt/dummy");
		System.out.println("Page Source: " + driver.getPageSource());
	}
}
