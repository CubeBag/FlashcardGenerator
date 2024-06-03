package flashcardgenerator;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

public class DictReaderTester {

    public DictReaderTester() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
	// this method wishes it were a unit test! too bad! we can't all be happy

	JSONObject joyoKanji = JMdictReader.generateJoyoKanjiInfoJson();

	String kanjiToGet = "版";

	System.out.println("I want info on the kanji " + kanjiToGet);

	JSONObject na = joyoKanji.getJSONObject(kanjiToGet);

	System.out.println("Kunyomi Readings: " + na.getJSONArray("kunReadings"));
	System.out.println("Onyomi Readings: " + na.getJSONArray("onReadings"));
	System.out.println("Meanings: " + na.getJSONArray("meanings"));

    }

}
