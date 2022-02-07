package src;

public class Grilla {

  String[][] matrix;
  private int rows;
  private int columns;
  public Simulation simulation;

  Grilla(int rows, int columns, Simulation simulation) {
    this.simulation = simulation;
    this.rows = rows;
    this.columns = columns;
    matrix = new String[this.rows][this.columns];

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        matrix[i][j] = Constants.EMPTY;
      }
    }
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }

  public void toConsole() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        System.out.print(matrix[i][j] + " ");
      }
    }
  }

  public boolean isValidPosition(int x, int y) {
    if ((x < 0) || (x >= this.rows) || (y < 0) || (y >= this.columns)) {
      return false;
    }

    return true;
  }

  public boolean isValidPosition(Position p) {
    if (
      p.getX() < 0 ||
      p.getX() >= this.rows ||
      p.getY() < 0 ||
      p.getY() >= this.columns
    ) {
      return false;
    }

    return true;
  }

  public String onPosition(Position p) {
    if (!isValidPosition(p)) {
      return Constants.EMPTY;
    }

    return matrix[p.getX()][p.getY()];
  }

  public String onPosition(int x, int y) {
    if (!isValidPosition(x, y)) {
      return Constants.EMPTY;
    }

    return matrix[x][y];
  }

  public void setShip(Position p, String role) {
    if (!isValidPosition(p)) {
      return;
    }

    matrix[p.getX()][p.getY()] = role;
  }

  public void removeShip(Position p) {
    if (
      onPosition(p).equals(Constants.PIRATE_SHIP) ||
      onPosition(p).equals(Constants.QUEEN_SHIP)
    ) {
      matrix[p.getX()][p.getY()] = Constants.EMPTY;
    }
  }

  public void setSurface(Position p, String type) {
    if (!isValidPosition(p)) {
      return;
    }

    matrix[p.getX()][p.getY()] = type;
  }

  public void removeSurface(Position p) {
    if (
      onPosition(p).equals(Constants.SITE) ||
      onPosition(p).equals(Constants.CAY)
    ) {
      this.matrix[p.getX()][p.getY()] = Constants.EMPTY;
    }
  }
}
