package me.Christian.pack;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.Christian.networking.Server;
import me.Christian.other.OtherStuff;
import me.Christian.threads.Thread_GetWeather;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
	public static Label calendar, town, weathericonlabel;
	public static ImageView Light_Head, Music_Head;
	public static ImageView Music_prev, Music_next, Music_pause, Music_play;
	public static Slider Music_Slider;
	public static ImageView Light1_Button1, Light2_Button1, Light3_Button1;
	public static ImageView Light1_Button2, Light2_Button2, Light3_Button2;
	public static Text Light1_Text, Light_HeadText, Light2_Text, Light3_Text, Music_Title;
	public static ImageView Light1_Lock, Light2_Lock, Light3_Lock;
	public static ImageView Light1_Lockcross, Light2_Lockcross, Light3_Lockcross;
	public static ImageView Light1_State1, Light1_State2, Light1_State3, Light2_State1, Light2_State2, Light2_State3, Light3_State1, Light3_State2, Light3_State3;
	public static String currenttitle = "Feting Title...";
	public static String volume;
	public static final String City = "Schweinfurt";
	StringProperty title = new SimpleStringProperty();
	public static boolean Weatherinit = false;

	public static void main(String[] args) {
		int port = Integer.parseInt("9977");
		Thread_GetWeather.StartCheck(City);
		try {
			Server.startServer( port );
		} catch (IOException e1) {
			e1.printStackTrace();
		}

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



		new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		}.start();

		calendar = new Label(OtherStuff.TheNormalTime());
		calendar.setLayoutX(20);
		calendar.setLayoutY(20);
		calendar.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(calendar);

		town = new Label(Main.City + ", " + Thread_GetWeather.degree + "°C");
		town.setLayoutX(198);
		town.setLayoutY(20);
		town.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(town);

		weathericonlabel = new Label("");
		weathericonlabel.setLayoutX(330);
		weathericonlabel.setLayoutY(8);
		weathericonlabel.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(weathericonlabel);

		Light_Head = new ImageView(new Image("B12.png"));
		Light_Head.setLayoutX(60);
		Light_Head.setScaleX(1.9);
		Light_Head.setLayoutY(72);
		root.getChildren().add(Light_Head);

		Light_HeadText = new Text();
		Light_HeadText.setText("Lichtsteuerung");
		Light_HeadText.setLayoutX(41);
		Light_HeadText.setLayoutY(99);
		Light_HeadText.setFont(Font.font(java.awt.Font.SERIF, 20));
		root.getChildren().add(Light_HeadText);

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

		// Button2
		Light2_Button1 = new ImageView(new Image("B12.png"));
		Light2_Button1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light2_Button1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_Button1.getOnMousePressed();
		Light2_Button1.getOnMouseReleased();
		Light2_Button1.setLayoutX(60);
		Light2_Button1.setLayoutY(175);
		root.getChildren().add(Light2_Button1);

		Light2_Button2 = new ImageView(new Image("B3.png"));
		Light2_Button2.setLayoutX(60);
		Light2_Button2.setLayoutY(175);
		Light2_Button2.setVisible(false);
		root.getChildren().add(Light2_Button2);

		Light2_Text = new Text();
		Light2_Text.setText("Licht II");
		Light2_Text.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light2_Text.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_Text.getOnMousePressed();
		Light2_Text.getOnMouseReleased();
		Light2_Text.getOnMousePressed();
		Light2_Text.setFont(Font.font(java.awt.Font.SERIF, 18));
		Light2_Text.setLayoutX(80);
		Light2_Text.setLayoutY(200);
		root.getChildren().add(Light2_Text);

		Light2_Lock = new ImageView(new Image("filledquad.png"));
		Light2_Lock.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_Lock.getOnMouseReleased();
		Light2_Lock.setLayoutX(210);
		Light2_Lock.setLayoutY(185);
		Light2_Lock.setFitHeight(40);
		Light2_Lock.setFitWidth(40);
		root.getChildren().add(Light2_Lock);

		Light2_Lockcross = new ImageView(new Image("redcross.png"));
		Light2_Lockcross.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_Lockcross.getOnMouseReleased();
		Light2_Lockcross.setLayoutX(215);
		Light2_Lockcross.setLayoutY(190);
		Light2_Lockcross.setFitHeight(30);
		Light2_Lockcross.setFitWidth(30);
		Light2_Lockcross.setVisible(false);
		root.getChildren().add(Light2_Lockcross);

		Light2_State1 = new ImageView(new Image("tealorb.png"));
		Light2_State1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_State1.getOnMouseReleased();
		Light2_State1.setLayoutX(10);
		Light2_State1.setLayoutY(175);
		Light2_State1.setFitHeight(35);
		Light2_State1.setFitWidth(35);
		root.getChildren().add(Light2_State1);

		Light2_State2 = new ImageView(new Image("greenorb.png"));
		Light2_State2.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_State2.getOnMouseReleased();
		Light2_State2.setLayoutX(10);
		Light2_State2.setLayoutY(175);
		Light2_State2.setFitHeight(35);
		Light2_State2.setFitWidth(35);
		Light2_State2.setVisible(false);
		root.getChildren().add(Light2_State2);

		Light2_State3 = new ImageView(new Image("redorb.png"));
		Light2_State3.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light2_State3.getOnMouseReleased();
		Light2_State3.setLayoutX(10);
		Light2_State3.setLayoutY(175);
		Light2_State3.setFitHeight(35);
		Light2_State3.setFitWidth(35);
		Light2_State3.setVisible(false);
		root.getChildren().add(Light2_State3);
		
		// Button3
		Light3_Button1 = new ImageView(new Image("B12.png"));
		Light3_Button1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light3_Button1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_Button1.getOnMousePressed();
		Light3_Button1.getOnMouseReleased();
		Light3_Button1.setLayoutX(60);
		Light3_Button1.setLayoutY(225);
		root.getChildren().add(Light3_Button1);

		Light3_Button2 = new ImageView(new Image("B3.png"));
		Light3_Button2.setLayoutX(60);
		Light3_Button2.setLayoutY(225);
		Light3_Button2.setVisible(false);
		root.getChildren().add(Light3_Button2);

		Light3_Text = new Text();
		Light3_Text.setText("Licht III");
		Light3_Text.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light3_Text.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_Text.getOnMousePressed();
		Light3_Text.getOnMouseReleased();
		Light3_Text.getOnMousePressed();
		Light3_Text.setFont(Font.font(java.awt.Font.SERIF, 18));
		Light3_Text.setLayoutX(80);
		Light3_Text.setLayoutY(250);
		root.getChildren().add(Light3_Text);

		Light3_Lock = new ImageView(new Image("filledquad.png"));
		Light3_Lock.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_Lock.getOnMouseReleased();
		Light3_Lock.setLayoutX(210);
		Light3_Lock.setLayoutY(235);
		Light3_Lock.setFitHeight(40);
		Light3_Lock.setFitWidth(40);
		root.getChildren().add(Light3_Lock);

		Light3_Lockcross = new ImageView(new Image("redcross.png"));
		Light3_Lockcross.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_Lockcross.getOnMouseReleased();
		Light3_Lockcross.setLayoutX(215);
		Light3_Lockcross.setLayoutY(240);
		Light3_Lockcross.setFitHeight(30);
		Light3_Lockcross.setFitWidth(30);
		Light3_Lockcross.setVisible(false);
		root.getChildren().add(Light3_Lockcross);

		Light3_State1 = new ImageView(new Image("tealorb.png"));
		Light3_State1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_State1.getOnMouseReleased();
		Light3_State1.setLayoutX(10);
		Light3_State1.setLayoutY(225);
		Light3_State1.setFitHeight(35);
		Light3_State1.setFitWidth(35);
		root.getChildren().add(Light3_State1);

		Light3_State2 = new ImageView(new Image("greenorb.png"));
		Light3_State2.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_State2.getOnMouseReleased();
		Light3_State2.setLayoutX(10);
		Light3_State2.setLayoutY(225);
		Light3_State2.setFitHeight(35);
		Light3_State2.setFitWidth(35);
		Light3_State2.setVisible(false);
		root.getChildren().add(Light3_State2);

		Light3_State3 = new ImageView(new Image("redorb.png"));
		Light3_State3.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light3_State3.getOnMouseReleased();
		Light3_State3.setLayoutX(10);
		Light3_State3.setLayoutY(225);
		Light3_State3.setFitHeight(35);
		Light3_State3.setFitWidth(35);
		Light3_State3.setVisible(false);
		root.getChildren().add(Light3_State3);

		
		// Music
		
		Music_Title = new Text();
		Music_Title.setText(currenttitle);
		Music_Title.setFont(Font.font(java.awt.Font.SERIF, 9));
		Music_Title.setLayoutX(850);
		Music_Title.setLayoutY(130);
		root.getChildren().add(Music_Title);
		
		Music_Slider = new Slider();
		Music_Slider.setMin(0);
		Music_Slider.setMax(100);
		Music_Slider.setValue(55);
		Music_Slider.setShowTickLabels(true);
		Music_Slider.setMajorTickUnit(50);
		Music_Slider.setMinorTickCount(5);
		Music_Slider.setBlockIncrement(10);
		Music_Slider.setLayoutX(860);
		Music_Slider.setLayoutY(200);
		Music_Slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	int volume = (int) Math.floor(new_val.doubleValue());
            	try {
					Runtime.getRuntime().exec(new String[]{"bash","-c","mpc -h 192.168.11.205 volume " + volume});
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		root.getChildren().add(Music_Slider);
		
		Music_prev = new ImageView(new Image("prev.png"));
		Music_prev.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_prev.getOnMouseReleased();
		Music_prev.setLayoutX(850);
		Music_prev.setLayoutY(150);
		Music_prev.setFitHeight(35);
		Music_prev.setFitWidth(35);
		Music_prev.setVisible(true);
		root.getChildren().add(Music_prev);
		
		Music_pause = new ImageView(new Image("pause.png"));
		Music_pause.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_pause.getOnMouseReleased();
		Music_pause.setLayoutX(890);
		Music_pause.setLayoutY(150);
		Music_pause.setFitHeight(35);
		Music_pause.setFitWidth(35);
		Music_pause.setVisible(true);
		root.getChildren().add(Music_pause);
		
		Music_play = new ImageView(new Image("play.png"));
		Music_play.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_play.getOnMouseReleased();
		Music_play.setLayoutX(930);
		Music_play.setLayoutY(150);
		Music_play.setFitHeight(35);
		Music_play.setFitWidth(35);
		Music_play.setVisible(true);
		root.getChildren().add(Music_play);
		
		Music_next = new ImageView(new Image("next.png"));
		Music_next.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_next.getOnMouseReleased();
		Music_next.setLayoutX(970);
		Music_next.setLayoutY(150);
		Music_next.setFitHeight(35);
		Music_next.setFitWidth(35);
		Music_next.setVisible(true);
		root.getChildren().add(Music_next);
		
		
		primaryStage.setScene(new Scene(root, 1024, 600));
		primaryStage.show();
	}


	protected void update() {
		calendar.setText(OtherStuff.TheNormalTime());	
		if(Thread_GetWeather.weathericon != null && !Weatherinit){
			resetweather();
			Weatherinit = true;
		}
	}

	public void resetweather(){
		weathericonlabel.setGraphic(new ImageView(new Image(Thread_GetWeather.weathericon + ".png")));
		town.setText((Main.City + ", " + Thread_GetWeather.degree + "°C"));
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
					System.out.println("Released & Triggered Light1_Button");
					Light1_Button2.setVisible(false);
					Light1_Button1.setVisible(true);
					Light1_Text.setLayoutX(Light1_Text.getLayoutX()-12);
					Light1_Text.setLayoutY(Light1_Text.getLayoutY()-10);
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light1_Button");
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
			if(e.getSource() == Light2_Button1 || e.getSource() == Light2_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light2_Button");
					Light2_Button2.setVisible(false);
					Light2_Button1.setVisible(true);
					Light2_Text.setLayoutX(Light2_Text.getLayoutX()-12);
					Light2_Text.setLayoutY(Light2_Text.getLayoutY()-10);
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light2_Button");
					Light2_Button1.setVisible(false);
					Light2_Button2.setVisible(true);
					Light2_Text.setLayoutX(Light2_Text.getLayoutX()+12);
					Light2_Text.setLayoutY(Light2_Text.getLayoutY()+10);
				}
			}
			if(e.getSource() == Light2_Lock || e.getSource() == Light2_Lockcross){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					if(!Light2_Lockcross.isVisible()){
						Light2_Lockcross.setVisible(true);
						System.out.println("Locked Light2");
					}else if(Light2_Lockcross.isVisible()){
						Light2_Lockcross.setVisible(false);
						System.out.println("Unlocked Light2");
					}
				}
			}
			if(e.getSource() == Light3_Button1 || e.getSource() == Light3_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light3_Button");
					Light3_Button2.setVisible(false);
					Light3_Button1.setVisible(true);
					Light3_Text.setLayoutX(Light3_Text.getLayoutX()-12);
					Light3_Text.setLayoutY(Light3_Text.getLayoutY()-10);
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light3_Button");
					Light3_Button1.setVisible(false);
					Light3_Button2.setVisible(true);
					Light3_Text.setLayoutX(Light3_Text.getLayoutX()+12);
					Light3_Text.setLayoutY(Light3_Text.getLayoutY()+10);
				}
			}
			if(e.getSource() == Light3_Lock || e.getSource() == Light3_Lockcross){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					if(!Light3_Lockcross.isVisible()){
						Light3_Lockcross.setVisible(true);
						System.out.println("Locked Light3");
					}else if(Light3_Lockcross.isVisible()){
						Light3_Lockcross.setVisible(false);
						System.out.println("Unlocked Light3");
					}
				}
			}
			if(e.getSource() == Music_next){
            	try {
					String[] commands = {"bash","-c","mpc -h 192.168.11.205 next"};
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(commands);
			        BufferedReader stdInput = new BufferedReader(new 
			                InputStreamReader(proc.getInputStream()));

			        BufferedReader stdError = new BufferedReader(new 
			                InputStreamReader(proc.getErrorStream()));
			           String s = null;
			           int line = 0;
			           while ((s = stdInput.readLine()) != null) {
			        	   if(line == 0){
			        		   currenttitle = s;
			        		   Music_Title.setText(currenttitle);
			        	   }else if (line == 2){
			        		   volume = s;
			        	   }
			        	   line++;
			           }
			           while ((s = stdError.readLine()) != null) {System.out.println(s);}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if(e.getSource() == Music_prev){
            	try {
					String[] commands = {"bash","-c","mpc -h 192.168.11.205 prev"};
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(commands);
			        BufferedReader stdInput = new BufferedReader(new 
			                InputStreamReader(proc.getInputStream()));

			        BufferedReader stdError = new BufferedReader(new 
			                InputStreamReader(proc.getErrorStream()));
			           String s = null;
			           int line = 0;
			           while ((s = stdInput.readLine()) != null) {
			        	   if(line == 0){
			        		   currenttitle = s;
			        		   Music_Title.setText(currenttitle);
			        	   }else if (line == 2){
			        		   volume = s;
			        	   }
			        	   line++;
			           }
			           while ((s = stdError.readLine()) != null) {System.out.println(s);}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if(e.getSource() == Music_pause){
            	try {
					String[] commands = {"bash","-c","mpc -h 192.168.11.205 pause"};
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(commands);
			        BufferedReader stdInput = new BufferedReader(new 
			                InputStreamReader(proc.getInputStream()));

			        BufferedReader stdError = new BufferedReader(new 
			                InputStreamReader(proc.getErrorStream()));
			           String s = null;
			           int line = 0;
			           while ((s = stdInput.readLine()) != null) {
			        	   if(line == 0){
			        		   currenttitle = s;
			        		   Music_Title.setText(currenttitle);
			        	   }else if (line == 2){
			        		   volume = s;
			        	   }
			        	   line++;
			           }
			           while ((s = stdError.readLine()) != null) {System.out.println(s);}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if(e.getSource() == Music_play){
            	try {
					String[] commands = {"bash","-c","mpc -h 192.168.11.205 play"};
					Runtime rt = Runtime.getRuntime();
					Process proc = rt.exec(commands);
			        BufferedReader stdInput = new BufferedReader(new 
			                InputStreamReader(proc.getInputStream()));

			        BufferedReader stdError = new BufferedReader(new 
			                InputStreamReader(proc.getErrorStream()));
			           String s = null;
			           int line = 0;
			           while ((s = stdInput.readLine()) != null) {
			        	   if(line == 0){
			        		   currenttitle = s;
			        		   Music_Title.setText(currenttitle);
			        	   }else if (line == 2){
			        		   volume = s;
			        	   }
			        	   line++;
			           }
			           while ((s = stdError.readLine()) != null) {System.out.println(s);}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}

	}

}

