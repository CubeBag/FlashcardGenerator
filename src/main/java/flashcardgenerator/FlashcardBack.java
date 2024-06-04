package flashcardgenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;

public class FlashcardBack {

    public FlashcardBack() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws DocumentException, ParserConfigurationException, SAXException, IOException {

	boolean INVERT_PAGE_ORDER = true; // when true, count down instead of up
	int INDEX_START = 0;
	int INDEX_END = 2137;

	System.out.println("The width measure of letter sheet (8.5 inches) is " + PageSize.LETTER.getWidth());
	float marginSize = 0;
	Document document = new Document(new Rectangle(360f, 216f), marginSize, marginSize, marginSize, marginSize);

	System.out.println("left margin = " + document.leftMargin());
	System.out.println("right margin = " + document.rightMargin());
	System.out.println("bottom margin = " + document.bottomMargin());
	System.out.println("top margin = " + document.topMargin());

	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Kanji " + INDEX_START + " to " + INDEX_END + " Back.pdf"));

	document.open();

	Font hiragino = FontFactory.getFont("/Users/cubeb/Downloads/hiragino_real.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16f, Font.NORMAL,
		BaseColor.BLACK);
	Font kyokasho = FontFactory.getFont("/Users/cubeb/Downloads/UDDigiKyokashoNP-R-02.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16f,
		Font.NORMAL, BaseColor.BLACK);

	JSONObject kanjiInfo = JMdictReader.generateJoyoKanjiInfoJson();

	// baeldung is my daddy

	// comment this out when everything is aligned
	Jpeg flashcardTemplate = new Jpeg(new URL("file:///Users/cubeb/kanji/flashcard_scan.jpg"));
	flashcardTemplate.scaleToFit(new Rectangle(360f, 216f));
	document.add(flashcardTemplate);
	// this tepmlate to align everything lol

	String kanji = "行"; // litmus test

	ColumnText definition = new ColumnText(writer.getDirectContent());

	definition.setSimpleColumn(20, 10, 340, 206);
	// kanjiInfo.getJSONObject(kanji).getJSONArray("meanings").toString()
	JSONArray meanings = kanjiInfo.getJSONObject(kanji).getJSONArray("meanings");
	definition.setText(new Phrase(formatDefinitions(meanings) + "\n" + formatDefinitions(meanings), hiragino));
	definition.setLeading(0f, 1.15f);
	definition.go();

	ColumnText kun = new ColumnText(writer.getDirectContent());
	kun.setSimpleColumn(15, 0, 170, 133);
	kun.setText(new Phrase("訓　ああああああああ\nわんぴーすは\n実在する\nあいうえお\nかきくけこ\nさしすせそそそそそそそそそそ\nたちつてと", kyokasho));
	kun.setLeading(0f, 1.135f);
	kun.go();

	// https://stackoverflow.com/a/11765424
	writer.getDirectContent().saveState();
	PdfGState state = new PdfGState();
	state.setFillOpacity(0f);
	writer.getDirectContent().setGState(state);
	writer.getDirectContent().setRGBColorFill(0xFF, 0xFF, 0xFF);
	writer.getDirectContent().setLineWidth(1);
	writer.getDirectContent().rectangle(15, 112.5, 16, 16);
	writer.getDirectContent().fillStroke();
	writer.getDirectContent().restoreState();

	ColumnText on = new ColumnText(writer.getDirectContent());
	on.setSimpleColumn(190, 0, 345, 133);
	on.setText(new Phrase("音　アイウエオ\nカキクケコ\nサシスセソ\nタチツテト\nナニヌネノ\nハヒフヘホホホホホホホホホホホホホホホホホ", kyokasho));
	on.setLeading(0f, 1.135f);
	on.go();

	writer.getDirectContent().saveState();
	state = new PdfGState();
	state.setFillOpacity(0f);
	writer.getDirectContent().setGState(state);
	writer.getDirectContent().setRGBColorFill(0xFF, 0xFF, 0xFF);
	writer.getDirectContent().setLineWidth(1);
	writer.getDirectContent().rectangle(190, 112.5, 16, 16);
	writer.getDirectContent().fillStroke();
	writer.getDirectContent().restoreState();

	document.close();

    }

    public static String formatDefinitions(JSONArray definitions) {
	StringBuilder ret = new StringBuilder();
	for (int i = 0; i < definitions.length(); i++) {
	    ret.append(definitions.get(i));
	    if (i != definitions.length() - 1) {
		ret.append(";  ");
	    }
	}
	return ret.toString();

    }

}
