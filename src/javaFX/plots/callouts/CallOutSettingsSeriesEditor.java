package javaFX.plots.callouts;

import java.util.HashMap;
import java.util.Map;

import javaFX.ext.controls.Editor;
import javaFX.ext.css.CSS;
import javaFX.ext.css.CSS.FontStyle;
import javaFX.ext.css.CSS.FontWeight;
import javaFX.ext.utility.FXUtil;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CallOutSettingsSeriesEditor  {

		static Map<CallOut,Editor> map2Editor = new HashMap<CallOut,Editor>();
		// This routine sets up the Editable window
		public static void open(Scene scene, CallOut callOut, CSS css, double screenX, double screenY) {
			Editor editor = map2Editor.get(callOut);
			if (editor != null) {
				System.out.println("Editor already open");
			}
			else {
				editor = new Editor(screenX, screenY, scene);
				editor.show("CallOut Settings", getGridPaneForEditor(scene, callOut), () -> {map2Editor.remove(callOut); return true;});
			}
		}
		
		// Editor for the settings

		ChoiceBox<Double> lineLengthChoiceBox = null;
		ChangeListener<? super Double> lineLengthListener = null; 
		ChoiceBox<Double> angleChoiceBox = null;
		ChangeListener<? super Double> angleListener = null; 
		
		
		static final double MAX_CHOICEBOX_SIZE = 100.0;  // set a universal max size for the Choice Boxes that are created below 

		private static GridPane getGridPaneForEditor(Scene scene, CallOut callOut) {

			// set up GridPane for Editor labels and entries, set up spacing between entries and between other elements in the editor
			GridPane gridPane = new GridPane();
			gridPane.setVgap(6);
			gridPane.setHgap(2);
			CSS.setBorderWidth(gridPane, 0,10,10,10);
			CSS.setBorderColor(gridPane, Color.TRANSPARENT);		// needed or the border will have nno size despite setting it below
						
			
			int row = 1;

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// Text rotated 
			////////////////////////////////////////////////////////////////////////////////////////////////////

			{
				Text text = new Text("Changes effect '"+callOut.getName()+"' CallOuts");
				CSS.setFontWeight(text, FontWeight.bold);
				gridPane.add(text, 1, row++, 3, 1); // col, row
			}
			
			addSeparator(gridPane, row++);
			
			{
				RadioButton textRotatedButton = new RadioButton("Text Rotated");
				textRotatedButton.setSelected(callOut.defaultCallOutSettings.getTextRotated());
				textRotatedButton.setMaxWidth(MAX_CHOICEBOX_SIZE*2);
				textRotatedButton.setMinSize(FXUtil.getWidth(textRotatedButton)+30, FXUtil.getHeight(textRotatedButton));
				gridPane.add(textRotatedButton, 1, row++, 3, 1); // col, row
				textRotatedButton.setOnAction((ActionEvent event) -> { 
					callOut.defaultCallOutSettings.setTextRotated(textRotatedButton.isSelected());
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setTextRotated(textRotatedButton.isSelected()));
					resetLineAndText(callOut);
					resetCallOutLocation(callOut);
				});
			}

			addSeparator(gridPane, row++);

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// angle
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				gridPane.add(new Text("Angle"), 1, row); // col, row
				ComboBox<Double> angleChoiceBox = Editor.getDoubleComboBox(CallOutSettings.Angle, callOut.defaultCallOutSettings.getAngle());
				angleChoiceBox.setMaxSize(MAX_CHOICEBOX_SIZE, Double.MAX_VALUE);

				ChangeListener<? super Double> angleListener = (observable, oldValue, newValue) -> {
					callOut.defaultCallOutSettings.setAngle(newValue);
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setAngle(newValue));
					resetText(callOut);
					resetLineAndText(callOut);
					resetCallOutLocation(callOut);
				}; 
				angleChoiceBox.getSelectionModel().selectedItemProperty().addListener(angleListener);
				gridPane.add(angleChoiceBox,3,row++);
			}

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// lineLength
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				gridPane.add(new Text("Line Length"), 1, row); // col, row
				ComboBox<Double> lineLengthComboBox = Editor.getDoubleComboBox(CallOutSettings.LineLength, callOut.defaultCallOutSettings.getLineLength());
				lineLengthComboBox.setMaxSize(MAX_CHOICEBOX_SIZE, Double.MAX_VALUE);
				ChangeListener<? super Double> lineLengthListener = (observable, oldValue, newValue) -> {
					callOut.defaultCallOutSettings.setLineLength((Double)newValue);
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setLineLength((Double)newValue));
					resetLineAndText(callOut);
					resetCallOutLocation(callOut);
				};
				lineLengthComboBox.getSelectionModel().selectedItemProperty().addListener(lineLengthListener);
				gridPane.add(lineLengthComboBox,3,row++);
			}	

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// lineWidth
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				gridPane.add(new Text("Line Width"), 1, row); // col, row
				ComboBox<Double> choicelineWidthComboBox = Editor.getDoubleComboBox(CallOutSettings.LineWidth, callOut.defaultCallOutSettings.getLineWidth());
				choicelineWidthComboBox.setMaxSize(MAX_CHOICEBOX_SIZE, Double.MAX_VALUE);
				choicelineWidthComboBox.getSelectionModel().selectedItemProperty().addListener(
						(observable, oldValue, newValue) -> {
							callOut.defaultCallOutSettings.setLineWidth((Double)newValue);
							callOut.getData().stream().forEach(data -> ((CSS) callOut.mapData2CallOutSettings.get(data)).setLineWidth((Double)newValue));
							resetLineAndText(callOut);
						});
				gridPane.add(choicelineWidthComboBox,3,row++);
			}	

			addSeparator(gridPane, row++);

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// Color
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				gridPane.add(new Text("Color"), 1, row); // col, row
				ColorPicker colorPicker = Editor.getColorPicker(callOut.defaultCallOutSettings.getColor());
				colorPicker.setMaxSize(MAX_CHOICEBOX_SIZE, Double.MAX_VALUE);
				colorPicker.setOnAction(event -> {
					callOut.defaultCallOutSettings.setColor(colorPicker.getValue());
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setColor(colorPicker.getValue()));
					resetText(callOut);
					resetLineAndText(callOut);
				});
				gridPane.add(colorPicker,3,row++);
			}			

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// fontSize
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				gridPane.add(new Text("Font Size"), 1, row); // col, row
				ComboBox<Double> fontSizeComboBox = Editor.getDoubleComboBox(CallOutSettings.FontSize, callOut.defaultCallOutSettings.getFontSize());
				fontSizeComboBox.setMaxSize(MAX_CHOICEBOX_SIZE, Double.MAX_VALUE);
				fontSizeComboBox.getSelectionModel().selectedItemProperty().addListener(
						(observable, oldValue, newValue) -> {
							callOut.defaultCallOutSettings.setFontSize((Double)newValue);
							callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setFontSize((Double)newValue));
							resetText(callOut);
							resetCallOutLocation(callOut);
						});
				gridPane.add(fontSizeComboBox,3,row++);
			}			


			////////////////////////////////////////////////////////////////////////////////////////////////////
			// fontStyle (Italics)
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				RadioButton italicsButton = new RadioButton("Italic           ");
				italicsButton.setSelected(callOut.defaultCallOutSettings.getFontStyle().equals(FontStyle.italic));
				italicsButton.setMaxWidth(MAX_CHOICEBOX_SIZE*2);
				italicsButton.setMinSize(FXUtil.getWidth(italicsButton)+30, FXUtil.getHeight(italicsButton));
				italicsButton.setOnAction((ActionEvent event) -> { 
					FontStyle tempFontStyle = FontStyle.normal;
					if (italicsButton.isSelected()) { tempFontStyle = FontStyle.italic; }
					final FontStyle fontStyle = tempFontStyle;
					callOut.defaultCallOutSettings.setFontStyle(fontStyle);
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setFontStyle(fontStyle));
					resetText(callOut);
				});
				gridPane.add(italicsButton,1,row);
			}

			////////////////////////////////////////////////////////////////////////////////////////////////////
			// fontWeight (Bold)
			////////////////////////////////////////////////////////////////////////////////////////////////////
			{
				RadioButton boldButton = new RadioButton("Bold           ");
				boldButton.setSelected(callOut.defaultCallOutSettings.getFontWeight().equals(FontWeight.bold));
				boldButton.setMaxWidth(MAX_CHOICEBOX_SIZE*2);
				boldButton.setMinSize(FXUtil.getWidth(boldButton)+30, FXUtil.getHeight(boldButton));
				boldButton.setOnAction((ActionEvent event) -> { 
					FontWeight tempFontWeight = FontWeight.normal;
					if (boldButton.isSelected()) { tempFontWeight = FontWeight.bold; }
					final FontWeight fontweight = tempFontWeight;
					callOut.defaultCallOutSettings.setFontWeight(fontweight);
					callOut.getData().stream().forEach(data -> ((CallOutSettings) callOut.mapData2CallOutSettings.get(data)).setFontWeight(fontweight));
					resetText(callOut);
				});
				gridPane.add(boldButton,3,row++);
			}
			
			addSeparator(gridPane, row++);

			return gridPane;
		}
		
		private static void resetText(CallOut callOut) {
			callOut.getData().stream().forEach(data -> callOut.setCalloutTextProperties(CallOut.getText((Data<Object, Object>) data),(CallOutSettings) callOut.mapData2CallOutSettings.get(data)));
		}
		
		private static void resetLineAndText(CallOut callOut) {
			callOut.getData().stream().forEach(data -> callOut.setCallOutLineAndPositioningProperties(CallOut.getText((Data<Object, Object>) data),(CallOutSettings) callOut.mapData2CallOutSettings.get(data)));				
		}

		private static void resetCallOutLocation(CallOut callOut) {
			callOut.getData().stream().forEach(data -> callOut.setCallOutDataLocation((Group)((Data<Object, Object>) data).getNode(),(CallOutSettings) callOut.mapData2CallOutSettings.get(data)));
			callOut.callOutSeries.getNode().getParent().getParent().getParent().requestLayout();   // needed because it won't relayout otherwise
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		// The following method sets the editor values when the CallOut has been moved by the Mouse
		////////////////////////////////////////////////////////////////////////////////////////////////////

		public void setEditorData() {
//			if (editor != null) {
//				xTextField.setText(getData().getXValue().toString());
//				yTextField.setText(getData().getYValue().toString());
//
//				lineLengthChoiceBox.getSelectionModel().selectedItemProperty().removeListener(lineLengthListener);
//				lineLengthChoiceBox.setValue(getLineLength());
//				lineLengthChoiceBox.getSelectionModel().selectedItemProperty().addListener(lineLengthListener);
//
//				angleChoiceBox.getSelectionModel().selectedItemProperty().removeListener(angleListener);
//				angleChoiceBox.setValue(getAngle());
//				angleChoiceBox.getSelectionModel().selectedItemProperty().addListener(angleListener);
//
//			}
		}
		
		private static void addSeparator(GridPane gridPane, int row) {
			Separator separator = new Separator(Orientation.HORIZONTAL);
			gridPane.add(separator, 1, row++, GridPane.REMAINING, 1);
			GridPane.setValignment(separator, VPos.CENTER);
		}
}
