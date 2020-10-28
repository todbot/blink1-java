package com.thingm.blink1;

public class Example0 {
  
  /**
   * Demonstrate listing all blink(1)s by serial number
   */
  public static void main(String args[]) {
    
    System.out.println("Looking for blink(1) devices...");

    String[] serials = Blink1Finder.listAll();
    
    if( serials.length == 0 ) {
      System.out.println("no blink(1)s found");
      return;
    }
    
    System.out.println("blink(1)s found:");
    for( int i=0; i < serials.length; i++) {
      System.out.println("i:" + i + "  serial:" + serials[i]);
    }

    // in case you want to exit abruptly, shutdown first
    Blink1Finder.shutdown();
  }
  
}
