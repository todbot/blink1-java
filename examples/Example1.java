

import com.thingm.blink1.*;

import java.util.List;
import java.awt.Color;

public class Example1 {
  
  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    int rc;
    
    System.out.println("Looking for blink(1) devices...");


    Blink1 blink1 = Blink1Finder.open();
    //Blink1 blink1 = Blink1Finder.openBySerial("20003299");

    if( blink1 == null ) { 
      System.out.println("no blink(1) found, next call will return error ");
    }

    System.out.printf("blink(1) found: serial:%s version:%d\n",
                      blink1.getSerial(), blink1.getVersion() );

    System.out.println("Fading to purple");
    rc = blink1.fadeToRGB( 1000, 100,20,100 );
    Blink1.pause(500);


    System.out.println("Setting to green");
    blink1.setColor(Color.green);
    Blink1.pause(500);
    
    System.out.println("Turning off");
    blink1.off();

    blink1.close();
    
  }

}
