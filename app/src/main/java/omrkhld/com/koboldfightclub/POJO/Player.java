package omrkhld.com.koboldfightclub.POJO;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Omar on 27/9/2016.
 */

public class Player extends RealmObject {

    @PrimaryKey @Required
    private String name;

    private String party, initMod;
    private int level, easy, med, hard, deadly;
    private int hp;

    public Player() {
        name = "";
        party = "Party";
        level = 1;
        easy = 25;
        med = 50;
        hard = 75;
        deadly = 100;
        initMod = "0";
        hp = 1;
    }

    public Player(Player p) {
        name = p.getName();
        party = p.getParty();
        level = p.getLevel();
        easy = p.getEasy();
        med = p.getMed();
        hard = p.getHard();
        deadly = p.getDeadly();
        initMod = p.getInitMod();
        hp = p.getHP();
    }

    public String getName() { return name; }
    public String getParty() { return party; }
    public int getLevel() { return level; }
    public int getEasy() { return easy; }
    public int getMed() { return med; }
    public int getHard() { return hard; }
    public int getDeadly() { return deadly; }
    public String getInitMod() { return initMod; }
    public int getHP() { return hp; }

    public void setName(String n) { name = n; }
    public void setParty(String p) { party = p; }
    public void setLevel(int l) { level = l; }
    public void setEasy(int e) { easy = e; }
    public void setMed(int m) { med = m; }
    public void setHard(int h) { hard = h; }
    public void setDeadly(int d) { deadly = d; }
    public void setInitMod(String i) { initMod = i; }
    public void setHP(int h) { hp = h; }
}
