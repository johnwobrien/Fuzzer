import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



public class Discover {
	private URL address;
	private String custom_auth, common_words;
	
	private WebClient webClient;
	// List of links already visited
	private ArrayList<String> av = new ArrayList<String>();
	// List of links that need to be visited
	private ArrayList<String> visit = new ArrayList<String>();
	
	public Discover(String url, String custom_auth, String common_words){
		this.custom_auth = custom_auth;
		this.common_words = common_words;
		try {
			address = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.waitForBackgroundJavaScript(50000);
		
		// Logs in if custom_auth is non-null and known.
		HtmlPage mainPage = Login.login(custom_auth, webClient);
		if(mainPage != null) {
			address = mainPage.getUrl();
		}
	}
	
	public void search(String inaddress){
		av.add(inaddress);
		visit.add(inaddress);
		while(visit.size() > 0) {
			searchSub(visit.get(0));
			visit.remove(0);
		}
		System.out.println("finished");
		
	}
	public void searchSub(String inaddress){
		try {
			System.out.println("\nIn search: "+inaddress);
			
			HtmlPage page = webClient.getPage(inaddress);
			List<HtmlForm> forms = page.getForms();
			for (HtmlForm form : forms) {
				String id = form.getId();
				if("".equals(id)) id = "(no ID for form) " + form.getCanonicalXPath();
				System.out.println("     Input discovered: " + id );
			}
			List<HtmlAnchor> links = page.getAnchors();
			String url;
			for (HtmlAnchor link : links) {
				// If this link is outside of the site (external link):
				if(!page.getFullyQualifiedUrl(link.getHrefAttribute()).getHost().equals(address.getHost())) {
					continue;
				}
				url = page.getFullyQualifiedUrl(link.getHrefAttribute()).toString();
				
				// Have we already visited this url?
				if(url.contains("dvwa")){
					System.out.println("breakpoint");
				}
				if(av.contains(url)) continue;
				
				// New internal URL!
				System.out.println("     Link discovered: " + link.asText() + " @URL=" + url);
				av.add(url);
				
				// Can we follow this url?  TODO: Blacklist should be expanded
				if(!url.contains(".zip")){
					visit.add(url);
				}
				
			}
			
			System.out.println("done");
			return;
			
		} catch(FailingHttpStatusCodeException e404){
			System.out.println("404 at URL="+inaddress);
		} catch(org.apache.http.conn.HttpHostConnectException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
}
