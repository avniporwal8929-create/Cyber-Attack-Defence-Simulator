import attacks.*;
import javax.swing.SwingUtilities;
import logs.SecurityLogger;
import network.*;
import security.*;
import threats.ThreatManager;
import ui.Dashboard;

public class Main {

    public static void main(String[] args) {

        // Creating Network Manager
        NetworkManager manager = new NetworkManager();

        // Creating Network Devices
        Server server = new Server("S101", "Main Server");
        Router router = new Router("R101", "Main Router");
        Database database = new Database("D101", "User Database");

        // Adding devices
        manager.addDevice(server);
        manager.addDevice(router);
        manager.addDevice(database);

        // Displaying devices
        manager.displayAllDevices();

        // Create Attack Manager
AttackManager attackManager = new AttackManager();

// Create simulated attacks
attackManager.addAttack(
    new PhishingAttack("Main Server")
);

attackManager.addAttack(
    new BruteForceAttack("User Database")
);

attackManager.addAttack(
    new MalwareAttack("Test Server")
);

attackManager.addAttack(
    new TrafficOverloadAttack("Main Router")
);

// Execute simulations
attackManager.executeAllAttacks();
// Create defense components
Firewall firewall = new Firewall();
IntrusionDetector detector = new IntrusionDetector();



ThreatManager threatManager = new ThreatManager();
SecurityLogger logger = new SecurityLogger();

System.out.println("\n===== DEFENSE SYSTEM =====");

for (Attack attack : attackManager.getAttacks()) {

    System.out.println("\nChecking: " + attack.getAttackName());

    boolean detected = detector.detect(attack);
    boolean blocked = firewall.shouldBlock(attack);

    if (detected) {
        threatManager.addThreat(attack);
    }

    String status = blocked ? "BLOCKED" : "ALLOWED";

    logger.logEvent(
        attack.getAttackName() +
        " | Target: " + attack.getTarget() +
        " | Status: " + status +
        " | Detected: " + detected
    );
}

threatManager.displayThreats();
logger.displayLogs();

        // Opening Dashboard
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}
