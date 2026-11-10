
package network;

public class Server extends NetworkDevice {

    public Server(String id, String name) {
        super(id, name);
    }

    @Override
    public void displayInfo() {
        System.out.println("Server: " + getDeviceName());
        System.out.println("Status: " + getStatus());
    }
}

