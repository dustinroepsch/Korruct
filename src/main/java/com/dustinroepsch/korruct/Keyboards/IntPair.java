package com.dustinroepsch.korruct.Keyboards;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class IntPair {
  public static Builder builder() {
    return new AutoValue_IntPair.Builder();
  }

  abstract int aIndex();

  abstract int bIndex();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setAIndex(int aIndex);

    public abstract Builder setBIndex(int bIndex);

    public abstract IntPair build();
  }
}
