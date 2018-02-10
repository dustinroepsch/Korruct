package com.dustinroepsch.korruct;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class StringSplit {
  static Builder builder() {
    return new AutoValue_StringSplit.Builder();
  }

  abstract String leftHalf();

  abstract String rightHalf();

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setLeftHalf(String leftHalf);

    abstract Builder setRightHalf(String rightHalf);

    abstract StringSplit build();
  }
}
