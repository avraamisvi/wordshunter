package br.com.pyx.games.words.hunter.core;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class Layer {
	
	private List<SceneObject> actors = new ArrayList<SceneObject>();
	
	public void draw(Canvas canvas) {
		
		SceneObject []array = actors.toArray(new SceneObject[0]);
		for (SceneObject actor : array) {
			actor.draw(canvas);
		}
	}
	

	public void update(long delay) {
		
		SceneObject[] acs = this.actors.toArray(new SceneObject[0]);
		for (SceneObject actor : acs) {
			
			if(actor.isRemove()) {
				remove(actor);
				continue;
			}
			
			actor.update(delay);
		}
	}
	
	public void add(SceneObject actor) {
		actors.add(actor);
	}


	public void remove(SceneObject actor) {
		actors.remove(actor);
		actor.destroy();
	}
}
