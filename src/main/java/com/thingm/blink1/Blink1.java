/**
 * ThingM blink(1) Java library
 *
 * Copyright 2008-2020, Tod E. Kurt / @todbot
 *
 * 
 */

package com.thingm.blink1;

import java.util.*;
import java.awt.Color;


public abstract class Blink1 
{
  private static final byte reportId = 1;
  private static final int reportLen = 8; //

  /**
   * Our serial number 
   */
  public String serialNumber;

  /**
   * Close blink(1) device. 
   */
  public abstract void close();

  /**
   *
   */
  public abstract int sendFeatureReport(byte[] buffer, byte reportId);
  
  /**
   *
   */
  public abstract int getFeatureReport(byte[] buffer, byte reportid);
  
  
  /**
   * Set blink(1) RGB color immediately.
   *
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @returns blink1_command response code, -1 == fail 
   */
  public int setRGB(int r, int g, int b) {
    byte[] buff = { (byte)'n', (byte)r, (byte)g, (byte)b, 0, 0, 0, 0};
    int rc = this.sendFeatureReport(buff, reportId);
    return rc;
  }

  /**
   *
   */
  public int off() {
    return this.setRGB(0,0,0);
  }
  
  /**
   * Set blink(1) RGB color immediately.
   *
   * @param c Color to set
   * @returns blink1_command response code, -1 == fail 
   */
  public int setRGB(Color c) {
    return setRGB( c.getRed(), c.getGreen(), c.getBlue() );
  }

  /**
   * Fade blink(1) to RGB color over fadeMillis milliseconds.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @returns blink1_command response code, -1 == fail 
   */
  public int fadeToRGB(int fadeMillis, int r, int g, int b ) {
    return this.fadeToRGB(fadeMillis,r,g,b,0);
  }
  
  /**
   * Fade blink(1) to RGB color over fadeMillis milliseconds.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @param ledn which LED to address (0=all)
   * @returns blink1_command response code, -1 == fail 
   */
  public int fadeToRGB(int fadeMillis, int r, int g, int b, int ledn) {
    int dms = fadeMillis/10;
    byte th = (byte)(dms >> 8);
    byte tl = (byte)(dms & 0xff);
    byte[] buff = { (byte)'c', (byte)r, (byte)g, (byte)b, th,tl, (byte)ledn, 0};
    int rc = this.sendFeatureReport(buff, reportId);
    return rc;
  }
  
  /**
   * Fade blink(1) to RGB color over fadeMillis milliseconds.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param c Color to set
   * @returns blink1_command response code, -1 == fail 
   */
  public int fadeToRGB(int fadeMillis, Color c) {
    return fadeToRGB( fadeMillis, c.getRed(), c.getGreen(), c.getBlue() );
  }

  /**
   * Fade blink(1) to RGB color over fadeMillis milliseconds.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param c Color to set
   * @param ledn which LED to address (0=all)
   * @returns blink1_command response code, -1 == fail 
   */
  public int fadeToRGB(int fadeMillis, Color c, int ledn) {
    return fadeToRGB( fadeMillis, c.getRed(), c.getGreen(), c.getBlue(), ledn );
  }

  /**
   * Write a blink(1) light pattern entry.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param r red component 0-255
   * @param g green component 0-255
   * @param b blue component 0-255
   * @param pos entry position 0-patt_max
   * @returns blink1_command response code, -1 == fail 
   */
  public int writePatternLine(int fadeMillis, int r, int g, int b, int pos) {
    int dms = fadeMillis/10;
    byte th = (byte)(dms >> 8);
    byte tl = (byte)(dms & 0x00ff);
    byte[] buf = {(byte)'P', (byte)r, (byte)g, (byte)b, th,tl, (byte)pos, 0};
    return this.sendFeatureReport(buf, reportId);
  }
  
