package omrkhld.com.koboldfightclub.POJO;

import io.realm.RealmObject;

/**
 * Created by Omar on 1/8/2016.
 */
public class Monster extends RealmObject {
    private String name;
    private float cr;
    private String size, type, tag, alignment, init, src;
    private int ac, hp, num, hd, add, exp;

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
        init = "0";
        exp = 0;
        src = "";
    }

    public Monster(Monster m) {
        name = m.getName();
        cr = m.getCR();
        size = m.getSize();
        type = m.getType();
        tag = m.getTag();
        alignment = m.getAlignment();
        ac = m.getAC();
        hp = m.getHP();
        num = m.getNumHD();
        hd = m.getHD();
        add = m.getAdd();
        init = m.getInit();
        exp = m.getExp();
        src = m.getSource();
    }

    public String getName() { return name; }
    public String getSize() { return size; }
    public String getType() { return type; }
    public String getTag() { return tag; }
    public String getAlignment() { return alignment; }
    public String getInit() { return init; }
    public String getSource() { return src; }
    public float getCR() { return cr; }
    public int getAC() { return ac; }
    public int getHP() { return hp; }
    public int getNumHD() { return num; }
    public int getHD() { return hd; }
    public int getAdd() { return add; }
    public int getExp() { return exp; }

    public void setName(String n) { name = n; }
    public void setSize(String s) { size = s; }
    public void setType(String t) { type = t; }
    public void setTag(String t) { tag = t; }
    public void setAlignment(String a) { alignment = a; }
    public void setInit(String i) { init = i; }
    public void setSource(String s) { src = s; }
    public void setCR(float c) { cr = c; }
    public void setAC(int a) { ac = a; }
    public void setHP(int h) { hp = h; }
    public void setNumHD(int n) { num = n; }
    public void setHD(int h) { hd = h; }
    public void setAdd(int a) { add = a; }
    public void setExp(int e) { exp = e; }
}
