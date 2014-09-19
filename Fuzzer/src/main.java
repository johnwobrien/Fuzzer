import java.io.IOException;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author John
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args[0].equals("discover")){
			System.out.println(args[1]);
			discover runner = new discover(args[1]);
			runner.search(args[1]);
		}
		
	}

}
