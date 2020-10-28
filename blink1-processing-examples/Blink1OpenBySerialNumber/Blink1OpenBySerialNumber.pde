
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

String serialnum = ""; // fill with serial number string, e.g. "330A32F9"

void setup() {

  if( serialnum.isEmpty() ) { 
    serialnum = null;
    println("no serial number specified, opening first blink(1)");
    String[] serials = Blink1Finder.listAll();
    if( serials.length > 0 ) { 
      serialnum = serials[0];
    }
  } 
  Blink1 blink1 = Blink1Finder.openBySerial(serialnum);

  if ( blink1 == null) {
    println("blink(1) with serial '"+serialnum+"' not found...");  
    return;
  }
  else { 
    println("opened blink(1) '"+serialnum+"'");
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
