package com.dustinroepsch.korruct.Keyboards;

import java.util.List;

public class QWERTYKeyboard extends Keyboard {
  private static final String[] keyTable = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
  private static final String keys = List.of(keyTable).stream().reduce("", (a, s) -> a + s);

  @Override
  public boolean isValidWord(String word) {
    return word.chars().mapToObj(i -> (char) i).allMatch(c -> keys.contains(String.valueOf(c)));
  }

  @Override
  public int getDistance(char charA, char charB) {
    // TODO calculate distance between characters
    return 0;
  }
}
