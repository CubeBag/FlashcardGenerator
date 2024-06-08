package flashcardgenerator;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class FlashcardFront {

    public FlashcardFront() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws DocumentException, IOException {

	int INDEX_START = 401;
	int INDEX_END = 450;

	System.out.println("The width measure of letter sheet (8.5 inches) is " + PageSize.LETTER.getWidth());
	float marginSize = 10;
	Document document = new Document(new Rectangle(360f, 216f), marginSize, marginSize, marginSize, marginSize);
	// 5 * 612 / 8.5 = 360, and 3 * 612 / 8.5 = 216

	System.out.println("left margin = " + document.leftMargin());
	System.out.println("right margin = " + document.rightMargin());
	System.out.println("bottom margin = " + document.bottomMargin());
	System.out.println("top margin = " + document.topMargin());

	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Kanji " + INDEX_START + " to " + INDEX_END + " Front.pdf"));

	document.open();
	Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
	// Chunk chunk = new Chunk("Hello World", font);

	// Chunk chunk2 = new Chunk();

	// document.add(chunk);

	Font kyokasho = FontFactory.getFont("/Users/cubeb/Downloads/UDDigiKyokashoNP-R-02.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 150f,
		Font.NORMAL, BaseColor.BLACK);
	Font hiragino = FontFactory.getFont("/Users/cubeb/Downloads/hiragino_real.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 24f, Font.NORMAL,
		BaseColor.BLACK);

	JSONObject kanjiIndex = KanjiIndexer.getIndexedJoyoKanji();
	JSONObject kanjiCategory = KanjiIndexer.getKanjiCategoryJson();

	// https://stackoverflow.com/a/34299793 i steal ur code >:3
	for (int i = INDEX_START; i <= INDEX_END; i++) {
	    ColumnText index = new ColumnText(writer.getDirectContent());
	    index.setSimpleColumn(17, 0, 150, 198);
	    index.setText(new Phrase("#" + i, hiragino));
	    index.go();

	    String kanji = kanjiIndex.getString("" + i);

	    ColumnText category = new ColumnText(writer.getDirectContent());
	    category.setAlignment(Element.ALIGN_RIGHT);
	    category.setSimpleColumn(0, 0, 343, 198);
	    category.setText(new Phrase(kanjiCategory.getString(kanji), hiragino));
	    category.go();

	    ColumnText kanjiText = new ColumnText(writer.getDirectContent());
	    kanjiText.setAlignment(Element.ALIGN_CENTER);
	    kanjiText.setSimpleColumn(0, -500, 360, 66);
	    kanjiText.setText(new Phrase(kanji, kyokasho));
	    kanjiText.go();

	    document.newPage();
	}

	document.close();

    }

}
