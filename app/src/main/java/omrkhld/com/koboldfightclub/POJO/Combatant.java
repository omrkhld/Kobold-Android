package omrkhld.com.koboldfightclub.POJO;

/**
 * Created by Omar on 22/10/2016.
 */

public class Combatant {

    public String name, initMod;
    public int init, hp;

    public Combatant() {
        name = "";
        initMod = "0";
        init = 1;
        hp = 0;
    }

    public void setName(String n) { name = n; }
    public void setInitMod(String i) { initMod = i; }
    public void setInit(int i) { init = i; }
    public void setHP(int h) { hp = h; }
}
