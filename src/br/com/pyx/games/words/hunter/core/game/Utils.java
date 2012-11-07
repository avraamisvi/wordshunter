package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import br.com.pyx.games.words.hunter.Engine;

public class Utils {
	
	public static void drawCircle(float x, float y, float r, Paint p, Canvas canvas) {
		if(Engine.get().isDebug()) {
			canvas.drawCircle(x, y, r, p);
		}
	}	
	
	public static float toRadians(float degrees) {
		return degrees * 0.0174532925f;
	}

	public static void drawText(int x, int y, String text, Paint paintText, Canvas canvas, int r, int g , int b, int a) {
		paintText.setColor(Color.BLACK);
		paintText.setAlpha(a);
		paintText.setStyle(Style.FILL_AND_STROKE);
		paintText.setStrokeWidth(2);
		
		canvas.drawText(text, x, y, paintText);
		
		paintText.setColor(Color.rgb(r,g,b));
		paintText.setStyle(Style.FILL);
		paintText.setAlpha(a);
		paintText.setStrokeWidth(0);
		
		canvas.drawText(text, x, y, paintText);		
	}
	
	public static void drawText(int x, int y, String text, Paint paintText, Canvas canvas, int r, int g , int b) {
		drawText(x, y, text, paintText, canvas, r, g, b, 255);
	}	
}
