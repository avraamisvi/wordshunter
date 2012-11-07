package br.com.pyx.games.words.hunter.core.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.MotionEvent;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.R;
import br.com.pyx.games.words.hunter.core.Factory;
import br.com.pyx.games.words.hunter.core.Scene;
import br.com.pyx.games.words.hunter.core.StaticSprite;

public class MenuScreen extends Scene implements OnCompletionListener{

	PainelInicial painel;
	private Paint backg;
	private LinearGradient gradient;
	Bitmap back;
	
	public MenuScreen() {
		
		float w = Engine.get().getDisplay().getWidth();
		float h = Engine.get().getDisplay().getHeight();
		
		createLayer();
		createLayer();
		
		StaticSprite stp = new StaticSprite("IMG_FUNDO");
		stp.setImage(BitmapFactory.decodeResource(Factory.get().getRes(), R.drawable.panel_inicial));
		
		painel = new PainelInicial();
//		painel.setX((w-480)/2);
		add(painel, 1);
		add(stp, 0);
		
		gradient = new LinearGradient(0, 0, w, h, Color.rgb(42, 127, 255), Color.rgb(255, 255, 255), TileMode.REPEAT);
		backg = new Paint();
		backg.setShader(gradient);		
	
		stp.setY((h-stp.getH())/2);
		stp.setX((w-stp.getW())/2);
		
		isLevel = false;
	}

	@Override
	public void initialize() {
		Engine.get().startMusic();
	}
	
	@Override
	protected Paint getPaint() {
		return backg;
	}

//	@Override
//	public void draw(Canvas canvas) {
//
//		super.draw(canvas);		
//	}
	
	@Override
	public void updateBehaviour(long delay) throws Exception {

	}

	@Override
	public void onBack() {
//		Engine.get().finish();
		System.exit(0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {

			float x = event.getX(), y = event.getY();
			painel.touchReleased((int) x, (int) y);

		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {

			float x = event.getX(), y = event.getY();
			painel.touchPressed((int) x, (int) y);
		}

		return true;
	}

	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}
}
