package br.com.pyx.games.words.hunter;

import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import br.com.pyx.games.words.hunter.core.Factory;
import br.com.pyx.games.words.hunter.core.Scene;
import br.com.pyx.games.words.hunter.core.Screen;
import br.com.pyx.games.words.hunter.core.SoundManager;
import br.com.pyx.games.words.hunter.core.Timer;
import br.com.pyx.games.words.hunter.core.game.DictionaryDatabase;
import br.com.pyx.games.words.hunter.core.game.GameType;
import br.com.pyx.games.words.hunter.core.game.SceneFactory;
import br.com.pyx.games.words.hunter.core.game.WordsDatabase;

public class Engine extends Activity {

	public static String TAG = "WORDSHUNTER";

	Screen screen;
	
	boolean quit = false;

	boolean debug = false;
	
	private int level = 1;
	
	final Handler mHandler = new Handler();

	private static Engine engine;

	private float gravity = 9;
	
	private float rotation = 0;
	
	private boolean sound = true;
	
	private boolean english = false;
	
	private Random rand;
	
	private GameType gameType = GameType.NORMAL;
	
	public int totalPoints = 1;
	
	public int levelPoints = 0;
	
	public int hiScore = 0;
	
	public int levelLife = 3;	
	
	public int fps;
	
	private DictionaryDatabase dictionary;
	
	private WordsDatabase database;
	
	public void addPoint(int p) {
		levelPoints = levelPoints + p;
	}
	
	public Random getRand() {
		if(rand == null) {
			rand = new Random();
		}
		
		return rand;
	}
	
	public boolean isEnglish() {
		return english;
	}
	
	public void setEnglish(boolean english) {
		this.english = english;
	}
	
	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	MediaPlayer mediaPlayer;
	
	public void startMusic() {
				
		if(mediaPlayer == null || !mediaPlayer.isPlaying()) {
			mediaPlayer = MediaPlayer.create(this.getBaseContext(), R.raw.musica_tema);
			mediaPlayer.start();
			mediaPlayer.setLooping(true);
		}
	}
	
	public void stopMusic() {
		mediaPlayer.stop();
		mediaPlayer.reset();
	}
	
	public void changeSoundState() {
		sound = !sound;
		if(sound) {
			startMusic();
		} else {
			stopMusic();
		}
	}
	
	public boolean isSound() {
		return sound;
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   		
		
		engine = this;
		
		english = !Locale.getDefault().getLanguage().equalsIgnoreCase("pt");
		
		Engine.get().load();		
		
		Factory.get().setRes(getResources());
		Factory.get().load();
		
		 SoundManager.getInstance();
	     SoundManager.initSounds(this);
	     SoundManager.loadSounds();
	        
		screen = new Screen(getApplicationContext(), init());
		setContentView(screen);		
		
		screen.setFocusable(true);
		screen.setFocusableInTouchMode(true);
		screen.requestFocus();
		
		Timer.start();

		//new ScreenThread().start();
	}
	
	long timeElapsed;
	long max = 100;
	
	
    int FRAMES_PER_SECOND = 25;
    int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

    long next_game_tick = System.currentTimeMillis();
    long sleep_time = 0;
	
	public float interpolation;
	
	public Scene init() {
		return SceneFactory.getStartScreen(getResources());
	}

	public void changeScene(Scene scene) {
		screen.changeScene(scene);
	}

	public void performingHandlingException(Throwable cause) {
		Log.e(TAG, "ERROR", cause);
	}

	public static Engine get() {
		return engine;
	}

	public Scene getScene() {
		return screen.getScene();
	}

	public int getLevel() {
		return level;
	}
	
	public int getTotalPoints() {
		return totalPoints;
	}
	
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public DisplayMetrics getMetrics() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
	
	public Display getDisplay() {
		return getWindowManager().getDefaultDisplay();
	}
	
	@Override
	public void onBackPressed() {
		quit = true;
		super.onBackPressed();
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		boolean ret = false;
		
		if(screen != null && screen.getScene() != null) {
			switch(keyCode) {
				case KeyEvent.KEYCODE_BACK:
					getScene().onBack();
					ret = true;
				break;
				case KeyEvent.KEYCODE_MENU:
					getScene().onShowMenu();
					ret = true;					
				case KeyEvent.KEYCODE_HOME:
					getScene().onShowMenu();
					ret = true;					
				break;
				case KeyEvent.KEYCODE_SEARCH:
					getScene().onShowMenu();
					ret = true;								
			}
		}
		
		return ret;
	}
	
	boolean paused = false;
	
	@Override
	protected void onPause() {
		getScene().onShowMenu();
		screen.onMinimize();
		paused = true;
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(screen != null && paused) {
			screen.onRestore();
			paused = false;
		}		
		super.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
		
	public void load() {
		try {
			database = new WordsDatabase();
			
			dictionary = new DictionaryDatabase();
			dictionary.initialize(this, getResources());
			level = dictionary.getLevel();
		
		} catch (Throwable e) {
			performingHandlingException(e);
		}
	}


	public DictionaryDatabase getDictionary() {
		return dictionary;
	}

	public WordsDatabase getWordsDatabase() {
		return database;
	}
	
	
	public void onCompletion(MediaPlayer mp) {
		mp.release();
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	
}