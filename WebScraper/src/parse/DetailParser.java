package parse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DetailParser {

	public static void main(String[] args){
		
		String URL = "https://www.glassdoor.com/partner/jobListing.htm?pos=2612&ao=89251&s=58&guid=00000162dedc61ebb4086ebb12bcf152&src=GD_JOB_AD&t=SR&extid=1&exst=O&ist=L&ast=OL&vt=w&slr=true&rtp=0&cs=1_17463922&cb=1524157408523&jobListingId=2699702721";
//		System.out.println("Passed URL: " + URL);
//		String newURL = followRedirect(URL);
//		System.out.println("Return URL: " + newURL);
		
		
		String html = getHtml(URL);
		System.out.println(html);
		ArrayList<String> details = parseDetails(html);
		
		
		for(String s : details){
			System.out.println(s);
		}
	}

	DetailParser(){
		
	}
	
	public static ArrayList<String> Details(String url){
		String html = getHtml(url);
		ArrayList<String> parsed = parseDetails(html);
		
		return parsed;
	}
	
	// In the case of a redirect, not currently used
	public static String followRedirect(String url){
		try{
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		conn.connect();
		
		// Might have to handle a redirect
		int responsecode = conn.getResponseCode();
		
		if(responsecode != HttpURLConnection.HTTP_OK)
			if(responsecode == HttpURLConnection.HTTP_MOVED_PERM 
				|| responsecode == HttpURLConnection.HTTP_MOVED_TEMP){
				
				String newUrl = conn.getHeaderField("Location");
				return followRedirect(newUrl);
			}
			else return url;
		else return url;

		
		}catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	public static String getHtml(String url){
		String html = "";
		try{

			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			conn.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			

	    	String line;
			while((line = in.readLine()) != null)
				html += line;
			in.close();
			
		}catch(Exception e){
			e.printStackTrace(System.out);
		}

		return html;
	}
	
	
	public static ArrayList<String> parseDetails(String html){
		ArrayList<String> bulletPoints = new ArrayList<String>();
		
		String description = "";
		if(html.contains("\"description\":") && html.contains("</script><div id=\"JobContent\">"))
			description = html.substring(html.indexOf("\"description\":"), html.indexOf("</script><div id=\"JobContent\">"));
		
		
		if(description.contains("Skills") || description.contains("Abilities") || description.contains("Qualifications")
			|| description.contains("skills") || description.contains("abilities") || description.contains("qualifications")
			|| description.contains("SKILLS") || description.contains("ABILITIES") || description.contains("QUALIFICATIONS")){
			
			if(description.contains("Skills"))
				description = description.substring(description.indexOf("Skills"));
			else if(description.contains("Abilities"))
				description = description.substring(description.indexOf("Abilities"));
			else if(description.contains("Qualifications"))
				description = description.substring(description.indexOf("Qualifications"));
			else if(description.contains("skills"))
				description = description.substring(description.indexOf("skills"));
			else if(description.contains("abilities"))
				description = description.substring(description.indexOf("abilities"));
			else if(description.contains("qualifications"))
				description = description.substring(description.indexOf("qualifications"));			
			else if(description.contains("SKILLS"))
				description = description.substring(description.indexOf("SKILLS"));
			else if(description.contains("ABILITIES"))
				description = description.substring(description.indexOf("ABILITIES"));
			else if(description.contains("QUALIFICATIONS"))
				description = description.substring(description.indexOf("QUALIFICATIONS"));
			
		}
		
		// the bullet point string might have weird capitalization, might want to handle this case
		while(description.indexOf("&lt;li&gt;") != -1 && description.indexOf("&lt;/li&gt;") != -1){
			int bulletStart = description.indexOf("&lt;li&gt;");
			bulletStart = bulletStart + 10;
			int bulletEnd = description.indexOf("&lt;/li&gt;");
			
			if(bulletEnd < bulletStart)
				bulletEnd = bulletStart + description.substring(bulletStart).indexOf("&lt;/li&gt;");
			
			// Only add the point to the list if it is good, add NA for parsing errors
			String point = "NA";
			if(bulletEnd != -1 && bulletEnd > bulletStart)
				point = description.substring(bulletStart, bulletEnd);
			
			if(point.indexOf("&#039;") != -1)
				point = point.substring(0, point.indexOf("&#039;")) + point.substring(point.indexOf("&#039;") + 6);
			if(point.indexOf("&lt;br/&gt;") != -1)
				point = point.substring(0, point.indexOf("&lt;br/&gt;")) + " " + point.substring(point.indexOf("&lt;br/&gt;") + 11);		
			if(point.indexOf("’") != -1)
				point = point.substring(0, point.indexOf("’")) + "'" + point.substring(point.indexOf("’") + 3);		
			if(point.indexOf("&amp;") != -1)
				point = point.substring(0, point.indexOf("&amp;")) + "&" + point.substring(point.indexOf("&amp;") + 5);	
			if(point.indexOf("&lt;strong&gt;") != -1)
				point = point.substring(0, point.indexOf("&lt;strong&gt;")) + point.substring(point.indexOf("&lt;strong&gt;") + 14);	
			if(point.indexOf("&lt;/strong&gt;") != -1)
				point = point.substring(0, point.indexOf("&lt;/strong&gt;")) + point.substring(point.indexOf("&lt;/strong&gt;") + 15);	
			if(point.indexOf("&lt;div&gt;") != -1)
				point = point.substring(0, point.indexOf("&lt;div&gt;")) + point.substring(point.indexOf("&lt;div&gt;") + 11);	
			if(point.indexOf("&lt;/div&gt;") != -1)
				point = point.substring(0, point.indexOf("&lt;/div&gt;")) + point.substring(point.indexOf("&lt;/div&gt;") + 12);			
			
			
			// TODO:
			// call new function to parse sentence into words here, within that function call
			// another function to take out all commas, semicolons, colons, brackets, and parentheses.
			// Then call a third function to add all cleaned words to a map of words as keys and the count as value
			
			bulletPoints.add(point);
			
			description = description.substring(bulletEnd + 11);
		}
		
		
		return bulletPoints;
	}
	
	
}
