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

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.POJO.CombatantPlayer;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 24/10/2016.
 */

public class EditPlayerDialogFragment extends DialogFragment {

    public static final String TAG = "EditPlayerFragment";
    @BindView(R.id.name_text) TextView nameText;
    @BindView(R.id.init_wrapper) TextInputLayout initWrapper;
    @BindView(R.id.init_edit) EditText initEdit;
    @BindView(R.id.hp_wrapper) TextInputLayout hpWrapper;
    @BindView(R.id.hp_edit) EditText hpEdit;
    @BindView(R.id.roll_init_button) Button rollInitButton;
    @BindView(R.id.confirm_edit) Button confirmButton;

    public String hpString, initString;
    public int hp = 1, init = 1;
    public static CombatantPlayer originalC;

    public EditPlayerDialogFragment() {
    }

    public static EditPlayerDialogFragment newInstance(String title, CombatantPlayer cOld) {
        EditPlayerDialogFragment frag = new EditPlayerDialogFragment();
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
        View view = inflater.inflate(R.layout.dialog_edit_player, container);
        ButterKnife.bind(this, view);

        rollInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int i = random.nextInt(20) + 1;
                i += Integer.valueOf(originalC.initMod);
                init = i;
                initWrapper.getEditText().setText(Integer.toString(init));
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
                        if (hp < 1) {
                            hpWrapper.setError("HP too low!");
                        } else if (init < 0) {
                            initWrapper.setError("Initative too low!");
                        } else {
                            CombatantPlayer player = new CombatantPlayer();
                            player.setName(originalC.name);
                            player.setInitMod(originalC.initMod);
                            player.setInit(init);
                            player.setHP(hp);
                            sendBackResult(player);
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
        String title = getArguments().getString("title", "Edit Player");
        getDialog().setTitle(title);

        nameText.setText(originalC.name);
        hpWrapper.getEditText().setText(Integer.toString(originalC.hp));
        initWrapper.getEditText().setText(Integer.toString(originalC.init));
    }

    public interface EditPlayerDialogListener {
        void onFinishEditingPlayer(CombatantPlayer player);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(CombatantPlayer player) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditPlayerDialogFragment.EditPlayerDialogListener listener = (EditPlayerDialogFragment.EditPlayerDialogListener) getActivity();
        listener.onFinishEditingPlayer(player);
        dismiss();
    }
}
