

import com.thingm.blink1.*;

import java.util.*;

public class Example3 {
  
  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    int rc;
    
    System.out.println("Looking for blink(1) devices...");

    String[] serials = Blink1Finder.findAll();

    if( serials.length == 0 ) {
      System.out.println("no devices found");
    }

    Blink1 blink1 = Blink1Finder.open();

    if( blink1 == null ) { 
      System.out.println("error on open(), no blink(1), next call will return error ");
    }

    System.out.printf("blink(1) found: serial:%s version:%d\n",
                      blink1.getSerial(), blink1.getVersion() );

    for( int i = 0; i< blink1.getPatternLineMaxCount(); i++ ) {
      PatternLine p = blink1.readPatternLine(i);
      System.out.printf("pattline: %d mssec - rgb:%d,%d,%d\n", p.fadeMillis,p.r,p.g,p.b);
    }
        
    List<PatternLine> mypattern = new ArrayList<> (
        Arrays.asList( new PatternLine(100, 255,0,255, 0),
                       new PatternLine(100, 155,1,155, 0),
                       new PatternLine(200, 40,40,40, 0) ) );


    System.out.println("blink(1) stored pattern:");
    mypattern = blink1.readPattern();
    for( int i = 0; i< mypattern.size(); i++ ) {
      PatternLine p = mypattern.get(i);
      System.out.printf("pattline: %d mssec - rgb:%d,%d,%d\n", p.fadeMillis,p.r,p.g,p.b);
    }

   
    blink1.close();
    

  }

}
