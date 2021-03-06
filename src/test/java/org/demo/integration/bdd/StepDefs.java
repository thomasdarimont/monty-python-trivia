package org.demo.integration.bdd;

import static org.junit.Assert.*;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.demo.Application;
import org.demo.integration.pages.AnswerPage;
import org.demo.integration.pages.QuestionPage;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class StepDefs {
	
	@Autowired private WebApplicationContext context;
	
	MockMvcHtmlUnitDriver driver;
	QuestionPage questionPage;
	AnswerPage answerPage;
	String lastQuestionAsked = "";
	
	@Before
	public void setup() throws IOException {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		//	The tests always failed when executing JavaScript such as document.addEventListener()
		//	(as found in JQuery).  Adding this chrome capability patched up the problem.
		//	Update: chrome doesn't seem to get AJAX replies from the server to work.  FireFox does:
		Capabilities capabilities = DesiredCapabilities.firefox(); 
		driver = new MockMvcHtmlUnitDriver(mockMvc, capabilities) {
			@Override
			protected WebClient configureWebClient(WebClient client) {
				client = super.configureWebClient(client);
				client.setAjaxController(new NicelyResynchronizingAjaxController());
				return client;
			}
		};
	}

	@After
	public void destroy() {
		if(driver != null) {
			driver.close();
		}
	}

	
	@Given("I am on the first page")	
	@When("I go to the landing page")
	public void i_go_to_the_landing_page() {
		questionPage = QuestionPage.to(driver);
	}


	@Then("I expect to see a list of Monty Python movies")
	public void i_expect_to_see_movie_list() {
		assertTrue(
			"Should have at least 3 options in the list", 
			questionPage.getNumberOfMovieOptions() > 3);  
	}

	
	@Then("one of the options should be 'Holy Grail'")
	public void i_expect_holy_grail() {
		assertTrue(
			"Holy Grail doesn't seem to be one of the options.", 
			questionPage.isMovieOptionPresent("Holy Grail"));
	}

	
	@When("I select 'Holy Grail'")
	public void i_select_category() {
		questionPage.selectMovieOption("Holy Grail");
	}

	
	@And("I select 'What do the Knights of Ni say'")
	public void i_select_question() { 
		lastQuestionAsked = "What do the Knights of Ni say?";
		questionPage.selectQuestionOption(lastQuestionAsked);
	}
	
	
	@And("I press submit")
	public void submit()  {
		questionPage.submit();
	}

	
	@Then("I should see the answer page")
	public void on_answer_page() { 
		assertTrue(
			"Was expecting to be on the Answer page", 
			AnswerPage.isCurrentPage(driver));
		answerPage = AnswerPage.at(driver);
	}

	
	@And("I should see the question displayed")
	public void question_displayed() { 
		assertTrue(
			"Was expecting to see the question \"" + lastQuestionAsked + "\".", 
			answerPage.hasQuestion(lastQuestionAsked)) ;
	}

	
	@And("I should see the answer 'Ni!'")
	public void answer_displayed() { 
		assertTrue(
			"Was expecting to see the answer 'Ni!'", 
			answerPage.hasAnswer("Ni!")) ;
	}
}