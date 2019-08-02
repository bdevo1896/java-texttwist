import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

/**
 * This class will be used to delegate to the WordDictionary class to work the game. It will contain everything
 * that is needed to keep the game running like the score, lives, and words to be checked.
 * @author Bryce DeVaughn
 *
 */
public class WordGame {

	private static final int MAX_WORD_SIZE = 10,MAX_POINTS = 50;
	private static int THRESHOLD = 6*MAX_POINTS;//The number of points needed to advance to the next level
	private static int TIMER_MAX = 180;//The maximum amount of time for the game
	private Random rand;
	private int points,lvlPoints,level,timer,lives;
	private ArrayList<String> wordList;//List to hold all used words
	private String[] letters;//List to hold the current word's mixed up letters
	private WordDictionary wordDict;//The object being delegated

	public WordGame() {
		rand = new Random();
		points = 0;
		lvlPoints = 0;
		level = 1;
		wordList = new ArrayList<String>();
		timer = TIMER_MAX;
		wordDict = new WordDictionary();
	}

	/**
	 * This method restarts the game.
	 */
	public void restart(){
		level = 1;
		timer = TIMER_MAX;
		letters = null;
		wordList.clear();
		points = 0;
		THRESHOLD = 6*MAX_POINTS;
	}

	/**
	 * This method will check to see if a word has already been entered into the list.
	 * @throws UsedWordException 
	 * @throws IOException 
	 */
	public boolean testWord(String word) throws UsedWordException, IOException,FileNotFoundException{
		boolean rtn = false;
		/*
		 * This loop checks to see if the list already has the word inputed
		 */
		for(String tWord: wordList){
			if(word.equals(tWord)){
				throw new UsedWordException();
			}
		}
		
		/*
		 *This checks to see if the word inputed will automatically advance the level, award points and checks to see if the game has been beaten.
		 */
		if(wordDict.wordInDictionary(word)&&word.length()==level+2){
			if(word.length()==10){
				JOptionPane.showMessageDialog(null, this, "Final Score: "+points+lvlPoints, JOptionPane.PLAIN_MESSAGE);
			}
			lvlPoints+=word.length()*10;
			advance();
			return true;
		}
		
		/*
		 * Checks if the word is in the dictionary and awards points
		 */
		if(wordDict.wordInDictionary(word)){
			wordList.add(word);
			lvlPoints+=word.length()*10;
			if(lvlPoints > THRESHOLD){
				this.advance();
			}
			rtn = true;
		}else{
			rtn = false;
		}

		return rtn;
	}

	/**
	 * This method will decrement the time.
	 * @throws TimeoutException 
	 */
	public void decrementTimer() throws TimeoutException{
		timer--;
		if(timer == 0){
			lives--;
			throw new TimeoutException();
		}
	}

	/**
	 * This method will advance the game to the next level.
	 */
	public void advance(){
		letters = null;
		level++;
		points+=lvlPoints;
		lvlPoints = 0;
		timer = TIMER_MAX;
	}

	/**
	 * This method checks to see if the game is over.
	 * @return
	 */
	public boolean isGameOver(){
		if(lives < 0){
			return true;
		}else if(level > MAX_WORD_SIZE){
			return true;
		}
		return false;
	}

	/**
	 * This word will shuffle the word's characters and return an array of them.
	 * @return
	 */
	public char[] newLetters(int n){
		int MAX_SHUFFLES = 5;//The maximum number of times the loops will run
		char[] rtnList = new char[n];
		String str = wordDict.fetchRandomWord(n);//A new word from the dictionary
		/*
		 * This loop will fill the letters array.
		 */
		for(int i = 0; i < str.length(); i++){
			rtnList[i] = str.charAt(i);
		}
		
		/*
		 * These loops will shuffle the letters
		 */
		int shuffles = 5+rand.nextInt(MAX_SHUFFLES+1);
		for(int i = 0; i < shuffles; i++){
			for(int k = 0; k < shuffles; k++){
				int fIndex = rand.nextInt(rtnList.length);
				char temp = rtnList[fIndex];
				int lIndex = rand.nextInt(rtnList.length);
				rtnList[fIndex] = rtnList[lIndex];
				rtnList[lIndex] = temp;
			}
		}
		/*
		 * This loop copies the shuffled letters to the list 'letters' to later be replaced
		 */
		letters = new String[rtnList.length];
		for(int i = 0; i < rtnList.length; i++){
			letters[i] = rtnList[i]+"";
		}

		return rtnList;
	}

	/**
	 * This method will shuffle the current list of letters.
	 * @return
	 */
	public void shuffleLetters(){
		int MAX_SHUFFLES = 5;//The maximum number of times the loops will run
		int shuffles = rand.nextInt(MAX_SHUFFLES+1);
		
		/*
		 * Shuffles the letters again.
		 */
		for(int i = 0; i < shuffles; i++){
			for(int k = 0; i < shuffles; k++){
				int fIndex = rand.nextInt(letters.length);
				String temp = letters[fIndex];
				int lIndex = rand.nextInt(letters.length);
				letters[fIndex] = letters[lIndex];
				letters[lIndex] = temp;
			}
		}
		points-=10;
	}
	
	/**
	 * This method will print all usable words containing the inputed phrase.
	 * @return
	 */
	public void printUsableWords(String phrase){
		String[] list = null;
		list = wordDict.word(phrase);//List with all the possible words found.
		
		/*
		 *Prints out every word in the list. 
		 */
		if(list!=null){
		for(String str: list){
			System.out.println(str);
		}
		}else{
			System.out.println("No words found.");
		}
	}

	public WordDictionary getWordDict() {
		return wordDict;
	}


	public static int getTHRESHOLD() {
		return THRESHOLD;
	}

	public static void setTHRESHOLD(int tHRESHOLD) {
		THRESHOLD = tHRESHOLD;
	}

	public Random getRand() {
		return rand;
	}

	public void setRand(Random rand) {
		this.rand = rand;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLvlPoints() {
		return lvlPoints;
	}

	public void setLvlPoints(int lvlPoints) {
		this.lvlPoints = lvlPoints;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}

	public String[] getLetters() {
		return letters;
	}

	public void setLetters(String[] letters) {
		this.letters = letters;
	}



}
