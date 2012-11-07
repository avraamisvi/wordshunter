package br.com.pyx.games.words.hunter.core.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.core.Actor;
import br.com.pyx.games.words.hunter.core.Scene;
import br.com.pyx.games.words.hunter.core.SoundManager;

public class BlockScreen extends Scene{

	Paint textPaint;
	
	boolean ini = false;
	float gap = 48;	
	ArrayList<Bric> brics = new ArrayList<Bric>();
	
	int lastColAdd;
	int pointByLen = 2;
	
	List<BricCol> cols = new ArrayList<BricCol>(); 
	
	SubmitButton submitButton;
	Display display;
	ScoreDisplay score;
	LevelUpText levelUpText;
	
	ArrayList<Bric> selected = new ArrayList<Bric>();
	Bric lastSelected;
	
	private float maxY = SceneFactory.getMaxy();
	float minorY = -64;
	long delayAdd = 500;
	long counterAdd = 0;
	
	boolean wait = true;
	boolean submited = false;
	long waitCount = 0;
	
	float addPosY = 0;
	float velY = 500;	
	
	StringBuffer word = new StringBuffer();
	String bonus;
	
	long timeMinExplosiveAdd = 50000;
	long timeExplosiveAdd = 0;
	
	
	int MAX_CELS = 88;
	int MAX_LINES = 11;
	int MAX_COLS = 8;
	
	private boolean gameOver = false;
	
	private boolean showMenu = false;
	
	private MenuBlockScene menu;
	
	public BlockScreen() {
		for(int i=0; i < MAX_COLS; i++)
			cols.add(new BricCol(i));
	}
	
	@Override
	public void initialize() {
		
		gap = Engine.get().getDisplay().getWidth()*.10f;
		
		while(brics.size() < MAX_CELS) {
			addBric();
			printMemory();
		}
		
		configLevel();
		
		Engine.get().stopMusic();
	}
	
	
	public void restart() {
		Bric[] bricsAr = this.brics.toArray(new Bric[0]);
		
		cols.clear();
		for(int i=0; i < MAX_COLS; i++)
			cols.add(new BricCol(i));
		
		for (Bric b : bricsAr) {
			b.setRemove(true);
		}
		
		setGameOver(false);
		score.setScore(0);
	}
	
	private void configLevel() {
		if(Engine.get().getGameType() == GameType.NORMAL) {
			timeMinExplosiveAdd = 50000 - 1000 * Engine.get().getLevel();
			
			if(timeMinExplosiveAdd < 10000) {
				timeMinExplosiveAdd = 25000;
			}
		} else {
			timeMinExplosiveAdd = 45000;
		}
	}
	
	
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	
	@Override
	public void updateBehaviour(long delay) throws Exception {			
		
		if(isShowMenu())
			return;
		
		timeExplosiveAdd += delay;

		if (waitCount < 1000 && wait) {
			waitCount += delay;
			return;
		} else if (waitCount >= 1000 && wait) {
			wait = false;
		}

		if (brics.size() < MAX_CELS) {
			counterAdd = counterAdd + delay;
			if (counterAdd >= delayAdd) {
				counterAdd = 0;
				addBric();
			}
		}

		Bric[] bricsAr = this.brics.toArray(new Bric[0]);
		for (Bric b : bricsAr) {
			if (b.isRemove()) {
				this.brics.remove(b);
				continue;
			}

			if (b.getVelY() > 0 && b.getY() > maxY) {
				b.setY(maxY);
				b.setVelY(0);
			}

			else if (b.getY() < maxY && b.getVelY() <= 0) {
				b.setVelY(velY);
			}
			
			if(submited) {
				b.verifyAndExplode();
			}
		}
		
		if(submited)
			submited = false;
		
	}
	
	
	void addToCol(BricCol col, Bric b) {
		if(col.last != null) {
			if(col.last.getRectF().top > col.last.getH())
				b.setY(minorY);
			else
				b.setY(col.last.getRectF().top - col.last.getH());
		}
		
		b.setCol(col.number);
		b.setX(gap+(b.getCol()*b.getW()));
		
		col.addBric(b);
	}
	
	
	private int getLastColAdd() {
		int ret = lastColAdd;
		
		lastColAdd++;
		
		if(lastColAdd >= MAX_COLS)
			lastColAdd = 0;
		
		return ret;
	}
	
