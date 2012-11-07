package br.com.pyx.games.words.hunter.core.game;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import br.com.pyx.games.words.hunter.R;
import br.com.pyx.games.words.hunter.core.Factory;
import br.com.pyx.games.words.hunter.core.StaticSprite;

public class LevelUpText extends StaticSprite {

	private int alpha = 0;
	private boolean show = false;
	private int delayAlpha;
	private boolean ini = false;
	
	public LevelUpText(Resources res) {
		super("LevelUp");
		
		setImage(Factory.get().levelUp);
		paint = new Paint();
		paint.setAlpha(0);
	}

	
	@Override
	public void updateBehaviour(long delay) throws Exception {
		if(!ini) {
			ini = true;
			setX(getScene().getView().getWidth()/2 - getW()/2);
			setY(getScene().getView().getHeight()/2 - getH()/2);
		}
		
		
		if(show) {
			if(alpha < 255) {
				delayAlpha += delay;
				
				if(delayAlpha > 150) {
					delayAlpha = 0;
					alpha += 51;
				}
				
				if(alpha > 255) {
					alpha = 255;
				}
				
				paint.setAlpha(alpha);
			} else {
				delayAlpha += delay;
				
				if(delayAlpha > 2000) {
					show = false;
				}
			}
		} else if(alpha > 0) {
			
			delayAlpha += delay;
			
			if(delayAlpha > 150) {
				delayAlpha = 0;
				alpha -= 60;
			}
			
			if(alpha < 0) {
				alpha = 0;
			}
			
			paint.setAlpha(alpha);
		}
	}
	
	public void show() {
		this.show = true;
	}
	
	public void hide() {
		this.show = false;
		alpha = 0;
		paint.setAlpha(alpha);
	}
	
	public boolean isShow() {
		return show;
	}
}
