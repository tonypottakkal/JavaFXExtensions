package test.plotEditMenu;

import java.util.Random;

import javaFX.ext.controls.Instructions;
import javaFX.ext.css.CSS;
import javaFX.ext.css.CSS.SymbolStyle;
import javaFX.ext.utility.Logger;
import javaFX.plots.NumberPlotData;
import javaFX.plots.overlay.SceneOverlayManager;
import javaFX.plots.overlay.SceneOverlayManager.SceneOption;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import test.FXTester;


public class TestSaveAsPngTitle implements FXTester {

	Random random = new Random();
	@Override
	public void execute(Logger logger) {

		XYChart.Series<Number,Number> series1 = FXTester.getSeriesData("series1", 20, 1.0,  0.0,  
				(xx -> xx.doubleValue()+Math.random()*4), 
				(yy -> -6+random.nextGaussian()*4));

		XYChart.Series<Number,Number> series2 = FXTester.getSeriesData("series2", 30, 0.0,  0.0,  
				(xx -> xx.doubleValue()+Math.random()*3.8), 
				(yy -> 6+random.nextGaussian()*6));

		NumberPlotData plotData = new NumberPlotData();
		plotData.addAll(series1,series2);

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("X");
		yAxis.setLabel("Y");
		final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);                
		lineChart.setTitle("Test Save/Save As using the Title as the File Name (Note special chars `~!@#$% ^&*() _-+={[]}|\'  \":;?/>.<,)");
		lineChart.getData().addAll(plotData.getJavaFXSeries());
		
		CSS css = new CSS(lineChart,SymbolStyle.unfilled);
		
		Scene scene = new Scene(lineChart,1200,600);
		
		SceneOverlayManager.addOverlays(scene, logger, SceneOption.Legend, SceneOption.EditMenu);	

		Stage stage = FXTester.displayResults(scene);
		
		Instructions txt = new Instructions(stage.getScene());
		txt.addCenter("Test 'Save' and 'Save As' contextmenu functions");
		txt.add("Use the Right-click context menu to select 'Save' and 'Save As'");
		txt.add("The saved file name will be derived from the title - which includes a whole bunch of characters that will need to be removed");
		txt.add("Note: I am not trying to control the output directory at this time so the output directory may not match expectations");
		txt.display();
	}
}
