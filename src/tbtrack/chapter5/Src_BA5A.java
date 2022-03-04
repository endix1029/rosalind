package tbtrack.chapter5;

import tools.FileIO;

public class Src_BA5A {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'a');
		int money = Integer.parseInt(file.read());
		
		String[] cstr = file.read().split(",");
		int[] coins = new int[cstr.length];
		for(int i = 0; i < cstr.length; i++) {
			coins[i] = Integer.parseInt(cstr[i]);
		}
		
		int maxc = 0;
		for(int coin : coins) {
			if(coin > maxc) maxc = coin;
		}
		
		int[] mins = new int[money + 1];
		mins[0] = 0;
		
		for(int val = 1; val <= money; val++) {
			mins[val] = 1 << 30;
			for(int coin : coins) {
				if(val - coin >= 0) {
					if(mins[val] > mins[val - coin] + 1) {
						mins[val] = mins[val - coin] + 1;
					}
				}
			}
		}
		
		file.typeObj(mins[money]);
		file.close();
	}
}
