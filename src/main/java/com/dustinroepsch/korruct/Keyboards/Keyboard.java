package com.dustinroepsch.korruct.Keyboards;

import java.util.List;

/**
 * A Keyboard is a representation of a physical keyboard, used by SpellCorrector to evaluate
 * probability a word is a typo of another word.
 */
public abstract class Keyboard {

  private String[] keyTable = null;
  private String keys = null;

  public int getMinDistance(String wordA, String wordB) {
    return 0; // TODO: Use Dynamic Programming to figure out the least-distance way to get from
    // wordA to wordB
  }

  public boolean isValidWord(String word) {
    checkKeyboardSet();
    return word.chars().mapToObj(i -> (char) i).allMatch(c -> keys.contains(String.valueOf(c)));
  }

  public int getDistance(char charA, char charB) {
    return 0; // TODO: Use keyTable to get the euclid distance between two keys.
  }

  abstract String[] getKeyboardLayout();

  private void checkKeyboardSet() {
    if (keyTable == null || keys == null) {
      keyTable = getKeyboardLayout();
      keys = List.of(keyTable).stream().distinct().reduce("", (a, s) -> a + s);
    }
  }
}
