

import com.thingm.blink1.*;

import java.util.List;

public class Example1 {
  
  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    int rc;
    
    System.out.println("Looking for blink(1) devices...");

    List<String> serials = Blink1Finder.findAll();

    if( serials.size() == 0 ) {
      System.out.println("no devices found");
    }

    Blink1 blink1 = Blink1Finder.open();

    if( blink1 == null ) { 
      System.out.println("error on open(), no blink(1), next call will return error ");
    }

    System.out.printf("blink(1) found: serial:%s version:%d\n",
                      blink1.getSerial(), blink1.getVersion() );

    System.out.println("Fading to purple");
    rc = blink1.fadeToRGB( 100, 100,20,100 );
    Blink1.pause(500);

    System.out.println("Turning off");
    blink1.off();

    
    blink1.close();
    

  }

}
