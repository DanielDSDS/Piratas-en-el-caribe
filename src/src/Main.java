package src;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

public class Main {

  private static class Action {

    public boolean should_run_next_action;
  }

  public static void main(String[] args) throws Exception {
    boolean booted = false;
    boolean isMainHost = false;
    int client = new RMIHelper().getClientNumber();
    Server server = null;
    RMIInterface rmi = null;
    Utils util = new Utils();

    if (client == -1) {
      System.out.println("Ocurrio un error estableciendo la conexion");
      return;
    }

    while (client <= Constants.SIMULATION_CLIENTS && booted == false) {
      String hostname = "//localhost/client" + String.valueOf(client);

      try {
        server = new Server(hostname, client);
        booted = server.booted;
        if (server.booted && client == 1) {
          isMainHost = true;
        }
      } catch (RemoteException e) {}
    }

    if (client > 1) {
      rmi = new RMIHelper().connect("//localhost/client1");
      client = rmi.connectClient();

      util.waitForAllClients(false, null, rmi);
    }

    if (isMainHost == true) {
      util.waitForAllClients(true, server, null);
    }

    boolean running = true;
    GUI gui = new GUI(Constants.BOARD_ROWS, Constants.BOARD_COLUMNS, client);
    server.setWorld(gui);

    Action act = new Action();
    act.should_run_next_action = true;

    final ExecutorService service = Executors.newCachedThreadPool();
    final class MyTask implements Runnable {

      @Override
      public void run() {
        gui.simulation.nextActions();

        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        act.should_run_next_action = true;
      }
    }
    while (running == true) {
      try {
        if (act.should_run_next_action == true) {
          service.submit(new MyTask());
          act.should_run_next_action = false;
        }

        gui.simulation.checkSimulation();
        gui.renderBoard();

        if (gui.simulation.isEndSimulation()) {
          running = false;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
