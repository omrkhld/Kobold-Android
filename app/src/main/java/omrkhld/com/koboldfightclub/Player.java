package omrkhld.com.koboldfightclub;

import io.realm.RealmObject;

/**
 * Created by Omar on 27/9/2016.
 */

public class Player extends RealmObject {
    private String name, party;
    private int level, easy, med, hard, deadly;
    private int initMod, hp;

    public Player() {
        name = "";
        party = "";
        level = 1;
        easy = 25;
        med = 50;
        hard = 75;
        deadly = 100;
        initMod = 0;
        hp = 1;
    }

    public String getName() { return name; }
    public String getParty() { return party; }
    public int getLevel() { return level; }
    public int getEasy() { return easy; }
    public int getMed() { return med; }
    public int getHard() { return hard; }
    public int getDeadly() { return deadly; }
    public int getInitMod() { return initMod; }
    public int getHP() { return hp; }

    public void setName(String n) { name = n; }
    public void setParty(String p) { party = p; }
    public void setLevel(int l) { level = l; }
    public void setEasy(int e) { easy = e; }
    public void setMed(int m) { med = m; }
    public void setHard(int h) { hard = h; }
    public void setDeadly(int d) { deadly = d; }
    public void setInitMod(int i) { initMod = i; }
    public void setHP(int h) { hp = h; }
}
