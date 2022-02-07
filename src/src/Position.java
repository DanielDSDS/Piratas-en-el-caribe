package src;

import java.rmi.RemoteException;

public class Position {

  private int x; // row
  private int y; // column
  private Grilla board;
  private Object object;

  Position(int x, int y, Grilla board) {
    this.board = board;
    this.x = x;
    this.y = y;
    this.object = null;
  }

  public void setX(int x) {
    if (!this.board.isValidPosition(x, this.y)) {
      return;
    }
    this.x = x;
  }

  public void setObject(Object o) {
    this.object = o;

    setClassObject();
  }

  public Object getObject() {
    return this.object;
  }

  public Grilla getBoard() {
    return this.board;
  }

  public void setBoard(Grilla b) {
    this.board = b;
  }

  public void setY(int y) {
    if (!this.board.isValidPosition(this.x, y)) {
      return;
    }
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void UP(int client) throws RemoteException {
    if (this.object == null) {
      throw new NullPointerException(
        "You can not move Position without object"
      );
    }
    int newX = this.x - 1;
    int newY = this.y;
    if (this.handleMovingToOtherWorld(client, this.x, this.y, newX, newY)) {
      return;
    }
    if (!this.board.isValidPosition(newX, newY)) {
      return;
    }
    removeClassObject();
    this.x--;
    setClassObject();
  }

  public void RIGHT(int client) throws RemoteException {
    if (this.object == null) {
      throw new NullPointerException(
        "You can not move Position without object"
      );
    }
    int newX = this.x;
    int newY = this.y + 1;
    if (this.handleMovingToOtherWorld(client, this.x, this.y, newX, newY)) {
      return;
    }
    if (!this.board.isValidPosition(newX, newY)) {
      return;
    }
    removeClassObject();
    this.y++;
    setClassObject();
  }

  public void DOWN(int client) throws RemoteException {
    if (this.object == null) {
      throw new NullPointerException(
        "You can not move Position without object"
      );
    }
    int newX = this.x + 1;
    int newY = this.y;
    if (this.handleMovingToOtherWorld(client, this.x, this.y, newX, newY)) {
      return;
    }
    if (!this.board.isValidPosition(newX, newY)) {
      return;
    }
    removeClassObject();
    this.x++;
    setClassObject();
  }

  public void LEFT(int client) throws RemoteException {
    if (this.object == null) {
      throw new NullPointerException(
        "You can not move Position without object"
      );
    }
    int newX = this.x;
    int newY = this.y - 1;
    if (this.handleMovingToOtherWorld(client, this.x, this.y, newX, newY)) {
      return;
    }
    if (!this.board.isValidPosition(newX, newY)) {
      return;
    }
    removeClassObject();
    this.y--;
    setClassObject();
  }

  // returns true if moved to another client
  public boolean handleMovingToOtherWorld(
    int client,
    int currentX,
    int currentY,
    int newX,
    int newY
  )
    throws RemoteException {
    if (
      !this.object.equals(PirateShip.class) &&
      !this.object.equals(QueenShip.class)
    ) {
      return false;
    }

    boolean movedToOtherWorld = false;
    int X_START = 0;
    int Y_START = 0;
    int X_END = 9;
    int Y_END = 9;
    int worldToGo = -1;
    int xInNewWorld = this.x;
    int yInNewWorld = this.y;

    switch (client) {
      case 1:
        if (newY > Y_END) {
          yInNewWorld = Y_START;
          worldToGo = 2;
        } else if (newX > X_END) {
          xInNewWorld = X_START;
          worldToGo = 3;
        }
        break;
      case 2:
        if (newY < Y_START) {
          yInNewWorld = Y_END;
          worldToGo = 1;
        } else if (newX > X_END) {
          xInNewWorld = X_START;
          worldToGo = 4;
        }
        break;
      case 3:
        if (newY > Y_END) {
          yInNewWorld = Y_START;
          worldToGo = 4;
        } else if (newX < X_START) {
          xInNewWorld = X_END;
          worldToGo = 1;
        }
        break;
      case 4:
        if (newY < Y_START) {
          yInNewWorld = Y_END;
          worldToGo = 3;
        } else if (newX < X_START) {
          xInNewWorld = X_END;
          worldToGo = 2;
        }
        break;
    }

    if (worldToGo != -1) {
      Ship oldShip =
        this.board.simulation.getShipInPosition(currentX, currentY);

      removeClassObject(); // stop painting in current world
      this.board.simulation.removeShipFromWorld(oldShip); // delete from world

      RMIInterface rmi = new RMIHelper()
      .connect("//localhost/client" + String.valueOf(worldToGo));

      rmi.addShip(
        oldShip.getRole(),
        oldShip.getName(),
        oldShip.getTripulation(),
        oldShip.getMunitions(),
        oldShip.getSpeed(),
        oldShip.getChest(),
        oldShip.getMaps(),
        oldShip.getCurrentMapIndex(),
        xInNewWorld,
        yInNewWorld
      );

      movedToOtherWorld = true;
    }

    return movedToOtherWorld;
  }

  private void removeClassObject() {
    if (
      this.object.equals(PirateShip.class) ||
      this.object.equals(QueenShip.class)
    ) {
      this.board.removeShip(this);
    }
  }

  private void setClassObject() {
    if (this.object.equals(PirateShip.class)) {
      this.board.setShip(this, Constants.PIRATE_SHIP);
    }

    if (this.object.equals(QueenShip.class)) {
      this.board.setShip(this, Constants.QUEEN_SHIP);
    }

    if (this.object.equals(Site.class)) {
      this.board.setSurface(this, Constants.SITE);
    }

    if (this.object.equals(Cay.class)) {
      this.board.setSurface(this, Constants.CAY);
    }
  }

  public boolean isEqual(Position p) {
    if (this.x == p.getX() && this.y == p.getY()) {
      return true;
    }
    return false;
  }

  /**
   * As we know, every class in Java (even classes defined by ourselves) are
   * subclasses of java.Object class. As java.Object have generic method
   * toString() we can override it to provide our own implementation of it.
   *
   * @return String like (x,y)
   */
  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  }
}
