package application;


import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

public class Scene3D extends SubScene
{
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    
    private final Group root3D;
    private final Xform cameraXform = new Xform();
    private final Xform cameraTranslateXForm = new Xform();
    private final Translate cameraPosition = new Translate(0,0,0);    
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    
	//private Timeline scrollEndTimer = null;
    
	Table3D table = null;
	
	Box boundingBox = null;
	
	private Scene3D(Group pParentGroup)
	{
		super(pParentGroup, 400.0, 400.0, true, SceneAntialiasing.BALANCED);
		root3D = pParentGroup;
		
		setupViewScene();
		setupEventHandlers();
		setup3DContent();
	}
	
	public static void runWithScene(Scene pParentScene)
	{
		Scene3D tessScene = new Scene3D(new Group());
		
		tessScene.widthProperty().bind(pParentScene.widthProperty());
		tessScene.heightProperty().bind(pParentScene.heightProperty());
		
		((Group) pParentScene.getRoot()).getChildren().add(tessScene);
	}
	
	private void setupViewScene()
	{      
        setCamera(camera);
        setFill(Color.ALICEBLUE);
        
        camera.setNearClip(1.0);
        camera.setFarClip(10000.0);
        camera.getTransforms().addAll(cameraPosition);

        cameraPosition.setZ(-1300.0);
        cameraXform.getChildren().add(cameraTranslateXForm);
        cameraTranslateXForm.getChildren().add(camera);

        root3D.getChildren().add(cameraXform);
	}
	
	
	private void setupEventHandlers()
	{
		setOnMousePressed(event->{            
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
		});
		
		setOnMouseDragged(event->{
			onMouseDragEventOccured(event);
		});
		
		setOnScroll(event->{
			//scrollEndTimer.playFromStart();
			onMouseScrollEventOccured(event);
		});
		
		/*scrollEndTimer = new Timeline(new KeyFrame(Duration.millis(250.0), timerEvent->
		{	
			scrollEndTimer.stop();
			onMouseScrollEventFinished();
		}));*/
	}
	
	void onMouseScrollEventFinished()
	{
		refreshBoundingBox();
		table.onFinishedUpdatingCameraPosition(this);
		refreshBoundingBox();
	}
	
	private void onMouseDragEventOccured(MouseEvent pEvent)
	{
        double modifier = 1.0;
        double modifierFactor = 0.3;

        if (pEvent.isControlDown()) {
            modifier = 0.1;
        }
        if (pEvent.isShiftDown()) {
            modifier = 10.0;
        }

        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        mousePosX = pEvent.getSceneX();
        mousePosY = pEvent.getSceneY();
        mouseDeltaX = (mousePosX - mouseOldX); 
        mouseDeltaY = (mousePosY - mouseOldY); 

        if (pEvent.isMiddleButtonDown())
        {
        	final double planeRatio = getCurrentViewPortHeight()/getHeight();
        	final double deltaX = mouseDeltaX*planeRatio;
        	final double deltaY = mouseDeltaY*planeRatio;
        	
        	cameraTranslateXForm.t.setX(cameraTranslateXForm.t.getX() - deltaX);
        	cameraTranslateXForm.t.setY(cameraTranslateXForm.t.getY() - deltaY);
        }
        else if (pEvent.isPrimaryButtonDown()) 
        {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle() + mouseDeltaX*modifierFactor*modifier*2.0);  
            cameraXform.rx.setAngle(cameraXform.rx.getAngle() - mouseDeltaY*modifierFactor*modifier*2.0);
        }		
	}
	
	private void onMouseScrollEventOccured(ScrollEvent pScrollEvent)
	{
		final double deltaZ = pScrollEvent.getDeltaY()*0.2;
        double newPosZ = cameraPosition.getZ()-deltaZ;
        newPosZ = Math.max(newPosZ,-10000);
        newPosZ = Math.min(newPosZ,0);        
        cameraPosition.setZ(newPosZ);
        
        onMouseScrollEventFinished();
	}
	
	
	public void setup3DContent()
	{
		setupAxis();
		
		table = new Table3D();
		root3D.getChildren().add(table);

		final Bounds bounds = table.getLayoutBounds();
		final double anchorPosX = bounds.getMinX() + bounds.getWidth() / 2.0;
		final double anchorPosY = bounds.getMinY() + bounds.getHeight() / 2.0;

		table.setTranslateX(-anchorPosX);
		table.setTranslateY(-anchorPosY);

		refreshBoundingBox();
	}
	
	
	public void refreshBoundingBox()
	{
		if(boundingBox != null)
		{
			root3D.getChildren().remove(boundingBox);
		}
		
		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.web(Color.CRIMSON.toString(), 0.25));
		
		Bounds tableBounds = table.getBoundsInParent();
		boundingBox = new Box(tableBounds.getWidth(), tableBounds.getHeight(), tableBounds.getDepth());
		boundingBox.setMaterial(blueMaterial);
		boundingBox.setTranslateX(tableBounds.getMinX() + tableBounds.getWidth()/2.0);
		boundingBox.setTranslateY(tableBounds.getMinY() + tableBounds.getHeight()/2.0);
		boundingBox.setTranslateZ(tableBounds.getMinZ() + tableBounds.getDepth()/2.0);
		boundingBox.setMouseTransparent(true);
		
		root3D.getChildren().add(boundingBox);
	}
	
	
	public double getCurrentViewPortHeight()
	{
		double tanTheta = Math.tan(Math.toRadians(camera.getFieldOfView()/2.0));
		return 2*Math.abs(cameraPosition.getZ())*tanTheta;
	}
	
	public double convertToScreenHeight(double pHeightOnViewPort)
	{
		final double planeRatio = getHeight()/getCurrentViewPortHeight();
		return pHeightOnViewPort*planeRatio;
	}
	
	public double convertToViewPortHeight(double pScreenHeight)
	{
		final double planeRatio = getCurrentViewPortHeight()/getHeight();
		return pScreenHeight*planeRatio;		
	}
	
	public double getPlaneRatio(double pZPosition) 
	{
		final double tanTheta = Math.tan(Math.toRadians(camera.getFieldOfView()) * 0.5f);
		final double planeRatio = 2 * Math.abs(cameraPosition.getZ() - pZPosition) * tanTheta/getLayoutBounds().getHeight();
		return planeRatio;
	}
	
	private void setupAxis()
	{
		Box xAxis = new Box(1000, 1.0, 1.0);
		root3D.getChildren().add(xAxis);
		
		Box yAxis = new Box(1.0, 1000.0, 1.0);
		root3D.getChildren().add(yAxis);

		Box zAxis = new Box( 1d,1d,3000d );
		root3D.getChildren().add(zAxis);
		
        PhongMaterial shader = new PhongMaterial();
        shader.setDiffuseColor(Color.web(Color.DARKBLUE.toString(), 0.75));
        xAxis.setMaterial(shader);	
		yAxis.setMaterial(shader);
		zAxis.setMaterial(shader);
	}
}
