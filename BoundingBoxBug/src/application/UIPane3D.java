package application;

import java.util.ArrayList;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class UIPane3D extends Pane
{
	final VBox textPane = new VBox(2.0);
	
	final ArrayList<String> infoTextKeys = new ArrayList<>();
	final ArrayList<Text> infoTextValues = new ArrayList<>();
	
	Rectangle bgCanvasRect = null;
	
	final double fontSize = 16.0;	
	
	public UIPane3D() {
		setMouseTransparent(true);
		setBackground( new Background( new BackgroundFill( Color.AQUA, null, null)) );
	}
	
	public void updateContent(boolean pEnableWrapping) {
		textPane.getChildren().clear();
		getChildren().clear();
		
		for (Text textNode : infoTextValues) {
			
			textPane.getChildren().add( textNode );
			textPane.autosize();
			if (textPane.getHeight() > getHeight()) {
				textPane.getChildren().remove(textNode);
				textPane.autosize();
				
				break;
			}
		
		}
		
		textPane.setTranslateY(getHeight() / 2 - textPane.getHeight() / 2.0);
		
		bgCanvasRect = new Rectangle(getWidth(), getHeight());
		bgCanvasRect.setFill(Color.web(Color.BURLYWOOD.toString(), 0.10));
		bgCanvasRect.setVisible(true);

		getChildren().addAll(bgCanvasRect, textPane);
		
		System.out.println( "UIPane3D, bounds in parent: " + getBoundsInParent() );
	}
	
	
	public void resetInfoTextMap()
	{
		if (infoTextKeys != null || infoTextValues != null) 
		{
			try 
			{
				infoTextKeys.clear();
				infoTextValues.clear();		
			} catch (Exception e){e.printStackTrace();}
		}
	}
	
	
	public void updateInfoTextMap(String pKey, String pValue)
	{
		int index = -1;
		boolean objectFound = false;
		
		for (String string : infoTextKeys) 
		{
			index++;
			if(string.equals(pKey))
			{
				objectFound = true;
				break;
			}
		}
		
		if(objectFound)
		{
			infoTextValues.get(index).setText(pValue.toUpperCase());
		}
		else
		{
			if (pValue != null) 
			{	
				Text textNode = new Text(pValue.toUpperCase());
				textNode.setFont(Font.font("Consolas", FontWeight.BLACK, FontPosture.REGULAR, 10));
				textNode.wrappingWidthProperty().bind(widthProperty());
				textNode.setTextAlignment(TextAlignment.CENTER);
				infoTextKeys.add(pKey);
				infoTextValues.add(textNode);
			}
		}
	}
}
