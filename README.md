todo: license under agpl

you can run anything that has a main method! This is the world's first edible Flashcard Generator Program....

on June 2nd, I received a vision. I was struck by the realization that I can run an index card through a printer. I spent all of June 3rd carrying this out, while researching how I can obtain many flashcards. The code is now "done" and kanji flashcards can be easily generated and printed by anyone.

### Usage

Step 1. Buy an obscene amount of 3x5 index cards

Step 2. ???

Step 3. Grind

### Customization

"How do I change the index numbers for each kanji?"

- Modify getIndexedJoyoKanji() inside KanjiIndexer

"How do I set custom categories for each kanji?"

- Modify getKanjiCategoryJson() inside KanjiIndexer

"How do I change the stuff on the back of the cards?"

- Modify generateJoyoKanjiInfoJson() inside JMdictReader (actually this class is a misnomer because I ended up not using jmdict)

### FAQ

"Where do I get the files used to generate these cards?"

- kyokasho is from https://frcoal.cfd/UDDigiKyokashoNP-R-02.ttf hiragino from https://fontsgeek.com/fonts/hiragino-maru-gothic-pron-w4/download jmdict2 kanjidic2 from http://www.edrdg.org/wiki/index.php/KANJIDIC_Project good luck finding the other ones lol
