
package network;

public abstract class NetworkDevice {

    private String deviceId;
    private String deviceName;
    private String status;

    public NetworkDevice(String id, String name) {
        this.deviceId = id;
        this.deviceName = name;
        this.status = "ACTIVE";
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract void displayInfo();
}

