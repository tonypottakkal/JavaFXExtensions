package test.callouts;

import java.util.Random;

import javaFX.ext.controls.Instructions;
import javaFX.ext.css.CSS;
import javaFX.ext.css.CSS.SymbolStyle;
import javaFX.ext.utility.ListIterator;
import javaFX.ext.utility.Logger;
import javaFX.ext.utility.MyColors;
import javaFX.plots.NumberPlotData;
import javaFX.plots.PlotData;
import javaFX.plots.axis.StableTicksAxis;
import javaFX.plots.callouts.CallOut;
import javaFX.plots.callouts.CallOutSettings;
import javaFX.plots.overlay.SceneOverlayManager;
import javaFX.plots.overlay.SceneOverlayManager.SceneOption;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import test.FXTester;


public class TestCallOutSeriesEditor implements FXTester {

	Random random = new Random();
	@Override
	public void execute(Logger logger) {

		final int RANGE = CallOutSettings.Angle.length;
		
		ListIterator<Double> angleList = new ListIterator<Double>(CallOutSettings.Angle);
		
		ListIterator<Double> lengthList = new ListIterator<Double>(new Double[] {6.0, 8.0, 10.0, 14.0, 20.0, 30.0, 40.0, 60.0, 80.0, 100.0});
		ListIterator<Double> widthList = new ListIterator<Double>(new Double[] {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0});
		ListIterator<Color> colorList = new ListIterator<Color>(MyColors.plotColors);
		
		PlotData<Number,String> plotData = new PlotData<Number,String>();
		
		FXTester.setPlotData(plotData, "Line Angles1", angleList.size()/4, 1.0,  "Angles 000 - 090",  
				(xx -> xx.doubleValue()+RANGE/(angleList.size()/4)), 
				(yy -> "Angles 000 - 090"));
		
		FXTester.setPlotData(plotData, "Line Angles2", angleList.size()/4, 1.0,  "Angles 090 - 180",  
				(xx -> xx.doubleValue()+RANGE/(angleList.size()/4)), 
				(yy -> "Angles 090 - 180"));
		
		FXTester.setPlotData(plotData, "Line Angles3", angleList.size()/4, 1.0,  "Angles 180 - 270",  
				(xx -> xx.doubleValue()+RANGE/(angleList.size()/4)), 
				(yy -> "Angles 180 - 270"));
		
		FXTester.setPlotData(plotData, "Line Angles4", angleList.size()/4, 1.0,  "Angles 270 - 360",  
				(xx -> xx.doubleValue()+RANGE/(angleList.size()/4)), 
				(yy -> "Angles 270 - 360"));

		
		FXTester.setPlotData(plotData, "Line Color", RANGE/5, 1.0,  "Line Color",  
				(xx -> xx.doubleValue()+5), 
				(yy -> "Line Color"));

		FXTester.setPlotData(plotData, "Line Length", lengthList.size(), 1.0,  "Line Length",  
				(xx -> xx.doubleValue()+RANGE/lengthList.size()), 
				(yy -> "Line Length"));
		
		FXTester.setPlotData(plotData, "Line Width", widthList.size(), 1.0,  "Line Width",  
				(xx -> xx.doubleValue()+RANGE/widthList.size()), 
				(yy -> "Line Width"));

		var callOutAngles = new CallOut("Line Angles",plotData);
		{
			var cos = callOutAngles.copyDefaultSettings();
			cos.setTextRotated(false);
			for (var data : plotData.getSeriesData("Line Angles1")) {
				cos.setAngle(angleList.getNext());
				callOutAngles.create(data.x, data.y,angleList.repeat().toString(),cos);			
			}
			for (var data : plotData.getSeriesData("Line Angles2")) {
				cos.setAngle(angleList.getNext());
				callOutAngles.create(data.x, data.y,angleList.repeat().toString(),cos);			
			}
			for (var data : plotData.getSeriesData("Line Angles3")) {
				cos.setAngle(angleList.getNext());
				callOutAngles.create(data.x, data.y,angleList.repeat().toString(),cos);			
			}
			for (var data : plotData.getSeriesData("Line Angles4")) {
				cos.setAngle(angleList.getNext());
				callOutAngles.create(data.x, data.y,angleList.repeat().toString(),cos);			
			}
		}
		var callOutColor = new CallOut("Line and Text Colors",plotData);
		{
			callOutColor.defaultCallOutSettings.setAngle(345.0);
			var cos = callOutColor.copyDefaultSettings();
			for (var data : plotData.getSeriesData("Line Color")) {
				cos.setColor(colorList.getNext());
//				cos.setFontColor(colorList.repeat());
				callOutColor.create(data.x, data.y,"\u25a0 colored",cos);			
			}
		}
		var callOutLength = new CallOut("Line Lengths",plotData);
		{
			var cos = callOutLength.copyDefaultSettings();
			for (var data : plotData.getSeriesData("Line Length")) {
				cos.setLineLength(lengthList.getNext());
				cos.setAngle(345.0);
				cos.setTextRotated(false);
				callOutLength.create(data.x, data.y,"length "+lengthList.repeat().toString(),cos);			
			}
		}
		
		var callOutWidth = new CallOut("Line Widths",plotData);
		{
			var cos = callOutWidth.copyDefaultSettings();
			for (var data : plotData.getSeriesData("Line Width")) {
				cos.setLineWidth(widthList.getNext());
				cos.setAngle(345.0);
				cos.setTextRotated(false);
				callOutWidth.create(data.x, data.y,"width "+widthList.repeat().toString(),cos);			
			}
		}

		
		final StableTicksAxis xAxis = new StableTicksAxis();
		final StableTicksAxis yAxis = new StableTicksAxis();
		xAxis.setLabel("X");
		yAxis.setLabel("Y");
		final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);              
		lineChart.setTitle("CallOut Line Properties");

		plotData.setYAxisComparator(PlotData.reverseSort);
		lineChart.getData().addAll(plotData.getJavaFXSeries());
		yAxis.setAxisTickFormatter(plotData.getYAxisTickFormatter());
		
		CSS css = new CSS(lineChart,SymbolStyle.unfilled);
		
		Scene scene = new Scene(lineChart,1200,600);
		
		SceneOverlayManager.addOverlays(scene, logger, SceneOption.All);	

		CallOut.configure(scene,callOutAngles,callOutColor,callOutLength,callOutWidth);

		Stage stage = FXTester.displayResults(scene);

		Instructions txt = new Instructions(stage.getScene());
		txt.addCenter("Tests CallOut Series Editor");
		txt.add("Use the Context Menu to select one or more Call Out Editors");
		txt.add("-- Rotation Angle, Color, Length, and Width");
		txt.add("Only the CallOuts associated with the named editor you select will be changed");
		txt.display();

	}
}
