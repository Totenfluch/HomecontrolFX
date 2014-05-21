package me.Christian.pack;





import me.Christian.threads.Thread_GetWeather;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	public static ImageView Light1_Button1;
	public static ImageView Light1_Button2;
	public static Text Light1_Text;
	public static ImageView Light1_Lock;
	public static ImageView Light1_Lockcross;
	public static ImageView Light1_State1, Light1_State2, Light1_State3;
	public static final String City = "Schweinfurt";
	
	public static void main(String[] args) {
		Thread_GetWeather.StartCheck(City);
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Homecontrol");

		primaryStage.setResizable(false);

		Pane root = new Pane();

		ImageView imgView = new ImageView(new Image("438120.jpg"));
		imgView.setFitWidth(1100);
		imgView.setFitHeight(625);
		root.getChildren().add(imgView);

		Light1_Button1 = new ImageView(new Image("B12.png"));
		Light1_Button1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light1_Button1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Button1.getOnMousePressed();
		Light1_Button1.getOnMouseReleased();
		Light1_Button1.setLayoutX(60);
		Light1_Button1.setLayoutY(125);
		root.getChildren().add(Light1_Button1);

		Light1_Button2 = new ImageView(new Image("B3.png"));
		Light1_Button2.setLayoutX(60);
		Light1_Button2.setLayoutY(125);
		Light1_Button2.setVisible(false);
		root.getChildren().add(Light1_Button2);
		
		Light1_Text = new Text();
		Light1_Text.setText("Licht I");
		Light1_Text.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light1_Text.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Text.getOnMousePressed();
		Light1_Text.getOnMouseReleased();
		Light1_Text.getOnMousePressed();
		Light1_Text.setFont(Font.font(java.awt.Font.SERIF, 18));
		Light1_Text.setLayoutX(80);
		Light1_Text.setLayoutY(150);
		root.getChildren().add(Light1_Text);
		
		Light1_Lock = new ImageView(new Image("filledquad.png"));
		Light1_Lock.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Lock.getOnMouseReleased();
		Light1_Lock.setLayoutX(210);
		Light1_Lock.setLayoutY(135);
		Light1_Lock.setFitHeight(40);
		Light1_Lock.setFitWidth(40);
		root.getChildren().add(Light1_Lock);
		
		Light1_Lockcross = new ImageView(new Image("redcross.png"));
		Light1_Lockcross.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Lockcross.getOnMouseReleased();
		Light1_Lockcross.setLayoutX(215);
		Light1_Lockcross.setLayoutY(140);
		Light1_Lockcross.setFitHeight(30);
		Light1_Lockcross.setFitWidth(30);
		Light1_Lockcross.setVisible(false);
		root.getChildren().add(Light1_Lockcross);
		
		Light1_State1 = new ImageView(new Image("tealorb.png"));
		Light1_State1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_State1.getOnMouseReleased();
		Light1_State1.setLayoutX(10);
		Light1_State1.setLayoutY(125);
		Light1_State1.setFitHeight(35);
		Light1_State1.setFitWidth(35);
		root.getChildren().add(Light1_State1);
		
		Light1_State2 = new ImageView(new Image("greenorb.png"));
		Light1_State2.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_State2.getOnMouseReleased();
		Light1_State2.setLayoutX(10);
		Light1_State2.setLayoutY(125);
		Light1_State2.setFitHeight(35);
		Light1_State2.setFitWidth(35);
		Light1_State2.setVisible(false);
		root.getChildren().add(Light1_State2);
		
		Light1_State3 = new ImageView(new Image("redorb.png"));
		Light1_State3.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_State3.getOnMouseReleased();
		Light1_State3.setLayoutX(10);
		Light1_State3.setLayoutY(125);
		Light1_State3.setFitHeight(35);
		Light1_State3.setFitWidth(35);
		Light1_State3.setVisible(false);
		root.getChildren().add(Light1_State3);
		
		/*
		 * 	g.drawImage(ResourceLoader.ImageLoad("/tealorb.png"), 10, 125, 35, 35, null);
			g.drawImage(ResourceLoader.ImageLoad("/B1.png"), 60, 125, null);
			g.drawImage(ResourceLoader.ImageLoad("/B2.png"), 60, 125, null);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Kanji", Font.BOLD, 18));
			g.drawString("Licht I", 80, 150);
			g.drawImage(ResourceLoader.ImageLoad("/emptyquad.png"), 210, 135, 40, 40, null);
			
			g.drawImage(ResourceLoader.ImageLoad("/redcross.png"), 215, 140, 30, 30, null);
		 */

		primaryStage.setScene(new Scene(root, 1024, 600));
		primaryStage.show();
	}
	
	public static void SetState(ImageView img1, ImageView img2, ImageView img3, int state){
		if(state == 0){
			img1.setVisible(true);
			img2.setVisible(false);
			img3.setVisible(false);
		}else if(state == 1){
			img1.setVisible(false);
			img2.setVisible(true);
			img3.setVisible(false);
		}else if(state == 2){
			img1.setVisible(false);
			img2.setVisible(false);
			img3.setVisible(true);
		}else{
			System.out.println("SetStates Error - Out of bounds");
		}
	}
	
	
	class MyEventHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent e) {
			if(e.getSource() == Light1_Button1 || e.getSource() == Light1_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light_Button1");
					Light1_Button2.setVisible(false);
					Light1_Button1.setVisible(true);
					Light1_Text.setLayoutX(Light1_Text.getLayoutX()-12);
					Light1_Text.setLayoutY(Light1_Text.getLayoutY()-10);
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light_Button1");
					Light1_Button1.setVisible(false);
					Light1_Button2.setVisible(true);
					Light1_Text.setLayoutX(Light1_Text.getLayoutX()+12);
					Light1_Text.setLayoutY(Light1_Text.getLayoutY()+10);
				}
			}
			if(e.getSource() == Light1_Lock || e.getSource() == Light1_Lockcross){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					if(!Light1_Lockcross.isVisible()){
						Light1_Lockcross.setVisible(true);
						System.out.println("Locked Light1");
					}else if(Light1_Lockcross.isVisible()){
						Light1_Lockcross.setVisible(false);
						System.out.println("Unlocked Light1");
					}
				}
			}
		}

	}

}

