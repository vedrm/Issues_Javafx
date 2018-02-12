package application;


import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;


public class UIPaneBoxGroup extends Group
{
	final Box contentShape;
	UIPane3D displaypane = null;
	
	public UIPaneBoxGroup(final double pWidth, final double pHeight, 
						  final double pDepth, final Color pColor) 
	{	
		contentShape = new Box(pWidth, pHeight, pDepth);
        PhongMaterial shader = new PhongMaterial();
        shader.setDiffuseColor(pColor);
        contentShape.setMaterial(shader);
        getChildren().add(contentShape);
        addInfoUIPane();
	}
	
	
	public double getContentHeight()
	{
		return contentShape.getHeight();
	}
	
	
	public void addInfoUIPane()
	{
		if(displaypane == null)
		{
			displaypane = new UIPane3D();
	        displaypane.setTranslateZ(-contentShape.getDepth()/2.0 - 2.0);
	        getChildren().add(displaypane);
	        updateInfoPanesLayout();			
		}
	}
	
	
	void updateInfoPanesLayout()
	{
		displaypane.resize(contentShape.getWidth(), contentShape.getHeight());
		displaypane.setTranslateX(contentShape.getTranslateX() - displaypane.getWidth()/2.0);
		displaypane.setTranslateY(contentShape.getTranslateY() - displaypane.getHeight()/2.0);
		displaypane.updateContent(true);
	}
	
	public void removeInfoUIPane()
	{
		getChildren().remove(displaypane);
	}
	
	public void addUIDetailText(String pDetailText)
	{
		if(displaypane != null)
		{
			displaypane.updateInfoTextMap(pDetailText, pDetailText);
			displaypane.updateInfoTextMap(pDetailText + "1", pDetailText + "1");
			displaypane.updateInfoTextMap(pDetailText + "2", pDetailText + "3");
			//updateInfoPanesLayout();
		}
	}
	
	public void resizeByHeight(double pNewHeight)
	{
		contentShape.setHeight(pNewHeight);
		updateInfoPanesLayout();
	}
	
	
	public void refreshBomUIPane(Scene3D pSubScene)
	{
		if(displaypane != null)
		{
			Bounds bounds = localToScene(contentShape.getBoundsInParent());
			
			System.out.println( "pane before refreshBomUiPane: " + displaypane.getBoundsInParent() );
			
			final double zPosition = (bounds.getMaxZ() + bounds.getMinZ())/2.0;
			
			final double height = contentShape.getLayoutBounds().getHeight()/pSubScene.getPlaneRatio(zPosition);
			final double aspectRatio = contentShape.getLayoutBounds().getWidth()/contentShape.getLayoutBounds().getHeight();
			final double width = height*aspectRatio;

			final double scaleX = contentShape.getWidth()/width;
			final double scaleY = contentShape.getHeight()/height;

			displaypane.resize(width, height);
			displaypane.updateContent(true);
			
			displaypane.setScaleX(scaleX);
			displaypane.setScaleY(scaleY);
			
			displaypane.setTranslateX(contentShape.getTranslateX() - displaypane.getWidth()/2.0);
			displaypane.setTranslateY(contentShape.getTranslateY() - displaypane.getHeight()/2.0);
			
			System.out.println( "pane after refreshBomUiPane: " + displaypane.getBoundsInParent() );
		}
	}	
}
