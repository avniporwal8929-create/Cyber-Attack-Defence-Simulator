
package security;

import attacks.Attack;

public class IntrusionDetector {

    public boolean detect(Attack attack) {

        String severity = attack.getSeverity();
        String name = attack.getAttackName();

        if (severity.equalsIgnoreCase("HIGH") ||
            severity.equalsIgnoreCase("CRITICAL") ||
            name.equalsIgnoreCase("Phishing") ||
            name.equalsIgnoreCase("Malware")) {

            System.out.println(
                "IDS ALERT: Suspicious activity detected - "
                + name
            );

            return true;
        }

        System.out.println("IDS: No suspicious activity detected.");
        return false;
    }
}
