/*
 *	tools.FileIO
 *	General file input/output module
 *	Access to files 'lib/rosalind_prob.txt', 'out/result_prob.txt'
 */

package tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;

public class FileIO {
	protected BufferedReader reader;
	protected PrintWriter writer;
	private char iostyle; // c : console; f : standard file; t : textbook file;
	
	public FileIO() throws IOException {
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out);
		iostyle = 'c';
	}
	
	public FileIO(String prob) throws IOException {
		reader = new BufferedReader(new FileReader("/Users/wook/Documents/eclipse-workspace/Rosalind/lib/~ba/rosalind_" + prob + ".txt"));
		writer = new PrintWriter("/Users/wook/Documents/eclipse-workspace/Rosalind/out/~ba/result_" + prob + ".txt");
		iostyle = 'f';
	}
	
	// exclusively for ROSALIND textbook track source files
	public FileIO(int chap, char prob) throws IOException {
		reader = new BufferedReader(new FileReader("/Users/wook/Documents/eclipse-workspace/Rosalind/lib/ba/ch" 
				+ String.valueOf(chap) + "/rosalind_BA" + String.valueOf(chap) + prob + ".txt"));
		writer = new PrintWriter("/Users/wook/Documents/eclipse-workspace/Rosalind/out/ba/ch" 
				+ String.valueOf(chap) + "/result_ba" + String.valueOf(chap) + prob + ".txt");
		iostyle = 't';
	}

	public String read() throws IOException {
		String line = reader.readLine();
		return line;
	}

	public void write(String str) throws IOException {
		writer.println(str);
	}

	public void type(String str) throws IOException {
		writer.print(str);
	}
	
	public void writeObj(Object obj) throws IOException {
		writer.println(String.valueOf(obj));
	}
	
	public void typeObj(Object obj) throws IOException {
		writer.print(String.valueOf(obj));
	}

	public void close() throws IOException {
		reader.close();
		writer.close();
	}
	
	public boolean isConsole() {return iostyle == 'c';}
}
