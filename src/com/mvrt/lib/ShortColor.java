package com.mvrt.lib;

import java.awt.Color;

public class ShortColor {
  private short red, green, blue, alpha;

  public ShortColor(short red, short green, short blue, short alpha) {
    this.red = red;
    this.blue = blue;
    this.green = green;
    this.alpha = alpha;
  }

  public ShortColor(short red, short green, short blue) {
    this(red, green, blue, (short) 255);
  }

  public short getRed() {
    return red;
  }

  public short getBlue() {
    return blue;
  }

  public short getGreen() {
    return green;
  }

  public short getAlpha() {
    return alpha;
  }

  public static ShortColor fromColor(Color color) {
    return new ShortColor((short) color.getRed(),
                          (short) color.getGreen(),
                          (short) color.getBlue(),
                          (short) color.getAlpha());
  }
}

