package br.com.pyx.games.words.hunter.core.game;

import br.com.pyx.games.words.hunter.core.Actor;

public class Explosion extends Actor {

	private int LAST_FRAME = 11;
	
	public static int count = 0;
	
	public Explosion() {
		super("Explosion", "Explosion");
		Explosion.count++;
	}

	public void start() {
		setAnimation("explosion");
	}
	
	@Override
	public void updateBehaviour(long delay) throws Exception {
		
		super.updateBehaviour(delay);
		
		if(getAnimation().getFrame() >= LAST_FRAME) {
			setRemove(true);
		}	
	}
	
	@Override
	public void destroy() {
		Explosion.count--;
	}
}
