
/**
 * Blink1OpenBySerialNumber. 
 * 
 * Sample code demonstrating how to find a 
 * blink(1) device by its serial number 
 * (e.g. "3ab9203f")connected to the computer
 * 
 * A blink(1)'s serial number can be found from
 * using Blink1Control or doing "blink1-tool --list"
 *
 */

import com.thingm.blink1.*;
import java.awt.Color;

String serialnum = "330A32F9";

void setup() {

  Blink1 blink1 = Blink1Finder.openBySerial(serialnum);

  if ( blink1 == null) {
    println("blink(1) with serial "+serialnum+" not found...");  
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
