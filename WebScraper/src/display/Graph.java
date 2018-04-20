package display;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Graph {

	private JFreeChart chart = null;
	
	// Graph fonts
	private Font titlefont = new Font("Dialog", Font.PLAIN, 40);
	private Font keyfont = new Font("Dialog", Font.PLAIN, 20);
	private Font font = new Font("Dialog", Font.PLAIN, 30);
	private Font tickfont = new Font("Dialog", Font.PLAIN, 20);	
	
	
	public Graph(){
		
	}
	
	public void createGraph(String title, DefaultCategoryDataset dataset, String xaxis, String yaxis){

        
        chart = ChartFactory.createBarChart(
                title, 
                xaxis, yaxis, 
                dataset,PlotOrientation.VERTICAL, 
                true, true, false);
           
        chart.getTitle().setFont(titlefont);
        chart.getLegend().setItemFont(keyfont);
        
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font);
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font);
        
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(tickfont);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(tickfont);
        
	}
	
	
	public void printGraph(String outfile, int height, int width){
        File Chart = new File(outfile); 
        try {
			ChartUtilities.saveChartAsJPEG( Chart, chart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JFreeChart getChart(){
		return chart;
	}
}
