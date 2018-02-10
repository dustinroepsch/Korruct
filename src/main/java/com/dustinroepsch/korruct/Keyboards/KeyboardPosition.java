package com.dustinroepsch.korruct.Keyboards;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class KeyboardPosition {

  public static Builder builder() {
    return new AutoValue_KeyboardPosition.Builder();
  }

  public abstract int row();

  public abstract int col();

  public double getDistance(KeyboardPosition other) {
    return Math.sqrt(
        (this.row() - other.row()) * (this.row() - other.row())
            + (this.col() - other.col()) * (this.col() - other.col()));
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setRow(int row);

    public abstract Builder setCol(int col);

    public abstract KeyboardPosition build();
  }
}
