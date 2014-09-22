import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
	// List of query inputs discovered
	private HashMap<String, Set<String>> queryInputs = new HashMap<String, Set<String>>();
	
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
		
		if(queryInputs.keySet().size() != 0) {
			// Print out collected query inputs
			System.out.println();
			System.out.println("Gathered URL Query Inputs:");
			for(String key : queryInputs.keySet()) {
				System.out.println("     For URL: " + key);
				for(String q : queryInputs.get(key)) {
					System.out.println("       Param: " + q);
				}
			}
		}
		System.out.println("finished");
		
	}
	public void searchSub(String inaddress){
		try {
			System.out.println("\nIn search: "+inaddress);
			
			// Load the requested page
			HtmlPage page = webClient.getPage(inaddress);
			
			// Forms discovery
			List<HtmlForm> forms = page.getForms();
			for (HtmlForm form : forms) {
				String id = form.getId();
				if("".equals(id)) id = "(no ID for form) " + form.getCanonicalXPath();
				System.out.println("     Form discovered: " + id );
				List<HtmlElement> inputs = form.getHtmlElementsByTagName("input");
				for(HtmlElement input : inputs){
					id = input.getAttribute("name");
					if("".equals(id)) continue;	// input must have name to be an input
					System.out.println("       Input discovered: " + id );
				}
			}
			
			// Links discovery
			List<HtmlAnchor> links = page.getAnchors();
			String url;
			for (HtmlAnchor link : links) {
				// If this link is outside of the site (external link):
				if(!page.getFullyQualifiedUrl(link.getHrefAttribute()).getHost().equals(address.getHost())) {
					continue;
				}
				URL urlObj = page.getFullyQualifiedUrl(link.getHrefAttribute());
				url = urlObj.toString();
				
				// Does URL have query inputs?
				if(urlObj.getQuery() != null) {
					url = url.replace('?' + urlObj.getQuery(), "");
					Set<String> qs;
					if(queryInputs.containsKey(url)) {
						qs = queryInputs.get(url);
					} else {
						qs = new HashSet<String>();
					}
					qs.add(urlObj.getQuery());
					queryInputs.put(url, qs);
				}
				
				// Have we already visited this url?
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
