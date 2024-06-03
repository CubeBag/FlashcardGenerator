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

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
	// yeetus https://stackoverflow.com/a/17212654

	File kanjidicXml = new File("/Users/cubeb/Downloads/kanjidic2.xml");

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(kanjidicXml);
	doc.getDocumentElement().normalize();

	Element root = doc.getDocumentElement();
	NodeList rootChildNodes = root.getChildNodes();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	System.out.println("Number of children : " + rootChildNodes.getLength());

	// Document shortenedKanji = dBuilder.newDocument();

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

		System.out.println("Node Name : " + currentProperty.getNodeName());
		System.out.println("Node Text Content : " + currentProperty.getTextContent());

		switch (currentProperty.getNodeName()) {
		case "literal":
		    kanji = currentProperty.getTextContent();
		    break;
		case "reading_meaning":
		    NodeList readingsMeanings = currentProperty.getChildNodes().item(1).getChildNodes();
		    System.out.println(readingsMeanings.item(0).getNodeName());
		    // item 1 is actually the second because the first is #text frcoal
		    // System.out.println(" Inside Readingmeaning Loop : " + readings.getLength());
		    for (int k = 0; k < readingsMeanings.getLength(); k++) {
			if (readingsMeanings.item(k).getNodeName().equals("reading")) {
			    // System.out.println(" Reading Found : " + readings.item(k).getTextContent() +
			    // " ---- " + readings.item(k).getAttributes().item(0).getNodeValue());
			    String readingType = readingsMeanings.item(k).getAttributes().item(0).getNodeValue();
			    if (readingType.equals("ja_on")) {
				onReadings.put(readingsMeanings.item(k).getTextContent());
			    } else if (readingType.equals("ja_kun")) {
				kunReadings.put(readingsMeanings.item(k).getTextContent());
			    }
			} else if (readingsMeanings.item(k).getNodeName().equals("meaning")
				&& !readingsMeanings.item(k).hasAttributes()) {
			    meanings.put(readingsMeanings.item(k).getTextContent());
			    // System.out.println(" Meaning found :: " +
			    // readingsMeanings.item(k).getTextContent());
			}

		    }
		    break;
		case "misc":
		    // System.out.println(currentProperty.getChildNodes().getLength());
		    NodeList miscChildren = currentProperty.getChildNodes();
//		    Node grade = currentProperty.getChildNodes().item(1);
//		    System.out.println(misc0.getNodeName() + misc0.getTextContent());
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
//		    System.out.println(currentProperty.getChildNodes().toString());
//		    System.out.println(currentProperty.getChildNodes().item(1).getChildNodes().getLength());
//		    NodeList meaningsNodeList = currentProperty.getChildNodes().item(2).getChildNodes();
//
//		    for (int k = 0; k < meaningsNodeList.getLength(); k++) {
//
//			if (meaningsNodeList.item(k).getNodeName().equals("meaning")
//				&& !readings.item(k).hasAttributes()) {
//			    meanings.put(meaningsNodeList.item(k).getTextContent());
//			    System.out.println(" Meaning found :: " + meaningsNodeList.item(k).getTextContent());
//			}
//		    }

//                        System.out.println("Attributes of reading: "
//                                + currentProperty.getAttributes());
////                        if (currentProperty.hasAttributes() && currentProperty.getAttributes().) {
////                            if (currentProperty.getAttributes().)
////                        }
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

	// outside for i

	System.out.println("Found " + joyoKanji.length() + " joyo kanji.");

    }

}
