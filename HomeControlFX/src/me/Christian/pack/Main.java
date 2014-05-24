package me.Christian.pack;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Logger;

import javax.swing.Timer;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceRelay;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.gpio.extension.piface.PiFaceGpioProvider;
import com.pi4j.gpio.extension.piface.PiFacePin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Spi;

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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	public static boolean Testbuild = true;

	public static TextArea Console;
	public static Label calendar, town, weathericonlabel;
	public static ImageView Light_Head, Music_Head;
	public static ImageView Music_prev, Music_next, Music_pause, Music_play;
	public static Slider Music_Slider;
	public static ImageView Light1_Button1, Light2_Button1, Light3_Button1, Console_Button1;
	public static ImageView Light1_Button2, Light2_Button2, Light3_Button2, Console_Button2;
	public static Text Light1_Text, Light_HeadText, Light2_Text, Light3_Text, Music_Title, Music_HeadText, Console_ButtonText;
	public static ImageView Light1_Lock, Light2_Lock, Light3_Lock;
	public static ImageView Light1_Lockcross, Light2_Lockcross, Light3_Lockcross;
	public static ImageView Light1_State1, Light1_State2, Light1_State3, Light2_State1, Light2_State2, Light2_State3, Light3_State1, Light3_State2, Light3_State3;
	public static String currenttitle = "Feting Title...";
	public static String volume;
	public static final String City = "Schweinfurt";
	private static Timer MpcRefreshTimer;
	StringProperty title = new SimpleStringProperty();
	public static PiFace piface = null;
	public static GpioController gpio = null;
	public static PiFaceGpioProvider gpioProvider = null;
	public static boolean Weatherinit = false;

	public static GpioPinDigitalOutput oPin0, oPin1, oPin2, oPin3, oPin4, oPin5, oPin6, oPin7;
	public static GpioPinDigitalInput iPin0, iPin1, iPin2, iPin3, iPin4, iPin5, iPin6, iPin7;

	public static void main(String[] args) {
		// Integer for Server
		System.out.println("Starting [1]: Server");
		int port = Integer.parseInt("9977");
		// Get the weather from thread
		Thread_GetWeather.StartCheck(City);
		// Start Server for mobile app & slave Pi's
		try {
			Server.startServer( port );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// build Refresh of music title
		MpcRefreshTimer = new Timer(2000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				RefreshMpc();
			}
		});
		if(!Testbuild){
			// Start Refresh
			MpcRefreshTimer.start();
			try {
				// Setup Piface instances
				gpio = GpioFactory.getInstance();
				gpioProvider = new PiFaceGpioProvider(PiFaceGpioProvider.DEFAULT_ADDRESS,Spi.CHANNEL_0);
				piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// Set up all Input Pins in an array
			GpioPinDigitalInput myInputs[] = {
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_00),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_01),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_02),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_03),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_04),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_05),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_06),
					gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_07)
			};

			//  Get them as single variable
			iPin0 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_00);
			iPin1 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_01);
			iPin2 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_02);
			iPin3 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_03);
			iPin4 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_04);
			iPin5 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_05);
			iPin6 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_06);
			iPin7 = gpio.provisionDigitalInputPin(gpioProvider, PiFacePin.INPUT_07);

			// Set up Listener if a Input Pin changes
			gpio.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					System.out.println(OtherStuff.TheSimpleNormalTime() + " Input Pin changed: " + event.getPin() + " = "
							+ event.getState());
				}
			}, myInputs);

			// Set up all Output Pins in an array
			GpioPinDigitalOutput myOutputs[] = { 
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_00),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_01),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_02),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_03),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_04),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_05),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_06),
					gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_07),
			};
			
			// Disable all output pins
			gpio.setState(false, myOutputs);

			// Set up all Output Pins as single variable
			oPin0 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_00);
			oPin1 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_01);
			oPin2 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_02);
			oPin3 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_03);
			oPin4 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_04);
			oPin5 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_05);
			oPin6 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_06);
			oPin7 = gpio.provisionDigitalOutputPin(gpioProvider, PiFacePin.OUTPUT_07);
		}
		// start GUI
		System.out.println("Starting [2]: GUI");
		launch(args);
	}

	public void start(Stage primaryStage) {
		// name the window
		primaryStage.setTitle("Homecontrol");
		primaryStage.setResizable(false);
		System.out.println("Loading: 10%");
		// Exit the programm on window close
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;
		Pane root = new Pane();
		
		// Background
		ImageView imgView = null;
		if(!Testbuild){
			imgView = new ImageView(new Image("438120.jpg"));
			imgView.setFitWidth(1100);
			imgView.setFitHeight(625);
			root.getChildren().add(imgView);
		}else{
			imgView = new ImageView(new Image("438120.jpg"));
			imgView.setFitWidth(1100);
			imgView.setFitHeight(625);
			root.getChildren().add(imgView);
			ImageView watermark = new ImageView(new Image("watermark.png"));
			watermark.setFitWidth(1100);
			watermark.setFitHeight(625);
			watermark.setOpacity(0.15);
			root.getChildren().add(watermark);
		}

		System.out.println("Loading: 20%");
		// Refresh timer for labels ect.
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		}.start();
		// Date & time
		calendar = new Label(OtherStuff.TheNormalTime());
		calendar.setLayoutX(20);
		calendar.setLayoutY(20);
		calendar.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(calendar);
		
		// State and weather degrees
		town = new Label(Main.City + ", " + Thread_GetWeather.degree + "°C");
		town.setLayoutX(198);
		town.setLayoutY(20);
		town.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(town);
		System.out.println("Loading: 30%");
		// Icon for weather
		weathericonlabel = new Label("");
		weathericonlabel.setLayoutX(330);
		weathericonlabel.setLayoutY(8);
		weathericonlabel.setFont(Font.font(java.awt.Font.SERIF, 18));
		root.getChildren().add(weathericonlabel);
		
		// Light head bar image
		Light_Head = new ImageView(new Image("B12.png"));
		Light_Head.setLayoutX(60);
		Light_Head.setScaleX(1.9);
		Light_Head.setLayoutY(72);
		root.getChildren().add(Light_Head);
		
		// Light head text
		Light_HeadText = new Text();
		Light_HeadText.setText("Lichtsteuerung");
		Light_HeadText.setLayoutX(41);
		Light_HeadText.setLayoutY(99);
		Light_HeadText.setFont(Font.font(java.awt.Font.SERIF, 20));
		root.getChildren().add(Light_HeadText);
		
		// Light1_Button1 unpressed
		Light1_Button1 = new ImageView(new Image("B12.png"));
		Light1_Button1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Light1_Button1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Button1.getOnMousePressed();
		Light1_Button1.getOnMouseReleased();
		Light1_Button1.setLayoutX(60);
		Light1_Button1.setLayoutY(125);
		root.getChildren().add(Light1_Button1);

		// Light1_Button1 pressed
		Light1_Button2 = new ImageView(new Image("B3.png"));
		Light1_Button2.setLayoutX(60);
		Light1_Button2.setLayoutY(125);
		Light1_Button2.setVisible(false);
		root.getChildren().add(Light1_Button2);
		System.out.println("Loading: 40%");
		// Light1_Button1 text
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
		
		// Light1_Lock quadrat
		Light1_Lock = new ImageView(new Image("filledquad.png"));
		Light1_Lock.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Lock.getOnMouseReleased();
		Light1_Lock.setLayoutX(210);
		Light1_Lock.setLayoutY(135);
		Light1_Lock.setFitHeight(40);
		Light1_Lock.setFitWidth(40);
		root.getChildren().add(Light1_Lock);
		
		// Light1_Lock red X
		Light1_Lockcross = new ImageView(new Image("redcross.png"));
		Light1_Lockcross.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Light1_Lockcross.getOnMouseReleased();
		Light1_Lockcross.setLayoutX(215);
		Light1_Lockcross.setLayoutY(140);
		Light1_Lockcross.setFitHeight(30);
		Light1_Lockcross.setFitWidth(30);
		Light1_Lockcross.setVisible(false);
		root.getChildren().add(Light1_Lockcross);
		
		// Light1 state of it
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
		System.out.println("Loading: 50%");
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
		System.out.println("Loading: 60%");
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
		System.out.println("Loading: 70%");

		// Console
		Console = new TextArea();
		Console.setPrefSize(250, 400);
		Console.setLayoutX(500);
		Console.setLayoutY(130);
		Console.setWrapText(true);
		Console.setEditable(false);
		Console.setFont(Font.font(java.awt.Font.SERIF, 13));

		Console.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue,
					Object newValue) {
				Console.setScrollTop(Double.MIN_VALUE);
			}
		});
		System.out.println("Loading: 80%");
		
		root.getChildren().add(Console);
		// Console Toggle
		Console_Button1 = new ImageView(new Image("iB12.png"));
		Console_Button1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Console_Button1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Console_Button1.getOnMousePressed();
		Console_Button1.getOnMouseReleased();
		Console_Button1.setLayoutX(585);
		Console_Button1.setLayoutY(80);
		root.getChildren().add(Console_Button1);

		Console_Button2 = new ImageView(new Image("iB3.png"));
		Console_Button2.setLayoutX(585);
		Console_Button2.setLayoutY(80);
		Console_Button2.setVisible(false);
		root.getChildren().add(Console_Button2);

		Console_ButtonText = new Text();
		Console_ButtonText.setText("Toggle");
		Console_ButtonText.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Console_ButtonText.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Console_ButtonText.getOnMousePressed();
		Console_ButtonText.getOnMouseReleased();
		Console_ButtonText.getOnMousePressed();
		Console_ButtonText.setFont(Font.font(java.awt.Font.SERIF, 18));
		Console_ButtonText.setLayoutX(675);
		Console_ButtonText.setLayoutY(105);
		root.getChildren().add(Console_ButtonText);
		System.out.println("Loading: 90%");
		// Music
		Music_Head = new ImageView(new Image("iB12.png"));
		Music_Head.setLayoutX(809);
		Music_Head.setScaleX(1.9);
		Music_Head.setLayoutY(72);
		root.getChildren().add(Music_Head);

		Music_HeadText = new Text();
		Music_HeadText.setText("Musiksteuerung");
		Music_HeadText.setLayoutX(860);
		Music_HeadText.setLayoutY(99);
		Music_HeadText.setFont(Font.font(java.awt.Font.SERIF, 20));
		root.getChildren().add(Music_HeadText);


		Music_Title = new Text();
		Music_Title.setText(currenttitle);
		Music_Title.setFont(Font.font(java.awt.Font.SERIF, 14));
		Music_Title.setLayoutX(800);
		Music_Title.setLayoutY(135);
		root.getChildren().add(Music_Title);

		Music_Slider = new Slider();
		Music_Slider.setMin(0);
		Music_Slider.setMax(100);
		Music_Slider.setValue(55);
		Music_Slider.setShowTickLabels(true);
		Music_Slider.setMajorTickUnit(25);
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
		Music_prev.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Music_prev.getOnMouseReleased();
		Music_prev.getOnMousePressed();
		Music_prev.setLayoutX(850);
		Music_prev.setLayoutY(150);
		Music_prev.setFitHeight(35);
		Music_prev.setFitWidth(35);
		Music_prev.setVisible(true);
		root.getChildren().add(Music_prev);

		Music_pause = new ImageView(new Image("pause.png"));
		Music_pause.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_pause.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Music_pause.getOnMouseReleased();
		Music_pause.getOnMousePressed();
		Music_pause.setLayoutX(890);
		Music_pause.setLayoutY(150);
		Music_pause.setFitHeight(35);
		Music_pause.setFitWidth(35);
		Music_pause.setVisible(true);
		root.getChildren().add(Music_pause);

		Music_play = new ImageView(new Image("play.png"));
		Music_play.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_play.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Music_play.getOnMouseReleased();
		Music_play.getOnMousePressed();
		Music_play.setLayoutX(930);
		Music_play.setLayoutY(150);
		Music_play.setFitHeight(35);
		Music_play.setFitWidth(35);
		Music_play.setVisible(true);
		root.getChildren().add(Music_play);

		Music_next = new ImageView(new Image("next.png"));
		Music_next.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		Music_next.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		Music_next.getOnMouseReleased();
		Music_prev.getOnMousePressed();
		Music_next.setLayoutX(970);
		Music_next.setLayoutY(150);
		Music_next.setFitHeight(35);
		Music_next.setFitWidth(35);
		Music_next.setVisible(true);
		root.getChildren().add(Music_next);
		
	
		
		System.out.println("Loading: 100%");
		System.out.println("Launching Now!!!");
		primaryStage.setScene(new Scene(root, 1024, 600));
		primaryStage.show();
		System.out.println("Starting [2]: GUI - Done");
		System.out.println("--- finished loading ---");
		
		System.setOut(new PrintStream(System.out) {
			public void println(String s) {
				Console.appendText(OtherStuff.TheSimpleNormalTime() + ": "+ s+"\n");
				Logger log = Logger.getLogger("");
				log.info(s);
			}
		});
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

	// to change the icons of the check states // Light/door/window ect.
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
	
	// Refresh of music title
	public static void RefreshMpc(){
		try {
			String[] commands = {"bash","-c","mpc -h 192.168.11.205"};
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
	class MyEventHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent e) {
			// Buttons pressed, set state to pressed and change icon & do smth.
			if(e.getSource() == Light1_Button1 || e.getSource() == Light1_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light1_Button");
					Light1_Button2.setVisible(false);
					Light1_Button1.setVisible(true);
					Light1_Text.setLayoutX(Light1_Text.getLayoutX()-12);
					Light1_Text.setLayoutY(Light1_Text.getLayoutY()-10);
					if(GuiVariables.Light1_State == 0){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K0).close();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 1);
						GuiVariables.Light1_State = 1;
					}else if(GuiVariables.Light1_State == 1){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K0).open();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 2);
						GuiVariables.Light1_State = 2;
					}else if(GuiVariables.Light1_State == 2){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K0).close();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 1);
						GuiVariables.Light1_State = 1;
					}
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
					if(GuiVariables.Light2_State == 0){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K1).close();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 1);
						GuiVariables.Light2_State = 1;
					}else if(GuiVariables.Light2_State == 1){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K1).open();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 2);
						GuiVariables.Light2_State = 2;
					}else if(GuiVariables.Light2_State == 2){
						if(!Testbuild){
							piface.getRelay(PiFaceRelay.K1).close();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 1);
						GuiVariables.Light2_State = 1;
					}
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
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_next.setOpacity(1);
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
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_next.setOpacity(0.5);
				}
			}
			if(e.getSource() == Music_prev){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_prev.setOpacity(1);
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
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_prev.setOpacity(0.5);
				}
			}
			if(e.getSource() == Music_pause){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_pause.setOpacity(1);
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
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_pause.setOpacity(0.5);
				}
			}
			if(e.getSource() == Music_play){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_play.setOpacity(1);
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
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_play.setOpacity(0.5);
				}
			}
			if(e.getSource() == Console_Button1 || e.getSource() == Console_ButtonText){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Console Toggle");
					Console_Button2.setVisible(false);
					Console_Button1.setVisible(true);
					if(Console.isVisible()){
						Console.setVisible(false);
					}else{
						Console.setVisible(true);
					}
					Console_ButtonText.setLayoutX(Console_ButtonText.getLayoutX()+12);
					Console_ButtonText.setLayoutY(Console_ButtonText.getLayoutY()-10);
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Console Toggle");
					Console_Button1.setVisible(false);
					Console_Button2.setVisible(true);
					Console_ButtonText.setLayoutX(Console_ButtonText.getLayoutX()-12);
					Console_ButtonText.setLayoutY(Console_ButtonText.getLayoutY()+10);
				}
			}

		}

	}

}

