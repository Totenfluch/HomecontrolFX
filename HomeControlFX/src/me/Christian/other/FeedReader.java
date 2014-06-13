package me.Christian.other;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReader {
	public static String[][][] Feeds = new String[10][10][3];
	//				[Feed][Entry count][data]
	//					   Entry count -> position in feed | 0=newest, 10=oldest
	//					   data -> 0: Titel, 1: Description, 2: link
	public static int FeedCounter = 0;
	public static void GetFeed(String feedurl) {
		boolean ok = false;
		try {
			URL feedUrl = new URL(feedurl);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			List<?> temp = feed.getEntries();


			for(int i=0; i<temp.size(); i++){
				if(i<10){
					String[] tempentry = temp.get(i).toString().split("\\r?\\n");
					for(int b=0;b<tempentry.length; b++){
						if(tempentry[b].contains("SyndEntryImpl.title=")){
							String[] tempstring = tempentry[b].split("=");
							Feeds[FeedCounter][i][0] = tempstring[1];
						}else if(tempentry[b].contains("SyndEntryImpl.description.value=")){
							String[] tempstring = tempentry[b].split("=");
							Feeds[FeedCounter][i][1] = tempstring[1];
						}else if(tempentry[b].contains("SyndEntryImpl.uri=")){
							String[] tempstring = tempentry[b].split("=");
							Feeds[FeedCounter][i][2] = tempstring[1];
						}
					}
				}else{
					i=temp.size();
				}
			}
			
			// Print out for testing purposes
			/*for(int i = 0; i<10; i++){
				System.out.println(Feeds[FeedCounter][i][0]);
				System.out.println(Feeds[FeedCounter][i][1]);
				System.out.println(Feeds[FeedCounter][i][2]);
				System.out.println("");
			}*/
			
			FeedCounter++;
			System.out.println("Feed " + FeedCounter + " loaded.");
			ok = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: "+ex.getMessage());
		}

		if (!ok) {
			System.out.println("FeedReader:: Feed:" + FeedCounter + " is INVALID!");
		}
	}
	
	public static void ReadFeedFile(){
		File file = new File("RSSFeeds.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
				System.out.println("Creating Empty RSSFeeds file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Path filePath = new File("RSSFeeds.txt").toPath();
		Charset charset = Charset.defaultCharset();        
		List<String> stringList = null;
		try {
			stringList = Files.readAllLines(filePath, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] stringArray = stringList.toArray(new String[]{});
		if(stringArray.length > 10){
			System.out.println("RSS File is larger than 10 Feeds. This Programm is only reading the first 10.");
		}
		for(int m = 0; m<stringArray.length;m++){
			if(!stringArray[m].equals("") && !stringArray[m].equals(" ")){
				if(stringArray[m].contains("rss") || stringArray[m].contains("xml") || stringArray[m].contains("atom")){
					if(m<10){
						GetFeed(stringArray[m]);
					}
				}else{
					System.out.println("Feed *" + stringArray[m] + "* is INVALID! exchange it.");
				}
			}
		}
		System.out.println("Loaded " + stringArray.length + " Rss Feed(s).");
	}

}