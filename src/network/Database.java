
package network;

public class Database extends NetworkDevice {

    public Database(String id, String name) {
        super(id, name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Database: " + getDeviceName());
        System.out.println("Status: " + getStatus());
    }
}
