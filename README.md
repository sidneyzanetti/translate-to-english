Translate to English and Say it!
==============

This project is just a proof of concept to test	[Text to Speech](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/text-to-speech.html), [Language Translation](http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/language-translation.html) and
[Cloudant](https://cloudant.com/) from IBM Watson Developer Cloud on Bluemix.

## How it works

If you submit a text in any language, this project will try to detect the language of this text and try to translate it to english. Then, you can listen to this translation.


[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/sidneyzanetti/translate-to-english)

## Check it out
http://talk-test.mybluemix.net/

## Running the Application in Bluemix using Eclipse

1. Download and install [IBM Eclipse Tools for Bluemix](https://developer.ibm.com/wasdev/downloads/#asset/tools-IBM_Eclipse_Tools_for_Bluemix).
2. In the Servers view of Eclipse, right-click to create a new IBM Bluemix server. Follow the steps in the wizard.
3. Import this project into Eclipse.
4. Deploy the app into Bluemix server. Right click on the *TalkTestApp* project and select *Run As -> Run on Server* option. Find and select the Bluemix server and press *Finish*. 
