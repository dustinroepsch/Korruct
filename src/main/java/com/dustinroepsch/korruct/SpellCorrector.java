package com.dustinroepsch.korruct;

import com.dustinroepsch.korruct.Keyboards.Keyboard;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

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
   * Attemps to correct the given word to a word in the wordfile.
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
        .min(Comparator.comparingInt(a -> keyboard.getMinDistance(a, sanitizedWord)));
  }

  private Set<String> getVariations(String word) {
    // TODO: generate variations of the word
    return Set.of(word);
  }
}
