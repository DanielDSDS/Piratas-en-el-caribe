package src;
import java.io.Serializable;

public class Treasure implements Serializable {

    private String name;
    private int weight;

    public Treasure(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isMap() {
        return this.name.contains("Mapa");
    }

    public int getMapId() {
        if (!this.isMap()) {
            return -1;
        }

        return Integer.parseInt(this.name.substring(this.name.lastIndexOf("#") + 1));
    }

    @Override
    public String toString() {
        return this.name + " (" + this.weight + " libras)";
    }
}
