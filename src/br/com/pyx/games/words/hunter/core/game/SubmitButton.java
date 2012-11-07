package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.Actor;
import br.com.pyx.games.words.hunter.core.SceneObject;

public class SubmitButton extends SceneObject {

	private Paint paint;
	private float TEXT_SIZE = 20;
	private String submeter = "[SUBMETER]";
	private String submit = "[SUBMIT]";
	private Rect bounds = new Rect();
	private String text = submit;
	private int r, g, b;
	
	public SubmitButton() {
		super("Button", "SubmitButton");
		
		paint = new Paint();
		
		paint.setColor(Color.BLACK);
		paint.setTypeface(SceneFactory.getSystemFont());
		paint.setTextSize(32);
		paint.setAntiAlias(true);
		
		if(Engine.get().isEnglish()) {
			text = submit;			
		} else {
			text = submeter;
		}
		
		paint.getTextBounds(text, 0, submit.length(), bounds);
		
		setW(bounds.width());
		setH(bounds.height());
		
	}


	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
//		Rect bounds = new Rect();
//		if(Engine.get().isEnglish()) {
//			paint.getTextBounds(submit, 0, 1, bounds);
//		} else {
//			paint.getTextBounds(submeter, 0, 1, bounds);
//		}
//		
//		float dy = bounds.height()+((getH()-bounds.height())/2);
//		
//		if(Engine.get().isEnglish()) {
//			canvas.drawText(submit, getX()+(getW()/2), getY()+dy, paint);
//		} else {
//			canvas.drawText(submeter, getX()+(getW()/2), getY()+dy, paint);			
//		}
		
		Utils.drawText((int)getX(), (int)(getY()+getH()), text, paint, canvas, r, g, b);
	}
	
	public void select() {
		b = 255;
	}
	
	public void diSelect() {
		b = 0;
	}
	
	@Override
	public void updateBehaviour(long delay) throws Exception {
	}
}
