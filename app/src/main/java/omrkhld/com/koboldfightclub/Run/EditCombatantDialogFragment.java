package omrkhld.com.koboldfightclub.Run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.POJO.Combatant;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 22/10/2016.
 */

public class EditCombatantDialogFragment extends DialogFragment {

    public static final String TAG = "EditCombatantFragment";
    @BindView(R.id.name_text) TextView nameText;
    @BindView(R.id.init_wrapper) TextInputLayout initWrapper;
    @BindView(R.id.init_edit) EditText initEdit;
    @BindView(R.id.hp_wrapper) TextInputLayout hpWrapper;
    @BindView(R.id.hp_edit) EditText hpEdit;
    @BindView(R.id.confirm_edit) Button confirmButton;

    public String hpString, initString;
    public int hp = 1, init = 1;
    public static Combatant originalC;

    public EditCombatantDialogFragment() {
    }

    public static EditCombatantDialogFragment newInstance(String title, Combatant cOld) {
        EditCombatantDialogFragment frag = new EditCombatantDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        if (cOld != null) {
            originalC = cOld;
        }

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_combatant, container);
        ButterKnife.bind(this, view);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initString = initWrapper.getEditText().getText().toString();
                    if (TextUtils.isEmpty(initString)) {
                        initWrapper.setError("Init Modifier is empty!");
                    } else init = Integer.parseInt(initString);

                    hpString = hpWrapper.getEditText().getText().toString();
                    if (TextUtils.isEmpty(hpString)) {
                        hpWrapper.setError("HP is empty!");
                    } else hp = Integer.parseInt(hpString);

                    if (!TextUtils.isDigitsOnly(hpString)) {
                        hpWrapper.setError("Not a number!");
                    } else if (hp < 1) {
                        hpWrapper.setError("HP too low!");
                    } else if (init < 0) {
                        initWrapper.setError("Initative too low!");
                    } else {
                        Combatant combatant = new Combatant();
                        combatant.setName(originalC.getName());
                        combatant.setInitMod(originalC.getInitMod());
                        combatant.setInit(init);
                        combatant.setHP(hp);
                        sendBackResult(combatant);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Edit Combatant");
        getDialog().setTitle(title);

        nameText.setText(originalC.getName());
        hpWrapper.getEditText().setText(Integer.toString(originalC.getHP()));
        initWrapper.getEditText().setText(Integer.toString(originalC.getInit()));
    }

    public interface EditCombatantDialogListener {
        void onFinishEditing(Combatant combatant);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(Combatant combatant) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditCombatantDialogListener listener = (EditCombatantDialogListener) getActivity();
        listener.onFinishEditing(combatant);
        dismiss();
    }
}
