
/**
 * Blink1InternalPatterns 
 * 
 * Sample code demonstrating how to play
 * and create color patterns that run on
 * the blink(1) device.
 */

import com.thingm.blink1.*;
import java.awt.Color;

void setup() {

  Blink1 blink1 = Blink1Finder.open();

  if ( blink1 == null) {
    println("blink(1) not found...");  
    return;
  }

  println("playing built-in pattern");
  blink1.play();  // play pattern currently stored
  delay(1000);
  
  println("writing new pattern");
          
  PatternLine[] mypattern = {
      new PatternLine(500, 255,  0,255, 0),
      new PatternLine(500, 100, 10,100, 0),
      new PatternLine(500,  20, 80, 20, 0),
      new PatternLine(500,   0,130,  0, 0),
    };
    
  blink1.playPattern(mypattern);

  blink1.close();
  
  noLoop();
}

void draw() {
}
