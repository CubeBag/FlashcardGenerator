todo: license under agpl

you can run anything that has a main method! This is the world's first edible Flashcard Generator Program....

on June 2nd, I received a vision. I was struck by the realization that I can run an index card through a printer. I worked on this code on June 3rd from 5am to 1am the next day. The code is now "done" and kanji flashcards can be easily generated and printed by anyone, but I'm still making tweaks for my own benefit. 

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

- kyokasho is from https://frcoal.cfd/UDDigiKyokashoNP-R-02.ttf hiragino from https://fontsgeek.com/fonts/hiragino-maru-gothic-pron-w4/download kanjidic2 from http://www.edrdg.org/wiki/index.php/KANJIDIC_Project good luck finding the other ones lol

"I'm having a Maven problem / dependency problem"

- me too

"What cards do I buy to make this work?"

- the cards I've settled on are the Staples 3x5in line ruled 5-packs where all the lines are blue. These seem to be pretty good quality, and assuming there is not much variance between batches, you should be able to use offset 0. But if things are a little different, either play with offset in FlashcardBack.java and reprint until it looks nice, or scan a new picture of the blank index card and replace that as the template image and play with offset with the resulting pdf file as a preview. Even if you do not buy this specific index card, any 3x5 index card should work as long as you mess with offset to find the most visually-pleasing result :)
