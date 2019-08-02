import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * An instance of this class will act as a helper object to fetch and test words from a text file (wordFile).
 * @author Bryce DeVaughn
 *
 */
public class WordDictionary {
	private Random rand;
	public WordDictionary() {
		rand = new Random();
	}

	/**
	 * This method will create the filename needed to locate the correct text file.
	 * @return
	 */
	public String getFilename(int n){
		String rtnStr = "";
		rtnStr = n+".txt";
		return rtnStr;
	}

	/**
	 * This method will test to see if the inputed word is inside the current word list.
	 * @return
	 */
	public boolean wordInDictionary(String word)throws IOException{
		BufferedReader reader = null;//This reader will read the words in
		try{
			reader = new BufferedReader(new FileReader(getFilename(word.length())));
			while(reader.ready() ){
				/*
				 * Reads in a line and checks to see if the inputed word equals the checked word from the dictionary.
				 */
				String checkWord = reader.readLine();
				if(word.equals(checkWord)){
					return true;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
				System.exit(0);//Close the program.
			}
		}

		return false;
	}

	/**
	 * This method will find a random word from the list file.
	 * @return
	 */
	public String fetchRandomWord(int n) {
		String rtnStr = "";
		BufferedReader reader = null;//Reader for the dictionary
		try{
			reader = new BufferedReader(new FileReader(new File(getFilename(n))));
			rtnStr = reader.readLine();
			int size = Integer.parseInt(rtnStr);
			/*
			 * Searches for the randomly selected word.
			 */
			for(int i = 0; i < rand.nextInt(size);i++){
				rtnStr = reader.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				reader.close();
			}catch(Exception e){
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		return rtnStr;
	}

	/**
	 * This method will return a list of all possible words including the part inputed.
	 */
	public String[] word(String letters){
		String[] rtnList = null;

		BufferedReader br = null;
		try{
			ArrayList<String> tempList = new ArrayList<String>();//This list will hold all the words that contain the inputed phrase
			for(int i = 3; i < 10; i++){
				br = new BufferedReader(new FileReader(this.getFilename(i)));
				String str = "";
				/*
				 * This loop will read a line and check to see if it contains the phrase then adds it to the tempList
				 */
				do{
					str = br.readLine();
					if(str != null){
						if(str.contains(letters.toUpperCase())){
							tempList.add(str);
						}
					}
				}while(str!=null);
			}
			/*
			 * Checks to see if a word has actually been put into the list, if not the rtnList remains null.
			 */
			if(tempList.size()>0){
				rtnList = new String[tempList.size()];
				for(int i = 0; i < rtnList.length; i++){
					rtnList[i] = tempList.get(i);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}finally{
			try{
				br.close();
			}catch(Exception e){
				e.printStackTrace();
				System.exit(0);
			}
		}

		return rtnList;
	}



}
