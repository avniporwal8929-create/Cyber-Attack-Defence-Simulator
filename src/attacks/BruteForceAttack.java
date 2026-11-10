
package attacks;

public class BruteForceAttack extends Attack {

    public BruteForceAttack(String target) {
        super("Brute Force", target, "HIGH");
    }

    @Override
    public String execute() {
        return "Repeated login attempts simulated on " + getTarget();
    }
}
