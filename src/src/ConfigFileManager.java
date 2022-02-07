package src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigFileManager {

  private File file;
  private DocumentBuilderFactory dbf;
  private DocumentBuilder db;
  private Document document;
  private int clientNumber;
  private Grilla board;

  public ConfigFileManager(int clientNumber, Grilla board)
    throws ParserConfigurationException, SAXException, IOException {
    this.file = new File(Constants.CONFIG_FILE);
    this.dbf = DocumentBuilderFactory.newInstance();
    this.db = this.dbf.newDocumentBuilder();
    this.document = this.db.parse(this.file);
    this.clientNumber = clientNumber;
    this.board = board;

    this.document.getDocumentElement().normalize();
  }

  public ArrayList iterateNodeList(NodeList nodeList, String methodName) {
    ArrayList items = new ArrayList<>();
    Node node = null;
    Element element = null;

    for (int i = 0; i < nodeList.getLength(); i++) {
      node = nodeList.item(i);

      if (node.getNodeType() == Node.ELEMENT_NODE) {
        element = (Element) node;

        switch (methodName) {
          case "createSurface":
            items.add(createSurface(element));
            break;
          case "createTreasure":
            items.add(createTreasure(element));
            break;
          case "createShip":
            if (
              this.clientNumber ==
              Integer.parseInt(element.getAttribute("worldId"))
            ) {
              items.add(createShip(element));
            }
            break;
          case "createMap":
            items.add(createMap(element));
            break;
          case "createNavigationCoordinate":
            items.add(createNavigationCoordinate(element));
            break;
          default:
            items.add(null);
        }
      }
    }

    return items;
  }

  public Treasure createTreasure(Element treasureElement) {
    String name = treasureElement.getAttribute("name");
    int weight = Integer.parseInt(treasureElement.getAttribute("weight"));

    return new Treasure(name, weight);
  }

  public Chest createChest(Element element, int chestCapacity) {
    NodeList treasureNodeList = element.getElementsByTagName("treasure");
    ArrayList<Treasure> treasures = iterateNodeList(
      treasureNodeList,
      "createTreasure"
    );

    return new Chest(chestCapacity, treasures);
  }

  public Surface createSurface(Element surfaceElement) {
    String type = surfaceElement.getAttribute("type");
    String name = surfaceElement.getAttribute("name");
    int x = Integer.parseInt(surfaceElement.getAttribute("x"));
    int y = Integer.parseInt(surfaceElement.getAttribute("y"));
    int landingLosses = Integer.parseInt(
      surfaceElement.getAttribute("landingLosses")
    );
    Position position = new Position(x, y, this.board);
    Chest chest = createChest(surfaceElement, Constants.CAPACITY_SURFACE_CHEST);

    return new Surface(type, name, position, landingLosses, chest);
  }

  public Ship createShip(Element shipElement) {
    String role = shipElement.getAttribute("role");
    String name = shipElement.getAttribute("name");
    int tripulation = Integer.parseInt(shipElement.getAttribute("tripulation"));
    int munitions = Integer.parseInt(shipElement.getAttribute("munitions"));
    int speed = Integer.parseInt(shipElement.getAttribute("speed"));
    int startX = Integer.parseInt(shipElement.getAttribute("startX"));
    int startY = Integer.parseInt(shipElement.getAttribute("startY"));
    Chest chest = createChest(shipElement, Constants.CAPACITY_SHIP_CHEST);
    var pos = new Position(startX, startY, this.board);
    Element treasureElement = (Element) shipElement
      .getElementsByTagName("treasure")
      .item(0);
    var treasureObj = new Treasure(
      treasureElement.getAttribute("name"),
      Integer.parseInt(treasureElement.getAttribute("weight"))
    );
    Map starterMap = null;

    if (treasureObj.isMap()) {
      var mapId = treasureObj.getMapId();
      var maps = document.getElementsByTagName("map");

      for (int i = 0; i < maps.getLength(); i++) {
        Node node = maps.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;
          int _mapId = Integer.parseInt(element.getAttribute("id"));

          if (_mapId == mapId) {
            starterMap = createMap(element);
          }
        }
      }
    }

    return new Ship(
      role,
      name,
      tripulation,
      munitions,
      speed,
      chest,
      pos,
      starterMap,
      this.clientNumber
    );
  }

  public NavigationCoordinate createNavigationCoordinate(
    Element navigationCoordinateElement
  ) {
    int x = Integer.parseInt(navigationCoordinateElement.getAttribute("x"));
    int y = Integer.parseInt(navigationCoordinateElement.getAttribute("y"));
    int worldId = Integer.parseInt(
      navigationCoordinateElement.getAttribute("worldId")
    );

    return new NavigationCoordinate(x, y, worldId);
  }

  public Map createMap(Element mapElement) {
    int id = Integer.parseInt(mapElement.getAttribute("id"));

    NodeList navigationCoordinateNodeList = mapElement.getElementsByTagName(
      "navigation_coordinate"
    );
    ArrayList<NavigationCoordinate> navigationCoordinates = iterateNodeList(
      navigationCoordinateNodeList,
      "createNavigationCoordinate"
    );

    return new Map(id, navigationCoordinates);
  }

  public ArrayList<Surface> getSurfaces() {
    Element worldElement = (Element) document
      .getElementsByTagName("world")
      .item(this.clientNumber - 1);
    NodeList surfaceNodeList = worldElement.getElementsByTagName("surface");

    return iterateNodeList(surfaceNodeList, "createSurface");
  }

  public ArrayList<Ship> getShips() {
    NodeList shipNodeList = document.getElementsByTagName("ship");

    return iterateNodeList(shipNodeList, "createShip");
  }

  public ArrayList<Map> getMaps() {
    NodeList mapNodeList = document.getElementsByTagName("map");

    return iterateNodeList(mapNodeList, "createMap");
  }
}
