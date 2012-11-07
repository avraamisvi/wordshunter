package br.com.pyx.games.words.hunter.core.game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import br.com.pyx.games.words.hunter.Engine;
import br.com.pyx.games.words.hunter.R.raw;

public class DictionaryDatabase {
	
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "words_hunter";
    private static final String WORDS_TABLE = "WORDS";
    private static final String WORD_COLUMN = "WORD";
    private static final String USER_TABLE = "USER";
    private static final String LEVEL_COLUMN = "LEVEL";
    private static final String POINTS_COLUMN = "POINTS";
    private static final String INSTALLED_COLUMN = "INSTALLED";
    private static final String COD_COLUMN = "COD";
    
	private DictionaryOpenHelper helper;
	private HashMap<String, String> tableMap = buildMap();
	private HashMap<String, String> tableMapUser = buildMapUser();
	
	private float maxWords = 28638;
	public static int COMPLETE = -1;
	private float perc = 0;
	boolean loading = false;
	
	public void initialize(Context context, Resources res) {
		
		helper = new DictionaryOpenHelper(context, res);
		
		/*try {
			if(!isLoading()) {
				long skip = getInstalled();
				if(skip != COMPLETE) {
					Log.w(WordsHunter.TAG, "Skip:" + skip);
					loadDictionary(skip);
				} else if(skip == COMPLETE){
					perc = 1;
				}		
			}
		} catch (Throwable e) {
			WordsHunter.get().performingHandlingException(e);
		}*/
	}	
	
	HashMap<String, String> buildMap() {
		HashMap<String, String> cols = new HashMap<String, String>();
		
		cols.put(WORD_COLUMN, WORD_COLUMN);
		
		return cols; 
	}
	
	HashMap<String, String> buildMapUser() {
		HashMap<String, String> cols = new HashMap<String, String>();
		
		cols.put(LEVEL_COLUMN, LEVEL_COLUMN);
		cols.put(POINTS_COLUMN, POINTS_COLUMN);
		cols.put(INSTALLED_COLUMN, INSTALLED_COLUMN);
		
		return cols; 
	}
	
	public boolean isLoading() {
		return loading;
	}
	
	public void loadDictionary(long skip) {
		helper.loadDictionary(skip);
	}
	
	/*public void existsWord(final String word, final Display display) {
		
        new Thread(new Runnable() {
            public void run() {
                try {
            		
            		display.setWordExists(existsWord(word));
                } catch (Throwable e) {
                	WordsHunter.get().performingHandlingException(e);
                    throw new RuntimeException(e);
                }
            }
        }).start();
	}*/
	
	public boolean isLoadComplete() {
		return getInstalled() == 100;
	}
	
	public float getPercentComplete() {
		return perc;
	}
	
	/*public boolean existsWord(String word) {
		
		Cursor c = query(word);
		boolean ret = c != null && c.moveToFirst();

		if (c != null)
			c.close();

		return ret;
	}*/
	
    private Cursor query(String word) {    	
    	
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        
        builder.setTables(WORDS_TABLE);
        builder.setProjectionMap(tableMap);

        Cursor cursor = builder.query(helper.getReadableDatabase(), new String[]{WORD_COLUMN}, WORD_COLUMN + " = ? ", new String[]{word}, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
	
    
    ContentValues initialValues = new ContentValues();
    
    public long setLevelPoints(int value, int points) {
    	
    	initialValues.clear();
        initialValues.put(LEVEL_COLUMN, value);
        initialValues.put(POINTS_COLUMN, points);

        return helper.getWritableDatabase().update(USER_TABLE, initialValues, null, null);
    }
 
    
    public int getLevel() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        
        builder.setTables(USER_TABLE);
        builder.setProjectionMap(tableMapUser);

        Cursor cursor = builder.query(helper.getReadableDatabase(), new String[]{LEVEL_COLUMN}, COD_COLUMN + " = ? ", new String[]{"1"}, null, null, null);

        if (cursor == null) {
            return 1;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return 1;
        }
        
        return cursor.getInt(0);    	
    }
    
    public int getPoints() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        
        builder.setTables(USER_TABLE);
        builder.setProjectionMap(tableMapUser);

        Cursor cursor = builder.query(helper.getReadableDatabase(), new String[]{POINTS_COLUMN}, COD_COLUMN + " = ? ", new String[]{"1"}, null, null, null);

        if (cursor == null) {
            return 1;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return 1;
        }
        
        return cursor.getInt(0);    	
    }    
    
