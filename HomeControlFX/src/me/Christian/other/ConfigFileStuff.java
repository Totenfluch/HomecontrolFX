package me.Christian.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import me.Christian.pack.Main;

public class ConfigFileStuff {
	public static void startup(){
		Properties prop = new Properties();
		File file = new File("config.properties");
		OutputStream output = null;
		// Check if file exists
		if(!file.exists()){
			try {
				file.createNewFile();
				output = new FileOutputStream("config.properties");

				// set the properties value
				prop.setProperty("Is_Testbuild", "false");
				prop.setProperty("Mpc_enabled", "true");
				prop.setProperty("Mpc_refresh_delay_ms", "2000");
				prop.setProperty("Mpc_ServerIp", "192.168.11.205");
				prop.setProperty("Internal_server_port", "9977");
				prop.setProperty("Start_with_login_screen", "true");
				prop.setProperty("Weather_City", "Schweinfurt");
				prop.setProperty("Weather_refresh_delay", "600000");
				// Add more if needed, but make sure to read them afterwards otherwise they are useless
				

				// save properties to project root folder
				prop.store(output, null);

			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		// File exists ~ Read it!
		else{
			InputStream input = null;
			try {
				input = new FileInputStream("config.properties");
		 
				// load a properties file
				prop.load(input);
		 
				// get the property value and stores it in the variable of the class Main
				Main.Testbuild = Boolean.valueOf(prop.getProperty("Is_Testbuild"));
				Main.MPCEnabled = Boolean.valueOf(prop.getProperty("Mpc_enabled"));
				Main.MpcRefreshDelay = Integer.parseInt(prop.getProperty("Mpc_refresh_delay_ms"));
				Main.MPCServerIP = prop.getProperty("Mpc_ServerIp");
				Main.portz = Integer.parseInt(prop.getProperty("Internal_server_port"));
				Main.StartWithLoginScreen = Boolean.valueOf(prop.getProperty("Start_with_login_screen"));
				Main.City = prop.getProperty("Weather_City");
				Main.WeatherRefreshDelay = Integer.valueOf(prop.getProperty("Weather_refresh_delay"));
				
		 
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
