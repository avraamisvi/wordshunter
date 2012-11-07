package br.com.pyx.games.words.hunter.core.game;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.util.HashMap;

import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.R.raw;

public class WordsDatabase {
	
	HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
	private static final int HASHSIZE = 2000;
	
	public WordsDatabase() {
        new Thread(new Runnable() {
            public void run() {
                try {
                	
                	loadHash();
                	
                } catch (Throwable e) {
                	Engine.get().performingHandlingException(e);
                    throw new RuntimeException(e);
                }
            }
        }).start();	
	}
	
	public boolean existsWord(String key) {
		boolean ret = false;
		
		try {
			if(Engine.get().isEnglish()) {
				ret = find(key, hash, new DataInputStream(Engine.get().getResources().openRawResource(raw.dic_eng)));
			} else {
				ret = find(key, hash, new DataInputStream(Engine.get().getResources().openRawResource(raw.dic)));
			}
		}catch(Throwable e) {
			Engine.get().performingHandlingException(e);
		}
		
		return ret;
	}


	public void existsWord(final String text, final Display display) {
		
		if(text.length() < 3) {
			display.setWordExists(false, System.currentTimeMillis());
		} else {
	        new Thread(new Runnable() {
	        	long time = System.currentTimeMillis();
	        	
	            public void run() {
	                try {
	                	display.setWordExists(existsWord(text), time);
	                } catch (Throwable e) {
	                	Engine.get().performingHandlingException(e);
	                    throw new RuntimeException(e);
	                }
	            }
	        }).start();
		}
	}
	
	public void loadHashPortuguese() throws Exception {
		DataInputStream dt = new DataInputStream(Engine.get().getResources().openRawResource(raw.hash)); 
		
		try {
			int h, pos;
			while(true) {
				h = dt.readInt();
				pos = dt.readInt();
				
				hash.put(h, pos);
			}
		} catch (EOFException e) {
			// TODO: handle exception
		}
		
		dt.close();
	}	
    
	public void loadHashEnglish() throws Exception {
		DataInputStream dt = new DataInputStream(Engine.get().getResources().openRawResource(raw.hash_eng)); 
		
		try {
			int h, pos;
			while(true) {
				h = dt.readInt();
				pos = dt.readInt();
				
				hash.put(h, pos);
			}
		} catch (EOFException e) {
			// TODO: handle exception
		}
		
		dt.close();
	}	
	
	public void loadHash() throws Exception {
		if(Engine.get().isEnglish()) {
			loadHashEnglish();
		} else {
			loadHashPortuguese();
		}
	}

	public int hash(String word) {
		int HASHSHIFT = 5;
		int h = 0;

		for (int i = 0; i < word.length(); i++) {
			h = (h << 16) | word.charAt(i);
		}

		word = word.toUpperCase();
		for (int i = 0; i < word.length(); i++) {
			h = (h << HASHSHIFT) | ((h >> (32 - HASHSHIFT)) & ((1 << HASHSHIFT) - 1));
			h ^= word.charAt(i);
		}
		
		return Math.abs((int) (h % HASHSIZE));
	}
	
	public boolean find(String w, HashMap<Integer, Integer> sh, DataInputStream in) throws Exception {
		boolean ret = false;
		int has = hash(w);
		
		try {
		in.skipBytes(sh.get(has));
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		String line = bf.readLine();
		
		while (( line = bf.readLine()) != null)
		{
			if(line.equals(w)) {
				ret = true;
				break;
			}
		}
			return line!=null&&line.equals(w);
			
		} catch(Exception e) {
			Engine.get().performingHandlingException(e);
//			e.printStackTrace();
		}
		
		return ret;
	}	
}
