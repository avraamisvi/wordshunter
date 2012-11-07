package br.com.pyx.games.words.hunter.core.game;

import java.util.ArrayList;
import java.util.Random;

import br.com.pyx.games.words.hunter.Engine;



public class BricCol {
	
	ArrayList<Bric> brics = new ArrayList<Bric>();
	Bric last;
	int number;
	
	Random rand = new Random();
	
	public BricCol(int number) {
		this.number = number;
	}

	public void addBric(Bric bric) {
		brics.add(bric);
		
		bric.bottom = last;
		
		if(last != null)
			last.top = bric;
		
		last = bric;
		bric.setRow(brics.size()-1);
		
	}
	
	public void removeBric(Bric bric) {
		brics.remove(bric);
		
		try {
		if(bric.top != null) {
//			bric.top.setRow(bric.row);
			bric.top.bottom = bric.bottom;
			
			int index =  bric.row;
//			if(index < 6 && index >= 0) {
			
				for(int i = index; i < brics.size(); i++) {
					brics.get(i).row--;
				}
//			}
			
		}		
		
		if(bric.bottom != null) {
			bric.bottom.top = bric.top;
			
			if(bric.exploded) {
				if(bric.timeToExplode > 0) {
					bric.bottom.setTimeToExplode(bric.timeToExplode);
					bric.bottom.started = false;
				} else {
					int chance = rand.nextInt(10);
					switch (chance) {
					case 3:
					case 7:
					case 5:
						bric.bottom.setTimeToExplode(120000);
						bric.bottom.started = false;						
						break;
					}
				}
			}
		}
		
		if(brics.size() > 0)
			last = brics.get(brics.size()-1);
		else
			last = null;
		
		} catch(Throwable e) {
			Engine.get().performingHandlingException(e);
		}
	}
}
