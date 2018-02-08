package com.dustinroepsch.korruct.Keyboards;

import java.util.List;

public class QWERTYKeyboard implements Keyboard {
  private static final String[] keyTable = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
  private static final String keys = List.of(keyTable).stream().reduce("", (a, s) -> a + s);

  @Override
  public int getDistance(String wordA, String wordB) {
    return -1;
  }

  @Override
  public boolean isValidWord(String word) {
    return word.chars().mapToObj(i -> (char) i).allMatch(c -> keys.contains(String.valueOf(c)));
  }
}
