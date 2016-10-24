package omrkhld.com.koboldfightclub.POJO;

/**
 * Created by Omar on 22/10/2016.
 */

public class Combatant {

    private String name, initMod;
    private int init, hp;

    public Combatant() {
        name = "";
        initMod = "0";
        init = 1;
        hp = 0;
    }

    public String getName() { return name; }
    public String getInitMod() { return initMod; }
    public int getInit() { return init; }
    public int getHP() { return hp; }

    public void setName(String n) { name = n; }
    public void setInitMod(String i) { initMod = i; }
    public void setInit(int i) { init = i; }
    public void setHP(int h) { hp = h; }
}
