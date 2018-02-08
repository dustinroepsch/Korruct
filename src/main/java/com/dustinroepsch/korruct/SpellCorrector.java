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

  public Optional<String> getCorrection(String word) {
    checkNotNull(word);
    String sanitizedWord = word.trim().toLowerCase();

    if (validWords.contains(sanitizedWord)) {
      return Optional.empty();
    }

    return getVariations(sanitizedWord)
        .stream()
        .filter(validWords::contains)
        .min(Comparator.comparingInt(a -> keyboard.getDistance(a, sanitizedWord)));
  }

  private Set<String> getVariations(String word) {
    // TODO: generate variations of the word
    return Set.of(word);
  }
}
