package display;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class State {
	
	private String name;
	
	private int jobCount;
	private int salCount; // accounting for some posts that do not have salary info
	
	private int totalMinSal;
	private int totalAvgSal;
	private int totalMaxSal;
	
	private ArrayList<String> titles;
	private ArrayList<String> companies;
	private ArrayList<String> cities;
	
	
	public State(){
		name = "none";
		
		jobCount = 0;
		salCount = 0;
		
		totalMinSal = 0;
		totalAvgSal = 0;
		totalMaxSal = 0;
		
		titles = new ArrayList<String>();
		companies = new ArrayList<String>();
		cities = new ArrayList<String>();
	}
	
	public State(String n){
		name = n;
		
		jobCount = 0;
		salCount = 0;
		
		totalMinSal = 0;
		totalAvgSal = 0;
		totalMaxSal = 0;
		
		titles = new ArrayList<String>();
		companies = new ArrayList<String>();
		cities = new ArrayList<String>();
	}	
	
	
	public void addPost(String title, String comp, String city, int minsal, int avgsal, int maxsal){
		jobCount ++;
		
		if(minsal != -1)
			salCount ++;
		
		titles.add(title);
		companies.add(comp);
		cities.add(city);
		
		totalMinSal += minsal;
		totalAvgSal += avgsal;
		totalMaxSal += maxsal;
		
	}
	
	

	public ArrayList<String> getTitles(){
		return titles;
	}
	public ArrayList<String> getCompanies(){
		return companies;
	}
	public ArrayList<String> getCities(){
		return cities;
	}
	
	
	
	public int getTotalMinSal(){
		return totalMinSal;
	}
	public int getTotalAvgSal(){
		return totalAvgSal;
	}
	public int getTotalMaxSal(){
		return totalMaxSal;
	}
	
	
	
	public int getAvgMinSal(){
		if(salCount == 0)
			salCount = 1;
		return totalMinSal/salCount;
	}
	public int getAvgAvgSal(){
		if(salCount == 0)
			salCount = 1;
		return totalAvgSal/salCount;
	}
	public int getAvgMaxSal(){
		if(salCount == 0)
			salCount = 1;
		return totalMaxSal/salCount;
	}
	
	
	public int numOfCities(){
		Set<String> uniqueCities = new HashSet<String>(cities);
		return uniqueCities.size();
	}
	public String getName(){
		return name;
	}
	public int getJobCount(){
		return jobCount;
	}
	public int getSalCount(){
		return salCount;
	}
}





