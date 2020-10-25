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
  public static final byte reportId = 1;
  public static final byte reportLen = 8; //

  public static final byte report2Id = 2;
  public static final byte report2Len = 60;

  /**
   * Our serial number 
   */
  public String serialNumber;

  /**
   * Close blink(1) device. 
   */
  public abstract void close();

  /**
   * Send a USB HID feature report to blink(1). Implemented by subclass
   * @param buffer  byte buffer of correct length for report (e.g. 8 for blink1)
   *          with no reportId as the first byte 
   * @param reportId the reportId to send the buffer on (usually 1 for blink1)
   * @returns < 0 on error, or number of bytes received
   */
  public abstract int sendFeatureReport(byte[] buffer, byte reportId);
  
  /**
   * Get a USB HID feature report to blink(1). Implemented by subclass
   * @param buffer the byte buffer to write into, must be correct length for report
   * @param reportId the reportId to get on
   * @returns < 0 on error, or number of bytes received
   */
  public abstract int getFeatureReport(byte[] buffer, byte reportId);
  
  
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
    int rc = this.stop();
    return this.fadeToRGB(300, 0,0,0);
  }
  
  /**
   * Set blink(1) color immediately.
   *
   * @param c Color to set
   * @returns blink1_command response code, -1 == fail 
   */
  public int setColor(Color c) {
    return this.setRGB( c.getRed(), c.getGreen(), c.getBlue() );
  }

  /**
   * Get last color sent (current color
   * @return The current color of the device as int, or <0 on error
   */
  public int getColorAsInt(int ledn) {
    Color c = this.getColor(ledn);
    int rc = 0;
    if( c == null) { rc = -1; }
    return rc;
  }

  public Color getColor() {
    return this.getColor(0);
  }
  
  public Color getColor(int ledn) {
    //uint8_t bu = { blink1_report_id, 'r', 0,0,0, 0,0,ledn };
    int rc;
    byte[] buff = { (byte)'r', 0,0,0, 0,0,(byte)ledn,0};
    rc = this.sendFeatureReport(buff,reportId);
    rc = this.getFeatureReport(buff, reportId);
    //System.out.println("BLINK1:getColor:"+Arrays.toString((buff)));
    if( rc < 0 ) { return null;  }
    int r = buff[2] & 0xff;
    int g = buff[3] & 0xff;
    int b = buff[4] & 0xff;
    
    Color c = new Color( r,g,b );
    return c;
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
   * @param stayLit true/false to stay on when serverdown re-enabled or go off
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
   * @returns < 0 if error
   */
  public int writePatternLine(int fadeMillis, Color c, int pos) {
    return writePatternLine(fadeMillis, c.getRed(), c.getGreen(), c.getBlue(), pos);
  }

  /**
   *
   */
  public int writePatternLine(PatternLine p, int pos) {
    return writePatternLine( p.fadeMillis, p.r, p.g, p.b, pos);
  }

  /**
   * Write a pattern to blink(1) RAM
   * Alters blink(1) internal pattern space but doesn't save the pattern to flash
   * @param patternlines, a List of PatternLines
   * @param clear, true = zero-out any unused patternlines, false = keep unused
   * @returns < 0 if error
   */
  public int writePattern(PatternLine[] patternlines, boolean clear) {
    // FIXME: add clear logic
    int linecount = patternlines.length;
    if( linecount > this.getPatternLineMaxCount() ) {
      linecount = this.getPatternLineMaxCount();
    }
    int rc=0; 
    for( int i = 0; i< linecount; i++ ) {
      PatternLine p = patternlines[i];
      System.out.printf("writePattern:%d ",i,p);
      rc = this.writePatternLine(p,i);
      if(rc<0) { return rc; }
    }
    return rc;
  }

  /**
   * Read a color pattern line at position
   * @param pos pattern line to read
   * @return PatternLine object representing the line, or null on error
   */
  public PatternLine readPatternLine(int pos) {
    int rc;
    byte[] buf = {(byte)'R', 0, 0, 0, 0, 0, (byte)pos, 0};
    rc = this.sendFeatureReport(buf,reportId);
    rc = this.getFeatureReport(buf,reportId); // FIXME: check return code
    //System.out.println("BLINK1:readPatternline:"+Arrays.toString((buf)));
    if( rc < 0 ) { return null; }
    
    // java signed bytes are stupid
    int fadeMillis = ((((buf[5] & 0xff)<<8) | (buf[6] & 0xff)) * 10);
    int r = buf[2] & 0xff;
    int g = buf[3] & 0xff;
    int b = buf[4] & 0xff;
    int ledn = 0;

    PatternLine pattline = new PatternLine(fadeMillis, r,g,b, ledn); 
    
    return pattline;
  }

  /**
   * Read all blink(1) patternlines out
   * @return List of Patternlines
   */
  public PatternLine[] readPattern() {
    List<PatternLine> pattern = new ArrayList<>();
    for( int i=0; i< this.getPatternLineMaxCount(); i++) {
      PatternLine p = this.readPatternLine(i); // FIXME: check error case
      pattern.add(p);      
    }
    return pattern.toArray(new PatternLine[0]);
  }
  
  /**
   * Save internal RAM pattern to flash
   */
  public int savePattern() {
    byte[] buf = {(byte)'W', (byte)0xBE, (byte)0xEF, (byte)0xCA, (byte)0xFE, 0, 0, 0};
    return this.sendFeatureReport(buf,reportId);
  }

  /**
   * Play a given list of PatternLines as a pattern
   * Alters blink(1) internal pattern space but doesn't save the pattern to flash
   * @param patternlines, a List of PatternLines
   * @param clear, true = zero-out any unused patternlines, false = keep unused
   * @returns -1 if error, > 0 if success
   */
  public int playPattern(PatternLine[] patternlines, boolean clear) {
    int rc = this.writePattern(patternlines, clear);
    if(rc<0) return rc;
    return this.play();
  }
  
  /**
   * Play a given list of PatternLines as a pattern
   * Alters blink(1) internal pattern space but doesn't save the pattern to flash
   * @param patternlines, a List of PatternLines
   * @returns -1 if error, > 0 if success
   */
  public int playPattern(PatternLine[] patternlines) {
    return this.playPattern(patternlines,true);
  }
  
  /**
   *
   */
  public int getPatternLineMaxCount() {
    return 16; // FIXME: dependant on which blink(1)
  }
  

  //-------------------------------------------------------------------------
  // Utilty Class methods
  //-------------------------------------------------------------------------

  /**
   * one attempt at a degamma curve.
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
