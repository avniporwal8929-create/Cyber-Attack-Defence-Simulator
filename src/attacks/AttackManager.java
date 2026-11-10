
package attacks;

import java.util.ArrayList;

public class AttackManager {

    private ArrayList<Attack> attacks;

    public AttackManager() {
        attacks = new ArrayList<>();
    }

    // Add attack
    public void addAttack(Attack attack) {
        attacks.add(attack);
        System.out.println(attack.getAttackName() + " added!");
    }

    // Execute all simulated attacks
    public void executeAllAttacks() {

        System.out.println("\n===== ATTACK SIMULATION =====");

        for (Attack attack : attacks) {

            System.out.println("Attack: " + attack.getAttackName());
            System.out.println("Target: " + attack.getTarget());
            System.out.println("Severity: " + attack.getSeverity());

            System.out.println("Result: " + attack.execute());

            System.out.println("-------------------------");
        }
    }

    // Get attack list
    public ArrayList<Attack> getAttacks() {
        return attacks;
    }
}