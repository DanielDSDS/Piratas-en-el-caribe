package src;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ship {

  private String role; // P = Pirate | Q = Queen
  private String name;
  private int tripulation;
  private int munitions;
  private int speed;
  private Chest chest;
  public Position position;
  private int currentMapIndex;
  private ArrayList<Map> maps;
  private int clientNumber;
  private Object object;

  public Ship(
    String role,
    String name,
    int tripulation,
    int munitions,
    int speed,
    Chest chest,
    Position position,
    Map initialMap,
    int clientNumber
  ) {
    this.role = role;
    this.name = name;
    this.tripulation = tripulation;
    this.munitions = munitions;
    this.speed = speed;
    this.chest = chest;
    this.position = position;
    this.currentMapIndex = 0;
    this.maps = new ArrayList<Map>();
    this.clientNumber = clientNumber;
    this.object =
      this.role.equals(Constants.QUEEN_SHIP)
        ? QueenShip.class
        : PirateShip.class;

    this.position.setObject(this.object);
    this.maps.add(initialMap);
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<Map> getMaps() {
    return this.maps;
  }

  public int getCurrentMapIndex() {
    return this.currentMapIndex;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCurrentMapIndex(int currentMapIndex) {
    this.currentMapIndex = currentMapIndex;
  }

  public int getTripulation() {
    return this.tripulation;
  }

  public void setTripulation(int tripulation) {
    this.tripulation = tripulation;
  }

  public int getMunitions() {
    return this.munitions;
  }

  public void setMunitions(int munitions) {
    this.munitions = munitions;
  }

  public int getSpeed() {
    return this.speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public Chest getChest() {
    return this.chest;
  }

  public void setChest(Chest chest) {
    this.chest = chest;
  }

  public Position getPosition() {
    return this.position;
  }

  public void setPosition(Position position) {
    this.position = position;

    this.position.setObject(this.object);
  }

  public int getClientNumber() {
    return this.clientNumber;
  }

  public void setClientNumber(int clientNumber) {
    this.clientNumber = clientNumber;
  }

  public Object getObject() {
    return this.object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public void moveUp() throws RemoteException {
    this.position.UP(this.clientNumber);
  }

  public void moveRight() throws RemoteException {
    this.position.RIGHT(this.clientNumber);
  }

  public void moveDown() throws RemoteException {
    this.position.DOWN(this.clientNumber);
  }

  public void moveLeft() throws RemoteException {
    this.position.LEFT(this.clientNumber);
  }

  public void moveToNextPosition(Grilla b) {
    try {
      var util = new Utils();
      var currentWorld = this.clientNumber;
      var currentMap = this.maps.get(this.currentMapIndex);
      ArrayList<NavigationCoordinate> currentMapNavigationCoordinates = currentMap.getNavigationCoordinates();
      NavigationCoordinate nextNavigationCoordinate = null;

      // TODO:
      // check if all currentMapNavigationCoordinates are visited=true
      // if so, check if there is other map
      // update currentMapIndex to next map
      // refresh currentMap & currentMapNavigationCoordinates

      for (int i = 0; i < currentMapNavigationCoordinates.size(); i++) {
        var navCoord = currentMapNavigationCoordinates.get(i);
        var coordVisited = navCoord.getVisited();

        if (coordVisited == false) {
          // this is where we move to next
          nextNavigationCoordinate = navCoord;
          break;
          // TODO: if we are on top of coordinate, mark as visited
          // map as visited since we are moving to it
          // navCoord.setVisited(true);
        }
      }

      if (nextNavigationCoordinate == null) {
        // no new movements
        return;
      }

      Position destination = null;
      String direction = null;
      var destinationX = nextNavigationCoordinate.getX();
      var destinationY = nextNavigationCoordinate.getY();
      var destinationWorldId = nextNavigationCoordinate.getWorldId();

      if (currentWorld == destinationWorldId) {
        // if we are moving in the same world, move as usual
        destination = new Position(destinationX, destinationY, b);
        direction = util.getRelativeDirection(this.position, destination);
        this.move(direction);
      } else {
        switch (currentWorld) {
          case 1:
            if (destinationWorldId == 2) {
              destination = new Position(this.position.getX(), 99, b);
            } else if (destinationWorldId == 3 || destinationWorldId == 4) {
              destination = new Position(99, this.position.getY(), b);
            }
            break;
          case 2:
            if (destinationWorldId == 1) {
              destination = new Position(this.position.getX(), -1, b);
            } else if (destinationWorldId == 4 || destinationWorldId == 3) {
              destination = new Position(99, this.position.getY(), b);
            }
            break;
          case 3:
            if (destinationWorldId == 4) {
              destination = new Position(this.position.getX(), 99, b);
            } else if (destinationWorldId == 1 || destinationWorldId == 2) {
              destination = new Position(-1, this.position.getY(), b);
            }
            break;
          case 4:
            if (destinationWorldId == 3) {
              destination = new Position(this.position.getX(), -1, b);
            } else if (destinationWorldId == 2 || destinationWorldId == 1) {
              destination = new Position(-1, this.position.getY(), b);
            }
            break;
        }
        if (destination == null) {
          System.out.println("Error");
        } else {
          direction = util.getRelativeDirection(this.position, destination);
          this.move(direction);
        }
      }

      // mark as visited if we are in same coordinates after movement
      if (
        this.position.getX() == destinationX &&
        this.position.getY() == destinationY
      ) {
        nextNavigationCoordinate.setVisited(true);
      }
    } catch (RemoteException ex) {
      Logger.getLogger(Ship.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void move(String direction) throws RemoteException {
    switch (direction) {
      case "UP":
        this.moveUp();
        break;
      case "DOWN":
        this.moveDown();
        break;
      case "RIGHT":
        this.moveRight();
        break;
      case "LEFT":
        this.moveLeft();
        break;
    }
  }

  @Override
  public String toString() {
    return (
      this.name +
      "\nTripulacion: " +
      this.tripulation +
      "\nPosicion: " +
      this.position.toString() +
      " \n Cofre 💰:\n" +
      this.chest.toString()
    );
  }
}