  /**
   * Write a blink(1) light pattern entry.
   *
   * @param fadeMillis milliseconds to take to get to color
   * @param c Color to set
   * @param pos entry position 0-patt_max
   * @returns blink1_command response code, -1 == fail 
   */
  public int writePatternLine(int fadeMillis, Color c, int pos) {
    return writePatternLine(fadeMillis, c.getRed(), c.getGreen(), c.getBlue(), pos);
  }

  public PatternLine readPatternLine(int pos) {
    PatternLine pattline = new PatternLine();
    pattline.fadeMillis = 100;
    pattline.r = 255;
    pattline.g = 33;
    pattline.b = 11;
    return pattline;
  }
  
  /**
   * Play internal color pattern
   * @param start pattern line to start from
   * @param end pattern line to end at
   * @param count number of times to play, 0=play forever
   * @returns blink1_command response code, -1 == fail 
   */
  public int play( int start, int end, int count ) {
    byte[] buf = {(byte)'p', 1, (byte)start, (byte)end, (byte)count, 0, 0, 0};
    return this.sendFeatureReport(buf,reportId);
  }

  /**
   * Play the internal color pattern
   */
  public int play() {
    return this.play(0,0,0);
  }
  
  /**
   * Stop pattern playing.
   */
  public int stop() {
    byte[] buf = { (byte)'p', 0, 0,0, 0, 0,0,0 };
    return this.sendFeatureReport(buf,reportId);
  }

  /**
   * Enable or disable serverdown / servertickle mode
   * @param enable true = turn on serverdown mode, false = turn it off
   * @param millis milliseconds until light pattern plays if not updated 
   * @param stayLitt true/false to stay on when serverdown re-enabled or go off
   * @param start pattern line to start from
   * @param end pattern line to end at
   * @returns blink1_command response code, -1 == fail 
   */
  public int serverdown( boolean enable, int millis,
                         boolean stayLit, int start, int end) {
    byte on = (byte)((enable) ? 1:0);
    byte th = (byte)(millis >> 8);
    byte tl = (byte)(millis & 0xff);
    byte st = (byte)((stayLit) ? 1:0);
    byte[] buf = {(byte)'D', on, th,tl, st, (byte)start, (byte)end, 0 };
    return this.sendFeatureReport(buf, reportId);
  }

  /**
   * Enable or disable serverdown / servertickle mode
   * @param enable true = turn on serverdown mode, false = turn it off
   * @param millis milliseconds until light pattern plays if not updated 
   *
   */
  public int serverdown( boolean enable, int millis ) {
    return this.serverdown( enable, millis, false, 0, 0);
  }

  /**
   * Alias for getFirmwareVersion()
   */
  public int getVersion() {
    return this.getFirmwareVersion();
  }
  
  /** 
   * Get version of firmware code in blink(1) device.
   *
   * @returns blink1 version number as int (e.g. v1.0 == 100, v2.0 = 200)
   */
  public int getFirmwareVersion() {
    byte[] buff = { 'v', 0,0,0,0,0,0,0 };
    this.sendFeatureReport(buff, reportId);
    this.getFeatureReport(buff, reportId);
    //System.out.println("BLINK1:getVersion:"+Arrays.toString((buff)));
    int vh = Character.getNumericValue(buff[3]);
    int vl = Character.getNumericValue(buff[4]);
    int ver = (vh*100) + vl;
    return ver;
  }

  /**
   * Get serial number of this blink(1)
   *
   * @return serial number string of this blink(1)
   */
  public String getSerial() {
    return this.serialNumber;
  }

  //-------------------------------------------------------------------------
  // Utilty Class methods
  //-------------------------------------------------------------------------

  /**
   * one attempt at a degamma curve.
   * //FIXME: this is now in blink1-lib
   */
  public static final int log2lin( int n ) {
    //return  (int)(1.0* (n * 0.707 ));  // 1/sqrt(2)
    return (((1<<(n/32))-1) + ((1<<(n/32))*((n%32)+1)+15)/32);
  }

  /**
   * Utility: A simple delay
   */
  public static final void pause(int millis) {
    try {
        Thread.sleep(millis);
    } catch (Exception e) {
    }
  }


}
