package com.dustinroepsch.korruct.Keyboards;

import java.util.*;
import java.util.stream.Collectors;

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
  public double getMinDistance(String wordA, String wordB) {
    return getMinDistanceRecur(wordA, wordB, 0, 0, new HashMap<>());
  }

  private double getMinDistanceRecur(
      String wordA, String wordB, int wordAindex, int wordBindex, HashMap<IntPair, Double> memo) {
    IntPair memoKey = IntPair.builder().setAIndex(wordAindex).setBIndex(wordBindex).build();
    if (memo.containsKey(memoKey)) {
      return memo.get(memoKey);
    }

    // albama
    // alabama
    if (wordAindex >= wordA.length() || wordBindex >= wordB.length()) {
      return 0;
    }

    // insert
    double insertCost = 1 + getMinDistanceRecur(wordA, wordB, wordAindex + 1, wordBindex, memo);

    // delete
    double deleteCost = 1 + getMinDistanceRecur(wordA, wordB, wordAindex, wordBindex + 1, memo);

    // change
    double changeCost =
        getDistance(wordA.charAt(wordAindex), wordB.charAt(wordBindex)).getAsDouble()
            + getMinDistanceRecur(wordA, wordB, wordAindex + 1, wordBindex + 1, memo);

    double min = Math.min(insertCost, Math.min(deleteCost, changeCost));

    memo.put(memoKey, min);

    return min;
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
  public OptionalDouble getDistance(char charA, char charB) {
    checkKeyboardSet();

    Optional<KeyboardPosition> aPosition = Optional.empty();
    Optional<KeyboardPosition> bPosition = Optional.empty();

    for (int row = 0; row < keyTable.length; row++) {
      for (int col = 0; col < keyTable[row].length(); col++) {
        if (keyTable[row].charAt(col) == charA) {
          aPosition = Optional.of(KeyboardPosition.builder().setRow(row).setCol(col).build());
        }
        if (keyTable[row].charAt(col) == charB) {
          bPosition = Optional.of(KeyboardPosition.builder().setRow(row).setCol(col).build());
        }
      }
    }

    if (aPosition.isPresent() && bPosition.isPresent()) {
      return OptionalDouble.of(aPosition.get().getDistance(bPosition.get()));
    }
    return OptionalDouble.empty();
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

  /**
   * Returns a set of all characters in the keyboard.
   *
   * @return set of chars.
   */
  public Set<Character> getKeys() {
    checkKeyboardSet();
    return keys.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
  }
}
