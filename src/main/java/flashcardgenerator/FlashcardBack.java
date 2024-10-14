package flashcardgenerator;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.DefaultSplitCharacter;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;

public class FlashcardBack {

    public FlashcardBack() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws DocumentException, ParserConfigurationException, SAXException, IOException {

	final int OFFSET = 0;
	// apparently flashcard height is dependent on batch
	// on the kind where the plastic has an overlap on the blank side when
	// sealed, 0 works. but on the ones where the whole thing is shrink-
	// wrapped, and there is a hole in the wrap, I'll try 3
	// nts: 3 ended up a little low, next time try 2
	// When OFFSET is a positive number, it shall move everything DOWN.
	// the new batch Staples cards have offset 0 so far

	// boolean INVERT_PAGE_ORDER = true; // when true, count down instead of up
	// (unused rn, i might end up not implementing this)
	int INDEX_START = 1;
	int INDEX_END = 2137;

	System.out.println("The width measure of letter sheet (8.5 inches) is " + PageSize.LETTER.getWidth());
	float marginSize = 0;
	Document document = new Document(new Rectangle(360f, 216f), marginSize, marginSize, marginSize, marginSize);

	System.out.println("left margin = " + document.leftMargin());
	System.out.println("right margin = " + document.rightMargin());
	System.out.println("bottom margin = " + document.bottomMargin());
	System.out.println("top margin = " + document.topMargin());
	String filename = "Kanji " + INDEX_START + " to " + INDEX_END + " Back";
	if (OFFSET != 0) {
	    filename += " Offset" + OFFSET;
	}
	filename += ".pdf";

	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

	document.open();

	Font hiragino = FontFactory.getFont("/Users/cubeb/Downloads/hiragino_real.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16f, Font.NORMAL,
		BaseColor.BLACK);
	Font kyokasho = FontFactory.getFont("/Users/cubeb/Downloads/UDDigiKyokashoNP-R-02.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16f,
		Font.NORMAL, BaseColor.BLACK);

	JSONObject kanjiInfo = KanjidicReader.generateJoyoKanjiInfoJson();
	JSONObject kanjiIndex = KanjiIndexer.getIndexedJoyoKanji();

	// baeldung is my daddy

	// comment this out when everything is aligned
//	Jpeg flashcardTemplate = new Jpeg(new URL("file:///Users/cubeb/kanji/flashcard_scan.jpg"));
//	flashcardTemplate.scaleToFit(new Rectangle(360f, 216f));
//	document.add(flashcardTemplate);
	// this tepmlate to align everything should NOT be used in the final print

	// String kanji = "行"; // litmus test

	int weirdCrap = 0;

	for (int k = INDEX_END; k >= INDEX_START; k--) {

	    String kanji = kanjiIndex.getString("" + k);

	    // obtain a brain
	    if (!kanjiInfo.has(kanji)) {
		if (k == 1739) {
		    kanji = "𠮟";
		} else if (k == 1740) {
		    kanji = "鶏";
		}
		System.out.println(k);
		weirdCrap++;
	    }

	    ColumnText definition = new ColumnText(writer.getDirectContent());
	    definition.setSimpleColumn(20, 10, 340, 206 - OFFSET);
	    // kanjiInfo.getJSONObject(kanji).getJSONArray("meanings").toString()
	    // System.out.println(kanji);
	    JSONArray meanings = kanjiInfo.getJSONObject(kanji).getJSONArray("meanings");
	    definition.setText(new Phrase(formatDefinitions(meanings), hiragino));
	    definition.setLeading(0f, 1.15f);
	    definition.go();

	    ColumnText kun = new ColumnText(writer.getDirectContent());
	    JSONArray kunyomi = kanjiInfo.getJSONObject(kanji).getJSONArray("kunReadings");
	    kun.setSimpleColumn(15, 10, 210, 133 - OFFSET);
	    Phrase p = new Phrase("訓 " + formatKunOn(kunyomi), kyokasho); //
	    for (Chunk c : p.getChunks()) {
		c.setSplitCharacter(new DefaultSplitCharacter('　'));
	    }
	    kun.setText(p);
	    kun.setLeading(0f, 1.135f);
	    kun.go();

	    // https://stackoverflow.com/a/11765424
	    writer.getDirectContent().saveState();
	    PdfGState state = new PdfGState();
	    state.setFillOpacity(0f);
	    writer.getDirectContent().setGState(state);
	    writer.getDirectContent().setRGBColorFill(0xFF, 0xFF, 0xFF);
	    writer.getDirectContent().setLineWidth(1);
	    writer.getDirectContent().rectangle(15, 112.5 - OFFSET, 16, 16);
	    writer.getDirectContent().fillStroke();
	    writer.getDirectContent().restoreState();

	    ColumnText on = new ColumnText(writer.getDirectContent());
	    JSONArray onyomi = kanjiInfo.getJSONObject(kanji).getJSONArray("onReadings");
	    on.setSimpleColumn(220, 10, 345, 133 - OFFSET);
	    Phrase q = new Phrase("音 " + formatKunOn(onyomi), kyokasho);
	    for (Chunk c : q.getChunks()) {
		c.setSplitCharacter(new DefaultSplitCharacter('　'));
	    }
	    on.setText(q);
	    on.setLeading(0f, 1.135f);
	    on.go();

	    writer.getDirectContent().saveState();
	    state = new PdfGState();
	    state.setFillOpacity(0f);
	    writer.getDirectContent().setGState(state);
	    writer.getDirectContent().setRGBColorFill(0xFF, 0xFF, 0xFF);
	    writer.getDirectContent().setLineWidth(1);
	    writer.getDirectContent().rectangle(220, 112.5 - OFFSET, 16, 16);
	    writer.getDirectContent().fillStroke();
	    writer.getDirectContent().restoreState();

	    document.newPage();
	}
	document.close();
	System.out.println(weirdCrap);

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

    public static String formatKunOn(JSONArray readings) {
	StringBuilder ret = new StringBuilder();
	for (int i = 0; i < readings.length(); i++) {
	    String reading = readings.getString(i);
	    // ret.append(readings.get(i));
	    for (int j = 0; j < reading.length(); j++) {
		ret.append(reading.charAt(j));
		if (j != reading.length() - 1) {
		    // ret.append("\u2060");
		}
	    }
	    if (i != readings.length() - 1) {
		ret.append("　");
	    }
	}
	return ret.toString();

    }

}
