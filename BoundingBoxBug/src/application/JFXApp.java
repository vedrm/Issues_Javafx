package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXApp extends Application
{
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{        
		Scene scene = new Scene(new Group());
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavafxBoundingBoxIssue");
		primaryStage.show();
		
		Scene3D.runWithScene(scene);
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
