
package threats;

import attacks.Attack;
import java.util.ArrayList;

public class ThreatManager {

    private ArrayList<Attack> threats;
    private ArrayList<String> statuses;

    public ThreatManager() {
        threats = new ArrayList<>();
        statuses = new ArrayList<>();
    }

    // Add a newly detected threat
    public void addThreat(Attack attack) {

        threats.add(attack);
        statuses.add("PENDING");

        System.out.println(
            "Threat recorded: " + attack.getAttackName()
        );
    }

    // Get all detected threats
    public ArrayList<Attack> getThreats() {
        return threats;
    }

    // Get the status of a particular threat
    public String getStatus(int index) {

        if (index < 0 || index >= statuses.size()) {
            return "UNKNOWN";
        }

        return statuses.get(index);
    }

    // Investigate a threat
    public boolean investigateThreat(int index) {

        if (index < 0 || index >= threats.size()) {
            return false;
        }

        if (statuses.get(index).equals("RESOLVED")) {
            return false;
        }

        statuses.set(index, "INVESTIGATING");

        System.out.println(
            "Threat under investigation: "
            + threats.get(index).getAttackName()
        );

        return true;
    }

    // Resolve a threat
    public boolean resolveThreat(int index) {

        if (index < 0 || index >= threats.size()) {
            return false;
        }

        if (statuses.get(index).equals("RESOLVED")) {
            return false;
        }

        statuses.set(index, "RESOLVED");

        System.out.println(
            "Threat resolved: "
            + threats.get(index).getAttackName()
        );

        return true;
    }

    // Count unresolved threats
    public int getActiveThreatCount() {

        int count = 0;

        for (String status : statuses) {

            if (!status.equals("RESOLVED")) {
                count++;
            }
        }

        return count;
    }

    // Display all threats
    public void displayThreats() {

        System.out.println("\n===== THREAT MANAGEMENT =====");

        if (threats.isEmpty()) {
            System.out.println("No threats detected.");
            return;
        }

        for (int i = 0; i < threats.size(); i++) {

            Attack attack = threats.get(i);

            System.out.println(
                "Attack: " + attack.getAttackName()
            );

            System.out.println(
                "Target: " + attack.getTarget()
            );

            System.out.println(
                "Severity: " + attack.getSeverity()
            );

            System.out.println(
                "Status: " + statuses.get(i)
            );

            System.out.println("----------------------");
        }
    }
}
