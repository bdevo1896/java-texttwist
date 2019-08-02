import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Test;


public class WordDictionaryTests {

	@Test
	public void testGeneration() throws Exception {
		WordDictionary w = new WordDictionary();
		assertTrue(w!=null);
	}
	
	@Test
	public void testGetFileName(){
		WordDictionary w = new WordDictionary();
		
		assertTrue("3.txt".equals(w.getFilename(3)));
	}
	
	@Test
	public void testWordInDictionaryFalse()throws IOException{
		WordDictionary w = new WordDictionary();		
		assertFalse(w.wordInDictionary("hi"));
	}
	
	@Test
	public void testWordInDictionaryTrue() throws IOException{
		WordDictionary w = new WordDictionary();		
		assertTrue(w.wordInDictionary("lie".toUpperCase()));
	}
	
	@Test
	public void testFetchRandomWord() throws IOException{
		WordDictionary w = new WordDictionary();		
		String str = w.fetchRandomWord(3);
		
		assertTrue(str.length()==3);
		assertTrue(w.wordInDictionary(str));
	}
	
	@Test
	public void testWordsFilledList(){
		WordDictionary w = new WordDictionary();
		String[] list = w.word("can");
		assertTrue(list!=null);
	}
	
	@Test
	public void testWordsEmptyList(){
		WordDictionary w = new WordDictionary();
		String[] list = w.word("xxxxc");
		assertTrue(list==null);
	}

}
