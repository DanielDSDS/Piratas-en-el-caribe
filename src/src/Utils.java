package src;

import java.rmi.RemoteException;
import java.util.Random;

public class Utils {

  public void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  public void waitForAllClients(
    boolean isMainHost,
    Server server,
    RMIInterface rmi
  )
    throws RemoteException {
    Utils util = new Utils();

    if (isMainHost == true) {
      while (server.connectedClients != Constants.SIMULATION_CLIENTS) {
        util.wait(1000);
      }
    } else {
      boolean allConnected = false;
      while (allConnected == false) {
        allConnected = rmi.canStartSimulation();
        util.wait(1000);
      }
    }
  }

  public String getRelativeDirection(Position current, Position destination) {
    int currentX = current.getX();
    int currentY = current.getY();
    int destinationX = destination.getX();
    int destinationY = destination.getY();

    if (currentX == destinationX && currentY == destinationY) {
      // NOTE: reached destination
      return "NONE";
    }

    if (currentX != destinationX && currentY != destinationY) {
      if (currentX > destinationX) {
        return "UP";
      } else {
        return "DOWN";
      }
    }

    if (currentX == destinationX && currentY != destinationY) {
      // follow y axis
      if (currentY > destinationY) {
        return "LEFT";
      } else {
        return "RIGHT";
      }
    }

    if (currentX != destinationX && currentY == destinationY) {
      // follow x axis
      if (currentX > destinationX) {
        return "UP";
      } else {
        return "DOWN";
      }
    }

    return "NONE";
  }
}
