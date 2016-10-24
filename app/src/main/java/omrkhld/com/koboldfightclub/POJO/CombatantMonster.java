package omrkhld.com.koboldfightclub.POJO;

/**
 * Created by Omar on 24/10/2016.
 */

public class CombatantMonster extends Combatant {
    private int numDice, hitDice, additionalHP, avg;

    public CombatantMonster() {
        name = "";
        hp = 0;
        numDice = 0;
        hitDice = 0;
        additionalHP = 0;
        avg = 0;
        init = 1;
        initMod = "0";
    }

    public int getNumHD() { return numDice; }
    public int getHD() { return hitDice; }
    public int getAdd() { return additionalHP; }
    public int getAvg() { return avg; }

    public void setNumDice(int n) { numDice = n; }
    public void setHitDice(int h) { hitDice = h; }
    public void setAdditionalHP(int a) { additionalHP = a; }
    public void setAvg(int a) { avg = a; }
}
