package test.zoom;

import java.util.Random;

import javaFX.ext.controls.Instructions;
import javaFX.ext.css.CSS;
import javaFX.ext.css.CSS.SymbolStyle;
import javaFX.ext.utility.Logger;
import javaFX.plots.axis.StableTicksAxis;
import javaFX.plots.overlay.PlotInfo;
import javaFX.plots.overlay.SceneOverlayManager;
import javaFX.plots.overlay.SceneOverlayManager.SceneOption;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import test.FXTester;


public class TestZoom implements FXTester {

	Random random = new Random();
	@Override
	public void execute(Logger logger) {

		XYChart.Series<Number,Number> series1 = FXTester.getSeriesData("series1", 20, 1.0,  0.0,  
				(xx -> xx.doubleValue()+Math.random()*4), 
				(yy -> -6+random.nextGaussian()*4));

		XYChart.Series<Number,Number> series2 = FXTester.getSeriesData("series2", 30, 0.0,  0.0,  
				(xx -> xx.doubleValue()+Math.random()*3.8), 
				(yy -> 6+random.nextGaussian()*6));

//		final NumberAxis xAxis = new NumberAxis();
//		final NumberAxis yAxis = new NumberAxis();
		final StableTicksAxis xAxis = new StableTicksAxis();
		final StableTicksAxis yAxis = new StableTicksAxis();
		xAxis.setLabel("X");
		yAxis.setLabel("Y");

		var lineChart = new LineChart<Number,Number>(xAxis,yAxis);  
		lineChart.getData().add(series1);
		lineChart.getData().add(series2);

		lineChart.setTitle("Random Data");

		
		CSS css = new CSS(lineChart,SymbolStyle.unfilled);
		
		Scene scene = new Scene(lineChart,1200,600);
		
		PlotInfo.setText(scene, "Plot Info - FileName, etc.");
		
		SceneOverlayManager.addOverlays(scene, logger, SceneOption.Legend, SceneOption.EditMenu, SceneOption.ZoomManager);	

		Stage stage = FXTester.displayResults(scene);
		
		Instructions txt = new Instructions(stage.getScene());
		txt.addCenter("Test rewritten Zooming implemenation");
		txt.add("The Zoom controls are as follows:");
		txt.add("Zoom controls in the Chart area:");
		txt.add("-- Drag from upper left to lower right to zoom in");
		txt.add("-- Drag in the opposite direction to return to the original plot");
		txt.add("-- Double clicking on the screen zooms out ~10%.");
		txt.add("-- Double clicking with key pressed (Shift,Ctrl,or Alt) zooms in ~10%.");		
		txt.add("-- The Scroll wheel enables you to zoom in and out. Where the mouse is when scrolling affects the 'direction' you zoom in and out");
		txt.add("Zoom controls in the Axis area:");		
		txt.add("-- Dragging in the axis area selects an area to zoom into");
		txt.add("-- The Scroll Wheel in the axis area also allows you to zoom in and out. Where the mouse is on the axis effects where you will be scrolling in/out");
		txt.add("Zooming integrated with Axis editors");
		txt.display();
	}

}
