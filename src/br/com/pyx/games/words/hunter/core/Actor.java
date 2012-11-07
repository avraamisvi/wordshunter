package br.com.pyx.games.words.hunter.core;

import java.util.HashMap;

import br.com.pyx.games.words.hunter.Engine;

import android.graphics.Canvas;
import android.graphics.Paint;



public class Actor extends SceneObject {
	
	float velX, VelY;
	private float maxY;
	
	public Actor(String clazz, String id) {
		super(clazz, id);
	}

	HashMap<String, Animation> animations = new HashMap<String, Animation>();
	
	Animation animation;
	
	public Animation getAnimation(String anim) {
		return animations.get(anim);
	}
	
	public void addAnimation(Animation animation) {
		animations.put(animation.getName(), animation);
	}
	
	public void setAnimation(String name) {
		animation = animations.get(name);
		animation.start();
	}

	public Animation getAnimation() {
		return animation;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		animation.draw(canvas, getX(), getY());

		if(Engine.get().isDebug()) { 
			Paint p = new Paint();
			p.setARGB(255, 255, 115, 165);
		
			canvas.drawText(" MV:" + targetMovementAmount + " CO:" + movementCarryOver + " PX:" + pixelsToMove, getX()+50, getY(), p);
		}
		
	}
	
	long movementCarryOver, targetMovementAmount; 
	int pixelsToMove;
	
	public void updateBehaviour(long delay) throws Exception {
		animation.update(delay);
		
		targetMovementAmount = (long) ((delay * getVelY()) + movementCarryOver);
		pixelsToMove = (int) (targetMovementAmount / 1000);
		movementCarryOver = (int) (targetMovementAmount % 1000);
		if (movementCarryOver > 500) {
			// round up and carry over a debt.
			if(movementCarryOver < 0)
				movementCarryOver = movementCarryOver + 1000;
			else
				movementCarryOver = movementCarryOver - 1000;
			
			pixelsToMove++;
		}
		
		if(getY()+pixelsToMove > maxY && maxY > 0)
			setY(maxY);
		else
			setY(getY()+pixelsToMove);
	}

	public void colisionBehaviour(Actor target, long delay) throws Exception {
		
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return VelY;
	}

	public void setVelY(float velY) {
		VelY = velY;
	}
	
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	
}
