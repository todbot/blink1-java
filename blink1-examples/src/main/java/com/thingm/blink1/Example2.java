package com.thingm.blink1;

import java.awt.*;
import java.util.Random;

public class Example2 {

  /**
   * Simple command-line demonstration
   */
  public static void main(String args[]) {
    
    int rc;

    String[] serials = Blink1Finder.listAll();
    
    int count = serials.length;

    System.out.println("found "+count+ ((count==1) ? " device":" devices"));

    if( count == 0 ) {
      System.out.println("no devices found, would normally exit. continuing for error testing");
    }

    
    System.out.println("Opening deviceId 0");

    Blink1 blink1 = Blink1Finder.open();

    if( blink1 == null ) { 
      System.out.println("error on open(), no blink(1), next call will return error ");
    }

    System.out.print("fading to 10,20,30 ");
    rc = blink1.fadeToRGB( 100, 10,20,30 );
    System.out.println(" -- rc = "+rc);

    int ver = blink1.getFirmwareVersion();
    System.out.println("firmware version: " + ver);

    String serial = blink1.getSerial();
    System.out.println("serial number: " + serial);
    
    System.out.println("Playing internal color pattern for 5 secs...");
    rc = blink1.play();
    Blink1.pause(5000);
    System.out.println("Stopping pattern and closing");
    rc = blink1.stop();
    
    blink1.close();

    Blink1.pause(500);
    if( serials.length >= 2 ) {
      String serialA  = serials[0];
      String serialB  = serials[1];
      System.out.println("opening two devices: "+serialA+" and "+serialB);
      Blink1 blink1A = Blink1Finder.openBySerial( serialA );
      Blink1 blink1B = Blink1Finder.openBySerial( serialB );
      System.out.println("fading "+ serialA + " to red"); 
      blink1A.fadeToRGB( 100, 255,0,0 );
      Blink1.pause(500);
      System.out.println("fading "+ serialB + " to green");
      blink1B.fadeToRGB( 100, 0,255,0 );
      Blink1.pause(500);
      System.out.println("fading "+ serialA + " to blue"); 
      blink1A.fadeToRGB( 100, 0,0,255 );
      Blink1.pause(500);
      System.out.println("fading "+ serialB + " to purple"); 
      blink1B.fadeToRGB( 100, 255,0,255 );
      Blink1.pause(500);
      blink1A.close();
      blink1B.close();
      Blink1.pause(500);
    }

    Random rand = new Random();
    for( int i=0; i<10; i++ ) {
      int r = rand.nextInt() & 0xFF;
      int g = rand.nextInt() & 0xFF;
      int b = rand.nextInt() & 0xFF;
      
      int id = (count==0) ? 0 : rand.nextInt() & (count-1);
      
      System.out.print("setting device "+id+" to color "+r+","+g+","+b+"   ");

      blink1 = Blink1Finder.openById( id );
      if( blink1 == null ) { // blink1.error() ) { 
        System.out.print("couldn't open "+id+" ");
      }
      
      // can do r,g,b ints or a single Color
      //rc = blink1.setRGB( r,g,b );
      Color c = new Color( r,g,b );
      rc = blink1.setColor( c );
      if( rc == -1 ) 
        System.out.println("error detected");
      else 
        System.out.println();
      
      blink1.close();
      
      Blink1.pause( 300 );
    }

    System.out.println("Turn off all blink(1)s.");
    for(int n=0; n < count; n++){
      blink1 = Blink1Finder.openById(n);
      blink1.setColor(Color.BLACK);
      blink1.close();
    }

    System.out.println("Done.");
  }

}
