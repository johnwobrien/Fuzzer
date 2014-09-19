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
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;



public class discover {
	URL address;
	WebClient webClient;
	ArrayList av = new ArrayList();
	ArrayList visit = new ArrayList();
	public discover(String args){
		try {
			address=new URL(args);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.waitForBackgroundJavaScript(50000);
	}
	
	public void search(String inaddress){
		av.add(inaddress);
		visit.add(inaddress);
		while(visit.size()>0){
			searchSub((String)visit.get(0));
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
				System.out.println("     Input discovered:"+form.getId() );
				
			}
			List<HtmlAnchor> links = page.getAnchors();
			for (HtmlAnchor link : links) {
				System.out.println("     Link discovered: " + link.asText() + " @URL=" + link.getHrefAttribute());
				if(link.getHrefAttribute().contains("..")||(!link.getHrefAttribute().contains("http://")&&!link.getHrefAttribute().contains("https://")||link.getHrefAttribute().contains(address.toString()))){
					System.out.println("               same site "+link.getHrefAttribute());
					if((av.contains(link.getHrefAttribute())||link.getHrefAttribute().contains(".zip"))){
						
					}else{
						System.out.println("          addingL: "+page.getFullyQualifiedUrl(link.getHrefAttribute() ));
						av.add(link.getHrefAttribute());
						visit.add(page.getFullyQualifiedUrl(link.getHrefAttribute() ).toString());
					}
				}
				
			}
			
			System.out.println("done");
			return;
			
		}catch(FailingHttpStatusCodeException e404){
			System.out.println("404 at URL="+inaddress);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		
		return;
	}
}
