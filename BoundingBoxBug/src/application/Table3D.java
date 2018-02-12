package application;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Table3D extends Group {

	final ArrayList<UIPaneBoxGroup> boxPanes = new ArrayList<>();
	
	public Table3D() {
		addTableContent();
	}
	
	public void addTableContent()
	{
		double minY = 0.0;
		final double cellHeight = 15.0;
		final double cellWidth = 50.0;
		final double cellDepth = 15.0;
		
		for (int row = 0; row < 5; row++)
		{
			double minX = 0.0;
			
			for (int attIndex = 0; attIndex < 5; attIndex++) 
			{
				UIPaneBoxGroup cell = new UIPaneBoxGroup(cellWidth, cellHeight, cellDepth, Color.BURLYWOOD);
				cell.setTranslateX(minX);
				cell.setTranslateY(minY);
				//cell.setTranslateZ(cellDepth/2.0);
				cell.addUIDetailText("("+row+","+attIndex+")");
				
				boxPanes.add(cell);
				getChildren().addAll(cell);
				
				minX += (cellWidth + 1.0);
			}			
			minY +=(cellHeight + 1.0);
		}
	}	
	
	
	public void onFinishedUpdatingCameraPosition(Scene3D pParentScene)
	{
		for (UIPaneBoxGroup uiPaneBoxGroup : boxPanes) {
			uiPaneBoxGroup.refreshBomUIPane(pParentScene);
		}
	}
}
