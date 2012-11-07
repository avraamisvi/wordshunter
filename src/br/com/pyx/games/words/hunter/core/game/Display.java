package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.SceneObject;

public class Display extends SceneObject {

	private Rect bounds;
	Paint paintText;
	String text = new String();
	String bonus = new String();
	boolean wordExists = false;
	int r,g,b;
	
	
	public Display() {
		super("Display", "Display");
		
		paintText = new Paint();
		paintText.setColor(Color.BLACK);
		paintText.setTypeface(SceneFactory.getSystemFont());
		paintText.setTextSize(getFontSize());
		paintText.setAntiAlias(true);		
	}	
	
	public int getFontSize() {
		return 16;
	}
	
	public void drawTexts(Canvas canvas) {		
		
		if(text !=null && text.length()>0) {
			
			Rect bounds = new Rect();
			getTextPaint().getTextBounds(text, 0, 1, bounds);
			
			float dy = bounds.height()+((getH()-bounds.height())/2);
			
			canvas.drawText(text, (getX()+bounds.width()/2)+getH()/3, getY()+dy, getTextPaint());	
		}
	}

	private Paint getTextPaint() {
		return paintText;
	}
	
	public void setText(String text) {
		
		if (!this.text.equals(text)) {
			Engine.get().getWordsDatabase().existsWord(text, this);
		}
		
		this.text = text;
		
//		bounds = new Rect();
//		
//		paintText.getTextBounds(text, 0, text.length(), bounds);
//		
//		setH(bounds.height());
//		setW(bounds.width());
	}
	
	long timeLast = 0;
	public void setWordExists(boolean wordExists, long time) {
		
		if(timeLast == 0 || time > timeLast) {
			timeLast = time;
			this.wordExists = wordExists;
			if(wordExists) {
				r = 0;
				g = 0;
				b = 241;
			} else {
				r = 0;
				g = 0;
				b = 0;
			}
			
			getTextPaint().setARGB(255, r, g, b);
		}
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		drawTexts(canvas);
	}
	
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	
	public String getBonus() {
		return bonus;
	}
	
	public boolean isBonusWord() {
		return bonus!=null&&bonus.length()>0&&text!=null&&bonus.equals(text);
	}
	
	public int getBasicPoints() {

		int pts = 0;
		int basic = 20;
		for(int i = 0; i < text.length(); i++) {
			switch(text.charAt(i)) {
			 	case 'D': pts+=basic -  4; break;
				case 'E': pts+=basic -  9; break;
				case 'F': pts+=basic -  1; break;
				case 'G': pts+=basic -  1; break;
				case 'A': pts+=basic -  14; break;
				case 'B': pts+=basic -  1; break;
				case 'C': pts+=basic -  5; break;
				case 'L': pts+=basic -  4; break;
				case 'M': pts+=basic -  3; break;
				case 'N': pts+=basic -  5; break;
				case 'O': pts+=basic -  10; break;
				case 'H': pts+=basic -  1; break;
				case 'I': pts+=basic -  9; break;
				case 'J': pts+=basic -  1; break;
				case 'K': pts+=basic -  1; break;
				case 'U': pts+=basic -  2; break;
				case 'T': pts+=basic -  5; break;
				case 'W': pts+=basic -  1; break;
				case 'V': pts+=basic -  1; break;
				case 'Q': pts+=basic -  1; break;
				case 'P': pts+=basic -  2; break;
				case 'S': pts+=basic -  4; break;
				case 'R': pts+=basic -  9; break;			
			}
		}
		
		return pts;
	}
	
}
