package me.Christian.other;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;








import me.Christian.pack.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;







import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReader {
	public static String[][][] Feeds = new String[10][10][3];
	public static String[][] checkedFeeds = new String[10][3];
	public static int[] split;
	//				[Feed][Entry count][data]
	//					   Entry count -> position in feed | 0=newest, 10=oldest
	//					   data -> 0: Titel, 1: Description, 2: link
	public static int FeedCounter = 0;
	public static Text[] RssTextObject = new Text[10];
	public static Text RssTextObjectTooltip = new Text("");
	public static int transint;
	public static double[] transdouble = new double[10];
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

			FeedCounter++;
			if(!Main.StartupDone){
				System.out.println("Feed " + FeedCounter + " loaded.");
			}
			ok = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: "+ex.getMessage());
		}

		if (!ok) {
			System.out.println("FeedReader:: Feed: " + FeedCounter + " is INVALID!");
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
				if(stringArray[m].contains("rss") || stringArray[m].contains("xml") || stringArray[m].contains("atom") || stringArray[m].contains("RSS") || stringArray[m].contains("XML") || stringArray[m].contains("ATOM")){
					if(m<10){
						GetFeed(stringArray[m]);
					}
				}else{
					System.out.println("Feed *" + stringArray[m] + "* is INVALID! exchange it. (RSS, XML & Atom only and link must contain atleast one)");
				}
			}
		}
		if(!Main.StartupDone){
			System.out.println("Loaded " + stringArray.length + " Rss Feed(s).");
		}
	}

	public static void CreateFeedObjects(){
		int maxy = 145;
		int difnext = -1;
		if(FeedCounter ==0){return;}
		if(FeedCounter == 0){split = new int[]{0};}
		if(FeedCounter == 1){split = new int[]{10};}
		if(FeedCounter == 2){split = new int[]{5,5};}
		if(FeedCounter == 3){split = new int[]{4,3,3};}
		if(FeedCounter == 4){split = new int[]{3,3,2,2};}
		if(FeedCounter == 5){split = new int[]{2,2,2,2,2};}
		if(FeedCounter == 6){split = new int[]{2,2,2,2,1,1};}
		if(FeedCounter == 7){split = new int[]{2,2,2,1,1,1,1};}
		if(FeedCounter == 8){split = new int[]{2,2,1,1,1,1,1,1};}
		if(FeedCounter == 9){split = new int[]{2,1,1,1,1,1,1,1,1};}
		if(FeedCounter == 10){split = new int[]{1,1,1,1,1,1,1,1,1,1};}

		int z = 0;
		for(int w = 0; w<split.length; w++){
			for(int q = 0; q<split[w]; q++){
				checkedFeeds[z][0] = Feeds[w][q][0];
				checkedFeeds[z][1] = Feeds[w][q][1];
				checkedFeeds[z][2] = Feeds[w][q][2];
				z++;
			}
		}

		for(int i = 0; i < 10; i++) {
			if(!Main.StartupDone){
				RssTextObject[i] = new Text();
				RssTextObject[i].prefWidth(100);
				RssTextObject[i].setX(265);
			}
			double n = maxy;
			if(difnext == 0){
				n = maxy+17;
				maxy = maxy+17;
			}else if(difnext == 1){
				n = maxy+37;
				maxy = maxy+37;
			}else if(difnext == 2){
				n = maxy+57;
				maxy = maxy+57;
			}
			transdouble[i] = n;
			transint = i;


			if(Main.StartupDone){
				OtherStuff.addToCmdQueue("setParams@Y@"+ transdouble[i] +"@RssTextObject@"+ i);
			}else{
				RssTextObject[transint].setY(transdouble[transint]);
			}


			String temp = checkedFeeds[i][0];
			StringBuilder ctemp = new StringBuilder(temp);
			int csize1 = 30;
			if(ctemp.length() > csize1){
				int x = csize1;
				boolean sw = false;
				char pos = ctemp.charAt(30);
				while(pos != ' ' && !sw){
					pos = ctemp.charAt(x);
					x++;
					if(x > ctemp.length()-2){
						sw = true;
					}
				}
				ctemp.insert(x, "\n");
				difnext = 1;

				int csize2 = 65;
				if(ctemp.length() > csize2){
					x = csize2;
					char pos2 = ctemp.charAt(csize2);
					boolean sd = false;
					while(pos2 != ' ' && !sd){
						pos2 = ctemp.charAt(x);
						x++;
						if(pos2 == ' '){
							ctemp.insert(x, "\n");
							difnext = 2;
						}
						if(x > ctemp.length()-2){
							sd = true;
						}
					}
				}
			}else{
				difnext = 0;
			}
			if(transdouble[i] < 510){
				if(Main.StartupDone){
					OtherStuff.addToCmdQueue("Set@RssFeedObject@"+transint+"@"+ctemp.toString());
					OtherStuff.addToCmdQueue("Set@RssFeedTooltip@"+transint+"@"+checkedFeeds[transint][1]);
				}else{
					RssTextObject[transint].setText(ctemp.toString());
					RssTextObject[transint].setId(checkedFeeds[transint][1]);
				}
			}else{
				if(Main.StartupDone){
					OtherStuff.addToCmdQueue("Set@RssFeedObject@"+transint+"@...");
					OtherStuff.addToCmdQueue("Set@RssFeedTooltip@"+transint+"@...");
				}else{
					RssTextObject[transint].setText("...");
					RssTextObject[transint].setId("...");
				}
			}

			if(!Main.StartupDone){
				RssTextObject[i].setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent e) {
						boolean back = false;
						String[] temp = e.getTarget().toString().split(",");
						String sp = temp[0].replace("Text[id=", "");
						StringBuilder sptemp = new StringBuilder(sp);
						int csize3 = 125;
						if(sptemp.length() > csize3){
							char pos3 = sptemp.charAt(csize3);
							int x = csize3;
							while(pos3 != ' ' && x < sptemp.length()-1){
								pos3 = sptemp.charAt(x);
								if(pos3 == ' '){
									sptemp.insert(x, "\n");
								}
								if(x < 130 && !back){
									x++;
								}else{
									back = true;
									x--;
								}
							}

						}
						Platform.runLater(new Runnable() {
							@Override public void run() {
								FeedReader.RssTextObjectTooltip.setFont(Font.font("Futura", FontWeight.BOLD, 16));
								RssTextObjectTooltip.setText(sptemp.toString());
								Main.isClicked = true;
							}
						});
					}
				});
			}
		}
		FeedCounter = 0;
	}


}