	private BricCol nextCol() {
		
		BricCol c = cols.get(getLastColAdd());
		
		while(c.brics.size() == MAX_LINES ) {
			c = cols.get(getLastColAdd());
		}
		
		return c;
	}
	
	void addBric() {
		
		Bric b = null;
		
		if(timeExplosiveAdd > timeMinExplosiveAdd) {
			b = SceneFactory.createBric(0, true);
			timeExplosiveAdd = timeExplosiveAdd - timeMinExplosiveAdd;
		} else {
			b = SceneFactory.createBric(0, false);
		}
		
		b.setScene(this);
		b.setVelY(velY);
		b.setMaxY(maxY);
		
		this.add(b, 0);
		
		brics.add(b);
		
		addToCol(nextCol(), b);
	}
	
	
	public boolean canRemoveBric() {
		return brics.size() == MAX_CELS;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if(Engine.get().isDebug()) {
			Paint p = new Paint();
			p.setARGB(255, 110, 115, 165);
			
			for(int i = 0; i <= Engine.get().getMetrics().heightPixels; i=i+10) {
				canvas.drawText(""+i, 0, i, p);
			}
		}
		
//		drawTexts(canvas);
	}
	
	void cleanSelected() {
				
		for(Bric b : selected) {
			b.setSelected(false);
			b.setSelectedId(0);
//			b.setAnimation("deselecionando");
		}
		
		cleanWord();
		lastSelected = null;
		selected.clear();
	}
	
	public void addPoints(int len, float x, float y, boolean bonus) {
		int points = 0;
		
		if(len > 4) {
			points = len*pointByLen;
		} else {
			points = len;
		}
		
		score.add(points);
		showPointAnimation(points, x, y);
		
		if(score.getScore() > (Engine.get().getLevel()*1000)) {
			score.setScore(0);
			levelUPGame();
		}
		
	}
	
	public void showPointAnimation(int points, float x, float y) {
		add(SceneFactory.createPoint(Engine.get().getResources(), x, y, points), 2);
	}
	
	public void submitWord() {
			
			
			boolean exist = display.text.length() >= 3 && Engine.get().getWordsDatabase().existsWord(display.text);
			
			if(canRemoveBric() && exist) {
								
				Bric sel = this.selected.get(this.selected.size()/2);
				
				addPoints(display.getBasicPoints(), sel.getX(), sel.getY(), display.isBonusWord());
				
				Bric[] brics = this.selected.toArray(new Bric[0]);
				for (Bric b : brics) {				
					cols.get(b.col).removeBric(b);
					
					b.setRemove(true);
					//b.setSelected(false);
					b.setSelectedId(0);
					
					if(b.getTimeToExplode() > 0) {
						SceneFactory.explosiveBricsCount--;
					}
				}
				
				lastSelected = null;
				selected.clear();
								
				cleanWord();
				submited = true;
				
				SoundManager.playSound(SoundManager.SUBMIT, 1);
			} else {
				Bric[] brics = this.selected.toArray(new Bric[0]);
				for (Bric b : brics) {
					b.explode();
				}
				
				lastSelected = null;
				selected.clear();
			}
			
	}
	
	
	public void explosiveRemove(Bric b) {
		
			if(!gameOver) {
				if(b.isSelected()) {
					cleanSelected();
				}
			
				b.setRemove(true);
				
				cols.get(b.col).removeBric(b);
				
				if(b.row <= 0 && score.getScore() <= 0) {
					setGameOver(true);				
				} else {
					addPoints(-30, b.getX(), b.getY(), false);
				}
			}
		
	}
	
