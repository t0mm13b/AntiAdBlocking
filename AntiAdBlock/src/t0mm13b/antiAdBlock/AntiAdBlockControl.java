/*
 * AntiAdBlockControl.java
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

import t0mm13b.antiAdBlock.R;

import com.google.ads.AdView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * A Custom user control that implements a mechanism for ad-blocking and handling.
 * Usage of ViewFlipper comes into play here, first layout is the actual admob control, the other is the textview, which 
 * is a scrolling marquee, so let's annoy those that pilfer our app :P
 * 
 * The adMob user control is positioned as a BANNER, and placed at the bottom of the screen!
 * 
 * If ad-blocking is detected, the view flipper flips to the scrolling marquee layout and post a message.... :D
 * 
 * @author t0mm13b
 *
 */
public class AntiAdBlockControl extends RelativeLayout{
	private static final String TAG = "AntiAdBlockControl";
	private static final boolean D = false;
	//
	private static final int VIEW_ADVERT = 0; 
	private static final int VIEW_NOADVERT = 1;
	//
	private ViewFlipper _vwFlipper;
	private AdView _ourAdView;
	private ScrollingMarquee _txtView;
	//
	private String _sNoNetworkMsg = "";
	private String _sAdblockDetectedMsg = "";
	//
	private boolean _blnFireEvents = false;
	//
	private IAntiAdBlock _listener = null;
	//
	private Context _cntxt;
	
	public AntiAdBlockControl(Context context) {
		super(context);
		this._cntxt = context;
		initView();
	}

	public AntiAdBlockControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._cntxt = context;
		
