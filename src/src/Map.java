package src;

import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {

    private int id;
    private ArrayList<NavigationCoordinate> navigationCoordinates;

    public Map(int id, ArrayList<NavigationCoordinate> navigationCoordinates) {
        this.id = id;
        this.navigationCoordinates = navigationCoordinates;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<NavigationCoordinate> getNavigationCoordinates() {
        return this.navigationCoordinates;
    }

    public void setNavigationCoordinates(ArrayList<NavigationCoordinate> navigationCoordinates) {
        this.navigationCoordinates = navigationCoordinates;
    }
    
     @Override
    public String toString() {
        String string = "Id: " + this.id + "\nNavigation Coordinate List:\n";

        for (NavigationCoordinate navigationCoordinate : navigationCoordinates) {
            string += "-> " + navigationCoordinate.toString() + "\n";
        }

        return string;
    }
}
