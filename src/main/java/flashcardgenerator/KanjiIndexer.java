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

	for (int i = 1; i <= allKanji.length(); i++) {
	    String currentKanji = allKanji.substring(i - 1, i);

	    if (i == 1739) {
		currentKanji = "𠮟";
		System.out.println("workaround applied i=1739");
	    } else if (i == 1740) {
		currentKanji = "鶏";
		System.out.println("workaround applied i=1740");
	    }

	    indexedJoyoKanji.put("" + i, currentKanji);
	    // System.out.println(currentKanji + "=" + categoryDict.get(currentKanji));

	}

	return indexedJoyoKanji;

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
	    for (int i = 0; i < catKanji.length(); i++) {
		if (!categoryDict.has(catKanji.substring(i, i + 1))) {
		    categoryDict.put(catKanji.substring(i, i + 1), catName);
		}
	    }
	}
    }

}
