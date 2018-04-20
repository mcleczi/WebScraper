package analyze;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordCount {

	
	public static String[] COMMON_WORDS = {"the","of","to","and","a","in","is","it","you","that","he","was","for","on","are","with",
									"as","i","his","they","be","at","one","have","this","from","or","had","by","hot","but",
									"some","what","there","we","can","out","other","were","all","your","when","up","use","word",
									"how","said","an","each","she","which","do","their","time","if","will","way","about","many",
									"then","them","would","write","like","so","these","such","long","make","thing","see","him",
									"two","has","look","more","day","could","go","come","did","my","sound","no","most","number",
									"who","over","know","than","call","first","people","may","down","side","been","now","find",
									"any","new","candidates","experience","using","dental","–","within","work","etc","and/or",
									"practices","high","401k","us","life","years'","minimum","&","-","multiple","/","basic",
									"vision","employee","including","must","desired","insurance","medical","full","company",
									"best","hands","year","least","end","years","not","eg","big","off","level","plus","preferred",
									"our","computer","science","related","good","field","well","paid","open","similar","plans",
									"large","system","based","office","highly","working","required","health","products","benefits",
									"through","skills","assistance","proficiency","information","both","strong","knowledge","ability",
									"development","software","processes"};
	
	
	public static String[] COMMON_BUZZ_WORDS = {"highly","skills","working","excellent","familiarity","effectively","demonstrated",
									"ability","knowledge","strong","understanding","able","professional","develop","solving",
									"proficiency",""};
	
	public static void main(String[] args){
		
		String test = "The quick brown fox, jumped, over over  garbage  garbage, value, java, c++ c++ c++ c++ java java	  (the) lazy: dog.}\n";
		ArrayList<String> testlist = new ArrayList<String>();
		testlist.add(test);
		
		Map<String, Integer> testmap = wordFreq(testlist);
		
		printMap(testmap, 0);
		System.out.println("------------------");
		printMap(sortMap(testmap), 0);
		
	}
	
	public static Map<String, Integer> wordFreq(ArrayList<String> arr){
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		
		ArrayList<String> commonWords = new ArrayList<String>();
		for(int i = 0; i < COMMON_WORDS.length; ++i)
			commonWords.add(COMMON_WORDS[i]);
		
		for(String bullet: arr){
			String[] words = bullet.split(" ");
			
			for(String word : words){
				word = word.replace(",", "");
				word = word.replace("(", "");
				word = word.replace(")", "");
				word = word.replace(":", "");
				word = word.replace(";", "");
				word = word.replace("{", "");
				word = word.replace("}", "");
				word = word.replace(".", "");
				word = word.replace("\n", "");
				word = word.replace("\t", "");
				
				if(!word.equals("") && !commonWords.contains(word.toLowerCase()))
					if(wordMap.containsKey(word.toLowerCase()))
						wordMap.put(word.toLowerCase(), wordMap.get(word.toLowerCase()) + 1);
					else
						wordMap.put(word.toLowerCase(), 1);
			}
		}

		
		return wordMap;
	}
	
	
	public static void printMap(Map<String, Integer> map, int cutoff){
		for(String key : map.keySet()){
			if(map.get(key) >= cutoff)
				System.out.println(key + " : " + map.get(key));
		}
	}
	
	public static void saveMap(Map<String, Integer> map, String filename, int cutoff){
		try {
			PrintWriter out = new PrintWriter(filename);
			
			for(String key : map.keySet()){
				if(map.get(key) >= cutoff)
					out.println(key + " : " + map.get(key));
			}
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Map<String, Integer> sortMap(Map<String, Integer> m){
		int maxVal = 0;
		String maxKey = "";
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map = m;
		Map<String, Integer> sorted = new LinkedHashMap<String, Integer>();
		
		while(map.size() != 0){
			for(String key : map.keySet()){
				if( map.get(key) > maxVal){
					maxVal = map.get(key);
					maxKey = key;
				}	
			}
			sorted.put(maxKey, maxVal);
			map.remove(maxKey);
			maxVal = 0;
			maxKey = "";
		}

		
		return sorted;
	}

}
