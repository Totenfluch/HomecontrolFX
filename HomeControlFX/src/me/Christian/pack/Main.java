package me.Christian.pack;



import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.Timer;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceLed;
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
import me.Christian.other.ChangeOutStream;
import me.Christian.other.ConfigFileStuff;
import me.Christian.other.FeedReader;
import me.Christian.other.OtherStuff;
import me.Christian.threads.Thread_GetWeather;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	//
	//
	// SET TO FALSE IF YOU ARE USING ON RASPBERRY!!!!!!
	public static boolean Testbuild = true;
	// SET TO FALSE IF YOU ARE USING ON RASPBERRY!!!!!!
	//
	//
	// Enable MPC ( Internet Radio [Requires extern Server])
	public static boolean MPCEnabled = false;
	//
	// Delay to refresh MPC in ms
	// default: 2000 (2s)
	public static int MpcRefreshDelay = 2000;
	//
	// The MPC Server Ip - should be in the same network
	public static String MPCServerIP = "192.168.11.205";
	//
	// INTERNAL SERVER PORT
	public static int portz = 9977;
	//
	// START WITH LOGIN SCREEN ??
	public static boolean StartWithLoginScreen = false;
	//
	// THE CITY WE ARE LIVING IN
	public static String City = "Schweinfurt";
	//
	// Delay to refresh the weather in ms
	// default: 600000 (10 minutes)
	public static int WeatherRefreshDelay = 600000;

	// Main Stage - where everything goes thing thing
	public static Stage MainStage;
	// Scene for Root(control GUI) and Scene for Login - both can be places in MainStage.
	public static Scene Sroot, SLogin;


	//Rss Feeds
	public static boolean RssEnabled = true;
	//Rss Refresh delay in ms
	public static int RssRefreshDelay = 15000;


	// Root Window Stuff
	public static TextArea Console;
	public static Label calendar, town, weathericonlabel, User_Logout;
	public static Slider Music_Slider;
	public static ImageView Light_Head, Music_Head;
	public static ImageView Music_prev, Music_next, Music_pause, Music_play;
	public static ImageView Light1_Button1, Light2_Button1, Light3_Button1, Console_Button1;
	public static ImageView Light1_Button2, Light2_Button2, Light3_Button2, Console_Button2;
	public static ImageView Light1_Lock, Light2_Lock, Light3_Lock;
	public static ImageView Light1_Lockcross, Light2_Lockcross, Light3_Lockcross;
	public static ImageView Light1_State1, Light1_State2, Light1_State3, Light2_State1, Light2_State2, Light2_State3, Light3_State1, Light3_State2, Light3_State3;
	public static Text Light1_Text, Light_HeadText, Light2_Text, Light3_Text, Music_Title, Music_HeadText, Console_ButtonText;
	public static String currenttitle = "Fetching Title...";
	double Music_title_size = 19;
    static final int MUSIC_TITLE_MAX_WIDTH = 235;
	public static String volume;
	// Login thingy for later
	public static String ActiveUser = "Root";
	// Login thingy for later
	private static Timer MpcRefreshTimer, WeatherRefreshTimer, RssRefreshTimer;
	public static boolean Light1_Lockstate = false, Light2_Lockstate = false, Light3_Lockstate = false, Door1_Lock = false, Door2_Lock = false;
	public static int Light1_State = 0, Light2_State = 0, Light3_State = 0, Door1_State = 0, Door2_State = 0;
	public static int Login_LoginButton1_State = 0, Login_LoginButton2_State = 0, Login_LoginButton3_State = 0, Login_LoginButton4_State = 0, Login_LoginButton5_State = 0, Login_LoginButton6_State = 0;
	public static boolean goLeft, goRight;
	public static int entrypos = 265;

	// print queue
	public static String[] todoprint = new String[1000];
	public static int todoprintsize = 0;

	// cmd queue
	public static String[] todocmd = new String[1000];
	public static int todocmdsize = 0;

	// Server and it's thread
	public static Thread Thread_MainServer;
	public static Server server;

	public static boolean Weatherinit = false;

	public static boolean StartupDone = false;

	// Login Window Stuff
	public static ImageView Login_LoginButton1, Login_LoginButton2, Login_LoginButton3, Login_LoginButton4, Login_LoginButton5, Login_LoginButton6;
	public static ImageView Login_Spark[] = new ImageView[6];
	public static double Login_SparkPos[][] = new double[6][2];
	public static int Login_SparkSeq[] = new int[6];
	//private static ImageView Login_Background;


	// Setup Piface instances
	public static GpioController gpio;
	public static PiFaceGpioProvider gpioProvider;
	public static PiFace piface;


	public static void main(String[] args) throws IOException{
		System.out.println("|> checking config file <|");
		// Create config file if empty, load if it's there
		System.out.println("Config File location: " + OtherStuff.jarlocation() + "\\config.properties");
		ConfigFileStuff.startup();
		System.out.println("|< config file checked >|");

		System.out.println("|> checking Rss feed file <|");
		if(RssEnabled){
			FeedReader.ReadFeedFile();
			FeedReader.CreateFeedObjects();
			System.out.println("RssFeeds File location: " + OtherStuff.jarlocation() + "\\RSSFeeds.txt");
			if(RssRefreshDelay > 0){
				RssRefreshTimer = new Timer(RssRefreshDelay, new ActionListener()
				{
					@Override
					public void actionPerformed(java.awt.event.ActionEvent arg0) {
						FeedReader.ReadFeedFile();
						FeedReader.CreateFeedObjects();
						OtherStuff.addToPrintQueue("Refreshed Rss feeds");
					}
				});
				RssRefreshTimer.start();
			}
		}else{
			System.out.println("Rss is disabled.");
		}
		System.out.println("|> checked Rss feed file <|");


		// Make both queues empty
		for(int i = 999; i>-1; i--){
			todoprint[i] = "";
			todocmd[i] = "";
		}
		Platform.setImplicitExit(false);
		// Integer for Server
		System.out.println("Starting [1]: Server");
		// Init user database
		OtherStuff.initDatabase();
		// Get the weather from thread
		Thread_GetWeather.StartCheck(City);

		// Create a object, create a Thread, start the Thread
		server = new Server();
		Thread_MainServer = new Thread(server);
		Thread_MainServer.start();

		// build Refresh of music title
		if(MpcRefreshDelay > 0){
			MpcRefreshTimer = new Timer(MpcRefreshDelay, new ActionListener()
			{
				@Override
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					RefreshMpc();
				}
			});
		}

		if(WeatherRefreshDelay > 0){
			WeatherRefreshTimer = new Timer(WeatherRefreshDelay, new ActionListener()
			{
				@Override
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					refreshweather();
				}
			});
			WeatherRefreshTimer.start();
		}

		if(!Testbuild){

			gpio = GpioFactory.getInstance();
			gpioProvider = new PiFaceGpioProvider(PiFaceGpioProvider.DEFAULT_ADDRESS,Spi.CHANNEL_0);
			piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);


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

			// Set up Listener if a Input Pin changes
			gpio.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					OtherStuff.addToPrintQueue(" Input Pin changed: " + event.getPin() + " = "
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
			gpio.setState(true, myOutputs);


			// Shutdown hook
			Runtime.getRuntime().addShutdownHook(new Thread() { 
				public void run() { 
					// Disable all LED's
					gpio.setState(false, myOutputs);
					gpio.shutdown();
				}
			}); 
		}
		// Start Refresh if MPC is enabled
		if(MPCEnabled){
			MpcRefreshTimer.start();
		}
		// start GUI
		System.out.println("Starting [2]: GUI");
		StartupDone = true;
		launch(args);
	}

	public void start(Stage primaryStage) {
		// define the window, so we can handle it later
		MainStage = primaryStage;
		// multiple threads possible?
		Platform.setImplicitExit(false);
		// name the window
		primaryStage.setTitle("Homecontrol");
		//No resize for you sir!
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
		Sroot = new Scene(root, 1024, 600);

		// Background
		ImageView imgView = null;
		// Watermark for Testbuild
		if(!Testbuild){
			imgView = new ImageView(new Image("MainBackground.jpg"));
			imgView.setFitWidth(1100);
			imgView.setFitHeight(625);
			root.getChildren().add(imgView);
		}else{
			imgView = new ImageView(new Image("MainBackground.jpg"));
			imgView.setFitWidth(1100);
			imgView.setFitHeight(625);
			root.getChildren().add(imgView);
			ImageView watermark = new ImageView(new Image("watermark.png"));
			watermark.setFitWidth(1100);
			watermark.setFitHeight(625);
			watermark.setOpacity(0.15);
			root.getChildren().add(watermark);
			watermark.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
				@Override
				public void handle(javafx.scene.input.MouseEvent e) {
					Platform.runLater(new Runnable() {
						@Override public void run() {
							FeedReader.RssTextObjectTooltip.setText("");
						}
					});
				}
			});
		}
		imgView.setOnMouseEntered(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent e) {
				Platform.runLater(new Runnable() {
					@Override public void run() {
						FeedReader.RssTextObjectTooltip.setText("");
					}
				});
			}
		});

		System.out.println("Loading: 20%");
		// Refresh timer for anything
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
		town = new Label(Main.City + ", " + Thread_GetWeather.degree + "�C");
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

		for(int i=0; i<10;i++){
			root.getChildren().add(FeedReader.RssTextObject[i]);
		}
		FeedReader.RssTextObjectTooltip.setX(20);
		FeedReader.RssTextObjectTooltip.setY(580);
		root.getChildren().add(FeedReader.RssTextObjectTooltip);

		// <Username>, Logout
		User_Logout = new Label(Main.ActiveUser + ", Logout");
		User_Logout.setLayoutX(880);
		User_Logout.setLayoutY(20);
		User_Logout.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
		User_Logout.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
		User_Logout.getOnMousePressed();
		User_Logout.getOnMouseReleased();
		User_Logout.setFont(Font.font(java.awt.Font.SERIF, 22));
		root.getChildren().add(User_Logout);


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
		Console.setStyle("-fx-background-color: #000000; -fx-text-fill: #9400d3;" );
		Console.setFont(Font.font(java.awt.Font.SERIF, 13));
		root.getChildren().add(Console);

		// Permanently scroll down for new text
		Console.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue,
					Object newValue) {
				Console.setScrollTop(Double.MIN_VALUE);
			}
		});

		System.out.println("Loading: 80%");

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
		if(MPCEnabled){
			Music_Title.setText(currenttitle);
		}else{
			Music_Title.setText("Mpc is disabled.");
		}
		Music_Title.setFont(Font.font(java.awt.Font.SERIF, 14));
		Music_Title.setLayoutX(790);
		Music_Title.setLayoutY(135);
		root.getChildren().add(Music_Title);
		setFontSize();
		
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
				if(!Testbuild && MPCEnabled){
					try {
						Runtime.getRuntime().exec(new String[]{"bash","-c","mpc -h " + MPCServerIP +  " volume " + volume});
					} catch (IOException e) {
						e.printStackTrace();
					}
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
		Music_next.getOnMousePressed();
		Music_next.setLayoutX(970);
		Music_next.setLayoutY(150);
		Music_next.setFitHeight(35);
		Music_next.setFitWidth(35);
		Music_next.setVisible(true);
		root.getChildren().add(Music_next);


		System.out.println("Loading: 100%");
		System.out.println("Launching GUI Now!!!");
		//primaryStage.setScene(Sroot);

		if(StartWithLoginScreen){
			Pane Login = new Pane();

			/*Login_Background = new ImageView(new Image("loginb.gif"));
			Login_Background.setFitHeight(630);
			Login_Background.setFitWidth(1050);
			Login.getChildren().add(Login_Background);*/

			Login_LoginButton1 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton1.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton1.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton1.getOnMouseReleased();
			Login_LoginButton1.getOnMousePressed();
			Login_LoginButton1.setLayoutX(115);
			Login_LoginButton1.setLayoutY(115);
			Login_LoginButton1.setFitHeight(80);
			Login_LoginButton1.setFitWidth(80);
			Login_LoginButton1.setVisible(true);
			Login.getChildren().add(Login_LoginButton1);
			Login_SparkPos[0][0] = Login_LoginButton1.getLayoutX();
			Login_SparkPos[0][1] = Login_LoginButton1.getLayoutY();

			Login_LoginButton2 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton2.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton2.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton2.getOnMouseReleased();
			Login_LoginButton2.getOnMousePressed();
			Login_LoginButton2.setLayoutX(115);
			Login_LoginButton2.setLayoutY(415);
			Login_LoginButton2.setFitHeight(80);
			Login_LoginButton2.setFitWidth(80);
			Login_LoginButton2.setVisible(true);
			Login.getChildren().add(Login_LoginButton2);
			Login_SparkPos[1][0] = Login_LoginButton2.getLayoutX();
			Login_SparkPos[1][1] = Login_LoginButton2.getLayoutY();

			Login_LoginButton3 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton3.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton3.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton3.getOnMouseReleased();
			Login_LoginButton3.getOnMousePressed();
			Login_LoginButton3.setLayoutX(475);
			Login_LoginButton3.setLayoutY(115);
			Login_LoginButton3.setFitHeight(80);
			Login_LoginButton3.setFitWidth(80);
			Login_LoginButton3.setVisible(true);
			Login.getChildren().add(Login_LoginButton3);
			Login_SparkPos[2][0] = Login_LoginButton3.getLayoutX();
			Login_SparkPos[2][1] = Login_LoginButton3.getLayoutY();

			Login_LoginButton4 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton4.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton4.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton4.getOnMouseReleased();
			Login_LoginButton4.getOnMousePressed();
			Login_LoginButton4.setLayoutX(475);
			Login_LoginButton4.setLayoutY(415);
			Login_LoginButton4.setFitHeight(80);
			Login_LoginButton4.setFitWidth(80);
			Login_LoginButton4.setVisible(true);
			Login.getChildren().add(Login_LoginButton4);
			Login_SparkPos[3][0] = Login_LoginButton4.getLayoutX();
			Login_SparkPos[3][1] = Login_LoginButton4.getLayoutY();

			Login_LoginButton5 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton5.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton5.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton5.getOnMouseReleased();
			Login_LoginButton5.getOnMousePressed();
			Login_LoginButton5.setLayoutX(835);
			Login_LoginButton5.setLayoutY(115);
			Login_LoginButton5.setFitHeight(80);
			Login_LoginButton5.setFitWidth(80);
			Login_LoginButton5.setVisible(true);
			Login.getChildren().add(Login_LoginButton5);
			Login_SparkPos[4][0] = Login_LoginButton5.getLayoutX();
			Login_SparkPos[4][1] = Login_LoginButton5.getLayoutY();

			Login_LoginButton6 = new ImageView(new Image("tapbutton.png"));
			Login_LoginButton6.addEventHandler(MouseEvent.MOUSE_RELEASED, new MyEventHandler());
			Login_LoginButton6.addEventHandler(MouseEvent.MOUSE_PRESSED, new MyEventHandler());
			Login_LoginButton6.getOnMouseReleased();
			Login_LoginButton6.getOnMousePressed();
			Login_LoginButton6.setLayoutX(835);
			Login_LoginButton6.setLayoutY(415);
			Login_LoginButton6.setFitHeight(80);
			Login_LoginButton6.setFitWidth(80);
			Login_LoginButton6.setVisible(true);
			Login.getChildren().add(Login_LoginButton6);
			Login_SparkPos[5][0] = Login_LoginButton6.getLayoutX();
			Login_SparkPos[5][1] = Login_LoginButton6.getLayoutY();

			// Set up sparks (flying around the buttons)
			for(int i=0;i<6;i++){
				Login_Spark[i] = new ImageView(new Image("spark.png"));
				Login_Spark[i].setFitHeight(100);
				Login_Spark[i].setFitWidth(100);
				Login.getChildren().add(Login_Spark[i]);
			}


			SLogin = new Scene(Login, 1024, 600);
			primaryStage.setScene(SLogin);
		}else{
			primaryStage.setScene(Sroot);
		}

		primaryStage.show();
		System.out.println("Finished [2]: GUI");
		System.out.println("Starting [3]: Final init");
		FinalInit();
		System.out.println("--- finished loading ---");
	}

	public void FinalInit(){
		// Turn LED's off again
		if(!Testbuild){
			for(int index = PiFaceLed.LED7.getIndex(); index >= PiFaceLed.LED0.getIndex(); index--) {
				piface.getLed(index).off();
			}
		}
		System.out.println("Finished [3]: Final init");
		new ChangeOutStream();
		System.out.println("Stream changed into GUI - now Operating fully in the GUI console. ( only FX Thread )");
	}
	
    private void setFontSize(){
    	Music_Title.setFont(Font.font ("Arial", Music_title_size));
        Music_Title.applyCss();

        double width = Music_Title.getLayoutBounds().getWidth();

        if(width > MUSIC_TITLE_MAX_WIDTH){
            Music_title_size = Music_title_size - 0.25;
            setFontSize();
        }else{
        	Music_title_size = 19;
        }
   
    }


	// Complete handeling for the login screen and the code .. ps: secret code :p
	public static void LoginChecker(Object e){
		if(e == Login_LoginButton1){
			if(Login_LoginButton1_State < 3){
				Login_LoginButton1_State++;
				if(Login_LoginButton1_State == 1){
					Login_Spark[0].setEffect(new Glow(0.33));
				}else if(Login_LoginButton1_State == 2){
					Login_Spark[0].setEffect(new Glow(0.66));
				}else if(Login_LoginButton1_State == 3){
					Login_Spark[0].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton1_State = 0;
				Login_Spark[0].setEffect(new Glow(0));
			}
		}else if(e == Login_LoginButton2){
			if(Login_LoginButton2_State < 3){
				Login_LoginButton2_State++;
				if(Login_LoginButton2_State == 1){
					Login_Spark[1].setEffect(new Glow(0.33));
				}else if(Login_LoginButton2_State == 2){
					Login_Spark[1].setEffect(new Glow(0.66));
				}else if(Login_LoginButton2_State == 3){
					Login_Spark[1].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton2_State = 0;
				Login_Spark[1].setEffect(new Glow(0));
			}
		}else if(e == Login_LoginButton3){
			if(Login_LoginButton3_State < 3){
				Login_LoginButton3_State++;
				if(Login_LoginButton3_State == 1){
					Login_Spark[2].setEffect(new Glow(0.33));
				}else if(Login_LoginButton3_State == 2){
					Login_Spark[2].setEffect(new Glow(0.66));
				}else if(Login_LoginButton3_State == 3){
					Login_Spark[2].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton3_State = 0;
				Login_Spark[2].setEffect(new Glow(0));
			}
		}else if(e == Login_LoginButton4){
			if(Login_LoginButton4_State < 3){
				Login_LoginButton4_State++;
				if(Login_LoginButton4_State == 1){
					Login_Spark[3].setEffect(new Glow(0.33));
				}else if(Login_LoginButton4_State == 2){
					Login_Spark[3].setEffect(new Glow(0.66));
				}else if(Login_LoginButton4_State == 3){
					Login_Spark[3].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton4_State = 0;
				Login_Spark[3].setEffect(new Glow(0));
			}
		}else if(e == Login_LoginButton5){
			if(Login_LoginButton5_State < 3){
				Login_LoginButton5_State++;
				if(Login_LoginButton5_State == 1){
					Login_Spark[4].setEffect(new Glow(0.33));
				}else if(Login_LoginButton5_State == 2){
					Login_Spark[4].setEffect(new Glow(0.66));
				}else if(Login_LoginButton5_State == 3){
					Login_Spark[4].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton5_State = 0;
				Login_Spark[4].setEffect(new Glow(0));
			}
		}else if(e == Login_LoginButton6){
			if(Login_LoginButton6_State < 3){
				Login_LoginButton6_State++;
				if(Login_LoginButton6_State == 1){
					Login_Spark[5].setEffect(new Glow(0.33));
				}else if(Login_LoginButton6_State == 2){
					Login_Spark[5].setEffect(new Glow(0.66));
				}else if(Login_LoginButton6_State == 3){
					Login_Spark[5].setEffect(new Glow(1.0));
				}
			}else{
				Login_LoginButton6_State = 0;
				Login_Spark[5].setEffect(new Glow(0));
			}
		}
		if(Login_LoginButton1_State == 1 && Login_LoginButton4_State == 2 && Login_LoginButton5_State == 1){
			if(Login_LoginButton2_State == 0 && Login_LoginButton3_State == 0 && Login_LoginButton6_State == 0){
				Login_LoginButton1_State = 0;
				Login_LoginButton2_State = 0;
				Login_LoginButton3_State = 0;
				Login_LoginButton4_State = 0;
				Login_LoginButton5_State = 0;
				Login_LoginButton6_State = 0;
				SwitchToMainScene();
			}
		}
	}

	// Let the sparks fly and work the Queues
	protected void update() {
		if(goLeft){
			// starts moving away from the textarea
			if(entrypos > 265){
				entrypos = entrypos - 6;
				for(int i=0;i<10;i++){
					FeedReader.RssTextObject[i].setX(entrypos);
				}
			}else{
				goLeft = false;
				Console.setVisible(true);
				// Hits the left side
			}
		}else if(goRight){
			// Starts moving right towards the textarea
			Console.setVisible(false);
			if(entrypos < 500){
				entrypos = entrypos + 6;
				for(int i=0;i<10;i++){
					FeedReader.RssTextObject[i].setX(entrypos);
				}
			}else{
				goRight = false;
				// hits the right side
			}
		}
		
		if(MainStage.getScene() == SLogin){
			for(int i = 0; i < 6; i++){
				Login_Spark[i].setLayoutX(Login_SparkPos[i][0] + 80*Math.cos(Math.toRadians(Login_SparkSeq[i])));
				Login_Spark[i].setLayoutY(Login_SparkPos[i][1] + 80*Math.sin(Math.toRadians(Login_SparkSeq[i])));
				Login_Spark[i].setRotate(Login_SparkSeq[i]);

				Login_SparkSeq[i] = Login_SparkSeq[i]+3;
				if(Login_SparkSeq[i] > 360){
					Login_SparkSeq[i] = 0;
				}
			}
		}
		// Print queue
		if(todoprint[0] != ""){
			for(int x = todoprintsize; x > -1; x--){
				System.out.println(todoprint[x]);
				todoprint[x] = "";
				todoprintsize--;
			}
		}
		// Cmd queue
		if(todocmd[0] != ""){
			for(int y = todocmdsize; y > -1; y--){
				// USAGE:
				// CMD:-> * On * Off * Toggle *  Add + @
				// Item: -> Full Item name + @
				// List of Items: Light1, Light2, Light3, Console
				// params: -> special things, text
				// example: On@Light1, Toggle@Light2, Toggle@Door1, Add@Console@THIS IS AWESOME AS FUCK!!! <3, -

				String temp[] = todocmd[y].split("@");
				if(temp[0].equals("On")){
					if(temp[1].equals("Light1")){
						SetState(Light1_State1, Light1_State2, Light1_State3, 1);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED7.getIndex()).on();
						}
					}
					if(temp[1].equals("Light2")){
						SetState(Light2_State1, Light2_State2, Light2_State3, 1);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED6.getIndex()).on();
						}
					}
					if(temp[1].equals("Light3")){
						SetState(Light3_State1, Light1_State3, Light1_State3, 1);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED5.getIndex()).on();
						}
					}
				}else if(temp[0].equals("Off")){
					if(temp[1].equals("Light1")){
						SetState(Light1_State1, Light1_State2, Light1_State3, 2);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED7.getIndex()).off();
						}
					}
					if(temp[1].equals("Light2")){
						SetState(Light2_State1, Light2_State2, Light2_State3, 2);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED6.getIndex()).off();
						}
					}
					if(temp[1].equals("Light3")){
						SetState(Light3_State1, Light1_State3, Light1_State3, 2);
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED5.getIndex()).off();
						}
					}
				}else if(temp[0].equals("Toggle")){
					// Todo
				}else if(temp[0].equals("Add")){
					if(temp[1].equals("Console")){
						OtherStuff.addToPrintQueue(temp[2]);
					}
				}else if(temp[0].equals("Set")){
					if(temp[1].equals("Music_Slider")){
						Music_Slider.setValue(Double.parseDouble(temp[2]));
					}else if(temp[1].equals("Music_Title")){
						Music_Title.setText(temp[2]);
						setFontSize();
					}else if(temp[1].equals("RssFeedObject")){
						FeedReader.RssTextObject[Integer.valueOf(temp[2])].setText(temp[3]);
					}else if(temp[1].equals("RssFeedTooltip")){
						FeedReader.RssTextObject[Integer.valueOf(temp[2])].setId(temp[3]);
					}
				}else if(temp[0].equals("Refresh")){
					if(temp[1].equals("WeatherTextLabel")){
						town.setText((Main.City + ", " + Thread_GetWeather.degree + "�C"));
					}else if(temp[1].equals("WeatherIconLabel")){
						weathericonlabel.setGraphic(new ImageView(new Image(Thread_GetWeather.weathericon + ".png")));
					}
				}else if(temp[0].equals("setParams")){
					// setParams@Y@<double>@RssTextObject@1
					//      0    1    2           3       4
					if(temp[1].equals("Y")){
						if(temp[3].equals("RssTextObject")){
							FeedReader.RssTextObject[Integer.valueOf(temp[4])].setY(Double.parseDouble(temp[2]));
						}
					}
				}else{
					System.out.println("ERROR: Thread: Main.update.cmdqueue @ Invalid CMD!");
				}

				todocmd[y] = "";
				todocmdsize--;
			}
		}
		// Get le time
		calendar.setText(OtherStuff.TheNormalTime());	
		if(Thread_GetWeather.weathericon != null && !Weatherinit){
			refreshweather();
			Weatherinit = true;
		}
		try {
			Thread.sleep(33);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Resets the weater, obviously
	public static void refreshweather(){
		OtherStuff.addToPrintQueue("Refreshed the Weather");
		Thread_GetWeather.StartCheck(City);
		OtherStuff.addToCmdQueue("Refresh@WeatherTextLabel");
		OtherStuff.addToCmdQueue("Refresh@WeatherIconLabel");
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

	// Switches from Main to Login Scene
	public static void SwitchToMainScene(){
		/*Login_LoginButton1 = null;
		Login_LoginButton2 = null;
		Login_LoginButton3 = null;
		Login_LoginButton4 = null;
		Login_LoginButton5 = null;
		Login_LoginButton6 = null;
		for(int i = 0; i<6; i++){
			Login_Spark[i] = null;
		}*/
		Login_LoginButton1_State = 0;
		Login_LoginButton2_State = 0;
		Login_LoginButton3_State = 0;
		Login_LoginButton4_State = 0;
		Login_LoginButton5_State = 0;
		Login_LoginButton6_State = 0;
		MainStage.setScene(Sroot);
		//Login_Background = null;
		//SLogin = null;
	}

	public static void SwitchToLoginScene(){
		MainStage.setScene(SLogin);
	}

	// Refresh of music title
	public static void RefreshMpc(){
		try {
			String[] commands = {"bash","-c","mpc -h "+ MPCServerIP};
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(commands);
			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(proc.getErrorStream()));
			String linestring = null;
			int line = 0;
			while ((linestring = stdInput.readLine()) != null) {
				if(line == 0){
					currenttitle = linestring;
					if(Music_Title != null){
						OtherStuff.addToCmdQueue("Set@Music_Title@"+ currenttitle);
					}
				}else if (line == 2){
					volume = linestring;
					String[] temp = volume.split(" ");
					volume = temp[1];
					volume = volume.replace("%", "");
					volume = volume.replace(" ", "");
					if(Music_Slider != null){
						//Music_Slider.setValue(Double.parseDouble(volume));
						OtherStuff.addToCmdQueue("Set@Music_Slider@" + volume);
					}
				}
				line++;
			}
			while ((linestring = stdError.readLine()) != null) {System.out.println(linestring);}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	// EVERY Button EVENT! Release, click, pressed all the cool stuff :p Both login and root scene
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
					if(Light1_State == 0){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED7.getIndex()).on();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 1);
						Light1_State = 1;
					}else if(Light1_State == 1){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED7.getIndex()).off();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 2);
						Light1_State = 2;
					}else if(Light1_State == 2){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED7.getIndex()).on();
						}
						SetState(Light1_State1, Light1_State2, Light1_State3, 1);
						Light1_State = 1;
					}
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light1_Button");
					Light1_Button1.setVisible(false);
					Light1_Button2.setVisible(true);
					Light1_Text.setLayoutX(Light1_Text.getLayoutX()+12);
					Light1_Text.setLayoutY(Light1_Text.getLayoutY()+10);
				}
			}
			else if(e.getSource() == Light1_Lock || e.getSource() == Light1_Lockcross){
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
			else if(e.getSource() == Light2_Button1 || e.getSource() == Light2_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light2_Button");
					Light2_Button2.setVisible(false);
					Light2_Button1.setVisible(true);
					Light2_Text.setLayoutX(Light2_Text.getLayoutX()-12);
					Light2_Text.setLayoutY(Light2_Text.getLayoutY()-10);
					if(Light2_State == 0){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED6.getIndex()).on();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 1);
						Light2_State = 1;
					}else if(Light2_State == 1){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED6.getIndex()).off();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 2);
						Light2_State = 2;
					}else if(Light2_State == 2){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED6.getIndex()).on();
						}
						SetState(Light2_State1, Light2_State2, Light2_State3, 1);
						Light2_State = 1;
					}
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light2_Button");
					Light2_Button1.setVisible(false);
					Light2_Button2.setVisible(true);
					Light2_Text.setLayoutX(Light2_Text.getLayoutX()+12);
					Light2_Text.setLayoutY(Light2_Text.getLayoutY()+10);
				}
			}
			else if(e.getSource() == Light2_Lock || e.getSource() == Light2_Lockcross){
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
			else if(e.getSource() == Light3_Button1 || e.getSource() == Light3_Text){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Light3_Button");
					Light3_Button2.setVisible(false);
					Light3_Button1.setVisible(true);
					Light3_Text.setLayoutX(Light3_Text.getLayoutX()-12);
					Light3_Text.setLayoutY(Light3_Text.getLayoutY()-10);
					if(Light3_State == 0){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED5.getIndex()).on();
						}
						SetState(Light3_State1, Light3_State2, Light3_State3, 1);
						Light3_State = 1;
					}else if(Light3_State == 1){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED5.getIndex()).off();
						}
						SetState(Light3_State1, Light3_State2, Light3_State3, 2);
						Light3_State = 2;
					}else if(Light3_State == 2){
						if(!Testbuild){
							piface.getLed(PiFaceLed.LED5.getIndex()).on();
						}
						SetState(Light3_State1, Light3_State2, Light3_State3, 1);
						Light3_State = 1;
					}
				}else if (e.getEventType() == MouseEvent.MOUSE_PRESSED){
					System.out.println("Pressed Light3_Button");
					Light3_Button1.setVisible(false);
					Light3_Button2.setVisible(true);
					Light3_Text.setLayoutX(Light3_Text.getLayoutX()+12);
					Light3_Text.setLayoutY(Light3_Text.getLayoutY()+10);
				}
			}
			else if(e.getSource() == Light3_Lock || e.getSource() == Light3_Lockcross){
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
			else if(e.getSource() == Music_next){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_next.setOpacity(1);
					if(!Testbuild && MPCEnabled){
						try {
							String[] commands = {"bash","-c","mpc -h "+ MPCServerIP +" next"};
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
					System.out.println("Triggered *Next Title*");
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_next.setOpacity(0.5);
				}
			}
			else if(e.getSource() == Music_prev){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_prev.setOpacity(1);
					if(!Testbuild && MPCEnabled){
						try {
							String[] commands = {"bash","-c","mpc -h "+ MPCServerIP +"  prev"};
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
					System.out.println("Triggered *Prev Title*");
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_prev.setOpacity(0.5);
				}
			}
			else if(e.getSource() == Music_pause){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_pause.setOpacity(1);
					if(!Testbuild && MPCEnabled){
						try {
							String[] commands = {"bash","-c","mpc -h "+ MPCServerIP +" pause"};
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
					System.out.println("Triggered *Pause Music*");
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_pause.setOpacity(0.5);
				}
			}
			else if(e.getSource() == Music_play){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					Music_play.setOpacity(1);
					if(!Testbuild && MPCEnabled){
						try {
							String[] commands = {"bash","-c","mpc -h "+ MPCServerIP +" play"};
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
					System.out.println("Triggered *Play Music*");
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					Music_play.setOpacity(0.5);
				}
			}
			else if(e.getSource() == Console_Button1 || e.getSource() == Console_ButtonText){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					System.out.println("Released & Triggered Console Toggle");
					Console_Button2.setVisible(false);
					Console_Button1.setVisible(true);

					if(Console.isVisible()){
						goRight = true;
						goLeft = false;
					}else if(!Console.isVisible() && goLeft){
						goRight = true;
						goLeft = false;
					}else{
						goRight = false;
						goLeft = true;
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

			// Login Stuff
			else if(e.getSource() == Login_LoginButton1 || e.getSource() == Login_LoginButton2 || e.getSource() == Login_LoginButton3 || e.getSource() == Login_LoginButton4 || e.getSource() == Login_LoginButton5 || e.getSource() == Login_LoginButton6){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					((Node) e.getSource()).setOpacity(1);
					LoginChecker(e.getSource());
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					((Node) e.getSource()).setOpacity(0.5);
				}
			}
			else if(e.getSource() == User_Logout){
				if(e.getEventType() == MouseEvent.MOUSE_RELEASED){
					User_Logout.setLayoutX(User_Logout.getLayoutX()+12);
					User_Logout.setLayoutY(User_Logout.getLayoutY()-10);
					User_Logout.setTextFill(Color.web("#000000"));
					if(StartWithLoginScreen){
						SwitchToLoginScene();
					}
				}else if(e.getEventType() == MouseEvent.MOUSE_PRESSED){
					User_Logout.setLayoutX(User_Logout.getLayoutX()-12);
					User_Logout.setLayoutY(User_Logout.getLayoutY()+10);
					User_Logout.setTextFill(Color.web("#FF0000"));
				}
			}
		}
	}
}

