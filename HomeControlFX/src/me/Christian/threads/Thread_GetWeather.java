package me.Christian.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Thread_GetWeather implements Runnable {
	
	public static String City;
	public static int degree;
	public static String weathericon; 
	
	public void run() {
		URL url = null;
		try {
			url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + City + "&mode=xml&units=metric");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

		    for (String line; (line = reader.readLine()) != null;) {
		        System.out.println(line);
		        if(line.contains("temperature")){
		        	String temp;
		        	temp = line;
		        	temp = temp.substring(22, 26);
		        	Double tempdouble = Double.parseDouble(temp);
		        	tempdouble = tempdouble + 0.5;
		        	tempdouble = Math.floor(tempdouble);
		        	int tempint = tempdouble.intValue();
		        	degree = tempint;
		        }
		        if(line.contains("weather number")){
		        	String halfturn;
		        	String[] temp = line.split("icon=");
		        	System.out.println(temp[0]);
		        	System.out.println(temp[1]);
		        	String r1 = "\"\\";
		        	r1 = r1.replace("\\", "");
		        	temp[1] = temp[1].replace(r1, "");
		        	halfturn = temp[1].replace("/>", "");
		        	System.out.println(halfturn);
		        	weathericon = halfturn;
		        }
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    if (reader != null) try { reader.close(); } catch (IOException ignore) {}
		}
		
	}
	
	public static void StartCheck(String tempCity){
		City = tempCity;
	    (new Thread(new Thread_GetWeather())).start();
	}
}