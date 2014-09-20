import java.io.IOException;
import java.util.Scanner;

/**
 * @author John
 *
 */
public class Main {

	/**
	 * @param args [discover | test] url OPTIONS
	 */
	public static void main(String[] args) {
		// Stop HTMLUnit for outputting warnings.
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

		if(args.length < 2) {
			System.out.println("USAGE: java Main [discover | test] url OPTIONS");
		} else {
			if(args[0].equals("discover")){
				String url, custom_auth, common_words;
				custom_auth = null;
				common_words = null;
				
				// URL
				url = args[1];
				System.out.println(url);
				
				// OPTIONS
				for(int i=2; i<args.length; i++){
					System.out.println(args[i]);
					if(args[i].contains("--custom-auth")) {
						custom_auth = args[i].split("=", 2)[1];
					} else if(args[i].contains("--common-words")) {
						common_words = args[i].split("=", 2)[1];
					}
				}
				
				Discover runner = new Discover(url, custom_auth, common_words);
				runner.search(url);
			}
		}
	}

}