	public void levelUPGame() {
		int level = Engine.get().getLevel();
		
		if(level < 99) {
			level++;
			
			Engine.get().getDictionary().setLevelPoints(level, (int) score.getScore());
			Engine.get().setLevel(level);
			
			levelUpText.show();
			configLevel();
		}
		
		SoundManager.playSound(SoundManager.EVOLUI, 1);
	}
	
	void addLetter(String l) {
		
		word.append(l);
		
		display.setText(word.toString());
	}
	
	void removeLetter(int len) {

			if(len < 2) {
				word.deleteCharAt(word.length()-1);
			} else {
				word.delete(word.length()-2, word.length());
			}
		
		display.setText(word.toString());
	}
	
	void cleanWord() {
		word = new StringBuffer();
		display.setText(word.toString());
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
//		if(levelUpText.isShow())
//			levelUpText.hide();
		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			submitButton.diSelect();
		}
		
		if(isShowMenu()) {
			
			menu.onTouchEvent(event.getX(), event.getY());
			
		}else if(event.getAction() == MotionEvent.ACTION_DOWN && canRemoveBric() && !gameOver) {
			try {
				float x = event.getX();
				float y = event.getY();
				
				if(submitButton.getRectF().contains(x, y)) {
					
					submitButton.select();
					submitWord();
					return true;
				} else {
					
					Bric[] brics = this.brics.toArray(new Bric[0]);
					for (Bric b : brics) {
						if(b.getRectF().contains(x,y)) {
							
							if(!b.isSelected()) {
								
								if(selected.size() > 12)
									cleanSelected();
								
								if(lastSelected != null) {
									
									if((lastSelected.col == b.col) && (lastSelected.bottom != b && lastSelected.top != b)) {
											cleanSelected();
									} else if(Math.abs(lastSelected.row - b.row) > 1 || Math.abs(lastSelected.col - b.col) > 1) {
											cleanSelected();								
									}
								}
								
								b.setSelected(true);
//								b.setAnimation("selecionando");
								selected.add(b);
								b.setSelectedId(selected.size()-1);
								
								if(lastSelected != null) {
									lastSelected.setNextSelecion(b.col, b.row);
								}
								
								lastSelected = b;
								
								addLetter(b.letter);
							} else {
								if(b.getSelectedId() == selected.size()-1) {
									b.setSelected(false);
									selected.remove(b);
									
									if(selected.size() > 0) {
										lastSelected = selected.get(lastSelected.selectedId-1);
										lastSelected.removeNextSelecion();
									} else { 
										lastSelected = null;
									}
									//word.length()-1, b.letter.length()
									removeLetter(b.letter.length());
								}
							}
							
							break;
						}
					}
				}
			}catch(Exception e) {
				Engine.get().performingHandlingException(e);
			}
			return true;
			
		} 
//		else if(isGameOver() && event.getAction() == MotionEvent.ACTION_DOWN) {
//			
//			Engine.get().changeScene(SceneFactory.getScoreScreen(Engine.get().getResources()));
//			return true;
//		}
		
		return false;
	}

	public void onShowMenu() {
		showMenu = true;
	}
	
	public void onBack() {
		
		if(isShowMenu()) {
			showMenu = false;
		}
	}	
	
	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	
	public boolean isShowMenu() {
		return showMenu;
	}
	
	public void setMenu(MenuBlockScene menu) {
		this.menu = menu;
		menu.setScene(this);
		add(menu, 2);
	}
	
	public void setLevelUpText(LevelUpText levelUpText) {
		this.levelUpText = levelUpText;
		levelUpText.setScene(this);
		add(levelUpText, 2);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		textPaint = null;		
		brics = null;
		cols = null; 
		submitButton = null;
		display = null;
		score = null;
		levelUpText = null;
		selected = null;
		lastSelected = null;
		word = null;
		bonus = null;		
		menu = null;		
	}

	@Override
	protected Paint getPaint() {
		// TODO Auto-generated method stub
		return null;
	}
}
