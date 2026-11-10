
package attacks;

public class TrafficOverloadAttack extends Attack {

    public TrafficOverloadAttack(String target) {
        super("Traffic Overload", target, "HIGH");
    }

    @Override
    public String execute() {
        return "Excessive network traffic simulated on " + getTarget();
    }
}
