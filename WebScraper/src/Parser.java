import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtilities;

public class Parser {
	
    public static void main(String[] args) throws Exception{
       
    	int startPage = 1;
    	int endPage = 30;
    	int j = 0;
    	
    	String url = "";
    	String html = "";

    	
    	String title = "";
    	String minSal = "";
    	String avgSal = "";
    	String maxSal = "";
    	String loc = "";
    	String company = "";
    	
		State AL = new State("AL");
		State AR = new State("AR");
		State AZ = new State("AZ");
		State CA = new State("CA");
		State CO = new State("CO");
		State CT = new State("CT");
		State DC = new State("DC");
		State DE = new State("DE");
		State FL = new State("FL");
		State GA = new State("GA");
		State HI = new State("HI");
		State IA = new State("IA");
		State ID = new State("ID");
		State IL = new State("IL");
		State IN = new State("IN");
		State KS = new State("KS");
		State KY = new State("KY");
		State LA = new State("LA");
		State MA = new State("MA");
		State MD = new State("MD");
		State ME = new State("ME");
		State MI = new State("MI");
		State MN = new State("MN");
		State MO = new State("MO");
		State NC = new State("NC");
		State NE = new State("NE");
		State NH = new State("NH");
		State NJ = new State("NJ");
		State NM = new State("NM");
		State NV = new State("NV");
		State NY = new State("NY");
		State OH = new State("OH");
		State OK = new State("OK");
		State OR = new State("OR");
		State PA = new State("PA");
		State RI = new State("RI");
		State SC = new State("SC");
		State TN = new State("TN");
		State TX = new State("TX");
		State UT = new State("UT");
		State VA = new State("VA");
		State VT = new State("VT");
		State WA = new State("WA");
		State WI = new State("WI");
		State OTHER = new State("OTHER");
		

    	
    	
		try {
			FileOutputStream fileOut = new FileOutputStream("C:\\Users\\mleczin\\Desktop\\Salaries.xlsx");
			Workbook workbook = new XSSFWorkbook();
			

			
			Sheet worksheet = workbook.createSheet("Sheet1");
			Sheet statesheet = workbook.createSheet("Sheet2");
		
			Row titleRow = worksheet.createRow(0);
			
			Cell cellA0 = titleRow.createCell( 0);
			cellA0.setCellValue("Title");
			
			Cell cellB0 = titleRow.createCell(1);
			cellB0.setCellValue("Company");
			
			Cell cellC0 = titleRow.createCell(2);
			cellC0.setCellValue("Location");
			
			Cell cellD0 = titleRow.createCell(3);
			cellD0.setCellValue("Min Salary");
			
			Cell cellE0 = titleRow.createCell(4);
			cellE0.setCellValue("Avg Salary");
			
			Cell cellF0 = titleRow.createCell(5);
			cellF0.setCellValue("Max Salary");
			
			
			
    	for(int i = startPage; i <= endPage; ++i){
    		
    		System.out.println("Page: " + i);
    		url = "https://www.glassdoor.com/Job/entry-level-software-engineer-jobs-SRCH_KO0,29_IP" + Integer.toString(i) + ".htm";

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

        		
        		//System.out.println("HERE");	
        		
        		System.out.println(loc);
        		System.out.println(title);
        		System.out.println(company);
        		System.out.println(minSal); 
        		System.out.println(avgSal); 
        		System.out.println(maxSal); 
        			
        		
        		
    			Row row = worksheet.createRow(i+j);
    			
    			Cell cellA1 = row.createCell(0);
    			cellA1.setCellValue(title);
    			
    			Cell cellB1 = row.createCell(1);
    			cellB1.setCellValue(company);
    			
    			Cell cellC1 = row.createCell(2);
    			cellC1.setCellValue(loc);
    			
    			Cell cellD1 = row.createCell(3);
    			cellD1.setCellValue(minSal);
    			
    			Cell cellE1 = row.createCell(4);
    			cellE1.setCellValue(avgSal); 
    			
    			Cell cellF1 = row.createCell(5);
    			cellF1.setCellValue(maxSal);
    			
    			
    			
    			
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
    			
    			
    	    	if(state.equals("AL")){
    	    		AL.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("AR")){
    	    		AR.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("AZ")){
    	    		AZ.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("CA")){
    	    		CA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("CO")){
    	    		CO.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("CT")){
    	    		CT.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("DC")){
    	    		DC.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("DE")){
    	    		DE.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("FL")){
    	    		FL.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("GA")){
    	    		GA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("HI")){
    	    		HI.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("IA")){
    	    		IA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("ID")){
    	    		ID.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("IL")){
    	    		IL.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("IN")){
    	    		IN.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("KS")){
    	    		KS.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("KY")){
    	    		KY.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("LA")){
    	    		LA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("MA")){
    	    		MA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("MD")){
    	    		MD.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("ME")){
    	    		ME.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("MI")){
    	    		MI.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("MN")){
    	    		MN.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("MO")){
    	    		MO.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NC")){
    	    		NC.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NE")){
    	    		NE.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NH")){
    	    		NH.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NJ")){
    	    		NJ.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NM")){
    	    		NM.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NV")){
    	    		NV.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("NY")){
    	    		NY.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("OH")){
    	    		OH.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("OK")){
    	    		OK.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("OR")){
    	    		OR.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("PA")){
    	    		PA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("RI")){
    	    		RI.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("SC")){
    	    		SC.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("TN")){
    	    		TN.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("TX")){
    	    		TX.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("UT")){
    	    		UT.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("VA")){
    	    		VA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("VT")){
    	    		VT.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("WA")){
    	    		WA.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	else if(state.equals("WI")){
    	    		WI.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}else{
    	    		OTHER.addPost(title, company, city, Integer.parseInt(minSal), Integer.parseInt(avgSal), Integer.parseInt(maxSal));
    	    	}
    	    	
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
    	
    	final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
    	dataset.addValue(AL.getJobCount(), "AL", "");
    	dataset.addValue(AR.getJobCount(), "AR", "");
    	dataset.addValue(AZ.getJobCount(), "AZ", "");
    	dataset.addValue(CA.getJobCount(), "CA", "");
    	dataset.addValue(CO.getJobCount(), "CO", "");
    	dataset.addValue(CT.getJobCount(), "CT", "");
    	dataset.addValue(DC.getJobCount(), "DC", "");
    	dataset.addValue(DE.getJobCount(), "DE", "");
    	dataset.addValue(FL.getJobCount(), "FL", "");
    	dataset.addValue(GA.getJobCount(), "GA", "");
    	dataset.addValue(HI.getJobCount(), "HI", "");
    	dataset.addValue(IA.getJobCount(), "IA", "");
    	dataset.addValue(ID.getJobCount(), "ID", "");
    	dataset.addValue(IL.getJobCount(), "IL", "");
    	dataset.addValue(IN.getJobCount(), "IN", "");
    	dataset.addValue(KS.getJobCount(), "KS", "");
    	dataset.addValue(KY.getJobCount(), "KY", "");
    	dataset.addValue(LA.getJobCount(), "LA", "");
    	dataset.addValue(MA.getJobCount(), "MA", "");
    	dataset.addValue(MD.getJobCount(), "MD", "");
    	dataset.addValue(ME.getJobCount(), "ME", "");
    	dataset.addValue(MI.getJobCount(), "MI", "");
    	dataset.addValue(MN.getJobCount(), "MN", "");
    	dataset.addValue(MO.getJobCount(), "MO", "");
    	dataset.addValue(NC.getJobCount(), "NC", "");
    	dataset.addValue(NE.getJobCount(), "NE", "");
    	dataset.addValue(NH.getJobCount(), "NH", "");
    	dataset.addValue(NJ.getJobCount(), "NJ", "");
    	dataset.addValue(NM.getJobCount(), "NM", "");
    	dataset.addValue(NV.getJobCount(), "NV", "");
    	dataset.addValue(NY.getJobCount(), "NY", "");
    	dataset.addValue(OH.getJobCount(), "OH", "");
    	dataset.addValue(OK.getJobCount(), "OK", "");
    	dataset.addValue(OR.getJobCount(), "OR", "");
    	dataset.addValue(PA.getJobCount(), "PA", "");
    	dataset.addValue(RI.getJobCount(), "RI", "");
    	dataset.addValue(SC.getJobCount(), "SC", "");
    	dataset.addValue(TN.getJobCount(), "TN", "");
    	dataset.addValue(TX.getJobCount(), "TX", "");
    	dataset.addValue(UT.getJobCount(), "UT", "");
    	dataset.addValue(VA.getJobCount(), "VA", "");
    	dataset.addValue(VT.getJobCount(), "VT", "");
    	dataset.addValue(WA.getJobCount(), "WA", "");
    	dataset.addValue(WI.getJobCount(), "WI", "");
    	dataset.addValue(OTHER.getJobCount(), "OTHER", "");

    	
    	
    	// Average Salaries
    	final DefaultCategoryDataset salaryDataset = new DefaultCategoryDataset( );
    	salaryDataset.addValue(AL.getAvgAvgSal(), "AL", "Mid Salary");
    	salaryDataset.addValue(AR.getAvgAvgSal(), "AR", "Mid Salary");
    	salaryDataset.addValue(AZ.getAvgAvgSal(), "AZ", "Mid Salary");
    	salaryDataset.addValue(CA.getAvgAvgSal(), "CA", "Mid Salary");
    	salaryDataset.addValue(CO.getAvgAvgSal(), "CO", "Mid Salary");
    	salaryDataset.addValue(CT.getAvgAvgSal(), "CT", "Mid Salary");
    	salaryDataset.addValue(DC.getAvgAvgSal(), "DC", "Mid Salary");
    	salaryDataset.addValue(DE.getAvgAvgSal(), "DE", "Mid Salary");
    	salaryDataset.addValue(FL.getAvgAvgSal(), "FL", "Mid Salary");
    	salaryDataset.addValue(GA.getAvgAvgSal(), "GA", "Mid Salary");
    	salaryDataset.addValue(HI.getAvgAvgSal(), "HI", "Mid Salary");
    	salaryDataset.addValue(IA.getAvgAvgSal(), "IA", "Mid Salary");
    	salaryDataset.addValue(ID.getAvgAvgSal(), "ID", "Mid Salary");
    	salaryDataset.addValue(IL.getAvgAvgSal(), "IL", "Mid Salary");
    	salaryDataset.addValue(IN.getAvgAvgSal(), "IN", "Mid Salary");
    	salaryDataset.addValue(KS.getAvgAvgSal(), "KS", "Mid Salary");
    	salaryDataset.addValue(KY.getAvgAvgSal(), "KY", "Mid Salary");
    	salaryDataset.addValue(LA.getAvgAvgSal(), "LA", "Mid Salary");
    	salaryDataset.addValue(MA.getAvgAvgSal(), "MA", "Mid Salary");
    	salaryDataset.addValue(MD.getAvgAvgSal(), "MD", "Mid Salary");
    	salaryDataset.addValue(ME.getAvgAvgSal(), "ME", "Mid Salary");
    	salaryDataset.addValue(MI.getAvgAvgSal(), "MI", "Mid Salary");
    	salaryDataset.addValue(MN.getAvgAvgSal(), "MN", "Mid Salary");
    	salaryDataset.addValue(MO.getAvgAvgSal(), "MO", "Mid Salary");
    	salaryDataset.addValue(NC.getAvgAvgSal(), "NC", "Mid Salary");
    	salaryDataset.addValue(NE.getAvgAvgSal(), "NE", "Mid Salary");
    	salaryDataset.addValue(NH.getAvgAvgSal(), "NH", "Mid Salary");
    	salaryDataset.addValue(NJ.getAvgAvgSal(), "NJ", "Mid Salary");
    	salaryDataset.addValue(NM.getAvgAvgSal(), "NM", "Mid Salary");
    	salaryDataset.addValue(NV.getAvgAvgSal(), "NV", "Mid Salary");
    	salaryDataset.addValue(NY.getAvgAvgSal(), "NY", "Mid Salary");
    	salaryDataset.addValue(OH.getAvgAvgSal(), "OH", "Mid Salary");
    	salaryDataset.addValue(OK.getAvgAvgSal(), "OK", "Mid Salary");
    	salaryDataset.addValue(OR.getAvgAvgSal(), "OR", "Mid Salary");
    	salaryDataset.addValue(PA.getAvgAvgSal(), "PA", "Mid Salary");
    	salaryDataset.addValue(RI.getAvgAvgSal(), "RI", "Mid Salary");
    	salaryDataset.addValue(SC.getAvgAvgSal(), "SC", "Mid Salary");
    	salaryDataset.addValue(TN.getAvgAvgSal(), "TN", "Mid Salary");
    	salaryDataset.addValue(TX.getAvgAvgSal(), "TX", "Mid Salary");
    	salaryDataset.addValue(UT.getAvgAvgSal(), "UT", "Mid Salary");
    	salaryDataset.addValue(VA.getAvgAvgSal(), "VA", "Mid Salary");
    	salaryDataset.addValue(VT.getAvgAvgSal(), "VT", "Mid Salary");
    	salaryDataset.addValue(WA.getAvgAvgSal(), "WA", "Mid Salary");
    	salaryDataset.addValue(WI.getAvgAvgSal(), "WI", "Mid Salary");
    	salaryDataset.addValue(OTHER.getAvgAvgSal(), "OTHER", "Mid Salary");
    	
    	// Average High Salaries
    	salaryDataset.addValue(AL.getAvgMaxSal(), "AL", "High Salary");
    	salaryDataset.addValue(AR.getAvgMaxSal(), "AR", "High Salary");
    	salaryDataset.addValue(AZ.getAvgMaxSal(), "AZ", "High Salary");
    	salaryDataset.addValue(CA.getAvgMaxSal(), "CA", "High Salary");
    	salaryDataset.addValue(CO.getAvgMaxSal(), "CO", "High Salary");
    	salaryDataset.addValue(CT.getAvgMaxSal(), "CT", "High Salary");
    	salaryDataset.addValue(DC.getAvgMaxSal(), "DC", "High Salary");
    	salaryDataset.addValue(DE.getAvgMaxSal(), "DE", "High Salary");
    	salaryDataset.addValue(FL.getAvgMaxSal(), "FL", "High Salary");
    	salaryDataset.addValue(GA.getAvgMaxSal(), "GA", "High Salary");
    	salaryDataset.addValue(HI.getAvgMaxSal(), "HI", "High Salary");
    	salaryDataset.addValue(IA.getAvgMaxSal(), "IA", "High Salary");
    	salaryDataset.addValue(ID.getAvgMaxSal(), "ID", "High Salary");
    	salaryDataset.addValue(IL.getAvgMaxSal(), "IL", "High Salary");
    	salaryDataset.addValue(IN.getAvgMaxSal(), "IN", "High Salary");
    	salaryDataset.addValue(KS.getAvgMaxSal(), "KS", "High Salary");
    	salaryDataset.addValue(KY.getAvgMaxSal(), "KY", "High Salary");
    	salaryDataset.addValue(LA.getAvgMaxSal(), "LA", "High Salary");
    	salaryDataset.addValue(MA.getAvgMaxSal(), "MA", "High Salary");
    	salaryDataset.addValue(MD.getAvgMaxSal(), "MD", "High Salary");
    	salaryDataset.addValue(ME.getAvgMaxSal(), "ME", "High Salary");
    	salaryDataset.addValue(MI.getAvgMaxSal(), "MI", "High Salary");
    	salaryDataset.addValue(MN.getAvgMaxSal(), "MN", "High Salary");
    	salaryDataset.addValue(MO.getAvgMaxSal(), "MO", "High Salary");
    	salaryDataset.addValue(NC.getAvgMaxSal(), "NC", "High Salary");
    	salaryDataset.addValue(NE.getAvgMaxSal(), "NE", "High Salary");
    	salaryDataset.addValue(NH.getAvgMaxSal(), "NH", "High Salary");
    	salaryDataset.addValue(NJ.getAvgMaxSal(), "NJ", "High Salary");
    	salaryDataset.addValue(NM.getAvgMaxSal(), "NM", "High Salary");
    	salaryDataset.addValue(NV.getAvgMaxSal(), "NV", "High Salary");
    	salaryDataset.addValue(NY.getAvgMaxSal(), "NY", "High Salary");
    	salaryDataset.addValue(OH.getAvgMaxSal(), "OH", "High Salary");
    	salaryDataset.addValue(OK.getAvgMaxSal(), "OK", "High Salary");
    	salaryDataset.addValue(OR.getAvgMaxSal(), "OR", "High Salary");
    	salaryDataset.addValue(PA.getAvgMaxSal(), "PA", "High Salary");
    	salaryDataset.addValue(RI.getAvgMaxSal(), "RI", "High Salary");
    	salaryDataset.addValue(SC.getAvgMaxSal(), "SC", "High Salary");
    	salaryDataset.addValue(TN.getAvgMaxSal(), "TN", "High Salary");
    	salaryDataset.addValue(TX.getAvgMaxSal(), "TX", "High Salary");
    	salaryDataset.addValue(UT.getAvgMaxSal(), "UT", "High Salary");
    	salaryDataset.addValue(VA.getAvgMaxSal(), "VA", "High Salary");
    	salaryDataset.addValue(VT.getAvgMaxSal(), "VT", "High Salary");
    	salaryDataset.addValue(WA.getAvgMaxSal(), "WA", "High Salary");
    	salaryDataset.addValue(WI.getAvgMaxSal(), "WI", "High Salary");
    	salaryDataset.addValue(OTHER.getAvgMaxSal(), "OTHER", "High Salary");
    	
    	// Average Low Salaries
    	salaryDataset.addValue(AL.getAvgMinSal(), "AL", "Low Salary");
    	salaryDataset.addValue(AR.getAvgMinSal(), "AR", "Low Salary");
    	salaryDataset.addValue(AZ.getAvgMinSal(), "AZ", "Low Salary");
    	salaryDataset.addValue(CA.getAvgMinSal(), "CA", "Low Salary");
    	salaryDataset.addValue(CO.getAvgMinSal(), "CO", "Low Salary");
    	salaryDataset.addValue(CT.getAvgMinSal(), "CT", "Low Salary");
    	salaryDataset.addValue(DC.getAvgMinSal(), "DC", "Low Salary");
    	salaryDataset.addValue(DE.getAvgMinSal(), "DE", "Low Salary");
    	salaryDataset.addValue(FL.getAvgMinSal(), "FL", "Low Salary");
    	salaryDataset.addValue(GA.getAvgMinSal(), "GA", "Low Salary");
    	salaryDataset.addValue(HI.getAvgMinSal(), "HI", "Low Salary");
    	salaryDataset.addValue(IA.getAvgMinSal(), "IA", "Low Salary");
    	salaryDataset.addValue(ID.getAvgMinSal(), "ID", "Low Salary");
    	salaryDataset.addValue(IL.getAvgMinSal(), "IL", "Low Salary");
    	salaryDataset.addValue(IN.getAvgMinSal(), "IN", "Low Salary");
    	salaryDataset.addValue(KS.getAvgMinSal(), "KS", "Low Salary");
    	salaryDataset.addValue(KY.getAvgMinSal(), "KY", "Low Salary");
    	salaryDataset.addValue(LA.getAvgMinSal(), "LA", "Low Salary");
    	salaryDataset.addValue(MA.getAvgMinSal(), "MA", "Low Salary");
    	salaryDataset.addValue(MD.getAvgMinSal(), "MD", "Low Salary");
    	salaryDataset.addValue(ME.getAvgMinSal(), "ME", "Low Salary");
    	salaryDataset.addValue(MI.getAvgMinSal(), "MI", "Low Salary");
    	salaryDataset.addValue(MN.getAvgMinSal(), "MN", "Low Salary");
    	salaryDataset.addValue(MO.getAvgMinSal(), "MO", "Low Salary");
    	salaryDataset.addValue(NC.getAvgMinSal(), "NC", "Low Salary");
    	salaryDataset.addValue(NE.getAvgMinSal(), "NE", "Low Salary");
    	salaryDataset.addValue(NH.getAvgMinSal(), "NH", "Low Salary");
    	salaryDataset.addValue(NJ.getAvgMinSal(), "NJ", "Low Salary");
    	salaryDataset.addValue(NM.getAvgMinSal(), "NM", "Low Salary");
    	salaryDataset.addValue(NV.getAvgMinSal(), "NV", "Low Salary");
    	salaryDataset.addValue(NY.getAvgMinSal(), "NY", "Low Salary");
    	salaryDataset.addValue(OH.getAvgMinSal(), "OH", "Low Salary");
    	salaryDataset.addValue(OK.getAvgMinSal(), "OK", "Low Salary");
    	salaryDataset.addValue(OR.getAvgMinSal(), "OR", "Low Salary");
    	salaryDataset.addValue(PA.getAvgMinSal(), "PA", "Low Salary");
    	salaryDataset.addValue(RI.getAvgMinSal(), "RI", "Low Salary");
    	salaryDataset.addValue(SC.getAvgMinSal(), "SC", "Low Salary");
    	salaryDataset.addValue(TN.getAvgMinSal(), "TN", "Low Salary");
    	salaryDataset.addValue(TX.getAvgMinSal(), "TX", "Low Salary");
    	salaryDataset.addValue(UT.getAvgMinSal(), "UT", "Low Salary");
    	salaryDataset.addValue(VA.getAvgMinSal(), "VA", "Low Salary");
    	salaryDataset.addValue(VT.getAvgMinSal(), "VT", "Low Salary");
    	salaryDataset.addValue(WA.getAvgMinSal(), "WA", "Low Salary");
    	salaryDataset.addValue(WI.getAvgMinSal(), "WI", "Low Salary");
    	salaryDataset.addValue(OTHER.getAvgMinSal(), "OTHER", "Low Salary");
    	
    	
    	
    	// Number of Cities per state
    	final DefaultCategoryDataset citiesDataset = new DefaultCategoryDataset( );
    	citiesDataset.addValue(AL.numOfCities(), "AL", "");
    	citiesDataset.addValue(AR.numOfCities(), "AR", "");
    	citiesDataset.addValue(AZ.numOfCities(), "AZ", "");
    	citiesDataset.addValue(CA.numOfCities(), "CA", "");
    	citiesDataset.addValue(CO.numOfCities(), "CO", "");
    	citiesDataset.addValue(CT.numOfCities(), "CT", "");
    	citiesDataset.addValue(DC.numOfCities(), "DC", "");
    	citiesDataset.addValue(DE.numOfCities(), "DE", "");
    	citiesDataset.addValue(FL.numOfCities(), "FL", "");
    	citiesDataset.addValue(GA.numOfCities(), "GA", "");
    	citiesDataset.addValue(HI.numOfCities(), "HI", "");
    	citiesDataset.addValue(IA.numOfCities(), "IA", "");
    	citiesDataset.addValue(ID.numOfCities(), "ID", "");
    	citiesDataset.addValue(IL.numOfCities(), "IL", "");
    	citiesDataset.addValue(IN.numOfCities(), "IN", "");
    	citiesDataset.addValue(KS.numOfCities(), "KS", "");
    	citiesDataset.addValue(KY.numOfCities(), "KY", "");
    	citiesDataset.addValue(LA.numOfCities(), "LA", "");
    	citiesDataset.addValue(MA.numOfCities(), "MA", "");
    	citiesDataset.addValue(MD.numOfCities(), "MD", "");
    	citiesDataset.addValue(ME.numOfCities(), "ME", "");
    	citiesDataset.addValue(MI.numOfCities(), "MI", "");
    	citiesDataset.addValue(MN.numOfCities(), "MN", "");
    	citiesDataset.addValue(MO.numOfCities(), "MO", "");
    	citiesDataset.addValue(NC.numOfCities(), "NC", "");
    	citiesDataset.addValue(NE.numOfCities(), "NE", "");
    	citiesDataset.addValue(NH.numOfCities(), "NH", "");
    	citiesDataset.addValue(NJ.numOfCities(), "NJ", "");
    	citiesDataset.addValue(NM.numOfCities(), "NM", "");
    	citiesDataset.addValue(NV.numOfCities(), "NV", "");
    	citiesDataset.addValue(NY.numOfCities(), "NY", "");
    	citiesDataset.addValue(OH.numOfCities(), "OH", "");
    	citiesDataset.addValue(OK.numOfCities(), "OK", "");
    	citiesDataset.addValue(OR.numOfCities(), "OR", "");
    	citiesDataset.addValue(PA.numOfCities(), "PA", "");
    	citiesDataset.addValue(RI.numOfCities(), "RI", "");
    	citiesDataset.addValue(SC.numOfCities(), "SC", "");
    	citiesDataset.addValue(TN.numOfCities(), "TN", "");
    	citiesDataset.addValue(TX.numOfCities(), "TX", "");
    	citiesDataset.addValue(UT.numOfCities(), "UT", "");
    	citiesDataset.addValue(VA.numOfCities(), "VA", "");
    	citiesDataset.addValue(VT.numOfCities(), "VT", "");
    	citiesDataset.addValue(WA.numOfCities(), "WA", "");
    	citiesDataset.addValue(WI.numOfCities(), "WI", "");
    	citiesDataset.addValue(OTHER.numOfCities(), "OTHER", "");
    	

    	// Graph fonts
        Font titlefont = new Font("Dialog", Font.PLAIN, 40);
        Font keyfont = new Font("Dialog", Font.PLAIN, 20);
        Font font = new Font("Dialog", Font.PLAIN, 30);
        Font tickfont = new Font("Dialog", Font.PLAIN, 20);
        
    	// Graph one
        JFreeChart statesChart = ChartFactory.createBarChart(
                "Number of Jobs Per State", 
                "State", "Number of Jobs", 
                dataset,PlotOrientation.VERTICAL, 
                true, true, false);
           
        statesChart.getTitle().setFont(titlefont);
        statesChart.getLegend().setItemFont(keyfont);
        
        statesChart.getCategoryPlot().getDomainAxis().setLabelFont(font);
        statesChart.getCategoryPlot().getRangeAxis().setLabelFont(font);
        
        statesChart.getCategoryPlot().getDomainAxis().setTickLabelFont(tickfont);
        statesChart.getCategoryPlot().getRangeAxis().setTickLabelFont(tickfont);
        
             int width = 640*2;    /* Width of the image */
             int height = 480*2;   /* Height of the image */ 
             File StatesChart = new File( "C:\\Users\\mleczin\\Desktop\\StatesChart.jpeg"); 
             ChartUtilities.saveChartAsJPEG( StatesChart, statesChart, width, height);

             
             
         // Graph two
         JFreeChart salariesChart = ChartFactory.createBarChart(
                 "Average High/Low/Mid Salaries Per State", 
                 "State", "Salaries", 
                 salaryDataset,PlotOrientation.VERTICAL, 
                 true, true, false);
         
         salariesChart.getTitle().setFont(titlefont);
         salariesChart.getLegend().setItemFont(keyfont);
         
         salariesChart.getCategoryPlot().getDomainAxis().setLabelFont(font);
         salariesChart.getCategoryPlot().getRangeAxis().setLabelFont(font);
         
         salariesChart.getCategoryPlot().getDomainAxis().setTickLabelFont(tickfont);
         salariesChart.getCategoryPlot().getRangeAxis().setTickLabelFont(tickfont); 
         
         
              int width1 = 640*3;    /* Width of the image */
              int height1 = 480*3;   /* Height of the image */ 
              File SalariesChart = new File( "C:\\Users\\mleczin\\Desktop\\SalariesChart.jpeg"); 
              ChartUtilities.saveChartAsJPEG( SalariesChart, salariesChart, width1, height1);             

              
          // Graph three
          JFreeChart citiesChart = ChartFactory.createBarChart(
                  "Unique Cities with Jobs Per State", 
                  "State", "Number of Cities With Jobs", 
                  citiesDataset,PlotOrientation.VERTICAL, 
                  true, true, false);
               
          citiesChart.getTitle().setFont(titlefont);
          citiesChart.getLegend().setItemFont(keyfont);
          
          citiesChart.getCategoryPlot().getDomainAxis().setLabelFont(font);
          citiesChart.getCategoryPlot().getRangeAxis().setLabelFont(font);
          
          citiesChart.getCategoryPlot().getDomainAxis().setTickLabelFont(tickfont);
          citiesChart.getCategoryPlot().getRangeAxis().setTickLabelFont(tickfont); 
          
          
               int width2 = 640*2;    /* Width of the image */
               int height2 = 480*2;   /* Height of the image */ 
               File CitiesChart = new File( "C:\\Users\\mleczin\\Desktop\\CitiesChart.jpeg"); 
               ChartUtilities.saveChartAsJPEG( CitiesChart, citiesChart, width2, height2);              
             
               
               
    	System.out.println("Closing");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
    			
    	}catch(Exception e){
    		e.printStackTrace(System.out);
    		
    	}
    		
    	
        
     
        
    }
    
}
