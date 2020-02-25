package com.thingm.blink1;

public class PatternLine {
  public int fadeMillis;
  public int r,g,b;
  public int ledn;

  public PatternLine( int fadeMillis, int r, int g, int b, int ledn) {
    this.fadeMillis = fadeMillis;
    this.r = r; this.g = g; this.b = b;
    this.ledn = ledn;
  }
  
  @Override
  public String toString() {
    return "PatternLine [fadeMillis="+fadeMillis +
      ",r:" + r + ",g:" + ", b:" + b + ", ledn:"+ ledn + "]";
  }
}
