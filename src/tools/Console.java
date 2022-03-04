/*	
 *	tools.Console
 *	Contains static console input variable 'in'
 */

package tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Console {
	public static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public Console(){}

	public static void close() throws IOException {
		in.close();
	}
}
