package br.com.pyx.games.words.hunter.core.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.Factory;
import br.com.pyx.games.words.hunter.core.SceneObject;

public class MenuBlockScene extends SceneObject {

	private static final long VELOY = 150;
	private boolean ini;
	private Button exit, restart;
	private Bitmap backImage;
	
	public MenuBlockScene(Resources res) {
		super("Menu", "Menu");
		
		exit = new Button("[Exit]");
		restart = new Button("[Restart]");
		
		backImage = Factory.get().menuScenaBack;
		
		setH(backImage.getHeight());
		setW(backImage.getWidth());
	
	}

	public void init() {
		setX(this.getScene().getView().getWidth()/2 - (getW())/2);
		setY(this.getScene().getView().getHeight()+10);
		
		exit.setPos(getX(), getY());
		restart.setPos(getX(), getY()+exit.bounds.height());
	}
	
	
	@Override
	public void updateBehaviour(long delay) throws Exception {
		
		if(!ini) {
			ini = true;
			init();
		}				
		
		if(((BlockScreen)getScene()).isShowMenu()) {
			if(!isOnTop()) {
				calcPixelsToMove(delay);				
				setY(getY()-pixelsToMove);				
			} else if(isOnTop()) {
				setY(this.getScene().getView().getHeight()-getH());
			}
		} else if(!isOnBottom()) {
			calcPixelsToMove(delay);			
			setY(getY()+pixelsToMove);
		}
		
		updateButtons();
		
	}
	
	long movementCarryOver, targetMovementAmount; 
	int pixelsToMove;

	private void calcPixelsToMove(long delay) {
		
		targetMovementAmount = (long) ((delay * VELOY) + movementCarryOver);
		pixelsToMove = (int) (targetMovementAmount / 1000);
		movementCarryOver = (int) (targetMovementAmount % 1000);
		if (movementCarryOver > 500) {
			// round up and carry over a debt.
			movementCarryOver = movementCarryOver + 1000;				
			pixelsToMove++;
		}
	}
	
	
	
	
	public boolean onTouchEvent(float x, float y) {
		
		BlockScreen scene = (BlockScreen) getScene();
		
		if(exit.getRectF().contains(x, y)) {
			
			Engine.get().getDictionary().setLevelPoints(Engine.get().getLevel(), (int) scene.score.getScore());
			
			//System.exit(0);
		} else if(restart.getRectF().contains(x, y)) {
			scene.onBack();
			scene.restart();
		} else {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void draw(Canvas canvas) {		
		
		if(((BlockScreen)getScene()).isShowMenu()) {
			exit.draw(canvas);
			restart.draw(canvas);
			canvas.drawBitmap(backImage, getX(), getY(), null);
		}
	}
	
	
	public void updateButtons() {
		exit.y = getY();
		restart.y = getY();
	}
	
	public boolean isOnTop() {
		return getY() <= this.getScene().getView().getHeight() - getH();	
	}
	
	public boolean isOnBottom() {
		return getY() > this.getScene().getView().getHeight();	
	}
	
	class Button {
		
		private String text;
		private float x, y;
		private Rect bounds; 
		private Paint paintText; 
		
		public Button(String text) {
			this.text = text;
			paintText = new Paint();
			
			paintText.setColor(Color.BLACK);
			paintText.setTypeface(SceneFactory.getSystemFont());
			paintText.setTextSize(16);
			paintText.setAntiAlias(true);
			
			bounds = new Rect();
			
			paintText.getTextBounds(text, 0, text.length(), bounds);
		}
		
		void setPos(float x, float y) {			
			this.x = x;
			this.y = y;
		}
		
		
		void draw(Canvas canvas) {
			Utils.drawText((int)x, (int)y, text, paintText, canvas, 0, 0, 0);		
		}
		
		public RectF getRectF() {
			return new RectF(x,y,bounds.width()+x,bounds.height()+y);
		}
	}
}
