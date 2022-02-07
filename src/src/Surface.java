package src;

public class Surface {

  private String type; // S = Site | C = Cay
  private String name;
  private Object object;
  private Position position;
  private int landingLosses;
  private Chest chest;

  Surface(
    String type,
    String name,
    Position position,
    int landingLosses,
    Chest chest
  ) {
    this.type = type;
    this.name = name;
    this.object = this.type.equals(Constants.SITE) ? Site.class : Cay.class;
    this.position = position;
    this.landingLosses = landingLosses;
    this.chest = chest;

    this.position.setObject(this.object);
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public void setPosition(Position p) {
    this.position = p;

    this.position.setObject(this.object);
  }

  public Position getPosition() {
    return this.position;
  }

  public int getLandingLosses() {
    return this.landingLosses;
  }

  public void setLandingLosses(int landingLosses) {
    this.landingLosses = landingLosses;
  }

  public Chest getChest() {
    return this.chest;
  }

  public void setChest(Chest chest) {
    this.chest = chest;
  }

  @Override
  public String toString() {
    return (
      this.getName() +
      " - " +
      (this.getType().equals(Constants.SITE) ? "Sitio" : "Cayo") +
      "\nUbicación 🗺️: " +
      this.position.toString() +
      "\nBajas por desembarco 💀: " +
      this.getLandingLosses() +
      " \nTesoro 💰:\n" +
      this.chest.toString()
    );
  }
}
