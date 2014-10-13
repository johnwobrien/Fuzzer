import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author John
 */
//discover http://www.reddit.com
//discover http://127.0.0.1/ --custom-auth=dvwa --common-words=mywords.txt
//discover http://127.0.0.1/ --custom-auth=BodgeIt --common-words=mywords.txt
public class Main {

	/**
	 * @param args [discover | test] url OPTIONS
	 */
	public static void main(String[] args) {
		// Stop HTMLUnit from outputting warnings.
		boolean t= false;
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

		if(args.length < 2) {
			System.out.println("USAGE: java -jar Fuzzer.jar discover url OPTIONS");
		} else {
			if(args[0].equals("discover")){
				String url, custom_auth, common_words;
				custom_auth = null;
				common_words = null;
				// URL
				url = args[1];
				
				// OPTIONS
				String vects = "";
				String sens= "";
				for(int i=2; i<args.length; i++){
					if(args[i].contains("--custom-auth")) {
						custom_auth = args[i].split("=", 2)[1];
					} else if(args[i].contains("--common-words")) {
						common_words = args[i].split("=", 2)[1];
					}
				}

				Discover runner = new Discover(url, custom_auth, common_words, false, null, false, 9999);
				runner.search(url);
				
				
			}else if(args[0].equals("test")){
				t=true;
				int slow=500;
				boolean rand=false;
				String url, custom_auth, common_words;
				custom_auth = null;
				common_words = null;
				
				// URL
				url = args[1];
				
				// OPTIONS
				String vects = "";
				String sens= "";
				for(int i=2; i<args.length; i++){
					if(args[i].contains("--custom-auth")) {
						custom_auth = args[i].split("=", 2)[1];
					} else if(args[i].contains("--common-words")) {
						common_words = args[i].split("=", 2)[1];
					}else if(args[i].contains("--vectors")){
						vects = args[i].split("=", 2)[1];
					}else if(args[i].contains("--sensitive")){
						sens = args[i].split("=", 2)[1];
					}else if(args[i].contains("--slow")){
						slow = Integer.parseInt(args[i].split("=", 2)[1]);
					}else if(args[i].contains("--random")){
						rand = Boolean.parseBoolean( args[i].split("=", 2)[1]);
					}
				}
				Test test;
				try {
					test = new Test(vects, sens, rand,slow);
					Discover runner = new Discover(url, custom_auth, common_words, t, test, rand, slow);
					runner.search(url);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.err.println("Bad File");
				}
				
			}
		}
	}

}
