
package network;

public class Router extends NetworkDevice {

    public Router(String id, String name) {
        super(id, name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Router: " + getDeviceName());
        System.out.println("Status: " + getStatus());
    }
}

