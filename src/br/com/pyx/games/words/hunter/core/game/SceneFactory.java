package br.com.pyx.games.words.hunter.core.game;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.R;
import br.com.pyx.games.words.hunter.core.Actor;
import br.com.pyx.games.words.hunter.core.Animation;
import br.com.pyx.games.words.hunter.core.Scene;
import br.com.pyx.games.words.hunter.core.StaticSprite;
import br.com.pyx.games.words.hunter.core.Timer;


public class SceneFactory {

	static Bitmap bricBmp, submitBmp, displayBmp, explosionBmp;
	static Random random = new Random();
	static Random letras = new Random();
	static long bricsCreated = 0;
	
	public static Bitmap upBmp, downBmp, leftBmp, rightBmp;
	public static Bitmap rightUpBmp, rightDownBmp, leftUpBmp, leftDownBmp;
	
	
	public static final int LH = 1;
	public static final int NH = 2;
	public static final int SS = 3;
	public static final int RR = 4;
	public static final int CH = 5;
	public static final int GU = 6;  
	public static final int SC = 7; 
	public static final int SCEDIL = 8;
	public static final int XC = 9;
	public static final int maxY = 525; 
	
	public static char lastChar = 0;
	public static int explosiveBricsCount = 0;
	
	enum ArrowType{
		up, down, 
		left, right,
		rightUp, rightDown, 
		leftUp, leftDown;	
	};
	
	static char []letters;
	
	private static HashMap<String, Integer> letrasCounter = new HashMap<String, Integer>();
	
	private static HashMap<String, Integer> letrasMax = new HashMap<String, Integer>();
	
	public static Typeface systemFont;
	
	public static Typeface getSystemFont() {
		
		if(systemFont == null)
			systemFont = Typeface.createFromAsset(Engine.get().getResources().getAssets(), "fonts/Chalkduster.ttf");
		
		return systemFont;
	}
	
	public static void iniCharsEnglish() {
		letrasCounter = new HashMap<String, Integer>();
		letrasMax = new HashMap<String, Integer>();
		
		letters = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'L', 'M', 'N', 'O', 'P','E', 'Q', 'R', 'S', 'T', 'U','I',
				'V', 'I','A', 'X', 'Z', 'U', 'A', 'B', 'O', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'L', 'M', 'E', 'N', 'O', 'P', 'Q', 'A', 'R', 'U', 'S', 'T', 'U',
				'V', 'X', 'Z','O','1','2','3','A','E','I','O','U','Y','Y','W'};//,'4','5','6','7','9'
		letrasMax.put("D", 3);
		letrasMax.put("E", 11);
//		letrasMax.put("F", 1);
		letrasMax.put("G", 2);
		letrasMax.put("A", 8);
//		letrasMax.put("B", 1);
		letrasMax.put("C", 4);
		letrasMax.put("L", 5);
		letrasMax.put("M", 2);
		letrasMax.put("N", 7);
		letrasMax.put("O", 6);
		letrasMax.put("H", 2);
		letrasMax.put("I", 8);
//		letrasMax.put("J", 1);
//		letrasMax.put("K", 1);
		letrasMax.put("U", 3);
		letrasMax.put("T", 7);
//		letrasMax.put("W", 1);
//		letrasMax.put("V", 1);
//		letrasMax.put("Q", 1);
		letrasMax.put("P", 3);
		letrasMax.put("S", 6);
		letrasMax.put("R", 7);
		letrasMax.put("Y", 2);
//		letrasMax.put("X", 1);
//		letrasMax.put("Z", 1);	
		
	}
	
	public static void iniCharsPortuguese() {
		letrasCounter = new HashMap<String, Integer>();
		letrasMax = new HashMap<String, Integer>();
		
		letters = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'L', 'M', 'N', 'O', 'P','E', 'Q', 'R', 'S', 'T', 'U','I',
				'V', 'I','A', 'X', 'Z', 'U', 'A', 'B', 'O', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'L', 'M', 'E', 'N', 'O', 'P', 'Q', 'A', 'R', 'U', 'S', 'T', 'U',
				'V', 'X', 'Z','O','1','2','3','4','5','6','7','9','A','E','I','O','U'};//, '‚'
		letrasMax.put("D", 4);
		letrasMax.put("E", 9);
//		letrasMax.put("F", 1);
//		letrasMax.put("G", 1);
		letrasMax.put("A", 14);
//		letrasMax.put("B", 1);
		letrasMax.put("C", 5);
		letrasMax.put("L", 4);
		letrasMax.put("M", 3);
		letrasMax.put("N", 5);
		letrasMax.put("O", 10);
//		letrasMax.put("H",  1);
		letrasMax.put("I", 9);
//		letrasMax.put("J", 1);
//		letrasMax.put("K", 1);
		letrasMax.put("U", 2);
		letrasMax.put("T", 5);
