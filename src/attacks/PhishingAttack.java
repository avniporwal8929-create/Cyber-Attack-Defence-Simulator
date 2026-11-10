
package attacks;

public class PhishingAttack extends Attack {

    public PhishingAttack(String target) {
        super("Phishing", target, "MEDIUM");
    }

    @Override
    public String execute() {
        return "Phishing simulation detected on " + getTarget();
    }
}
