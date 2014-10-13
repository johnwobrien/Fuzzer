import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * 
 */

/**
 * @author John
 *
 */
public class Test {
		public void run(){
			
		}
		//content file broken out
		private ArrayList<String> conts=new ArrayList<String>();
		private ArrayList<String> vect=new ArrayList<String>();
		private boolean rand=false;
		private int slow=500;
		@SuppressWarnings("resource")
		public Test(String vectFile, String contFile, boolean rand, int slow) throws FileNotFoundException{
			this.rand=rand;
			this.slow=slow;
			Scanner sc = new Scanner(new File(contFile));
			while(sc.hasNextLine()){
				conts.add(sc.nextLine());
				
			}
			sc = new Scanner(new File(vectFile));
			while(sc.hasNextLine()){
				vect.add(sc.nextLine());
				
			}
			
			
		}
		
		
		public void inputAttack(HtmlTextInput input, HtmlSubmitInput sub) throws IOException{
		
		
			if(input instanceof HtmlTextInput){
				

				for(String v: vect){
					((HtmlTextInput)input).setValueAttribute(v);
					try{
					HtmlPage ret =sub.click();
					verifyOutput(ret, v);
					}catch(com.gargoylesoftware.htmlunit.ScriptException e){
					
					}

				}
			}
		}
		
		public void paramAttack(String inaddress, WebClient webClient, String param){
			try {
				for(String v:vect){
					HtmlPage page = webClient.getPage(inaddress+"?"+param+"="+v);
					verifyOutput(page, v);
					
				}
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		private void verifyOutput(HtmlPage ret, String v){
			boolean flag=false;
			for(String a : conts){
				
				//checks if the response contains important data about the system
				if(ret.getWebResponse().getContentAsString().contains(a)){
					if(!flag){
						System.out.println("At URL: "+ret.getUrl());
						flag=true;
					}
					System.out.println("	Sensative data leaked: "+a);
					System.out.println("	   Using input string: "+v);
				}
				
				
			}
			//checks if the response has the data we submited and if it shoudl be sanatized
			if(ret.getWebResponse().getContentAsString().contains(v)&&
					(v.contains("<")||v.contains(">")||v.contains("/")||
					v.contains("\\")||v.contains("*")||v.contains("'")||
					v.contains("\"")||v.contains("-")||v.contains("%"))){
				if(!flag){
					System.out.println("At URL: "+ret.getUrl());
					flag=true;
				}
				System.out.println("	Lack of Sanitization: "+v);
			}
			//Default 500 milliseconds
			if(ret.getWebResponse().getLoadTime()>slow){
				if(!flag){
					System.out.println("At URL: "+ret.getUrl());
					flag=true;
				}
				System.out.println("	Delayed response of "+ret.getWebResponse().getLoadTime());
			}
			//200 is everything normal
			if(ret.getWebResponse().getStatusCode()!=200){
				if(!flag){
					System.out.println("At URL: "+ret.getUrl());
					flag=true;
				}
				System.out.println("	Status Code of "+ret.getWebResponse().getStatusCode());
				System.out.println("	               "+ret.getWebResponse().getStatusMessage());
			}
		}
}
