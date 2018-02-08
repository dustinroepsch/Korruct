package com.dustinroepsch.korruct.Keyboards;

/**
 * A Keyboard is a representation of a physical keyboard, used by SpellCorrector to evaluate
 * probability a word is a typo of another word.
 */
public abstract class Keyboard {

  public int getMinDistance(String wordA, String wordB) {
    return 0; // TODO: Use Dynamic Programming to figure out the least-distance way to get from
              // wordA to wordB
  }

  public abstract boolean isValidWord(String word);

  public abstract int getDistance(char charA, char charB);
}
