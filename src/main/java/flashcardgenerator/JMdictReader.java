package flashcardgenerator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JMdictReader {

    public JMdictReader() {
	// TODO Auto-generated constructor stub
    }

    public static JSONObject generateJoyoKanjiInfoJson()
	    throws ParserConfigurationException, SAXException, IOException {
	// yeetus https://stackoverflow.com/a/17212654

	File kanjidicXml = new File("/Users/cubeb/Downloads/kanjidic2.xml");

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(kanjidicXml);
	doc.getDocumentElement().normalize();

	Element root = doc.getDocumentElement();
	NodeList rootChildNodes = root.getChildNodes();

	// System.out.println("Root element :" +
	// doc.getDocumentElement().getNodeName());
	// System.out.println("Number of children : " + rootChildNodes.getLength());

	JSONObject joyoKanji = new JSONObject();

	for (int i = 0; i < rootChildNodes.getLength(); i++) {
	    Node kanjiNode = rootChildNodes.item(i);
	    if (kanjiNode.getNodeName().charAt(0) == '#') {
		continue;
		// this is a fake ass node lmao
	    }

	    JSONArray kunReadings = new JSONArray();
	    JSONArray onReadings = new JSONArray();
	    JSONArray meanings = new JSONArray();
	    String kanji = null;
	    boolean foundGrade = false;

	    NodeList kanjiNodeChildren = kanjiNode.getChildNodes();
	    for (int j = 0; j < kanjiNodeChildren.getLength(); j++) {
		Node currentProperty = kanjiNodeChildren.item(j);

		// System.out.println("Node Name : " + currentProperty.getNodeName());
		// System.out.println("Node Text Content : " +
		// currentProperty.getTextContent());

		switch (currentProperty.getNodeName()) {
		case "literal":
		    kanji = currentProperty.getTextContent();
		    break;
		case "reading_meaning":
		    NodeList readingsMeanings = currentProperty.getChildNodes().item(1).getChildNodes();
		    // System.out.println(readingsMeanings.item(0).getNodeName());
		    // item 1 is actually the second because the first is #text frcoal
		    for (int k = 0; k < readingsMeanings.getLength(); k++) {
			if (readingsMeanings.item(k).getNodeName().equals("reading")) {
			    String readingType = readingsMeanings.item(k).getAttributes().item(0).getNodeValue();
			    if (readingType.equals("ja_on")) {
				onReadings.put(readingsMeanings.item(k).getTextContent());
			    } else if (readingType.equals("ja_kun")) {
				kunReadings.put(readingsMeanings.item(k).getTextContent());
			    }
			} else if (readingsMeanings.item(k).getNodeName().equals("meaning")
				&& !readingsMeanings.item(k).hasAttributes()) {
			    meanings.put(readingsMeanings.item(k).getTextContent());
			}

		    }
		    break;
		case "misc":
		    NodeList miscChildren = currentProperty.getChildNodes();

		    for (int k = 0; k < miscChildren.getLength(); k++) {
			if (miscChildren.item(k).getNodeName().equals("grade")) {
			    int grade = Integer.parseInt(miscChildren.item(k).getTextContent());
			    // System.out.println(" Grade : " + grade);
			    if (grade <= 8) {
				// this is a joyo kanji
				foundGrade = true;
			    }

			}
		    }

		    break;
		}

	    }

	    // outside for j

	    if (foundGrade) {
		JSONObject kanjiDetails = new JSONObject();
		kanjiDetails.put("kunReadings", kunReadings);
		kanjiDetails.put("onReadings", onReadings);
		kanjiDetails.put("meanings", meanings);
		joyoKanji.put(kanji, kanjiDetails);

	    }

	}

	JSONObject repeatPrevKanji = new JSONObject();
	repeatPrevKanji.put("kunReadings", new JSONArray());
	repeatPrevKanji.put("onReadings", new JSONArray());
	JSONArray onemeaning = new JSONArray();
	onemeaning.put("kanji repetition mark (repeat previous kanji)");
	repeatPrevKanji.put("meanings", onemeaning);
	joyoKanji.put("々", repeatPrevKanji);
	// technically this is not a kanji but the book gives it an index

	// outside for i

	// System.out.println("Found " + joyoKanji.length() + " joyo kanji.");
	// should be 2136

	return joyoKanji;

    }

}
