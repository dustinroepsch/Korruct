Korruct
=========
Korruct is a simple Spell Correcting library for Java.


'''java
public class ExampleUsage {
  public static void main(String[] args) throws IOException {
    Keyboard keyboard = new QWERTYKeyboard();

    // Create a spellCorrector with a US English (QWERTY) keyboard and a list of english words.
    SpellCorrector spellCorrector =
        new SpellCorrector(FileSystems.getDefault().getPath("common_english_words.txt"), keyboard);

    Scanner in = new Scanner(System.in);

    while (true) {
      System.out.println("Enter a word:");
      String inputWord = in.next();
      if (inputWord.trim().equals("q")) {
        break;
      }

      // Attempt to correct the input word
      Optional<String> correction = spellCorrector.getCorrection(inputWord);
      System.out.println(correction.map(s -> "Did you mean: " + s).orElse("No typo!"));
    }
  }
}
''''