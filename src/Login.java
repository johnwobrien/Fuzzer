import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


public class Login {
	// Public method to route to appropriate custom authentication logic.
	public static HtmlPage login(String custom, WebClient client) {
		switch(custom.toLowerCase()) {
			case "dvwa":
				return dvwa(client);
			case "bodgeit":
				return bodgeit(client);
		}
		try{
			return client.getPage(custom);
		}catch(Exception e){
			System.out.println("Nulled out\n\n"+e+"\n\n\n");
			return null;
		}
	}
	
	
	// Hardcoded custom authentication logic:
	// DVWA
	private static HtmlPage dvwa(WebClient client) {
		System.out.println("Logging in to DVWA.");
		HtmlPage returnPage = null;
		
		try {
			HtmlPage page = client.getPage("http://127.0.0.1/dvwa/login.php");
			
		    // Get the form that we are dealing with and within that form, 
		    // find the submit button and the field that we want to change.
		    final HtmlForm form = (HtmlForm) page.getByXPath("/html/body/div/form").get(0);

		    final HtmlSubmitInput button = form.getInputByName("Login");
		    final HtmlTextInput username = form.getInputByName("username");
		    final HtmlPasswordInput password = form.getInputByName("password");

		    // Change the value of the text fields
		    username.setValueAttribute("admin");
		    password.setValueAttribute("password");

		    // Now submit the form by clicking the button and get back the second page.
		    returnPage = button.click();
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		
		return returnPage;
	}
	
	private static HtmlPage bodgeit(WebClient client) {
		System.out.println("Logging in to BodgeIT.");
		HtmlPage returnPage = null;
		
		try {
			HtmlPage page = client.getPage("http://127.0.0.1:8080/bodgeit/register.jsp");
			
		    // Get the form that we are dealing with and within that form, 
		    // find the submit button and the field that we want to change.
		    final HtmlForm form = (HtmlForm) page.getByXPath("/html/body/center/table/tbody/tr/td/table/tbody/tr/td/form").get(0);

		    final HtmlSubmitInput button = form.getInputByValue("Register");
		    final HtmlTextInput username = form.getInputByName("username");
		    final HtmlPasswordInput password = form.getInputByName("password1");
		    final HtmlPasswordInput password2 = form.getInputByName("password2");

		    // Change the value of the text fields
		    username.setValueAttribute("1234567@123.com");
		    password.setValueAttribute("password");
		    password2.setValueAttribute("password");
		    // Now submit the form by clicking the button and get back the second page.
		    returnPage = button.click();
		} catch (FailingHttpStatusCodeException | IOException  e) {
			e.printStackTrace();
		}
		try {
			HtmlPage page = client.getPage("http://127.0.0.1:8080/bodgeit/login.jsp");
			
		    // Get the form that we are dealing with and within that form, 
		    // find the submit button and the field that we want to change.
		    final HtmlForm form = (HtmlForm) page.getByXPath("/html/body/center/table/tbody/tr/td/table/tbody/tr/td/form").get(0);

		    final HtmlSubmitInput button = form.getInputByValue("Login");
		    final HtmlTextInput username = form.getInputByName("username");
		    final HtmlPasswordInput password = form.getInputByName("password");

		    // Change the value of the text fields
		    username.setValueAttribute("1234567@123.com");
		    password.setValueAttribute("password");
		    // Now submit the form by clicking the button and get back the second page.
		    returnPage = button.click();
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		
		return returnPage;
	}
}
