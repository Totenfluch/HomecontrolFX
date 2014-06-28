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
    * lock all
    * unlock all
    * enable all
    * disable all
    
    
    
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
    - lock all
    - disable all
    