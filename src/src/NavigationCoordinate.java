package src;
import java.io.Serializable;

public class NavigationCoordinate implements Serializable {
    private int x;
    private int y;
    private int worldId;
    private boolean visited;

    public NavigationCoordinate(int x, int y, int worldId) {
        this.x = x;
        this.y = y;
        this.worldId = worldId;
        this.visited = false;
    }

    public NavigationCoordinate(int x, int y, int worldId, boolean visited) {
        this.x = x;
        this.y = y;
        this.worldId = worldId;
        this.visited = visited;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getWorldId() {
        return this.worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public boolean getVisited() {
        return this.visited;
    }
 
    public void markAsVisited() {
        this.visited = true;
    }
    
    @Override
    public String toString() {
        return "X: " + this.x + ", Y: " + this.y + ", World Id: " + this.worldId;
    }
}
