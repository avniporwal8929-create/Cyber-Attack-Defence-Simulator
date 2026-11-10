
package logs;

import java.util.ArrayList;

public class SecurityLogger {

    private ArrayList<String> logs;

    public SecurityLogger() {
        logs = new ArrayList<>();
    }

    // Record security event
    public void logEvent(String event) {

        logs.add(event);

        System.out.println("LOG: " + event);
    }

    // Display all logs
    public void displayLogs() {

        System.out.println("\n===== SECURITY LOGS =====");

        for (String log : logs) {
            System.out.println(log);
        }
    }

    public ArrayList<String> getLogs() {
        return logs;
    }
}