		initView();
		initAttrs(attrs);
	}

	public AntiAdBlockControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this._cntxt = context;
		initView();
		initAttrs(attrs);
	}
	
	@Override
	protected void onFinishInflate(){
		super.onFinishInflate();
	}
	
	/**
	 * We need a way to clean up once activity goes out of scope so we detach it and null-ify listeners!
	 */
	@Override
	protected void onDetachedFromWindow(){
		if (D) Log.d(TAG, "onDetachedFromWindow(...) *** ENTER ***");
		this._ourAdView.setAdListener(null);
		this._listener = null;
		super.onDetachedFromWindow();
		if (D) Log.d(TAG, "onDetachedFromWindow(...) *** LEAVE ***");
	}
	
	/**
	 * An optional call, the receiving activity *must* implement IAntiAdBlock interface to receive the events! 
	 * @param listener of type IAntiAdBlock interface
	 */
	public void setAntiAdBlockListener(IAntiAdBlock listener){
		this._listener = listener;
	}
	
	/**
	 * If the attribute 'adblockTriggerEvents' is not set in the activity's layout, allow the programmer to set it here.
	 * Default is false!
	 * 
	 * @param blnFireEvents of type Boolean
	 */
	public void setAntiAdBlockTriggerEvents(boolean blnFireEvents){
		this._blnFireEvents = blnFireEvents;
	}
	
	/**
	 * If the attribute 'adblockMsg' is not set in this control on the activity's layout, allow the programmer to set it here 
	 * or it falls back on the default string "Ad-Blocking detected....Ad-Blocking detected....Ad-Blocking detected...."
	 * 
	 * @param sAdblockDetectedMsg - a long string (preferably) for the scrolling marquee to work! 
	 */
	public void setAntiAdBlockMessageDetected(String sAdblockDetectedMsg){
		if (sAdblockDetectedMsg != null && sAdblockDetectedMsg.length() > 0) this._sAdblockDetectedMsg = sAdblockDetectedMsg;
	}
	/**
	 * If the attribute 'adblockNoNet' is not set in this control on the activity's layout, allow the programmer to set it here.
	 * or it falls back on the default string "No network detected....No network detected....No network detected...."
	 * 
	 * @param sNoNetworkMsg - a long string (preferably) for the scrolling marquee to work!
	 */
	public void setAntiAdBlockNoNetworkMessage(String sNoNetworkMsg){
		if (sNoNetworkMsg != null && sNoNetworkMsg.length() > 0) this._sNoNetworkMsg = sNoNetworkMsg;
		
	}
	/**
	 * This is the meat of it all, we pull in the attributes as defined in attrs.xml and apply them here! :)
	 * @param attrs
	 */
	private void initAttrs(AttributeSet attrs){
		if (D) Log.d(TAG, "initAttrs(...) *** ENTER ***");
		TypedArray a = this._cntxt.obtainStyledAttributes(attrs, R.styleable.antiAdBlock);
		if (a != null){
			String sAdblockDetectedMsg = a.getString(R.styleable.antiAdBlock_adblockDetectedMsg);
			if (D) Log.d(TAG, "initAttrs(...) sAdblockDetectedMsg = " + sAdblockDetectedMsg);
			if (sAdblockDetectedMsg != null && sAdblockDetectedMsg.length() > 0) this._sAdblockDetectedMsg = sAdblockDetectedMsg;
			else this._sAdblockDetectedMsg = this._cntxt.getString(R.string.adblockingsw_detected_default);
			String sNoNetworkMsg = a.getString(R.styleable.antiAdBlock_adblockNoConnectivityMsg);
			if (D) Log.d(TAG, "initAttrs(...) sNoNetworkMsg = " + sNoNetworkMsg);
			if (sNoNetworkMsg != null && sNoNetworkMsg.length() > 0) this._sNoNetworkMsg = sNoNetworkMsg;
			else this._sNoNetworkMsg = this._cntxt.getString(R.string.adblockingsw_nonetwork_default);
			this._blnFireEvents = a.getBoolean(R.styleable.antiAdBlock_adblockTriggerEvents, false);
			//
			int aTextColor = a.getColor(R.styleable.antiAdBlock_adblockTextColor, R.color.Crimson);
			int aTextSize = a.getDimensionPixelSize(R.styleable.antiAdBlock_adblockTextSize, 22);
			//
			boolean blnHasShadow = false;
			if (a.hasValue(R.styleable.antiAdBlock_adblockShadowColor) ||
				a.hasValue(R.styleable.antiAdBlock_adblockShadowRadius) ||
				a.hasValue(R.styleable.antiAdBlock_adblockShadowDx) ||
				a.hasValue(R.styleable.antiAdBlock_adblockShadowDy)) blnHasShadow = true;
			float aShadowRadius = a.getFloat(R.styleable.antiAdBlock_adblockShadowRadius, 1.00F);
			float aShadowDx = a.getFloat(R.styleable.antiAdBlock_adblockShadowDx, 2.0F);
			float aShadowDy = a.getFloat(R.styleable.antiAdBlock_adblockShadowDy, 2.0F);
			int aShadowColor = a.getColor(R.styleable.antiAdBlock_adblockShadowColor, R.color.Aqua);
			//
			if (blnHasShadow) this._txtView.setShadowLayer(aShadowRadius, aShadowDx, aShadowDy, aShadowColor);
			//
			this._txtView.setTextColor(aTextColor);
			this._txtView.setTextSize(aTextSize);
		}
		a.recycle();
		if (D) Log.d(TAG, "initAttrs(...) *** LEAVE ***");
	}
	/**
	 * This is where the view gets setup *AFTER* onFinishInflate.
	 */
	private void initView(){
		LayoutInflater inflater = LayoutInflater.from(this._cntxt);
		inflater.inflate(R.layout.anti_ad_block_control, this, true);
		this._ourAdView = (AdView)findViewById(R.id.ad);
		this._vwFlipper = (ViewFlipper)findViewById(R.id.viewAdMobFlipper);
		this._txtView = (ScrollingMarquee)findViewById(R.id.noAdMobText);
		if (this._txtView != null){
			this._txtView.setText(this._sAdblockDetectedMsg);
		}
		if (this._vwFlipper != null){
			// Are we in Eclipse Editor mode? If commented out - Eclipse whines... meh!
			if (!isInEditMode()){
				this._vwFlipper.setDisplayedChild(VIEW_ADVERT);
			}
		}
		if (this._ourAdView != null){
			// Are we in Eclipse Editor mode? If commented out - Eclipse whines... meh!
			if (!isInEditMode()){
				this._ourAdView.setAdListener(new AntiAdBlock(this._cntxt){
	
					@Override
					public void onAntiAdBlock() {
						if (D) Log.d(TAG, "onAntiAdBlock(...) *** ENTER ***");
						_vwFlipper.setDisplayedChild(VIEW_NOADVERT);
						_txtView.setText(_sAdblockDetectedMsg);
						_txtView.setSelected(true);
						if (_blnFireEvents){ 
							if (_listener != null) _listener.cbOnAdBlockFound();
						}
						if (D) Log.d(TAG, "onAntiAdBlock(...) *** LEAVE ***");
					}
	
					@Override
					public void onNoConnectivity() {
						if (D) Log.d(TAG, "onNoConnectivity(...) *** ENTER ***");
						_vwFlipper.setDisplayedChild(VIEW_NOADVERT);
						_txtView.setText(_sNoNetworkMsg);
						_txtView.setSelected(true);
						if (_blnFireEvents){
							if (_listener != null) _listener.cbOnNoConnectivityFound();
						}
						if (D) Log.d(TAG, "onNoConnectivity(...) *** LEAVE ***");
					}
	
					@Override
					public void onAdPassThrough() {
						if (D) Log.d(TAG, "onAdPassThrough(...) *** ENTER ***");
						_txtView.setSelected(false);
						_vwFlipper.setDisplayedChild(VIEW_ADVERT);
						if (D) Log.d(TAG, "onAdPassThrough(...) *** LEAVE ***");
					}
					
				});
			}
		}
	}
}
