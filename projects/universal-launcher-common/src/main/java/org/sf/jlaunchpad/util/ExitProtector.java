package org.sf.jlaunchpad.util;

import java.security.Permission;

public class ExitProtector {
  
  public void forbidSystemExitCall() {
    final SecurityManager securityManager = new SecurityManager() {
      public void checkPermission( Permission permission ) {
        System.out.println("permission.getName() " + permission.getName());
        
        if(permission.getName().startsWith("exitVM")) {
          throw new ExitTrappedException() ;
        }
      }
    };

    System.setSecurityManager( securityManager ) ;
  }

  public void enableSystemExitCall() {
    System.setSecurityManager( null ) ;
  }

}
