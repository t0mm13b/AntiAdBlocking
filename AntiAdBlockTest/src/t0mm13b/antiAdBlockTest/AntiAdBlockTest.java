/*
 * AntiAdBlockTest.java
 * This file is part of AntiAdBlockTest
 *
 * Copyright (C) 2012 - Tom Brennan.
 *
 * AntiAdBlockTest is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * AntiAdBlockTest is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with <program name>. If not, see <http://www.gnu.org/licenses/>.
 */
package t0mm13b.antiAdBlockTest;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import t0mm13b.antiAdBlock.IAntiAdBlock;
import t0mm13b.antiAdBlockTest.R;

public class AntiAdBlockTest extends Activity implements IAntiAdBlock{
	private static final String TAG = "AntiAdBlockTest";
	private t0mm13b.antiAdBlock.AntiAdBlockControl _antiAdBlockCtl;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _antiAdBlockCtl = (t0mm13b.antiAdBlock.AntiAdBlockControl)findViewById(R.id.antiAdBlockCtl);
        if (_antiAdBlockCtl != null){
        	_antiAdBlockCtl.setAntiAdBlockListener(this);
        }
	}
	
	@Override
	public void cbOnAdBlockFound() {
		Log.d(TAG, "cbOnAdBlockFound() - Y U USE ADBLOCK U SELFISH PIG!");
		/*AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setMessage(R.string.adblock_detected_message)
			.setTitle(R.string.adblock_detected_title)
			.setPositiveButton("Ok", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AntiAdBlockTest.this.finish();
				}
				
			});
		AlertDialog yoDlg = dlg.create();
		yoDlg.show();
*/		
	}
	@Override
	public void cbOnNoConnectivityFound() {
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setMessage(R.string.nonet_detected_message)
			.setTitle(R.string.nonet_detected_title)
			.setPositiveButton("Ok", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AntiAdBlockTest.this.finish();
				}
				
			});
		AlertDialog yoDlg = dlg.create();
		yoDlg.show();
	}
}