    public int getInstalled() {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        
        builder.setTables(USER_TABLE);
        builder.setProjectionMap(tableMapUser);

        Cursor cursor = builder.query(helper.getReadableDatabase(), new String[]{INSTALLED_COLUMN}, COD_COLUMN + " = ? ", new String[]{"1"}, null, null, null);

        if (cursor == null) {
            return 1;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return 1;
        }
        
        return cursor.getInt(0);    	
    }      
    
    public long setInstalled(int value) {
    	
    	initialValues.clear();
        initialValues.put(INSTALLED_COLUMN, value);

        return helper.getWritableDatabase().update(USER_TABLE, initialValues, null, null);
    }
    
	class DictionaryOpenHelper extends SQLiteOpenHelper {	    
	    
	    private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE " + WORDS_TABLE + " (" + WORD_COLUMN + " TEXT);";
	    private static final String USER_TABLE_CREATE = "CREATE TABLE "+USER_TABLE+" ("+COD_COLUMN+" INT, "+LEVEL_COLUMN+" INT, "+POINTS_COLUMN+" INT,"+INSTALLED_COLUMN+" INT);";
	    
	    private SQLiteDatabase dataBase;
	    private Resources res;
	    
	    DictionaryOpenHelper(Context context, Resources res) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);	        
	        this.res = res;
	    }

        /**
         * Starts a thread to load the database table with words
         */
        public void loadDictionary(final long skip) {
        	
        	/*if(dataBase == null)
        		dataBase = getWritableDatabase();
        	
        	DictionaryDatabase.this.loading = true;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords(skip);
                        DictionaryDatabase.this.loading = false;
                    } catch (Throwable e) {
                    	WordsHunter.get().performingHandlingException(e);
                        throw new RuntimeException(e);
                    }
                }
            }).start();*/
            
        }
        
        
        /**
         * Starts a thread to load the database table with words
         */
        private void loadWords(long skip) {
	        try {
		        float counter = 0; 
	        	InputStream dic = res.openRawResource(raw.dic);
	        	BufferedReader is = new BufferedReader(new InputStreamReader(dic, "UTF8"));
	        	
//	        	dataBase.beginTransaction();
	            try {
	            	
	                String line;
	                long id;
	                
	                
	                while ((line = is.readLine()) != null) {	                    
	                	
	                	if(skip > 0 && counter < skip)
	                		continue;
	                	
	                	if (line.length() > 2);
	                    	id = addWord(line);
	                    	
	                    counter++;
	                    DictionaryDatabase.this.setInstalled((int) counter);	                    
	                    DictionaryDatabase.this.perc = (counter/maxWords);
	                    
	                    /*Log.w(WordsHunter.TAG, "Perc2:"+DictionaryDatabase.this.perc);
	                    Log.w(WordsHunter.TAG, "Counter:"+counter);
	                    Log.w(WordsHunter.TAG, "counter/maxWords:"+(counter*100/maxWords));*/
//	                    if (id < 0) {
//	                        Log.e("WORDHUNTER", "unable to add word: " + line);
//	                    }
	                }
	                
	                DictionaryDatabase.this.setInstalled(COMPLETE);
	            } finally {
	                is.close();
	            }
//	            dataBase.setTransactionSuccessful();
//	            dataBase.endTransaction();
	            
	            
	        } catch(Throwable e) {
	        	Engine.get().performingHandlingException(e);
	        }
        }
        
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	dataBase = db;
	    	db.execSQL(DICTIONARY_TABLE_CREATE);
	    	db.execSQL(USER_TABLE_CREATE);
	    	initUser();
	        //loadDictionary(0);
	        dataBase.execSQL("CREATE INDEX "+WORDS_TABLE+"_"+WORD_COLUMN+"_idx ON " + WORDS_TABLE + "(" + WORD_COLUMN + ");"); 
	    }

	    ContentValues initialValues = new ContentValues();

        public long addWord(String word) {
        	if(word != null) {
	        	initialValues.clear();
	            initialValues.put(WORD_COLUMN, word);

	            return dataBase.insert(WORDS_TABLE, null, initialValues);
        	}
        	
        	return -1;
        }
        
        private void initUser() {
        	initialValues.clear();
            initialValues.put(LEVEL_COLUMN, 1);
            initialValues.put(COD_COLUMN, 1);
            initialValues.put(INSTALLED_COLUMN, 0);
        	
        	dataBase.insert(USER_TABLE, null, initialValues);
        }
        
        
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("WORDHUNTER", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + WORDS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
		}
	}
	
}
