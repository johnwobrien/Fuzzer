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
		switch(custom) {
			case "dvwa":
				return dvwa(client);
		}
		return null;
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
}
