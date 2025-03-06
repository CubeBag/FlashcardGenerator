package flashcardgenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONObject;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DictionaryIndexer {
    Map<String, ArrayList<JSONObject>> dictIndex;

    public void init() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
	JSONObject kanjiIndexProto = KanjiIndexer.kanjiAsKeys();
	String lol = "";
	Map<String, ArrayList<JSONObject>> kanjiIndex = new HashMap<>();

	for (String k : kanjiIndexProto.keySet()) {

	    kanjiIndex.put(k, new ArrayList<JSONObject>());
	    lol += k;

	}
	JmdictReader a = new JmdictReader();
	a.readCommonWords();
	List<Node> words = a.commonWordsIncludingKanji(Pattern.compile(".*"));

	Pattern k_elePattern = Pattern.compile("k_ele");
	Pattern kebPattern = Pattern.compile("^keb$");
	Pattern r_elePattern = Pattern.compile("r_ele");
	Pattern rebPattern = Pattern.compile("^reb$");
	Pattern sensePattern = Pattern.compile("^sense$");
	Pattern glossPattern = Pattern.compile("^gloss$");
	Pattern ke_priPattern = Pattern.compile("^ke_pri$");

	for (Node word : words) {

	    String kanjis = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, k_elePattern).get(0), kebPattern)
		    .getChildNodes().item(0).getNodeValue();

	    for (int codepoint : kanjis.codePoints().toArray()) {
		String kanji = new String(Character.toChars(codepoint));
		System.out.println(kanji);
		if (kanjiIndex.containsKey(kanji)) {
		    String reading = JmdictReader.getChildNodeByName(JmdictReader.getChildrenNodesByName(word, r_elePattern).get(0), rebPattern)
			    .getChildNodes().item(0).getNodeValue();
		    Priority prio = new Priority();
		    List<Node> ke_pri = JmdictReader.getChildrenNodesByName(JmdictReader.getChildrenNodesByName(word, k_elePattern).get(0),
			    ke_priPattern);
		    if (ke_pri != null) {
			for (Node k : ke_pri) {
			    prio.addPriority(k.getChildNodes().item(0).getNodeValue());
			}
		    }

		    StringBuilder definitions = new StringBuilder();
		    List<Node> definitionsNodes = JmdictReader.getChildrenNodesByName(JmdictReader.getChildNodeByName(word, sensePattern),
			    glossPattern);
		    for (Node def : definitionsNodes) {
			definitions.append(def.getChildNodes().item(0).getNodeValue() + ", ");
		    }
		    definitions.deleteCharAt(definitions.length() - 1);
		    definitions.deleteCharAt(definitions.length() - 1);

		    JSONObject worm = new JSONObject();
		    worm.put("reading", reading);
		    worm.put("priority", prio);
		    worm.put("definition", definitions.toString());

		    if (kanjis.length() > 0) { // change to 1 to test no singular kanji thing
			(kanjiIndex.get(kanji)).add(worm);
		    }

		    System.out.println(worm);

		}
	    }
	    System.out.println(word);
	}

	for (String k : kanjiIndex.keySet()) {
	    Collections.sort(((kanjiIndex.get(k))), (w1, w2) -> (((Priority) w1.get("priority")).compareTo((w2.get("priority")))));

	}

	// System.out.println(kanjiIndex.getJSONArray("満"));
	System.out.println(kanjiIndex.get("島"));

	this.dictIndex = kanjiIndex;
    }

    public DictionaryIndexer() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {

    }

    public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
	DictionaryIndexer a = new DictionaryIndexer();
	a.init();

    }

    public ArrayList<JSONObject> getWordsForKanjiByFrequency(String kanji) {
	return this.dictIndex.get(kanji);
    }

}
