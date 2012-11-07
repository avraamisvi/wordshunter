package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.Factory;
import br.com.pyx.games.words.hunter.core.StaticSprite;

public class PainelInicial extends StaticSprite {

	Button btnPlay;
	Paint debug = new Paint();
	
	public PainelInicial() {
		super("PainelInicial");
		
		int xh = Engine.get().getDisplay().getWidth()/2;
		int yh = Engine.get().getDisplay().getHeight()/2;
		
		btnPlay = new Button("[Play]");
		
		
		setY(yh);
		setX(xh);
		
		btnPlay.setPos(xh-btnPlay.getWidth()/2, getY());
		
		debug.setColor(Color.GREEN);
	}
	
	@Override
	public void draw(Canvas canvas) {		
				
		//canvas.drawRect(btnPlay.getRectF(), debug);
		btnPlay.draw(canvas);		
	}	
	
	
	public void touchPressed(int x, int y) {
		
		RectF r1 = btnPlay.getRectF();		
		
		if(r1.contains(x, y)) {
			btnPlay.select();
		} else {
			btnPlay.diSelect();
		}		
	}	
	
	public void touchReleased(int x, int y) {
		
		RectF r1 = btnPlay.getRectF();
		
		if(r1.contains(x, y)) {
			Engine.get().changeScene(SceneFactory.getBlockScreen(Factory.get().getRes()));
		} else {
			btnPlay.diSelect();
		}
	}
	
	class Button {
		
		private String text;
		private float x, y;
		private Rect bounds; 
		private Paint paintText; 
		private boolean select;
		int r, g, b;
		
		public Button(String text) {
			this.text = text;
			paintText = new Paint();
			
			paintText.setColor(Color.BLACK);
			paintText.setTypeface(SceneFactory.getSystemFont());
			paintText.setTextSize(Engine.get().getDisplay().getHeight()/16);
			paintText.setTextAlign(Paint.Align.LEFT);
			paintText.setAntiAlias(true);
			
			bounds = new Rect();
			
			paintText.getTextBounds(text, 0, text.length(), bounds);
		}
		
		void setPos(float x, float y) {			
			this.x = x;
			this.y = y;
		}
		
		public void select() {
			r = 0;
			g = 0;
			b = 255;
		}

		public void diSelect() {
			r = 0;
			g = 0;
			b = 0;
		}
		
		void draw(Canvas canvas) {
			Utils.drawText((int)x, (int)y+bounds.height(), text, paintText, canvas, r, g, b);		
		}
		
		public RectF getRectF() {
			return new RectF(x,y,bounds.width()+x,bounds.height()+y);
		}
		
		public int getHeight() {
			return bounds.height();
		}
		
		public int getWidth() {
			return bounds.width();
		}
	}	
}
