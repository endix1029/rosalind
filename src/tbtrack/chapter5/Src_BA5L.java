package tbtrack.chapter5;

import tools.*;
import java.util.*;

public class Src_BA5L {
	private static String pepx, pepy;
	private static List<Integer> tracks = new ArrayList<Integer>(); // 1 = down; 2 = right; 3 = diagonal;
	private static Blosum blosum = null;
	
	private static void linearAlignment(int top, int bot, int lft, int rgt) {
		if(lft == rgt) {
			for(int i = 0; i < bot - top; i++) tracks.add(1);
			return;
		}
		if(top == bot) {
			for(int i = 0; i < rgt - lft; i++) tracks.add(2);
			return;
		}
		
		int mid = (lft + rgt) / 2;
		int[] midEdge = Src_BA5K.midEdge(pepx, pepy, blosum, top, bot, lft, rgt);
		
		linearAlignment(top, midEdge[0], lft, mid);
		tracks.add(midEdge[1]);
		
		if(midEdge[1] == 1) midEdge[0]++;
		if(midEdge[1] == 2) mid++;
		if(midEdge[1] == 3) {
			midEdge[0]++;
			mid++;
		}
		
		linearAlignment(midEdge[0], bot, mid, rgt);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'l');
		pepx = file.read();
		pepy = file.read();
		blosum = new Blosum('B');
		
		linearAlignment(0, pepx.length(), 0, pepy.length());
		String trackx = "", tracky = "";
		int locx = 0, locy = 0, score = 0;
		
		for(int track : tracks) {
			if(track == 1) {
				trackx += pepx.charAt(locx++);
				tracky += '-';
				score -= 5;
			}
			if(track == 2) {
				tracky += pepy.charAt(locy++);
				trackx += '-';
				score -= 5;
			}
			if(track == 3) {
				trackx += pepx.charAt(locx);
				tracky += pepy.charAt(locy);
				score += blosum.get(pepx.charAt(locx++), pepy.charAt(locy++));
			}
		}
		
		file.writeObj(score);
		file.write(trackx);
		file.write(tracky);
		file.close();
	}
}
