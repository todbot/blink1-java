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

  static List<String> blink1SerialList = new ArrayList<String>();
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
    hidServices = HidManager.getHidServices();
    hidServices.addHidServicesListener(this);

    // Start the services
    //System.out.println("Starting HID services.");
    //hidServices.start();

  }

  public static void shutdown() {
    // Shut down and rely on auto-shutdown hook to clear HidApi resources
    hidServices.shutdown();
  }

  /**
   * Look for blink(1)s
   * @return List of blink(1) serial number Strings
   */
  public static List<String> findAll() {
    getFinder(); 
    for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
      if( hidDevice.getVendorId() == vendorId &&
          hidDevice.getProductId() == productId ) {
        blink1SerialList.add( hidDevice.getSerialNumber() ) ;
      }
    }
    return blink1SerialList;
  }
 
  public static Blink1 open() {
    Blink1 blink1 = null;
    String serialNumber = null;
    if( blink1SerialList.size() > 0 ) {
      HidDevice dev = hidServices.getHidDevice(vendorId, productId, serialNumber);
      blink1 = new Blink1Hid4Java(dev);
    }
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
    }
  }

  
  @Override
  public void hidDeviceAttached(HidServicesEvent event) {

    System.out.println("Device attached: " + event);

    // Add serial number when more than one device with the same
    // vendor ID and product ID will be present at the same time
    if (event.getHidDevice().isVidPidSerial(vendorId, productId, null)) {
      //sendMessage(event.getHidDevice());
    }

  }

  @Override
  public void hidDeviceDetached(HidServicesEvent event) {

    System.err.println("Device detached: " + event);

  }

  @Override
  public void hidFailure(HidServicesEvent event) {

    System.err.println("HID failure: " + event);

  }


  /*
  /**
   * (re)Enumerate the bus and return a count of blink(1) device found.
   * @returns blink1_command response code, -1 == fail 
   *
  public static int enumerate() {
    blink1DevList = new ArrayList<HidDeviceInfo>();
    try {
      List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
      for (HidDeviceInfo info : devList) {
        if (info.getVendorId() == (short) vendorId &&
            info.getProductId() == (short) productId) {
          blink1DevList.add(info);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    //Collections.sort(blink1DevList, (d1, d2) -> {
    //    return d2.getSerialNumberString() - d1.getSerialNumberString();
    //  });
    
    return getCount();
  }

  /**
   * Get a count of blink(1) devices that have been enumerated.
   *
   *
  public static int getCount() {
    return blink1DevList.size();
  }
  
  /**
   * Return the list of blink(1) device paths found by enumerate.
   *
   * @returns array of device paths
   *
  public static List<String> getDevicePaths() {
    List<String> paths = new ArrayList<String>();
    for (HidDeviceInfo dev : blink1DevList) {
      paths.add( dev.getPath() );
    }
    return paths;
  }
  
  /**
   * Return the list of blink(1) device serials found by enumerate.
   *
   * @returns array of device serials
   *
  public static List<String> getDeviceSerials() {
    List<String> serials = new ArrayList<String>();
    for (HidDeviceInfo dev : blink1DevList) {
      serials.add( dev.getSerialNumberString() );
    }
    return serials;
  }

  /**
   * Open blink(1) device by blink(1) numerical id (0-getCount()).
   * Id list is ordered by serial number.
   * @returns Blink1 object or NULL if no device with that id found
   *
  public static Blink1 openById( int id ) {
    Blink1 blink1 = null;
    try { 
      HidDeviceInfo devInfo = blink1DevList.get(id);
      final HidDevice dev = PureJavaHidApi.openDevice(devInfo);
      blink1 = new Blink1(dev);
    } catch(Exception e) {
    }
    return blink1;
  }
  
  /**
   * Open the first (or only) blink(1) device.
   *
  public static Blink1 openFirst() {
    return openById(0);
  }    

  /**
   * Open the first (or only) blink(1) device.  
   * Causes an enumerate to happen.
   * Stores open device id statically in native lib.
   *
   * @returns Blink1 object or null if no blink(1) device
   *
  public static Blink1 open() {
    return openFirst();
  }
  /**
   * Open blink(1) device by USB path, may be different for each insertion.
   *
   * @returns Blink1 object or NULL if no device with that path found
   *
  public static Blink1 openByPath( String devicepath ) {
    //int i =
    Blink1 blink1 = null;
    HidDeviceInfo devInfo = blink1DevList.stream()
      .filter(info -> devicepath.equals(info.getPath()))
      .findAny().orElse(null);
    try { 
      HidDevice dev = PureJavaHidApi.openDevice(devInfo);
      blink1 = new Blink1(dev);
    } catch(Exception e) {
    }
    return blink1;
  }
  
  /**
   * Open blink(1) device by blink(1) serial number.
   *
   * @returns Blink1 object or NULL if no device with that serial found
   *
  public static Blink1 openBySerial( String serialnumber ) {
    //int i =
    Blink1 blink1 = null;
    HidDeviceInfo devInfo = blink1DevList.stream()
      .filter(info -> serialnumber.equals(info.getSerialNumberString()))
      .findAny().orElse(null);
    try { 
      HidDevice dev = PureJavaHidApi.openDevice(devInfo);
      blink1 = new Blink1(dev);
    } catch(Exception e) {
    }
    
    return blink1;    
  }

*/
}
