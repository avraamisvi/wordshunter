package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import br.com.pyx.games.words.hunter.core.StaticSprite;

public class NewPoint extends StaticSprite {

	long movementCarryOverY, targetMovementAmountY; 
	int pixelsToMoveY;

	long movementCarryOverX, targetMovementAmountX; 
	int pixelsToMoveX;
	private int vx, vy;
	private int rvx, rvy;
	private int accx = 20;
	private int accy = 20;
	
	private String point;
	private int r, g, b;
	
	private Paint paintText = new Paint();
	private Paint p;
	private long pass;
	
	private static long idPoints = 0;
	
	public NewPoint(int vx, int vy, String point, int r, int g, int b) {
		
		super("NewPoint"+idPoints);
		setCanColide(false);
		
		this.r = r; 
		this.g = g;
		this.b = b;
		
		this.vx = vx; 
		this.vy = vy;
		
		accx = vx/10;
		accy = vy/10;		
		p = new Paint();
		this.point = point;
		
		paintText.setColor(Color.WHITE);
		paintText.setTypeface(SceneFactory.getSystemFont());
		paintText.setTextSize(48);
		paintText.setAntiAlias(true);
	}

	int alpha = 255;
	public void drawNumber(Canvas c, int x, int y) {
		
		Utils.drawText(x, y, point, paintText, c, r, g, b, alpha);
	}
	
	@Override
	public void updateBehaviour(long delay) throws Exception {		
		updateMovimento(delay);
		
		pass += delay;
		
		if(pass >= 100) {
			if(alpha>0) {
				int d = alpha-25;
				alpha = d>=0?d:0;
			} else {
				this.setRemove(true);
			}
			pass = 0;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {		
		drawNumber(canvas, (int)getX(), (int)getY());				
	}
	
	public void updateMovimento(long delay) {
		targetMovementAmountY = (long) ((delay * rvy) + movementCarryOverY);
		pixelsToMoveY = (int) (targetMovementAmountY / 1000);
		movementCarryOverY = (int) (targetMovementAmountY % 1000);
		if (movementCarryOverY > 500) {
			movementCarryOverY = movementCarryOverY + 1000;
			pixelsToMoveY++;
		}
	
		setY(getY()+(pixelsToMoveY));
		
		targetMovementAmountX = (long) ((delay * rvx) + movementCarryOverX);
		pixelsToMoveX = (int) (targetMovementAmountX / 1000);
		movementCarryOverX = (int) (targetMovementAmountX % 1000);
		if (movementCarryOverX > 500) {
			movementCarryOverX = movementCarryOverX + 1000;
			pixelsToMoveX++;
		}		

		setX(getX()+(pixelsToMoveX));
		
		
		rvx = accx + rvx;
		rvy = accy + rvy;
		
		if(Math.abs(rvx) > Math.abs(vx)) {
			rvx = vx;
		}
		
		if(Math.abs(rvy) > Math.abs(vy)) {
			rvy = vy;
		}		
	}	
}
