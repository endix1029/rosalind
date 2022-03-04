/*
 *	tools.Shell
 *	Command line access module
 *	Executes UNIX commands on $Workspace/Rosalind folder
 *	Internal call should include string instance in constructor
 */

package tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;

public class Shell {
	private Process process = null;
	private BufferedReader reader = null;
	private boolean external = true;

	public Shell(){}

	public Shell(String inter){
		external = false;
	}

	public void input() throws IOException, InterruptedException {
		String command = Console.in.readLine();
		execute(command);
	}

	public void execute(String command) throws IOException, InterruptedException {
		if(reader == null && external){
			System.out.print("Launching command line access");
			for(int i = 0; i < 5; i++){
				Thread.sleep(200);
				System.out.print(".");
			}

			Thread.sleep(200);
			System.out.println("");
		}

		process = Runtime.getRuntime().exec(command);
		System.out.print("Processing command [" + command + "]");
		for(int i = 0; i < 5; i++){
			Thread.sleep(200);
			System.out.print(".");
		}
		process.waitFor();

		Thread.sleep(200);
		System.out.println("");

		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	// Print the result of the execution on a console
	public void print() throws IOException {
		if(reader == null){
			System.out.println("No command has been executed");
			return;
		}

		String line = reader.readLine();
		while(line != null){
			System.out.println(line);
			line = reader.readLine();
		}
	}

	// Return the result of the execution as an array of strings
	public String[] raw() throws IOException {
		if(reader == null){
			System.out.println("No command has been executed");
			return null;
		}

		ArrayList<String> alist = new ArrayList<String>();
		String line = reader.readLine();
		while(line != null){
			alist.add(line);
			line = reader.readLine();
		}

		String[] ls = new String[alist.size()];
		for(int i = 0; i < alist.size(); i++){
			ls[i] = alist.get(i);
		}

		return ls;
	}

	public void close() throws IOException {
		if(external) System.out.println("Command line access terminated");
		reader.close();
	}
}
