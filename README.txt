README.txt
==========

Philosophy:
===========

Anti Ad-Blocking is a way to, combat those who downloads a Android application that is free and comes with adverts, to use the app without paying for the premium Ad-Free version, thus 
depriving the developer of said app of their income, however small it may be, a contribution would be appreciated.

The Anti-AdBlocking custom control is a banner that sits at the bottom of the screen, and will check for connectivity to the internet. 

Depending on the XML attribute, the developer can choose to end the application if there's no connectivity.

Usage:
======

1. Import the projects AntiAdBlock, AntiAdBlockTest, into Eclipse and be sure to adjust your publisher ID by replacing the string <YOUR_ADMOB_UNIT_ID_GOES_HERE> found in 
res/layout/anti_ad_block_control.xml in the AntiAdBlock project source.
2. Build it, now that's done and is treated as a library. So its ready to be dropped into your primary app that is to be targetted for the play store. Look at AntiAdBlockTest demo source.
3. In the AntiAdBlockTest demo app, ensure the AndroidManifest's has the identical publisher ID.
4. Examine the layout in res/layout/activity_main.xml

<t0mm13b.antiAdBlock.AntiAdBlockControl
        android:id="@+id/antiAdBlockCtl"
        style="@style/myAntiAdBlockStyle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true" />

This will drop a small control 50dp high on the bottom of the screen. The style layout is defined as 

   <style name="myAntiAdBlockStyle">
        <item name="adblockShadowColor">@color/Crimson</item>
        <item name="adblockShadowDx">2</item>
        <item name="adblockShadowDy">2</item>
        <item name="adblockShadowRadius">2</item>
        <!-- Comment the above out, plain text shows minus shadow! -->
        <item name="adblockTextColor">@color/yellow</item>
        <item name="adblockTextSize">20sp</item>
        <item name="adblockDetectedMsg">@string/adblockingsw_detected</item>
        <item name="adblockNoConnectivityMsg">@string/adblockingsw_noconnectivity</item>
        <item name="adblockTriggerEvents">true</item>
    </style>

Of course, you are free to change the text size, color. If any of the shadow's xml attributes are omitted, it falls back on default font face. The @color attribute can be found in the project AntiAdBlockTest demo.

Build your app. And watch, when connectivity is off, a message corresponding to the XML attribute adblockingsw_noconnectivity will be shown and triggers a Alert dialog box, indicating that
network connectivity is off and ends (this is optional, but is there for the purpose of the demo). 

When conenctivity is present, and if adblock is in effect, a scrolling marquee appears in place of the AdMob itself, with a gentle reminder. Otherwise, usual AdMob advert is in place. :

That's all you have to do. No more, no less. 

Feel free to push requests for enhancements, post issues on github's project page if you may have any :)

Cheers!
