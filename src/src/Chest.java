package src;

import java.io.Serializable;
import java.util.ArrayList;

public class Chest implements Serializable {

  private int capacity;
  private ArrayList<Treasure> treasures;
  private int accumulatedWeight;

  public Chest(int capacity, ArrayList<Treasure> treasures) {
    this.capacity = capacity;
    this.treasures = treasures;

    int acum = 0;

    for (Treasure t : this.treasures) {
      acum += t.getWeight();
    }

    this.accumulatedWeight = acum;
  }

  public int getCapacity() {
    return this.capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setAccumulatedWeight(int weight) {
    this.accumulatedWeight = weight;
  }

  public int getAccumlatedWeight() {
    return this.accumulatedWeight;
  }

  public ArrayList<Treasure> getTreasures() {
    return this.treasures;
  }

  public void setTreasures(ArrayList<Treasure> treasures) {
    this.treasures = treasures;
  }

  public Boolean addTreasure(Treasure newTreasure) {
    int newAccumulatedWeight = newTreasure.getWeight() + this.accumulatedWeight;

    if (newAccumulatedWeight > this.capacity) {
      return false;
    }

    this.setAccumulatedWeight(newAccumulatedWeight);

    return this.treasures.add(newTreasure);
  }

  public void removeTreasure(Treasure t) {
    int treasureIndex = this.treasures.indexOf(t);
    this.treasures.remove(treasureIndex);
  }

  @Override
  public String toString() {
    String string = "";

    for (Treasure treasure : treasures) {
      string += " ✔️" + treasure.toString() + "\n";
    }

    return string;
  }
}
