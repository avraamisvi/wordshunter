package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.Scene;

public class StartScreen extends Scene {

	private int maxTime = 3000;
	private int past;
	private boolean change = false;
	private RectF rect;
	
	@Override
	public void initialize() {
		
		int w = Engine.get().getDisplay().getWidth();
		int h = Engine.get().getDisplay().getHeight();
		
		rect = new RectF(w*.25f, h-h*.20f, w-w*.25f, (h-h*.20f)+h*.05f);
	}
	
	float perc;
	@Override
	public void updateBehaviour(long delay) throws Exception {
			past += delay;
			
			if(past > maxTime && !change) {
				change = true;
				Engine.get().changeScene(SceneFactory.getMenuScreen(Engine.get().getResources()));
			}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	protected Paint getPaint() {
		return null;
	}
}
