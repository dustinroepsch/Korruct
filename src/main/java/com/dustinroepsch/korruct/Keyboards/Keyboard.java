package com.dustinroepsch.korruct.Keyboards;

import java.util.List;
import java.util.OptionalInt;

/**
 * A Keyboard is a representation of a physical keyboard, used by SpellCorrector to evaluate
 * probability a word is a typo of another word.
 */
public abstract class Keyboard {

  private String[] keyTable = null;
  private String keys = null;

  /**
   * Finds the smallest keyboard distance number of changes needed to arrive from wordA to wordB.
   *
   * @param wordA
   * @param wordB
   * @return The minimum distance.
   */
  public int getMinDistance(String wordA, String wordB) {
    return 0; // TODO: Use Dynamic Programming to figure out the least-distance way to get from
    // wordA to wordB
  }

  /**
   * Checks if this keyboard is able to type the supplied word. I.E The word only contains
   * characters that can be typed by this keyboard.
   *
   * @param word A word (token).
   * @return The word is type-able by this keyboard.
   */
  public boolean isValidWord(String word) {
    checkKeyboardSet();
    return word.chars().mapToObj(i -> (char) i).allMatch(c -> keys.contains(String.valueOf(c)));
  }

  /**
   * Finds the Euclidean distance between two keys on the keyboard, or Optional.empty if either key
   * is not typeable by this keyboard.
   *
   * @param charA
   * @param charB
   * @return The euclidean distance, if possible.
   */
  public OptionalInt getDistance(char charA, char charB) {
    checkKeyboardSet();
    boolean foundA = false;
    boolean foundB = false;
    for (char c : keys.toCharArray()) {
      if (c == charA) {
        foundA = true;
      }
      if (c == charB) {
        foundB = true;
      }
    }

    if (!foundA || !foundB) {
      return OptionalInt.empty();
    }

    return OptionalInt.of(0); // TODO: Use keyTable to get the euclid distance between two keys.
  }

  /**
   * A keyboard layout, each row of the keybaord is a string. For example, a QWERTY keyboard looks
   * like {"qwertyuiop", "asdfghjkl", "zxcvbnm"}
   *
   * @return The keyboard layout.
   */
  abstract String[] getKeyboardLayout();

  private void checkKeyboardSet() {
    if (keyTable == null || keys == null) {
      keyTable = getKeyboardLayout();
      keys = List.of(keyTable).stream().distinct().reduce("", (a, s) -> a + s);
    }
  }
}
