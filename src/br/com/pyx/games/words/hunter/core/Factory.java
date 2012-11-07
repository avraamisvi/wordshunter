package br.com.pyx.games.words.hunter.core;

import br.com.pyx.games.words.hunter.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Factory {

	private static Factory resourceFactory = new Factory();
	private Resources res;
	
	
	public Bitmap menuScenaBack; 
	public Bitmap levelUp; 
	
	public void load() {
		menuScenaBack =  BitmapFactory.decodeResource(res, R.drawable.menu_panel);
		levelUp = BitmapFactory.decodeResource(res, R.drawable.level_up);
	}
	
	public static Factory get() {
		return resourceFactory;
	}
	
	public void setRes(Resources res) {
		this.res = res;
	}
	
	public Resources getRes() {
		return res;
	}
}
