package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Simulation {

  public Grilla board;
  public ArrayList<Ship> ships;
  public ArrayList<Surface> surfaces;
  public ArrayList<Surface> temporarySurfaces;
  public ArrayList<Map> maps;

  Simulation() {
    this.board = null;
    this.ships = null;
    this.surfaces = null;
    this.temporarySurfaces = new ArrayList<>();
  }

  public void setSimulation(int rows, int columns, int clientNumber) {
    this.board = new Grilla(rows, columns, this);
    ConfigFileManager config = null;

    try {
      config = new ConfigFileManager(clientNumber, board);
    } catch (ParserConfigurationException | SAXException | IOException ex) {
      Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
    }

    this.surfaces = config.getSurfaces();
    this.maps = config.getMaps();
    this.ships = config.getShips();
  }

  public void checkSimulation() {
    checkShips();
    checkSurfaces();
  }

  private void checkShips() {
    Ship[] arr = new Ship[this.ships.size()];
    for (int i = 0; i < this.ships.size(); i++) {
      arr[i] = this.ships.get(i);
    }
    for (int i = 0; i < ships.size(); i++) {
      for (int j = 0; j < ships.size(); j++) {
        if (i == j) {
          continue;
        }
        // TODO: si chocan los barcos, deben pelear
        // TODO: falta verificar el tipo de barco (reina o pirata)
        // if(ships.get(i).getPosition().isEqual(ships.get(j).getPosition())) {
        // 	this.b.removeShip(ships.get(i).getPosition());
        // 	this.b.removeShip(ships.get(j).getPosition());
        // 	surfaces.add(new Island(ships.get(i).getPosition()));
        // 	arr[i]=null;
        // 	arr[j]=null;
        // }
      }
    }
    ArrayList<Ship> temp = new ArrayList<Ship>();
    for (Ship p : arr) {
      if (p != null) {
        temp.add(p);
      }
    }
    this.ships = temp;
  }

  private void checkSurfaces() {
    Ship currentShip = null;
    Surface currentSurface = null;

    Iterator<Treasure> treasureListIterator = null;

    Treasure currentTreasure = null;

    for (int i = 0; i < ships.size(); i++) {
      currentShip = ships.get(i);

      for (int j = 0; j < surfaces.size(); j++) {
        currentSurface = surfaces.get(j);

        if (currentShip.getPosition().isEqual(currentSurface.getPosition())) {
          this.temporarySurfaces.add(currentSurface);
          this.board.removeSurface(currentSurface.getPosition());
          this.board.setShip(currentShip.getPosition(), currentShip.getRole());

          treasureListIterator =
            currentSurface.getChest().getTreasures().iterator();

          while (treasureListIterator.hasNext()) {
            currentTreasure = treasureListIterator.next();

            if (currentShip.getChest().addTreasure(currentTreasure)) {
              treasureListIterator.remove();
            }
          }
        }
      }
    }
  }

  public void removeShipFromWorld(Ship s) {
    int shipIndex = this.ships.indexOf(s);
    this.ships.remove(shipIndex);
  }

  public Ship getShipInPosition(int x, int y) {
    Ship s = null;

    for (int i = 0; i < this.ships.size(); i++) {
      Ship ship = this.ships.get(i);
      if (ship != null) {
        if (ship.position.getX() == x && ship.position.getY() == y) {
          s = ship;
        }
      }
    }

    return s;
  }

  public void nextActions() {
    for (int i = 0; i < this.ships.size(); i++) {
      Ship ship = this.ships.get(i);
      if (ship != null) {
        ship.moveToNextPosition(this.board);
      }
    }
  }

  public boolean isEndSimulation() {
    for (Ship ship : this.ships) {
      for (Treasure treasure : ship.getChest().getTreasures()) {
        if (treasure.getName().equals("Corazón de la Princesa")) {
          JOptionPane.showMessageDialog(
            null,
            " El Corazon de la Princesa fue encontrado por la flota " +
            ship.getName() +
            "\n Simulacion finalizada"
          );

          return true;
        }
      }
    }

    return false;
  }
}
