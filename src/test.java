import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * 
 */

/**
 * @author John
 *
 */
public class test {
		public void run(){
			
		}
		
		
		public void inputAttack(HtmlElement input, List<HtmlElement> inputs ) throws FileNotFoundException{
			if(input instanceof HtmlTextInput){
				
				Scanner sc = new Scanner(new File("sql.txt"));
				while(sc.hasNextLine()){
					String scn=sc.nextLine();
					System.out.println("Search String: "+scn);
					((HtmlTextInput)input).setValueAttribute(scn);
					for(HtmlElement elm : inputs){
						try{
						if(elm instanceof HtmlSubmitInput){
							Page ret =elm.click();
							System.out.println("content as string: "+ret.getWebResponse().getContentAsString());
						}
						}catch(Exception e){
							
						}
					}
				}
			}
		}
}
