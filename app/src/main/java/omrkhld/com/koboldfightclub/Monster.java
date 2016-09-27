package omrkhld.com.koboldfightclub;

import io.realm.RealmObject;

/**
 * Created by Omar on 1/8/2016.
 */
public class Monster extends RealmObject {
    private String name;
    private float cr;
    private String size, type, tag, alignment;
    private int ac, hp, num, hd, add, init, exp;

    public Monster() {
        name = "";
        cr = 0;
        size = "Medium";
        type = "Humanoid";
        tag = "";
        alignment = "U";
        ac = 10;
        hp = 0;
        num = 0;
        hd = 0;
        add = 0;
        init = 0;
        exp = 0;
    }

    public String getName() { return name; }
    public String getSize() { return size; }
    public String getType() { return type; }
    public String getTag() { return tag; }
    public String getAlignment() { return alignment; }
    public float getCR() { return cr; }
    public int getAC() { return ac; }
    public int getHP() { return hp; }
    public int getNumHD() { return num; }
    public int getHD() { return hd; }
    public int getAdd() { return add; }
    public int getInit() { return init; }
    public int getExp() { return exp; }

    public void setName(String n) { name = n; }
    public void setSize(String s) { size = s; }
    public void setType(String t) { type = t; }
    public void setTag(String t) { tag = t; }
    public void setAlignment(String a) { alignment = a; }
    public void setCR(float c) { cr = c; }
    public void setAC(int a) { ac = a; }
    public void setHP(int h) { hp = h; }
    public void setNumHD(int n) { num = n; }
    public void setHD(int h) { hd = h; }
    public void setAdd(int a) { add = a; }
    public void setInit(int i) { init = i; }
    public void setExp(int e) { exp = e; }
}
