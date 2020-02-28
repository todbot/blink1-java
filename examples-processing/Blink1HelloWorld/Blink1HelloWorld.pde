
/**
 * Blink1HelloWorld 
 * 
 * How to find a blink(1) connected to the computer 
 * and change its colors.
 *
 */

import com.thingm.blink1.*;
import java.awt.Color;

void setup() {

  Blink1 blink1 = Blink1Finder.open();

  if ( blink1 == null) {
    println("blink(1) not found...");  
    return;
  }
  
  blink1.setColor(Color.red);
  delay(500);
  blink1.setColor(Color.green);
  delay(500);
  blink1.setColor(Color.blue);
  delay(500);
  blink1.off();

  blink1.close();
  
  noLoop();
}

void draw() {
}
