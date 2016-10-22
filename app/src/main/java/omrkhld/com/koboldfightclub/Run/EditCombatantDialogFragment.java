package omrkhld.com.koboldfightclub.Run;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import omrkhld.com.koboldfightclub.POJO.Combatant;

/**
 * Created by Omar on 22/10/2016.
 */

public class EditCombatantDialogFragment extends DialogFragment {

    public static final String TAG = "EditCombatantFragment";

    public static Combatant c;

    public EditCombatantDialogFragment() {
    }

    public static EditCombatantDialogFragment newInstance(String title, Combatant cOld) {
        EditCombatantDialogFragment frag = new EditCombatantDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        if (cOld != null) {
            c = cOld;
        } else {
            c = null;
        }
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
    }
}