//		letrasMax.put("W", 1);
//		letrasMax.put("V", 1);
//		letrasMax.put("Q", 1);
		letrasMax.put("P", 2);
		letrasMax.put("S", 4);
		letrasMax.put("R", 9);
//		letrasMax.put("Y", 1);
//		letrasMax.put("X", 1);
//		letrasMax.put("Z", 1);	
	}
	
	public static void initChars() {
		
		if(Engine.get().isEnglish()) {
			iniCharsEnglish();
		}else {
			iniCharsPortuguese();
		}
	}
	
	public static int getMaxy() {
		return maxY;
	}
	
	public static Scene getMenuScreen(Resources res) {
		MenuScreen menuScreen = new MenuScreen();
		menuScreen.setBackgroundColor(Color.rgb(135, 135, 222));
		
		return menuScreen;
	}

	public static boolean achivedMax(String letter) {
		Integer c = letrasCounter.get(letter);
		Integer m = letrasMax.get(letter);
		boolean ret = false;
		
		if(c != null && c.intValue() > 0) {
			if(m == null) {
				ret = true;
			} else if(m != null){
				ret =  c.intValue() >= m.intValue();
			}
		}
		
//		//Log.w("WORDHUNTER" , "Letra:" + letter + " T:" + ret);
		
		return ret;
	}
	
	public static void removeLetter(String l) {
		Integer c = letrasCounter.get(l);
		
		if(c != null) {
			letrasCounter.put(l, new Integer(c-1));
		}
		
		//Log.w("WORDHUNTER" , "remove:Letra:" + l);
	}
	
	public static void addLetter(String l) {
		Integer c = letrasCounter.get(l);
		
		if(c != null) {
			letrasCounter.put(l, new Integer(c+1));
			//Log.w("WORDHUNTER" , "add:Letra:" + l + " TOTAL" + new Integer(c+1));
		} else {
			letrasCounter.put(l, new Integer(1));
			//Log.w("WORDHUNTER" , "add:Letra:" + l + " TOTAL" + new Integer(1));
			
		}
		
	}
	
	public static String convert(String text) {
		
		String t = text;
		if(Engine.get().isEnglish()) {
			t = convertEnglish(text);
		} else {
			t = convertPortuguese(text);
		}
		
		return t;
		
	}
	
	
	public static String convertPortuguese(String text) {
	
		return text.replaceAll("1", "LH").replaceAll("2", "NH").
		replaceAll("3", "SS").replaceAll("4", "RR").replaceAll("5", "CH").
		replaceAll("6", "GU").replaceAll("7", "SC").//replaceAll("8", "S‚").
		replaceAll("9", "XC").replaceAll("Q", "QU");
		
	}

	public static String convertEnglish(String text) {
		
		return text.replaceAll("1", "TH").replaceAll("2", "SH").
		replaceAll("3", "NG").replaceAll("Q", "QU");
		
	}
	
	public static void loadArrowsBmp(Resources res) {
		upBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_up);
		downBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_down);
		leftBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_left);
		rightBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_right); 
		rightUpBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_right_up);
		rightDownBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_right_down);
		leftUpBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_left_up);
		leftDownBmp = BitmapFactory.decodeResource(res, R.drawable.arrow_left_down);		
	}
	
	public static StaticSprite createArrow(float x, float y, ArrowType type) {
		StaticSprite arrow = new StaticSprite("ARROW");
		
		arrow.setX(x);
		arrow.setY(y);
		
		switch(type) {
		case up:
			arrow.setImage(upBmp);
			break;
		case down:
			arrow.setImage(downBmp);
			break;
		case left:
			arrow.setImage(leftBmp);
			break;
		case leftDown:
			arrow.setImage(leftDownBmp);
			break;			
		case leftUp:
			arrow.setImage(leftUpBmp);
			break;			
		case right:
			arrow.setImage(rightBmp);
			break;
		case rightDown:
			arrow.setImage(rightDownBmp);
			break;
		case rightUp:
			arrow.setImage(rightUpBmp);
			break;					
		}
		
		arrow.setX(x-arrow.getW()/2);
		arrow.setY(y-arrow.getH()/2);
		
		return arrow;
	}
	
	
	public static NewPoint createPoint(Resources res, float x, float y, int point) {
		
		int r=0, g=0, b=0;
		
		if(point < 0) {
			r = 255;
		} else {
			g = 255;
		}
		
    	NewPoint sp = new NewPoint(0, -200, ""+point, r, g, b);
    	sp.setX(x);
    	sp.setY(y);
    	
    	return sp;
	}
	
	public static Scene getStartScreen(Resources res) {
		StartScreen startScreen = new StartScreen();
		StaticSprite logo = new StaticSprite("lgo");
		
		startScreen.setBackgroundColor(Color.rgb(255, 255, 255));
		
		logo.setImage(BitmapFactory.decodeResource(res, R.drawable.logo));
		
		logo.setX(Engine.get().getDisplay().getWidth()/2 - logo.getW()/2);
		logo.setY(Engine.get().getDisplay().getHeight()/2 - logo.getH()/2);
		
		startScreen.createLayer();
		startScreen.add(logo, 0);
		
		return startScreen;
	}
	
	// 240x320
	@SuppressWarnings("static-access")
	public static Scene getBlockScreen(Resources res) {
		
		lastChar = 0;
		
		initChars();
		
		loadArrowsBmp(res);
		
		BlockScreen scene = new BlockScreen();

		DisplayMetrics met = Engine.get().getMetrics();

		scene.setBackgroundColor(Color.rgb(135, 135, 222));

		scene.createLayer(true, true, 0, 0);//brics 0
		scene.createLayer(true, true, 0, 0);//borda 1
		scene.createLayer(true, true, 0, 0);//outros 2

		StaticSprite sep = new StaticSprite("separador");
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.base_status_cena);

		sep.setImage(bmp);

		sep.setX((Engine.get().getDisplay().getWidth()-sep.getW())/2);//8
