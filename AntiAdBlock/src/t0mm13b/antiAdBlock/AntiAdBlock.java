/*
 * AntiAdBlock.java
 * This file is part of AntiAdBlock
 *
 * Copyright (C) 2012 - Tom Brennan.
 *
 * AntiAdBlock is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * AntiAdBlock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with <program name>. If not, see <http://www.gnu.org/licenses/>.
 */
package t0mm13b.antiAdBlock;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;

/**
 * An abstract class that performs a number of checks - looks for the /etc/hosts file and flags if its in there,
 * also, it checks on the connectivity of the handset.
 * 
 * @author t0mm13b
 *
 */
public abstract class AntiAdBlock implements AdListener{
	private static final String TAG = "AntiAdBlock";
	private static final boolean D = false;
	//
	private static final String HOSTS = "/etc/hosts";
	private static final String ADMOB = "admob";
	private static final String CONN_WIFI = "WIFI";
	private static final String CONN_MOBI = "MOBILE";
	//
	private boolean _blnHasAdBlock = false;
	//
	private Context _context;
	//
	public abstract void onAntiAdBlock(); // If the hosts contains adblock clues, fire this method, switch the viewflipper layout!
	public abstract void onAdPassThrough(); // If no adblock, fire this one! This will reset the viewflipper control!
	public abstract void onNoConnectivity();  // No net... fire this, will trigger switching of viewflipper!
	
	public AntiAdBlock(Context ctxt){
		this._context = ctxt;
	}
	/**
	 * Are we online or not? Fear not, let's check and scram!
	 *  
	 * @return true if online else false.
	 */
	private boolean getCheckIfOnLine() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) this._context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase(CONN_WIFI))
	            if (ni.isConnected()) haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase(CONN_MOBI))
	            if (ni.isConnected()) haveConnectedMobile = true;
	    }
	    if(!haveConnectedWifi && !haveConnectedMobile) return false;
	    return true;
	}
	/**
	 * Is ad-blocking in place? Let's check hosts file and determine!
	 *  
	 * @return true if is present else false.
	 */
	private boolean isAdBlockerPresent() {
		// _blnHasAdBlock is set to false initially, because once, if its found, the flag is set - this is to prevent
		// multiple times for this method to be called! Only read the hosts once and then forget it! :)
		if (!_blnHasAdBlock){
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(HOSTS)));
				String line;
				while ((line = in.readLine()) != null){
					if (line.contains(ADMOB)){
						_blnHasAdBlock = true;
						return true;
					}
				}
			}catch(Exception e){
				
			}finally{
				if (in != null){
					try{
						in.close();
					}catch(IOException e){
						
					}
				}
			}
		}else{
			if (_blnHasAdBlock) return true;
		}
		return false;
    }
	
	@Override
	public void onDismissScreen(Ad argAd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedToReceiveAd(Ad argAd, ErrorCode argErrorCode) {
		if (D) Log.d(TAG, "onFailedToReceiveAd(...) *** ENTER ***");
		// TODO Auto-generated method stub
		if (argErrorCode == ErrorCode.NETWORK_ERROR){
			if (D) Log.d(TAG, "onFailedToReceiveAd(...) - if (argErrorCode == ErrorCode.NETWORK_ERROR){ *** HERE *** }");
			if (getCheckIfOnLine()){
				if (D) Log.d(TAG, "onFailedToReceiveAd(...) - if (getCheckIfOnLine()){ *** HERE *** }else{}");
				if (isAdBlockerPresent()){
					if (D) Log.d(TAG, "onFailedToReceiveAd(...) - if (isAdBlockerPresent(true)){ *** HERE *** }");
					onAntiAdBlock();
				}		
			}else{
				if (D) Log.d(TAG, "onFailedToReceiveAd(...) - if (getCheckIfOnLine()){}else{ *** HERE *** }");
				onNoConnectivity();
			}
		}
		if (D) Log.d(TAG, "onFailedToReceiveAd(...) *** LEAVE ***");
	}

	@Override
	public void onLeaveApplication(Ad argAd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPresentScreen(Ad argAd) {
		// TODO Auto-generated method stub
		onAdPassThrough();
	}

	@Override
	public void onReceiveAd(Ad argAd) {
		// TODO Auto-generated method stub
		onAdPassThrough();
	}
	
}
