/*
 *	tools.Fasta
 *	Fasta file parsing tool
 *	Creates and deletes lib/fasta_tmp.txt for parsing
 *	'parse()' function extracts single sequence at a time
 */

package tools;

import java.io.PrintWriter;
import java.io.IOException;

public class Fasta{
	private FileIO file;
	private Shell bash;

	private String[] fasta_tmp;
	private int iter = 0;
	
	public Fasta(String prob_ID) throws IOException, InterruptedException {
		file = new FileIO(prob_ID);
		bash = new Shell();

		bash.execute("mkdir tmp");
		PrintWriter tmp = new PrintWriter("tmp/fasta_tmp.txt");

		String line = file.reader.readLine();
		while(line != null){
			if(line.startsWith(">")) tmp.println("");
			tmp.println(line);
			line = file.reader.readLine();
		}
		tmp.close();

		bash.execute("cat tmp/fasta_tmp.txt");
		fasta_tmp = bash.raw();
	}
	
	private boolean isChar(char ch){
		return ch >= 'A' && ch <= 'Z';
	}
	
	public String parse() throws IOException{
		String line = "";
		String seq = "";
		
		while(true){
			if (iter >= fasta_tmp.length) return "$END_OF_FILE";

			line = fasta_tmp[iter++];
			if (line.length() == 0) continue;
			if (line.startsWith(">")) break;
		}
		
		while(true){
			if (iter >= fasta_tmp.length) break;

			line = fasta_tmp[iter++];
			if (line.length() == 0) break;
			if (isChar(line.charAt(0))) seq += line;
			else break;
		}
		
		return seq;
	}
	
	public PrintWriter writer(){
		return file.writer;
	}
	
	public void end() throws IOException, InterruptedException {
		file.close();
		bash.execute("rm -rf tmp");
		bash.close();
	}
}
