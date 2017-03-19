import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class ZoneIndexSearch {
	
	public static final double[] WEIGHTS = { 0.2, 0.3, 0.5 };

	public static void main(String[] args) {
		File[] collection = new File("fb2").listFiles();
		ZoneIndex zi = new ZoneIndex(collection);
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Query:");
		String query = sc.nextLine();
		//String query = "игра мартин";
		
		System.out.println(search(query, zi));
		sc.close();
	}
	
	public static TreeMap<Integer, Double> search(String q, Index index) {
		String[] tokens = q.split(" ");
		for(int i = 0; i < tokens.length; i++)
			tokens[i] = ZoneIndex.distinct(tokens[i].toLowerCase());
		
		TreeMap<Integer, Double> result = tree(index.getPostingList(tokens[0]));
		for(int i = 1; i < tokens.length; i++) {
			TreeMap<Integer, Double> list = tree(index.getPostingList(tokens[i]));
			result = zoneScore(result, list);
		}
		
		return result;
	}
	
	public static TreeMap<Integer, Double> zoneScore(TreeMap<Integer, Double> l1, TreeMap<Integer, Double> l2) {
		Iterator<Integer> i1 = l1.keySet().iterator();
		Iterator<Integer> i2 = l2.keySet().iterator();
		
		TreeMap<Integer, Double> result = new TreeMap<>();
		
		Integer p1 = null;
		Integer p2 = null;
		while(i1.hasNext() && i2.hasNext()) {
			p1 = i1.next();
			p2 = i2.next();
			if(p1 < p2) {
				while(p1 < p2 && i1.hasNext()) {
					p1 = i1.next();
				}
			} else if(p1 > p2) {
				while(p1 > p2 && i2.hasNext()) {
					p2 = i2.next();
				}
			}
			if(p1 == p2) {
				result.put(p1, l1.get(p1) + l2.get(p2));
			}
		}
		
		return result;
	}
	
	public static double weight(Posting p) {
		return p.author*WEIGHTS[0] + p.title*WEIGHTS[1] + p.body*WEIGHTS[2];
	}
	
	public static TreeMap<Integer, Double> tree(ArrayList<Posting> postingList) {
		TreeMap<Integer, Double> result = new TreeMap<>();
		
		for(Posting posting: postingList)
			result.put(posting.docId, weight(posting));
			
		return result;
	}

}
