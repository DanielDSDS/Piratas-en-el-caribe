package src;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements RMIInterface {

  public int connectedClients = 1;
  public boolean booted = false;
  public GUI world = null;
  public int number = 1;

  public Server(String serverHost, int number) throws RemoteException {
    super();
    this.number = number;

    try {
      LocateRegistry.createRegistry(1099);
    } catch (Exception error) {}

    try {
      Naming.rebind(serverHost, this);
      this.booted = true;
    } catch (Exception e) {}
  }

  @Override
  public Treasure f(Treasure t) throws RemoteException {
    return new Treasure(t.getName(), t.getWeight());
  }

  @Override
  public Map f(Map m) throws RemoteException {
    return new Map(m.getId(), m.getNavigationCoordinates());
  }

  @Override
  public NavigationCoordinate f(NavigationCoordinate nc)
    throws RemoteException {
    return new NavigationCoordinate(
      nc.getX(),
      nc.getY(),
      nc.getWorldId(),
      nc.isVisited()
    );
  }

  public void setWorld(GUI newWorld) {
    this.world = newWorld;
  }

  @Override
  public int getConnectedClients() throws RemoteException {
    return this.connectedClients;
  }

  @Override
  public boolean canStartSimulation() throws RemoteException {
    return this.getConnectedClients() == Constants.SIMULATION_CLIENTS;
  }

  @Override
  public int connectClient() throws RemoteException {
    this.connectedClients += 1;
    return this.connectedClients;
  }

  @Override
  public void addShip(
    String role,
    String name,
    int tripulation,
    int munitions,
    int speed,
    Chest chest,
    ArrayList<Map> maps,
    int currentMap,
    int x,
    int y
  )
    throws RemoteException {
    Position pos = new Position(x, y, this.world.simulation.board);
    Ship s = new Ship(
      role,
      name,
      tripulation,
      munitions,
      speed,
      chest,
      pos,
      maps.get(currentMap),
      this.number
    );
    this.world.simulation.ships.add(s);
  }
}
