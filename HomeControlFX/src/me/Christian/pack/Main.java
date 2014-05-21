package me.Christian.pack;


import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) {
        primaryStage.setTitle("Homecontrol");
               
        Pane root = new Pane();
        Image img = new Image("438120.jpg");
       // Image img = new Image("loading.gif");
        ImageView imgView = new ImageView(img);
        root.getChildren().add(imgView);
        
     
        ImageView Light1_Button = new ImageView(new Image("B12.png"));
        Light1_Button.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
        Light1_Button.getOnMousePressed();
        Light1_Button.setLayoutX(10);
        Light1_Button.setLayoutY(70);
        root.getChildren().add(Light1_Button);
        primaryStage.setScene(new Scene(root, 1024, 600));
       
        Canvas canvas = new Canvas(1024, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawme(gc);
        root.getChildren().add(canvas);
        
        //Adding HBox to the scene
       // Scene scene = new Scene(sp);
        //primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private void drawme(GraphicsContext gc) {
		gc.fillText("Licht 1", 20, 20);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	class MyEventHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent e) {
			System.out.println("hi");
			
		}
	
	}

}

