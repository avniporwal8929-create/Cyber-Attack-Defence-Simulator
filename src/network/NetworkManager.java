
package network;

import java.util.ArrayList;

public class NetworkManager {

    private ArrayList<NetworkDevice> devices;

    public NetworkManager() {
        devices = new ArrayList<>();
    }

    // Add a new device
    public void addDevice(NetworkDevice device) {
        devices.add(device);
        System.out.println(device.getDeviceName() + " added successfully!");
    }

    // Display all devices
    public void displayAllDevices() {

        System.out.println("\n===== NETWORK DEVICES =====");

        for (NetworkDevice device : devices) {
            device.displayInfo();
            System.out.println("-----------------------");
        }
    }

    // Get all devices
    public ArrayList<NetworkDevice> getDevices() {
        return devices;
    }

    // Find device using ID
    public NetworkDevice findDevice(String id) {

        for (NetworkDevice device : devices) {
            if (device.getDeviceId().equals(id)) {
                return device;
            }
        }

        return null;
    }
}

