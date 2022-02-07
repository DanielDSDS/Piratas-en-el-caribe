package src;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GUI {

  private static final ImageIcon PIRATE_SHIP_ICON = new ImageIcon(
    Paths.get(".").toAbsolutePath().normalize().toString() +
    "/src/resources/pirate.png"
  );
  private static final ImageIcon QUEEN_SHIP_ICON = new ImageIcon(
    Paths.get(".").toAbsolutePath().normalize().toString() +
    "/src/resources/queen.png"
  );
  private static final ImageIcon SEA_ICON = new ImageIcon(
    Paths.get(".").toAbsolutePath().normalize().toString() +
    "/src/resources/sea.png"
  );
  private static final ImageIcon ISLAND_ICON = new ImageIcon(
    Paths.get(".").toAbsolutePath().normalize().toString() +
    "/src/resources/site.png"
  );
  private static final ImageIcon CAY_ICON = new ImageIcon(
    Paths.get(".").toAbsolutePath().normalize().toString() +
    "/src/resources/cay.png"
  );

  public Simulation simulation;
  public JFrame window;
  private final JPanel panelwindow;
  private final JPanel panelMenu;
  private final JPanel simulationInfoPanel;
  private final JPanel simulationPanel;
  private final JLabel clientLabel;
  private final JTextArea locationsTxtArea;
  private final JTextArea shipsTxtArea;
  private final JButton[][] field;
  private int clientNumber = 1;

  private static final Color BORDER_COLOR = new Color(96, 183, 215);
  private static final Color SEA_COLOR = new Color(106, 183, 215);

  private static Border border = new CompoundBorder(
    new EmptyBorder(10, 0, 10, 0),
    new LineBorder(BORDER_COLOR)
  );

  GUI(int rows, int columns, int client) {
    this.clientNumber = client;
    this.simulation = new Simulation();
    this.window =
      new JFrame("Piratas en el Caribe - Maquina " + String.valueOf(client));
    this.panelwindow = new JPanel(new BorderLayout());
    this.panelMenu = new JPanel(new BorderLayout());
    this.simulationInfoPanel = new JPanel();
    this.simulationPanel = new JPanel(new GridLayout(rows, columns));
    this.clientLabel = new JLabel();
    this.locationsTxtArea = new JTextArea();
    this.shipsTxtArea = new JTextArea();
    this.field = new JButton[rows][columns];

    this.window.setContentPane(this.panelwindow);

    this.panelMenu.setPreferredSize(new Dimension(600, 700));
    this.panelMenu.setBackground(Color.WHITE);
    this.panelMenu.setBorder(
        BorderFactory.createMatteBorder(0, 0, 0, 1, this.BORDER_COLOR)
      );
    this.panelwindow.add(this.simulationPanel, BorderLayout.LINE_START);
    this.panelwindow.add(this.panelMenu, BorderLayout.LINE_END);

    this.simulationInfoPanel.setBackground(Color.WHITE);
    this.panelMenu.add(this.simulationInfoPanel, BorderLayout.LINE_END);

    this.simulationInfoPanel.add(
        this.renderTextArea(locationsTxtArea, "Ubicaciones")
      );

    this.simulation.setSimulation(rows, columns, this.clientNumber);

    this.window.setSize(965, 800);
    this.window.setLocation(100, 200);
    this.window.setResizable(false);
    this.window.setVisible(true);
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        this.field[i][j] = new JButton();
        this.field[i][j].setEnabled(false);
        this.field[i][j].setBackground(this.SEA_COLOR);
        this.field[i][j].setBorder(new LineBorder(this.BORDER_COLOR));
        this.simulationPanel.add(this.field[i][j]);
      }
    }
  }

  public void updateData(JTextArea textArea, ArrayList list) {
    String txt = "";

    for (int i = 0; i < list.size(); i++) {
      txt += (i == 0 ? "" : "\n") + (i + 1) + ") " + list.get(i).toString();
    }

    textArea.setText(txt);
  }

  public void rewriteSurfacesOnTheBoard() {
    for (int i = 0; i < this.simulation.temporarySurfaces.size(); i++) {
      Surface surface = this.simulation.temporarySurfaces.get(i);

      if (
        this.simulation.board.onPosition(surface.getPosition())
          .equals(Constants.EMPTY)
      ) {
        this.simulation.board.setSurface(
            surface.getPosition(),
            surface.getType()
          );
        this.simulation.temporarySurfaces.remove(i);
      }
    }
  }

  public void renderBoard() {
    ImageIcon img = null;

    rewriteSurfacesOnTheBoard();

    updateData(this.locationsTxtArea, this.simulation.surfaces);

    for (int i = 0; i < this.simulation.board.getRows(); i++) {
      for (int j = 0; j < this.simulation.board.getColumns(); j++) {
        switch (this.simulation.board.onPosition(i, j)) {
          case Constants.PIRATE_SHIP:
            img = this.PIRATE_SHIP_ICON;
            break;
          case Constants.QUEEN_SHIP:
            img = this.QUEEN_SHIP_ICON;
            break;
          case Constants.SITE:
            img = this.ISLAND_ICON;
            break;
          case Constants.CAY:
            img = this.CAY_ICON;
            break;
          default:
            img = this.SEA_ICON;
            break;
        }

        this.field[i][j].setIcon(img);
        this.field[i][j].setDisabledIcon(img);
      }
    }
  }

  public JScrollPane renderTextArea(JTextArea textArea, String title) {
    JScrollPane scrollPane = new JScrollPane(textArea);

    scrollPane.setBorder(BorderFactory.createTitledBorder(this.border, title));
    scrollPane.setPreferredSize(new Dimension(275, 750));
    Font font = new Font("Roboto", 100, 10);
    textArea.setFont(font);
    scrollPane.setBackground(Color.WHITE);
    scrollPane.setAutoscrolls(true);

    return scrollPane;
  }

  public void msgBox(String s) {
    JOptionPane.showMessageDialog(this.window, s);
  }
}
