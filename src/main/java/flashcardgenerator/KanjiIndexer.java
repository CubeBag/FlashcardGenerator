package flashcardgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class KanjiIndexer {

    public KanjiIndexer() {
	// TODO Auto-generated constructor stub
    }

    /**
     *
     * Returns a JSON Object where each key-value is in the format:
     *
     * Key : Numerical index for the kanji
     *
     * Value : a String containing the kanji
     *
     * The indexing is done according to https://frcoal.cfd/kanji2.html
     *
     * @return you get the idea
     * @throws IOException
     */
    public static JSONObject getIndexedJoyoKanji() throws IOException {
	JSONObject indexedJoyoKanji = new JSONObject();

	BufferedReader kanjiTextFile = new BufferedReader(new FileReader(new File("/Users/cubeb/kanji/kanji.txt")));

	String allKanji = kanjiTextFile.readLine();

	System.out.println(allKanji.length());

	JSONObject categoryDict = getKanjiCategoryJson();

	int i = 1;
	int trueCount = 1;
	while (i <= allKanji.length()) {
	    int codepoint = allKanji.codePointAt(i - 1);
	    System.out.println(codepoint);
	    String currentKanji = new String(Character.toChars(allKanji.codePointAt(i - 1)));

	    indexedJoyoKanji.put("" + trueCount, currentKanji);
	    System.out.println(currentKanji);
	    i += Character.charCount(codepoint);
	    trueCount += 1;

	}

	return indexedJoyoKanji;

    }

    public static JSONObject kanjiAsKeys() throws IOException {
	JSONObject oldKanjiIndex = KanjiIndexer.getIndexedJoyoKanji();

	JSONObject kanjiIndex = new JSONObject();
	for (String index : oldKanjiIndex.keySet()) {
	    kanjiIndex.put(oldKanjiIndex.getString(index), index);
	}
	return kanjiIndex;
    }

    public static JSONObject getKanjiCategoryJson() throws IOException {
	JSONObject categoryDict = new JSONObject();

	// do it in this order because book numbering takes precedence
	addCategoriesToCategoryDict(categoryDict, "/Users/cubeb/kanji/book_units/");
	addCategoriesToCategoryDict(categoryDict, "/Users/cubeb/kanji/kanshudo_units/");

	return categoryDict;
    }

    public static void main(String[] args) throws IOException {
	getIndexedJoyoKanji();
    }

    public static void addCategoriesToCategoryDict(JSONObject categoryDict, String folderName) throws IOException {

	File categoryFolder = new File(folderName);
	for (File categoryFile : categoryFolder.listFiles()) {
	    String catName = categoryFile.getName();
	    BufferedReader catReader = new BufferedReader(new FileReader(categoryFile));
	    String catKanji = catReader.readLine();
	    int i = 0;
	    while (i < catKanji.length()) {
		int codepoint = catKanji.codePointAt(i);
		String thisKanji = new String(Character.toChars(codepoint));
		if (!categoryDict.has(thisKanji)) {
		    categoryDict.put(thisKanji, catName);
		}
		i += Character.charCount(codepoint);
	    }
	}
    }

}
