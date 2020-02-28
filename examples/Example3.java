

import com.thingm.blink1.*;

import java.util.*;

public class Example3 {
  
  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    int rc;
    
    Blink1 blink1 = Blink1Finder.open();

    if( blink1 == null ) { 
      System.out.println("error on open(), no blink(1), next call will return error ");
    }

    System.out.printf("blink(1) found: serial:%s version:%d\n",
                      blink1.getSerial(), blink1.getVersion() );

    for( int i = 0; i< blink1.getPatternLineMaxCount(); i++ ) {
      PatternLine p = blink1.readPatternLine(i);
      System.out.printf("pattline: %i %s\n",i,p);
    }
        
    PatternLine[] mypattern = {
      new PatternLine(500, 255,  0,255, 0),
      new PatternLine(500, 100, 10,100, 0),
      new PatternLine(500,  20, 80, 20, 0),
      new PatternLine(500,   0,130,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0),
      new PatternLine(  0,   0,  0,  0, 0)
    };


    System.out.println("blink(1) stored pattern:");
    mypattern = blink1.readPattern();
    for( int i = 0; i< mypattern.length; i++ ) {
      PatternLine p = mypattern[i];
      System.out.printf("pattline: %d %s\n",i,p);
    }
    
    blink1.playPattern(mypattern);
   
    blink1.close();
    

  }

}
