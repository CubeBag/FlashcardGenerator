package flashcardgenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
import com.itextpdf.text.pdf.PdfWriter;

public class Main {

    public Main() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
            throws FileNotFoundException, DocumentException {
        System.out.println("The width measure of letter sheet (8.5 inches) is "
                + PageSize.LETTER.getWidth());
        float marginSize = 10;
        Document document = new Document(new Rectangle(360f, 216f), marginSize,
                marginSize, marginSize, marginSize); // 5 * 612 / 8.5 = 360 and 3 * 612 / 8.5 = 216
        System.out.println("left margin = " + document.leftMargin());
        System.out.println("right margin = " + document.rightMargin());
        System.out.println("bottom margin = " + document.bottomMargin());
        System.out.println("top margin = " + document.topMargin());

        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16,
                BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);

        Chunk chunk2 = new Chunk();

        document.add(chunk);

        Font kyokasho = FontFactory.getFont(
                "/Users/cubeb/Downloads/UDDigiKyokashoNP-R-02.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18f, Font.NORMAL,
                BaseColor.BLACK);

        // https://stackoverflow.com/a/34299793 i steal ur code >:3
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(10, 0, 90, 208);
        ct.setText(new Phrase("String", kyokasho));
        ct.go();

        document.close();

    }

}
