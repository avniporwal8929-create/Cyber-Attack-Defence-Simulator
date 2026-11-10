
package security;

import attacks.Attack;

public class Firewall {

    // Check attack severity
    public boolean shouldBlock(Attack attack) {

        String severity = attack.getSeverity();

        if (severity.equalsIgnoreCase("HIGH") ||
            severity.equalsIgnoreCase("CRITICAL")) {

            System.out.println("FIREWALL: Attack BLOCKED!");
            return true;
        }

        System.out.println("FIREWALL: Attack ALLOWED!");
        return false;
    }
}