//		sep.setY(Engine.get().getDisplay().getHeight() - sep.getH());// 565
		sep.setY(SceneFactory.getMaxy()+sep.getH()*.3f);// 565

		scene.add(sep, 0);

		SubmitButton sb = createSubmitButton();		
		scene.add(sb, 2);
		scene.submitButton = sb;
		scene.submitButton.setX((float)(sep.getX() + (sep.getW() - sb.getW())/2));
		scene.submitButton.setY(sep.getY() + sep.getH()/2);
		
		scene.display = createDisplay();
		scene.display.setX(sep.getX()*1.3f);
		scene.display.setY(sep.getY()+scene.display.getFontSize());
		scene.add(scene.display, 2);
		
		
		scene.score = createScoreDisplay();
		scene.score.setX(sep.getX()*1.3f);
		scene.score.setY(sep.getY()+scene.display.getFontSize()*3);
		scene.add(scene.score, 2);

		//LOADING SCORE
		scene.score.setScore(Engine.get().getDictionary().getPoints());
		
		scene.setMenu(new MenuBlockScene(res));
		
		scene.setLevelUpText(new LevelUpText(res));
		
//		Log.w("WORDHUNTER", "Engine.get().getDisplay().getHeight()::"+Engine.get().getDisplay().getHeight());
		scene.setMaxY(Engine.get().getDisplay().getHeight()*60.98f/100);
		
		return scene;
	}

	
	public static Display createDisplay() {
		Display sub = new Display();
		return sub;
	}	
	
	public static Explosion createExplosion() {
		Explosion sub = new Explosion();
		
		if(explosionBmp == null)
			explosionBmp = BitmapFactory.decodeResource(Engine.get().getResources(), R.drawable.explosion);
		
		int w = explosionBmp.getWidth()/4;
		int h = explosionBmp.getHeight()/4;
		
		Animation explosion = new Animation();
		explosion.setRepeat(false);
		explosion.setName("explosion");
		explosion.setFrameDuration(60);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 4; j++) {
				explosion.addImage(Bitmap.createBitmap(explosionBmp, j*w, i*h, w, h));
			}
		}
		
		sub.addAnimation(explosion);
		sub.start();
		
		return sub;
	}		
	
	public static ScoreDisplay createScoreDisplay() {
		ScoreDisplay sub = new ScoreDisplay();
		
		return sub;
	}	
	
	public static SubmitButton createSubmitButton() {
		SubmitButton sub = new SubmitButton();
		
		return sub;
	}
	

	
	public static Bric createBric(int col, boolean timer) {
		
		bricsCreated += Timer.getLast();
		
		Bric bric = new Bric("Bric"+UUID.randomUUID());
		
		if(timer && explosiveBricsCount < 3) {
			bric.setTimeToExplode(random.nextInt(4)*120000);
			explosiveBricsCount++;
		} else {
			bric.setTimeToExplode(0);
		}
		
		if(bricsCreated > 40000) {
			bric.timeToTurnExplode = random.nextInt(2)*120000;
			bricsCreated = 0;
		}
		
		//ADICIONA LETRA
		char nova = 0;
		
		do{
			nova = letters[letras.nextInt(letters.length)];
		}while(nova == lastChar || achivedMax(""+nova));
		
		addLetter(""+nova);
		lastChar = nova;
		
		bric.setCol(col);
		bric.setTrueLetter(""+nova);
		bric.setLetter(convert(""+nova));
		
		if(bricBmp == null)
			bricBmp = BitmapFactory.decodeResource(Engine.get().getResources(), R.drawable.bloco_letra); 
		
		int w = bricBmp.getWidth();
		int h = bricBmp.getHeight();
		
		bric.setH(h);
		bric.setW(w);
		
		Animation parado = new Animation();
		parado.addImage(bricBmp);
		parado.setName("parado");
		
		bric.addAnimation(parado);
		bric.setAnimation("parado");
		
		bric.setVelY(10);
		
		return bric;
	}
	

}
