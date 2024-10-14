package flashcardgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JmdictReader {

    List<Node> commonWords;

    public JmdictReader() {
	this.commonWords = new ArrayList<>(210000);

    }

    public void readCommonWords() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
	// yeetus https://stackoverflow.com/a/17212654

	File kanjidicXml = new File("/Users/cubeb/Downloads/JMdict_e.xml");

	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(kanjidicXml);
	doc.getDocumentElement().normalize();

	Element root = doc.getDocumentElement();
	NodeList rootChildNodes = root.getChildNodes();

	// System.out.println("Root element :" +
	// doc.getDocumentElement().getNodeName());
	// System.out.println("Number of children : " + rootChildNodes.getLength());

	int countEntries = 0;
	int countCommonWords = 0;

	wordLoop: for (int i = 0; i < rootChildNodes.getLength(); i++) {
	    Node kanjiNode = rootChildNodes.item(i);
	    if (kanjiNode.getNodeName().charAt(0) == '#') {
		// System.out.println("fuck ma ballz");
		continue;
		// this is a fake ass node lmao
	    }

	    countEntries++;
//	    Pattern pattern1 = Pattern.compile("r_ele");
//	    Pattern pattern2 = Pattern.compile("re_pri");
//
//	    List<Node> firstSearch = JmdictReader.getChildrenNodesByName(kanjiNode, pattern1);
//	    for (Node a : firstSearch) {
//		if (JmdictReader.getChildNodeByName(a, pattern2) != null) {
//		    this.commonWords.add(kanjiNode);
//		    countCommonWords++;
//		    continue wordLoop;
//		}
//	    }
	    if (this.isCommon(kanjiNode)) {
		this.commonWords.add(kanjiNode);
		countCommonWords++;
		continue wordLoop;
	    }

	}

	System.out.println("" + countEntries + " entries");
	System.out.println("" + countCommonWords + " common words");

    }

    public List<Node> commonWordsIncludingKanji(Pattern kanji) {
	List<Node> matchedWords = new ArrayList<>(50);
	Pattern k_elePattern = Pattern.compile("k_ele");
	Pattern kebPattern = Pattern.compile("^keb$");

	outerForLoop: for (Node word : this.commonWords) {
	    List<Node> k_eleList = JmdictReader.getChildrenNodesByName(word, k_elePattern);
	    for (Node k_eleNode : k_eleList) {
		Node keb = JmdictReader.getChildNodeByName(k_eleNode, kebPattern);

		if (keb != null && kanji.matcher(keb.getChildNodes().item(0).getNodeValue()).find()) {

		    matchedWords.add(word);
		    continue outerForLoop;
		}
	    }
	}
	return matchedWords;
    }

    public static Node getChildNodeByName(Node node, Pattern searchPattern) {
	// return null if not found
	if (node == null) {
	    return null;
	}
	for (int i = 0; i < node.getChildNodes().getLength(); i++) {
	    if (searchPattern.matcher(node.getChildNodes().item(i).getNodeName()).find()) {
		return node.getChildNodes().item(i);
	    }
	}
	return null;
    }

    public static List<Node> getChildrenNodesByName(Node node, Pattern searchPattern) {
	// return null if not found
	List<Node> ret = new ArrayList<>(5);
	if (node == null) {
	    return ret;
	}

	for (int i = 0; i < node.getChildNodes().getLength(); i++) {
	    if (searchPattern.matcher(node.getChildNodes().item(i).getNodeName()).find()) {
		ret.add(node.getChildNodes().item(i));
	    }
	}
	return ret;
    }

    public boolean isCommon(Node node) {
	Pattern pattern1 = Pattern.compile("k_ele|r_ele");
	Pattern pattern2 = Pattern.compile("ke_pri|re_pri");

	List<Node> firstSearch = JmdictReader.getChildrenNodesByName(node, pattern1);
	for (Node a : firstSearch) {
	    List<Node> secondSearch = JmdictReader.getChildrenNodesByName(a, pattern2);
	    for (Node b : secondSearch) {
		String commonClassifiction = b.getChildNodes().item(0).getNodeValue();
		if (commonClassifiction.equals("news1") || commonClassifiction.equals("spec1") || commonClassifiction.equals("ichi1")) {

		    return true;
		}
	    }
	}
	return false;
    }

}
