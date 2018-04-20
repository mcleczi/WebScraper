package parse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import analyze.WordCount;
import display.Excel;
import display.Graph;
import display.State;



public class HeadParser {
	
    public static void main(String[] args) throws Exception{
       
    	parseHeaderDetails("entry-level-software-engineer", 1, 1);

    }
    
    
    
    public static void parseHeaderDetails(String SEARCHSTRING, int startPage, int endPage){
    	
/*    	final String SEARCHSTRING = "entry-level-software-engineer";
    	
    	int startPage = 1;
    	int endPage = 30;*/
    	
    	// Next open excel cell open helper
    	int j = 0;
    	
    	String url = "";
    	String html = "";

    	String fullPostPrefix = "https://www.glassdoor.com";
    	
    	String title = "";
    	String minSal = "";
    	String avgSal = "";
    	String maxSal = "";
    	String loc = "";
    	String company = "";
    	String fullPostUrl = "";
    	ArrayList<String> Details = new ArrayList<String>();
    	
    	
		State AL = new State("AL"); State AR = new State("AR"); State AZ = new State("AZ");
		State CA = new State("CA"); State CO = new State("CO"); State CT = new State("CT");
		State DC = new State("DC"); State DE = new State("DE"); State FL = new State("FL");
		State GA = new State("GA"); State HI = new State("HI"); State IA = new State("IA");
		State ID = new State("ID"); State IL = new State("IL"); State IN = new State("IN");
		State KS = new State("KS"); State KY = new State("KY"); State LA = new State("LA");
		State MA = new State("MA"); State MD = new State("MD"); State ME = new State("ME");
		State MI = new State("MI"); State MN = new State("MN"); State MO = new State("MO");
		State NC = new State("NC"); State NE = new State("NE"); State NH = new State("NH");
		State NJ = new State("NJ"); State NM = new State("NM"); State NV = new State("NV");
		State NY = new State("NY"); State OH = new State("OH"); State OK = new State("OK");
		State OR = new State("OR"); State PA = new State("PA"); State RI = new State("RI");
		State SC = new State("SC"); State TN = new State("TN"); State TX = new State("TX");
		State UT = new State("UT"); State VA = new State("VA"); State VT = new State("VT");
		State WA = new State("WA"); State WI = new State("WI"); State OTHER = new State("OTHER");
		
		State[] STATES = {AL, AR, AZ, CA, CO, CT, DC, DE, FL, GA, HI, IA, ID, IL, IN, KS, KY, LA, MA, MD, ME, 
				MI, MN, MO, NC, NE, NH, NJ, NM, NV, NY, OH, OK, OR, PA, RI, SC, TN, TX, UT, VA, VT, WA, WI, OTHER};
    	
    	
		try {
			
			Excel HeaderExcel = new Excel();
			String outFile = "C:\\Users\\mleczin\\Desktop\\Salaries.xlsx";
			HeaderExcel.createHeader(outFile, "Sheet1");
			
			
    	for(int i = startPage; i <= endPage; ++i){
    		
    		System.out.println("Page: " + i);
    		url = "https://www.glassdoor.com/Job/" + SEARCHSTRING + "-jobs-SRCH_KO0,29_IP" + Integer.toString(i) + ".htm";

    		URLConnection conn = new URL(url).openConnection();
    		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    		conn.connect();
    		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		

        	String line;
    		while((line = in.readLine()) != null)
    			html += line;
    		in.close();
    		

    		while(html.indexOf(" loc'>") != -1 && 
    				(html.indexOf("</span></div></div><div class='flexbox'>") != -1 ||
    				 html.indexOf("</span></div><div class='alignRt'>") != -1 ||
    				 html.indexOf("</span></div><span class='hideHH nowrap'>") != -1 )){
    			
    			System.out.println(" ---------------------------------------------------- ");
        		System.out.println(html);
        	   	
        		
        		// This is the end of the job posting item in the list of postings
        		int jobItemEnd = html.indexOf("</div></div></li>");
        		
        		
        		// GET LOC INDICES
        		int locStart = html.indexOf("subtle loc'>");
        		locStart = locStart + 12;
        		int locEnd = html.indexOf("</span></div></div><div class='flexbox'>");
        		
        		// Sometimes the location end doesn't have a </span> tag
        		if(locEnd == -1)
        		{
        			locEnd = html.indexOf("</div></div></div><div class='flexbox'>");
        			if(locEnd == -1)
        				locEnd = html.indexOf("</span></span></div><div class='flexbox'>");
        		}
        		
        		// GET COMPANY INDICES
        		int compStart = html.indexOf("empLoc'><div> ");
        		compStart = compStart + 14;
        		int compEnd = html.indexOf(" &ndash; <span class='subtle loc'>");
        		
        		// GET TITLE INDICES
        		int titleStart = html.indexOf("data-jobtitle='");
        		titleStart = titleStart + 15;
        		int titleEnd = html.indexOf("' data-jobtitle-id=");
        		
        		// Filter for Apply Easy, HOT, NEW LISTING elements
        		if(html.substring(locStart, locEnd).indexOf("</span></div><div class='alignRt'>") != -1)
        			locEnd = html.indexOf("</span></div><div class='alignRt'>");
        		
        		if(html.substring(locStart, locEnd).indexOf("</span></div><span class='hideHH nowrap'>") != -1)
        			locEnd = html.indexOf("</span></div><span class='hideHH nowrap'>");
        		
        		if(html.substring(locStart, locEnd).indexOf("</span></div><div class=\"hotListing\">") != -1)
        			locEnd = html.indexOf("</span></div><div class=\"hotListing\">");
        		
        		
        		// Filter out that stupid dash in the job title (COMMENT THESE LINES OUT FOR HELPFUL TESTING)
        		if(titleEnd != -1 && html.substring(titleStart, titleEnd).indexOf(" –") != -1)
        			titleEnd = titleStart + html.substring(titleStart, titleEnd).indexOf(" –");
        		
        		// GET MIN SALARY INDICES
        		int minSalStart = html.indexOf("data-displayed-min-salary='");
        		minSalStart = minSalStart + 27;
        		int minSalEnd = html.indexOf("' data-displayed-med-salary");
        		
        		
        		// GET MED SALARY INDICES
        		int medSalStart = html.indexOf("data-displayed-med-salary='");
        		medSalStart = medSalStart + 27;
        		int medSalEnd = html.indexOf("' data-displayed-max-salary");
        		
        		// GET MAX SALARY INDICES
        		int maxSalStart = html.indexOf("data-displayed-max-salary='");
        		maxSalStart = maxSalStart + 27;
        		int maxSalEnd = html.indexOf("' data-employer-shortname");
        		
        		
        		// GET URL INDICES
        		int urlStart = html.indexOf("<a href='/partner");
        		urlStart = urlStart + 9;
        		int urlEnd = html.indexOf("' rel='nofollow");

        		// These if else statements are a catch-all for small html discrepancies
        		if(locEnd != -1)
        			loc = html.substring(locStart, locEnd);
        		else 
        			loc = "NA";
        		
        		if(titleEnd != -1)
        			title = html.substring(titleStart, titleEnd);
        		else 
        			title = "NA";
        		
        		if(compEnd != -1)
        			company = html.substring(compStart, compEnd);
        		else 
        			company = "NA";
        		
        		if(urlEnd != -1)
        			fullPostUrl = fullPostPrefix + html.substring(urlStart, urlEnd);
        		else
        			fullPostUrl = "NA";
        		
        		// Only set salaries if the page has one for that job, skip if non found
        		if(minSalStart < jobItemEnd && (minSalEnd != -1)){
            		minSal = html.substring(minSalStart, minSalEnd);
            		avgSal = html.substring(medSalStart, medSalEnd);
            		maxSal = html.substring(maxSalStart, maxSalEnd);        			
        		}
        		else{
        			minSal = "NA";
        			avgSal = "NA";
        			maxSal = "NA";
        		}

        		
        		System.out.println(loc);
        		System.out.println(title);
        		System.out.println(company);
        		System.out.println(minSal); 
        		System.out.println(avgSal); 
        		System.out.println(maxSal); 
        		System.out.println(fullPostUrl);
        		
        		
        		HeaderExcel.AddRow(HeaderExcel.getWorkbook(), HeaderExcel.getSheet(), i+j, title, company, loc, minSal, avgSal, maxSal, fullPostUrl);
    			
        		
    			// FILTER for NA results
    			String state = "";
    			String city = "";
    			
    			if(loc != "NA" && loc.indexOf(",") != -1){
    				state = loc.substring(loc.indexOf(", ") + 2);
    				city = loc.substring(0, loc.indexOf(","));
    			}
    			else
    				state = city = "NA";

    			
    			if(minSal == "NA" || avgSal == "NA" || maxSal == "NA")
    				minSal = avgSal = maxSal = "-1";
    			
    			
    			// Add the parsed post to the list of states
    			for(int k = 0; k < STATES.length; ++k){
    				if(state.equals(STATES[k].getName())){
    					STATES[k].addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    					k = STATES.length;
    				}
    			}
    			
    			
    			Details.addAll(DetailParser.Details(fullPostUrl));
    			
    	    	System.out.println("CA JOB COUNT: " + CA.getJobCount());
    	    	System.out.println("CA SAL COUNT: " + CA.getSalCount());
    	    	
    			// Cut off the html from the already parsed list item
    			html = html.substring(jobItemEnd + 8);
    			j = j +1;
    		}
    		
    		System.out.println("DONE");
    		
		conn = null;
		in.close();
		
    	}
    	
    	
    	
	//
	//	CREATE THE DATASETS FOR THE GRAPHS
	//
    	// Jobcount, create Dataset
    	final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
    	for(int i = 0; i < STATES.length; ++i)
    		dataset.addValue(STATES[i].getJobCount(), STATES[i].getName(), "");
    	
    	
    	// Average Salaries, create Dataset
    	final DefaultCategoryDataset salaryDataset = new DefaultCategoryDataset( );
    	for(int i = 0; i < STATES.length; ++i){
    		salaryDataset.addValue(STATES[i].getAvgAvgSal(), STATES[i].getName(), "Mid Salary");
    		salaryDataset.addValue(STATES[i].getAvgMaxSal(), STATES[i].getName(), "High Salary");
    		salaryDataset.addValue(STATES[i].getAvgMinSal(), STATES[i].getName(), "Low Salary");
    	}
    		
    	
    	// Number of Cities per state, create Dataset
    	final DefaultCategoryDataset citiesDataset = new DefaultCategoryDataset( );
		for(int i = 0; i < STATES.length; ++i)
			citiesDataset.addValue(STATES[i].numOfCities(), STATES[i].getName(), "");
    	
	//
	//	CREATE THE GRAPHS
	//		
		
		// Graph one
    	String g1filename = "C:\\Users\\mleczin\\Desktop\\StatesChart.jpeg";
		Graph g1 = new Graph();
		g1.createGraph("Number of Jobs Per State", dataset, "State", "Number of Jobs");
		g1.printGraph(g1filename, 480*2, 680*2);
		
		// Graph two
    	String g2filename = "C:\\Users\\mleczin\\Desktop\\SalariesChart.jpeg";
		Graph g2 = new Graph();
		g2.createGraph("Average High/Low/Mid Salaries Per State", salaryDataset, "State", "Salaries");
		g2.printGraph(g2filename, 480*3, 640*3);       

		// Graph three
    	String g3filename = "C:\\Users\\mleczin\\Desktop\\CitiesChart.jpeg";
		Graph g3 = new Graph();
		g3.createGraph("Unique Cities with Jobs Per State", citiesDataset, "State", "Number of Cities With Jobs");
		g3.printGraph(g3filename, 480*2, 640*2);
             
            
		
		for(String s : Details){
			System.out.println(s);
		}       
        
		Map<String, Integer> detailsWordFreq = WordCount.wordFreq(Details);
		Map<String, Integer> sortedDetailsWordFreq = WordCount.sortMap(detailsWordFreq);
		
			
		WordCount.printMap(sortedDetailsWordFreq, 30);
		String mapfilename = "C:\\Users\\mleczin\\Desktop\\WordFreq.txt";
		WordCount.saveMap(sortedDetailsWordFreq, mapfilename, 30);
		
		
    	// Create Dataset for the wordcount
    	final DefaultCategoryDataset wordsDataset = new DefaultCategoryDataset( );
		for(String key : sortedDetailsWordFreq.keySet())
			if(sortedDetailsWordFreq.get(key) >= 40)
				wordsDataset.addValue(sortedDetailsWordFreq.get(key), key, "");
		
		// Graph four
    	String g4filename = "C:\\Users\\mleczin\\Desktop\\BuzzWordsChart.jpeg";
		Graph g4 = new Graph();
		g4.createGraph("Most Common Buzz Words in Job Postings", wordsDataset, "Word", "Number of Occurrences");
		g4.printGraph(g4filename, 480*3, 640*6);
		
		
    	System.out.println("Closing");

    	HeaderExcel.WriteAndClose();
    			
    	}catch(Exception e){
    		e.printStackTrace(System.out);
    		
    	}

    }
    
    
    
    
    
}
