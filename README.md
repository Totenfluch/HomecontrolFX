# HomecontrolFX
Compile:
    To compile this project you need:
    
    IDE
        - Eclipse (preferable Kepler)
    LIBARYS
        - Feed4J 1.0
        - jDom 1.1.3
        - mysql-connector-java-5.1.1.8
        - rome 1.0
        - commons-codec-1.8
        - pi4j 1.0
        (packed here: https://www.dropbox.com/s/p32k2h7dgc4k1tz/dist.zip)
        
    What is mainly needed on the pi
        - Java 8 ( SE & SDK )
        
    

Private Key

    - obtainable thru login
    * /login <Username> <Password>
    private key will be returned in a reply message and is active until server restart

Commands available thru socker server:

    ~ ID can only be between 0 and 7 - rID can be 0 to 9 - Text can be anything but "@"
    Cmd Structure /AuthAction <Private Key> ....@....@...@...
    
    * On
        * Output
            * <ID>
    * Off
        * Output
            * <ID>
    * Toggle
        * Output
            * <ID>
    * Add
        * Console
            * <TEXT>
    * Set
        * Music_Slider
            * <DOUBLE VALUE>
        * Music_Title
            * <TEXT>
        * RssFeedObject
            * <rID>
                * <TEXT>
        * RssFeedTooltip
            * <rID>
                * <TEXT>
    * Refresh
        * WeatherTextLabel
        * WeatherIconLabel
    * setParams
        * Y
            * <DOUBLE VALUE>
                * RssTextObject
                    * <rID>
    * Lock
    	* <ID>
   	* Unlock
   		* <ID>
   	* Music
   		* prev
   		* next
   		* pause
   		* play
    * lock_all
    * unlock_all
    * enable_all
    * disable_all
    * register
    	* <MasterPw>
    		* <Username>
    			* <Password>
    				* <Flags> 
    					* <Permissions>
    * removeuser
    	* <MaserPw>
    		* <username>
    * refreshdb
    	* <MasterPw>
    
    
EXAMPLES:

    - On@Output@0
    - Off@Output@7
    - Toggle@Output@6
    - Add@Console@This Is Cool Text_Stuff
    - Set@Music_Slider@31.8
    - Set@Music_Title@Bangeranz Skriller
    - Refresh@WeatherTextLabel
    - setParams@Y@90@RssTextObject@1
    - Lock@2
    - Unlock@7
    - Music@play
    - Music@pause
    - Music@prev
    - lock all
    - disable all
    - register@thisiscool@Adam@eva1@15:a@1337
    - removeuser@Adam
    - refreshdb
    
