package flashcardgenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

public class FlashcardBack {

    public FlashcardBack() {
	// TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
	System.out.println("The width measure of letter sheet (8.5 inches) is " + PageSize.LETTER.getWidth());
	float marginSize = 10;
	Document document = new Document(new Rectangle(360f, 216f), marginSize, marginSize, marginSize, marginSize);

    }

}
