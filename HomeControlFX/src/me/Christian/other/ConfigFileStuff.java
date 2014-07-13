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
				prop.setProperty("Version", "3");
				prop.setProperty("Is_Testbuild", "false");
				prop.setProperty("Mpc_enabled", "true");
				prop.setProperty("Mpc_refresh_delay_ms", "2000");
				prop.setProperty("Mpc_ServerIp", "192.168.11.205");
				prop.setProperty("Internal_server_port", "9977");
				prop.setProperty("Start_with_login_screen", "true");
				prop.setProperty("Weather_City", "Schweinfurt");
				prop.setProperty("Weather_refresh_delay", "600000");
				prop.setProperty("Rss_enabled", "true");
				prop.setProperty("Rss_refresh_delay_ms", "850000");
				prop.setProperty("Dev_prompt_enabled", "true");
				prop.setProperty("Dev_console_enabled", "true");
				prop.setProperty("Pi_Build", "true");
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
				Main.RssEnabled = Boolean.valueOf(prop.getProperty("Rss_enabled"));
				Main.RssRefreshDelay = Integer.valueOf(prop.getProperty("Rss_refresh_delay_ms"));
				Main.dev_promt_enabled = Boolean.valueOf(prop.getProperty("Dev_prompt_enabled"));
				Main.dev_console_enabled = Boolean.valueOf(prop.getProperty("Dev_console_enabled"));
				Main.rcfgVersion = Integer.valueOf(prop.getProperty("Version"));
				if(Main.rcfgVersion >= 3){
					Main.PiBuild = Boolean.valueOf(prop.getProperty("Pi_Build"));
				}
				
		 
			} catch (IOException ex) {
				System.out.println("Error in config File! Deleting old one, creating new one.");
				file.delete();
				startup();
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
	
	public static void customNameStartup(){
		Properties prop = new Properties();
		File file = new File("NameConfig.properties");
		OutputStream output = null;
		// Check if file exists
		if(!file.exists()){
			try {
				file.createNewFile();
				output = new FileOutputStream("NameConfig.properties");

				// set the properties value
				prop.setProperty("Version", "1");
				prop.setProperty("Output0_Name", "Licht I");
				prop.setProperty("Output1_Name", "Licht II");
				prop.setProperty("Output2_Name", "Licht III");
				prop.setProperty("Output3_Name", "Tür I");
				prop.setProperty("Output4_Name", "Tür II");
				prop.setProperty("Output5_Name", "Gerät I");
				prop.setProperty("Output6_Name", "Gerät II");
				prop.setProperty("Output7_Name", "Gerät III");
				prop.setProperty("Head0_Name", "Lichtsteuerung");
				prop.setProperty("Head1_Name", "Türsteuerung");
				prop.setProperty("Head2_Name", "Gerätesteuerung");
				// Add more if needed, but make sure to read them afterwards otherwise they are useless
				for(int i=0; i<8; i++){
					Main.Output_Name[i] = "OUTPUT "+i;
				}
				for(int i=0;i<3; i++){
					Main.Head_Name[i] = "HEAD "+i;
				}
				
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
				input = new FileInputStream("NameConfig.properties");
				
				// load a properties file
				prop.load(input);
		 
				// get the property value and stores it in the variable of the class Main
				Main.Output_Name[0] = prop.getProperty("Output0_Name");
				Main.Output_Name[1] = prop.getProperty("Output1_Name");
				Main.Output_Name[2] = prop.getProperty("Output2_Name");
				Main.Output_Name[3] = prop.getProperty("Output3_Name");
				Main.Output_Name[4] = prop.getProperty("Output4_Name");
				Main.Output_Name[5] = prop.getProperty("Output5_Name");
				Main.Output_Name[6] = prop.getProperty("Output6_Name");
				Main.Output_Name[7] = prop.getProperty("Output7_Name");
				Main.Head_Name[0] = prop.getProperty("Head0_Name");
				Main.Head_Name[1] = prop.getProperty("Head1_Name");
				Main.Head_Name[2] = prop.getProperty("Head2_Name");
		 
			} catch (IOException ex) {
				System.out.println("Error in config File! Deleting old one, creating new one.");
				file.delete();
				startup();
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
	
	public static void setupMySQL(){
		Properties prop = new Properties();
		File file = new File("MySQLconfig.properties");
		OutputStream output = null;
		// Check if file exists
		if(!file.exists()){
			try {
				file.createNewFile();
				output = new FileOutputStream("MySQLconfig.properties");

				// set the properties value
				prop.setProperty("Use_MySQL", "false");
				prop.setProperty("MySQL_IP", "<IP of the Server>");
				prop.setProperty("MySQL_User", "<MySQL user>");
				prop.setProperty("MySQL_Pass", "<MySQL pass>");
				prop.setProperty("MySQL_direction", "<e.g. (127.0.0.1)/*yourdirection*>");
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
				input = new FileInputStream("MySQLconfig.properties");
				
				// load a properties file
				prop.load(input);
		 
				// get the property value and stores it in the variable of the class Main
				MySQL.MySQL_enabled = Boolean.valueOf(prop.getProperty("Use_MySQL"));
				MySQL.MySQL_IP = prop.getProperty("MySQL_IP");
				MySQL.MySQL_dir = prop.getProperty("MySQL_direction");
				MySQL.MySQL_User = prop.getProperty("MySQL_User");
				MySQL.MySQL_Pass = prop.getProperty("MySQL_Pass");
		 
			} catch (IOException ex) {
				System.out.println("Error in config File! Deleting old one, creating new one.");
				file.delete();
				startup();
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
