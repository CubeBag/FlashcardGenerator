package flashcardgenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AnkiDeckMaker {

    public AnkiDeckMaker() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
	long startTime = System.currentTimeMillis();

	// a

	// a

	JmdictReader a = new JmdictReader();
	a.readCommonWords();

	// Pattern kanjiPattern = Pattern.compile("力|身|界|重|品|町|計|建|医|花|走|歌|堂|鳥|夕|牛");
	int start = 451;
	int end = 460;
	Pattern kanjiPattern = getPatternFromKanjiRange(start, end);
	List<Node> words = a.commonWordsIncludingKanji(kanjiPattern);

	System.out.println(words.size());
	Pattern k_elePattern = Pattern.compile("k_ele");
	Pattern kebPattern = Pattern.compile("^keb$");
	Pattern r_elePattern = Pattern.compile("r_ele");
	Pattern rebPattern = Pattern.compile("^reb$");
	Pattern sensePattern = Pattern.compile("^sense$");
	Pattern glossPattern = Pattern.compile("^gloss$");

	BufferedWriter writer = new BufferedWriter(new FileWriter("Anki, Kanji Applications, " + start + "-" + end + ".txt"));

	for (Node word : words) {
	    String kanji;
	    String reading;
	    StringBuilder definitions = new StringBuilder();

	    // sanity check to ensure it's not some super uncommon kanji form
	    kanji = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, k_elePattern).get(0), kebPattern).getChildNodes()
		    .item(0).getNodeValue();
	    if (!kanjiPattern.matcher(kanji).find()) {
		continue;
	    }

	    reading = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, r_elePattern).get(0), rebPattern).getChildNodes()
		    .item(0).getNodeValue();

	    List<Node> definitionsNodes = JmdictReader.getChildrenNodesByName(JmdictReader.getChildNodeByName(word, sensePattern), glossPattern);
	    for (Node def : definitionsNodes) {
		definitions.append(def.getChildNodes().item(0).getNodeValue() + ", ");
	    }
	    definitions.deleteCharAt(definitions.length() - 1);
	    definitions.deleteCharAt(definitions.length() - 1);

	    writer.write(kanji + "<br>" + reading + "	" + definitions.toString());
	    writer.newLine();
	    System.out.println(kanji + "<br>" + reading + "	" + definitions.toString());

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
