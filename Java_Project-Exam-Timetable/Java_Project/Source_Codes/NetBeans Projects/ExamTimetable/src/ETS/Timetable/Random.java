package ETS.Timetable;

import java.util.ArrayList;

public class Random {

	private ArrayList<Integer> RandQueue;
	private int modulus;
	private int add;
	private int multiplier;

	public Random (int n){
		RandQueue=new ArrayList<Integer>();
		genRandom(n);		
        for (int i=0; i<n; i++) {
        	RandQueue.add(i, new Integer(map(i)));
        }
	}
	public int getRandValue(int i) {
		return RandQueue.get(i).intValue();
	}
	
	private void genRandom(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		modulus = n;
		add = (int) (Math.random()*n);
		if (n == 1) {
			return;
		}

		// Initialize the multiplier and offset
		multiplier = (int) Math.sqrt(n);
		while (gcd(multiplier, n) != 1) {
			if (++multiplier == n) {
				multiplier = 1;
			}
		}
	}

	private int map(int i) {
        return (int) ((multiplier * i + add) % modulus);
	}

	private static int gcd(int a, int b) {
		while (b != 0) {
			int tmp = a % b;
			a = b;
			b = tmp;
		}
		return a;
	}
}
