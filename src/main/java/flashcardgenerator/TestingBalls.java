package flashcardgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class TestingBalls {

    public TestingBalls() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws IOException {
	// we are going to fix 𠮟鶏 once and for all.

//	JSONObject indexedJoyoKanji = new JSONObject();
//
//	InputStreamReader kanjiTextFile = new InputStreamReader(new FileInputStream(new File("/Users/cubeb/kanji/kanji_utf16be.txt")), "UTF-16");
//
//	// String allKanji = kanjiTextFile.readLine();
//
//	// System.out.println(allKanji.length());
//
//	JSONObject categoryDict = getKanjiCategoryJson();
//
//	// for (int i = 1; i <= allKanji.length(); i++) {
//
//	int readChar = kanjiTextFile.read();
//	int i = 1;
//	while (readChar != -1) {
//	    System.out.println(readChar);
//
//	    String currentKanji = Character.toString((char) readChar);
//
////	    if (i == 1739) {
////		currentKanji = "𠮟";
////		System.out.println("workaround applied i=1739");
////	    } else if (i == 1740) {
////		currentKanji = "鶏";
////		System.out.println("workaround applied i=1740");
////	    }
//
//	    indexedJoyoKanji.put("" + i, currentKanji);
//	    System.out.println(currentKanji);
//	    // System.out.println(currentKanji + "=" + categoryDict.get(currentKanji));
//
//	    readChar = kanjiTextFile.read();
//	    i++;
//	}
//	return;

	JSONObject indexedJoyoKanji = new JSONObject();

	BufferedReader kanjiTextFile = new BufferedReader(new FileReader(new File("/Users/cubeb/kanji/kanji.txt")));

	String allKanji = kanjiTextFile.readLine();

	System.out.println(allKanji.length());

	JSONObject categoryDict = getKanjiCategoryJson();

	int i = 1;
	while (i <= allKanji.length()) {
	    int codepoint = allKanji.codePointAt(i - 1);
	    System.out.println(codepoint);
	    String currentKanji = new String(Character.toChars(allKanji.codePointAt(i - 1)));

	    indexedJoyoKanji.put("" + i, currentKanji);
	    System.out.println(currentKanji);
	    i += Character.charCount(codepoint);

	}

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
