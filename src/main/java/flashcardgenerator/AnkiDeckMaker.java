package flashcardgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AnkiDeckMaker {

    public AnkiDeckMaker() {
	// TODO Auto-generated constructor stub
    }

    public static JSONArray getVocabWordsFromPattern(Pattern kanjiPattern, boolean useFutureKanji)
	    throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
	JmdictReader a = new JmdictReader();
	a.readCommonWords();
	List<Node> words = a.commonWordsIncludingKanji(kanjiPattern);

	Pattern k_elePattern = Pattern.compile("k_ele");
	Pattern kebPattern = Pattern.compile("^keb$");
	Pattern r_elePattern = Pattern.compile("r_ele");
	Pattern rebPattern = Pattern.compile("^reb$");
	Pattern sensePattern = Pattern.compile("^sense$");
	Pattern glossPattern = Pattern.compile("^gloss$");

	JSONObject flippedIndex = KanjiIndexer.kanjiAsKeys();

	JSONArray words2 = new JSONArray();

	int end = 0;
	if (!useFutureKanji) {
	    JSONObject regularIndex = KanjiIndexer.getIndexedJoyoKanji();
	    for (int i = 1; i < regularIndex.length(); i++) {
		String curKanji = regularIndex.getString("" + i);
		if (kanjiPattern.matcher(curKanji).find()) {
		    end = i;
		}
	    }
	}

	wordloop: for (Node word : words) {
	    JSONObject word2 = new JSONObject();
	    String kanji;
	    String reading;
	    StringBuilder definitions = new StringBuilder();

	    // sanity check to ensure it's not some super uncommon kanji form
	    kanji = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, k_elePattern).get(0), kebPattern).getChildNodes()
		    .item(0).getNodeValue();
	    if (!kanjiPattern.matcher(kanji).find()) {
		continue;
	    }

	    if (!useFutureKanji) {

		for (char c : kanji.toCharArray()) {
		    String trolled = Character.toString(c);
		    if (flippedIndex.has(trolled) && Integer.parseInt(flippedIndex.getString(trolled)) > end) {
			continue wordloop;
			// the 𠮟鶏 issue will rear its ugly head soon, idk how to deal with it yet
		    }
		}
	    }

	    reading = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, r_elePattern).get(0), rebPattern).getChildNodes()
		    .item(0).getNodeValue();

	    List<Node> definitionsNodes = JmdictReader.getChildrenNodesByName(JmdictReader.getChildNodeByName(word, sensePattern), glossPattern);
	    for (Node def : definitionsNodes) {
		definitions.append(def.getChildNodes().item(0).getNodeValue() + ", ");
	    }
	    definitions.deleteCharAt(definitions.length() - 1);
	    definitions.deleteCharAt(definitions.length() - 1);

	    word2.put("kanji", kanji);
	    word2.put("reading", reading);
	    word2.put("definition", definitions.toString());
	    words2.put(word2);

	}

	return words2;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
	long startTime = System.currentTimeMillis();

	// a

	// a

	// Pattern kanjiPattern = Pattern.compile("力|身|界|重|品|町|計|建|医|花|走|歌|堂|鳥|夕|牛");
	int start = 451;
	int end = 460;

	Pattern kanjiPattern = getPatternFromKanjiRange(start, end);
	boolean useFutureKanji = false;

	JSONArray vocabWords = getVocabWordsFromPattern(kanjiPattern, useFutureKanji);

	System.out.println(vocabWords.length());

	BufferedWriter writer = new BufferedWriter(new FileWriter("Anki, Kanji Applications, " + start + "-" + end + ".txt"));

	for (int i = 0; i < vocabWords.length(); i++) {
	    JSONObject k = vocabWords.getJSONObject(i);

	    writer.write(k.getString("kanji") + "<br>" + k.getString("reading") + "	" + k.getString("definition"));
	    writer.newLine();
	    System.out.println(k.getString("kanji") + "<br>" + k.getString("reading") + "	" + k.getString("definition"));
	}

	writer.close();
	// a

	// a

	long endTime = System.currentTimeMillis();
	float timeElapsed = ((float) (endTime - startTime)) / 1000;
	System.out.println("time taken: " + timeElapsed);

    }

    public static Pattern getPatternFromKanjiRange(int start, int end) throws IOException {
	JSONObject kanjiIndex = KanjiIndexer.getIndexedJoyoKanji(); // optimize want to but my brain is tired
	StringBuilder stringpat = new StringBuilder();
	for (int i = start; i <= end; i++) {
	    stringpat.append(kanjiIndex.getString("" + i));
	    stringpat.append('|');
	}
	stringpat.deleteCharAt(stringpat.length() - 1);
	return Pattern.compile(stringpat.toString());

    }

}
