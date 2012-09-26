/*
 * ScrollingMarquee.java
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

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

// Got the answer about the scrolling and enhanced it a bit by http://stackoverflow.com/1827751
public class ScrollingMarquee extends TextView{
	private boolean _blnSelected = false;
	public ScrollingMarquee(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ScrollingMarquee(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ScrollingMarquee(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect prevFocusdRect){
		if (focused || _blnSelected){
			super.onFocusChanged(focused, direction, prevFocusdRect);
		}
	}
	@Override
	public void onWindowFocusChanged(boolean focused){
		if (focused  || _blnSelected){
			super.onWindowFocusChanged(focused);
		}
	}
	@Override
	public boolean isFocused(){
		return _blnSelected;
	}
	
	@Override
	public void setSelected(boolean blnSelectd){
		super.setSelected(blnSelectd);
		_blnSelected = blnSelectd;
	}
}
