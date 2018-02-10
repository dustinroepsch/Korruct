package com.dustinroepsch.korruct;

import com.dustinroepsch.korruct.Keyboards.Keyboard;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpellCorrector {
  private final ImmutableSet<String> validWords;

  private final Keyboard keyboard;

  /**
   * Creates an instance of a Spellcorrector that corrects words in the given wordFile, using the
   * given keyboard as a heuristic. Only words that are valid in the keyboard will be corrected to.
   *
   * @param wordFile Path to a file containing line seperated words to correct to.
   * @param keyboard A keyboard that is able to type the words.
   * @throws IOException Invalid word file
   */
  public SpellCorrector(Path wordFile, Keyboard keyboard) throws IOException {
    checkNotNull(wordFile);
    checkNotNull(keyboard);

    this.keyboard = keyboard;

    validWords =
        Files.readAllLines(wordFile, Charset.defaultCharset())
            .stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .filter(keyboard::isValidWord)
            .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * Attempts to correct the given word to a word in the wordfile.
   *
   * @param word The word to correct.
   * @return The corrected word if possible, otherwise Optional.empty().
   */
  public Optional<String> getCorrection(String word) {
    checkNotNull(word);
    String sanitizedWord = word.trim().toLowerCase();

    if (validWords.contains(sanitizedWord)) {
      return Optional.empty();
    }

    return getVariations(sanitizedWord)
        .stream()
        .filter(validWords::contains)
        .min(Comparator.comparingDouble(a -> keyboard.getMinDistance(a, sanitizedWord)));
  }

  private Set<String> getVariations(String word) {
    List<StringSplit> splits = getSplits(word);

    Set<String> variations = new HashSet<>();
    splits.forEach(i -> variations.addAll(getEditDistanceOne(i)));
    splits.forEach(i -> variations.addAll(getEditDistanceTwo(i)));
    return variations;
  }

  private Set<String> getEditDistanceTwo(StringSplit split) {
    return getEditDistanceOne(split)
        .stream()
        .map(this::getSplits)
        .flatMap(Collection::stream)
        .map(this::getEditDistanceOne)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  private Set<String> getEditDistanceOne(StringSplit split) {
    Set<String> editOne = new HashSet<>();
    editOne.addAll(getInserts(split));
    editOne.add(getDeletion(split));
    editOne.addAll(getReplacements(split));

    return editOne;
  }

  private List<StringSplit> getSplits(String word) {
    List<StringSplit> splitList = new ArrayList<>();
    for (int i = 0; i < word.length(); i++) {
      splitList.add(
          StringSplit.builder()
              .setLeftHalf(word.substring(0, i))
              .setRightHalf(word.substring(i))
              .build());
    }
    return splitList;
  }

  private Set<String> getInserts(StringSplit split) {
    return keyboard
        .getKeys()
        .stream()
        .map(c -> split.leftHalf() + c + split.rightHalf())
        .collect(Collectors.toSet());
  }

  private String getDeletion(StringSplit split) {
    if (split.leftHalf().equals("")) {
      return split.rightHalf();
    }
    return split.leftHalf().substring(0, split.leftHalf().length() - 1) + split.rightHalf();
  }

  private Set<String> getReplacements(StringSplit split) {
    if (split.leftHalf().equals("")) {
      return new HashSet<>();
    }
    return keyboard
        .getKeys()
        .stream()
        .map(
            c ->
                split.leftHalf().substring(0, split.leftHalf().length() - 1)
                    + c
                    + split.rightHalf())
        .collect(Collectors.toSet());
  }
}
