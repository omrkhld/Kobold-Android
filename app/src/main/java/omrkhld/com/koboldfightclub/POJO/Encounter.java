package omrkhld.com.koboldfightclub.POJO;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Omar on 6/10/2016.
 */

public class Encounter extends RealmObject {

    @PrimaryKey
    private int id;

    private RealmList<Monster> enc;

    public Encounter() {
        id = 0;
        enc = new RealmList<>();
    }

    public int getID() { return id; }
    public RealmList<Monster> getEnc() { return enc; }

    public void setID(int i) { id = i; }
    public void setEnc(RealmList<Monster> e) { enc.addAll(e); }
}
