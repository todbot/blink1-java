/**
 *
 */

package com.thingm.blink1;

import java.util.*;

import org.hid4java.*;
import org.hid4java.event.HidServicesEvent;


public class Blink1Finder implements HidServicesListener {

  private static final Integer vendorId = 0x27b8;
  private static final Integer productId = 0x01ed;

  private static Blink1Finder finder = null; // singleton

  static List<String> blink1SerialList = null;
  static HidServices hidServices = null;
  
  // static method to create instance of Singleton class 
  public static Blink1Finder getFinder() 
  { 
    if (finder == null) {
      finder = new Blink1Finder();
    }
    return finder;
  }

  private Blink1Finder() {
    //HidServicesSpecification hidServicesSpec = new HidServicesSpecification();
    //hidServicesSpec.setScanMode(ScanMode.NO_SCAN);
    //hidServicesSpec.setAutoShutdown(true);

    // Get HID services using custom specification
    //hidServices = HidManager.getHidServices(hidServicesSpec);
    hidServices = HidManager.getHidServices();
    hidServices.addHidServicesListener(this);

    // Start the services
    //System.out.println("Starting HID services.");
    //hidServices.start();
  }

  /**
   * FIXME: Is it possible to avoid needing this?
   *     (e.g. this causes sigsegv in Processing on exit)
   */
  public static void shutdown() {
    // Shut down and rely on auto-shutdown hook to clear HidApi resources
    hidServices.shutdown();
  }

  /**
   * Look for blink(1)s
   * @return array of blink(1) serial number Strings
   */
  public static String[] listAll() {
    getFinder(); 
    blink1SerialList = new ArrayList<String>();
    for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
      if( hidDevice.getVendorId() == vendorId &&
          hidDevice.getProductId() == productId ) {
        blink1SerialList.add( hidDevice.getSerialNumber() ) ;
      }
    }
    return blink1SerialList.toArray(new String[0]);
  }

  /**
   * Open first blink(1) device and return it
   * @return Blink1 object opened and ready to go, or null if no blink(1)
   */
  public static Blink1 open() {
    Blink1 blink1 = null;
    if( blink1SerialList == null ) {
      listAll();
    }
    if( blink1SerialList.size() == 0 ) {
      return null;
    }
    String serialNumber = blink1SerialList.get(0);
    return openBySerial(serialNumber);
  }

  public static Blink1 openFirst() {
    return open();
  }

  /**
   * Open blink(1) device by blink(1) serial number.
   * @return Blink1 object or NULL if no device with that serial number found
   */
  public static Blink1 openBySerial(String serialNumber) {
    listAll();
    HidDevice dev = hidServices.getHidDevice(vendorId, productId, serialNumber);
    if( dev == null ) { return null; }
    Blink1 blink1 = new Blink1Hid4Java(dev);
    return blink1;
  }
  
  /**
   * Open blink(1) device by blink(1) numerical id (0-getCount()).
   * Id list is ordered by serial number.
   * @returns Blink1 object or NULL if no device with that id found
   */
  public static Blink1 openById( int id ) {
    listAll();
    int count = blink1SerialList.size();
    if( count == 0 || id >= count ) {
      return null;
    }
    String serialNumber = blink1SerialList.get(id);
    HidDevice dev = hidServices.getHidDevice(vendorId, productId, serialNumber);
    if( dev == null ) { return null; }
    Blink1 blink1 = new Blink1Hid4Java(dev);

    return blink1;
  }

  /**
   * Implemenation of Blink1 using Hid4Java
   */
  static class Blink1Hid4Java extends Blink1 {
    private HidDevice dev;
    Blink1Hid4Java(HidDevice dev) {
      this.dev = dev;
      this.serialNumber = dev.getSerialNumber();
    }
    @Override
    public int sendFeatureReport(byte[] buffer, byte reportId) {
      return this.dev.sendFeatureReport(buffer, reportId);
    }
    @Override
    public int getFeatureReport(byte[] buffer, byte reportId) {
      return this.dev.getFeatureReport(buffer, reportId);
    }
    public void close() {
      this.dev.close();
      this.dev = null;
    }
  }

  
  @Override
  public void hidDeviceAttached(HidServicesEvent event) {

    //System.out.println("Device attached: " + event);
    
    // Add serial number when more than one device with the same
    // vendor ID and product ID will be present at the same time
    if (event.getHidDevice().isVidPidSerial(vendorId, productId, null)) {
      //sendMessage(event.getHidDevice());
      
    }

  }

  @Override
  public void hidDeviceDetached(HidServicesEvent event) {

    //System.err.println("Device detached: " + event);

  }

  @Override
  public void hidFailure(HidServicesEvent event) {

    //System.err.println("HID failure: " + event);

  }

  
}
