package src;

import java.rmi.Naming;

public class RMIHelper {

  public RMIInterface connect(String hostname) {
    RMIInterface rmiConnection = null;
    try {
      rmiConnection = (RMIInterface) Naming.lookup(hostname);
      return rmiConnection;
    } catch (Exception ex) {
      System.out.println(
        "Hubo un error estableciendo la conexión con " + hostname
      );
      return null;
    }
  }

  public int getClientNumber() {
    RMIInterface rmiConnection = null;
    String hostname = "//localhost/client";
    boolean connected = false;
    int i = 1;

    while (i < 5 && connected == false) {
      String host = hostname + String.valueOf(i);
      try {
        rmiConnection = (RMIInterface) Naming.lookup(host);
        i += 1;
      } catch (Exception ex) {
        connected = true;
      }
    }

    if (connected) {
      return i;
    }

    return -1;
  }
}
