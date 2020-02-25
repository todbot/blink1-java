

import com.thingm.blink1.*;

import java.util.List;

public class Example0 {
  
  /**
   * Demonstrate listing all blink(1)s by serial number
   */
  public static void main(String args[]) {
    
    int rc;
    
    System.out.println("Looking for blink(1) devices...");

    List<String> serials = Blink1Finder.findAll();
    
    if( serials.size() == 0 ) {
      System.out.println("no blink(1)s found");
      return;
    }
    
    System.out.println("blink(1)s found:");
    for( int i=0; i< serials.size(); i++) { 
      System.out.println("i:"+i+ "  serial:"+serials.get(i));
    }

  }

}
