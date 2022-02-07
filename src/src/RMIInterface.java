package src;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterface extends Remote {
    public int connectClient() throws RemoteException;
    public int getConnectedClients() throws RemoteException;
    public boolean canStartSimulation() throws RemoteException;
    public void addShip(String role, String name, int tripulation, int munitions, int speed, Chest chest, ArrayList<Map> maps, int currentMap, int x, int y) throws RemoteException;
    Treasure f(Treasure x) throws RemoteException;
    Map f(Map x) throws RemoteException;
    NavigationCoordinate f(NavigationCoordinate x) throws RemoteException;
}
