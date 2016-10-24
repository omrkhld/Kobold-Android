package omrkhld.com.koboldfightclub.Run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.POJO.CombatantMonster;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 24/10/2016.
 */

public class EditMonsterDialogFragment extends DialogFragment {

    public static final String TAG = "EditMonsterFragment";
    @BindView(R.id.name_text) TextView nameText;
    @BindView(R.id.init_wrapper) TextInputLayout initWrapper;
    @BindView(R.id.init_edit) EditText initEdit;
    @BindView(R.id.hp_wrapper) TextInputLayout hpWrapper;
    @BindView(R.id.hp_edit) EditText hpEdit;
    @BindView(R.id.num) TextView numHitDice;
    @BindView(R.id.hd) TextView hitDice;
    @BindView(R.id.add) TextView additionalHP;
    @BindView(R.id.avg) TextView avgHP;
    @BindView(R.id.roll_init_button) Button rollInitButton;
    @BindView(R.id.roll_hp_button) Button rollHPButton;
    @BindView(R.id.confirm_edit) Button confirmButton;

    public String hpString, initString;
    public int hp = 1, init = 1, numHD = 1, hd = 4, add = 0;
    public static CombatantMonster originalC;

    public EditMonsterDialogFragment() {
    }

    public static EditMonsterDialogFragment newInstance(String title, CombatantMonster cOld) {
        EditMonsterDialogFragment frag = new EditMonsterDialogFragment();
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
        View view = inflater.inflate(R.layout.dialog_edit_monster, container);
        ButterKnife.bind(this, view);

        numHD = originalC.getNumHD();
        hd = originalC.getHD();
        add = originalC.getAdd();
        numHitDice.setText(Integer.toString(numHD));
        hitDice.setText(Integer.toString(hd));
        additionalHP.setText(Integer.toString(add));
        avgHP.setText(Integer.toString(originalC.getAvg()));

        rollInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int i = random.nextInt(20) + 1;
                i += Integer.valueOf(originalC.initMod);
                initWrapper.getEditText().setText(Integer.toString(init));
            }
        });

        rollHPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int roll = 0;
                for (int i = 0; i < numHD; i++) {
                    roll += random.nextInt(hd) + 1;
                    Log.e(TAG, "Roll: " + roll);
                }
                roll += add;
                hpWrapper.getEditText().setText(Integer.toString(roll));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initString = initWrapper.getEditText().getText().toString();
                    hpString = hpWrapper.getEditText().getText().toString();

                    if (TextUtils.isEmpty(hpString)) {
                        hpWrapper.setError("HP is empty!");
                    } else if (TextUtils.isEmpty(initString)) {
                        initWrapper.setError("Initiative is empty!");
                    } else if (!TextUtils.isDigitsOnly(hpString)) {
                        hpWrapper.setError("Not a number!");
                    } else if (!TextUtils.isDigitsOnly(initString)) {
                        initWrapper.setError("Not a number!");
                    } else {
                        init = Integer.parseInt(initString);
                        hp = Integer.parseInt(hpString);
                        if (hp < 0) {
                            hpWrapper.setError("HP too low!");
                        } else if (init < 0) {
                            initWrapper.setError("Initative too low!");
                        } else {
                            CombatantMonster monster = new CombatantMonster();
                            monster.setName(originalC.name);
                            monster.setInitMod(originalC.initMod);
                            monster.setInit(init);
                            monster.setHP(hp);
                            monster.setNumDice(numHD);
                            monster.setHitDice(hd);
                            monster.setAdditionalHP(add);
                            monster.setAvg(originalC.getAvg());
                            sendBackResult(monster);
                        }
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

        nameText.setText(originalC.name);
        hpWrapper.getEditText().setText(Integer.toString(originalC.hp));
        initWrapper.getEditText().setText(Integer.toString(originalC.init));
    }

    public interface EditMonsterDialogListener {
        void onFinishEditingMonster(CombatantMonster monster);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(CombatantMonster monster) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditMonsterDialogFragment.EditMonsterDialogListener listener = (EditMonsterDialogFragment.EditMonsterDialogListener) getActivity();
        listener.onFinishEditingMonster(monster);
        dismiss();
    }
}
