
package attacks;

public abstract class Attack {

    private String attackName;
    private String target;
    private String severity;

    public Attack(String attackName, String target, String severity) {
        this.attackName = attackName;
        this.target = target;
        this.severity = severity;
    }

    public String getAttackName() {
        return attackName;
    }

    public String getTarget() {
        return target;
    }

    public String getSeverity() {
        return severity;
    }

    public abstract String execute();
}
