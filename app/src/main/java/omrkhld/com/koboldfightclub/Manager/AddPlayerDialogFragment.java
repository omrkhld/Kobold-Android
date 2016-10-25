package omrkhld.com.koboldfightclub.Manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.Helper.XPThresholdsSingleton;
import omrkhld.com.koboldfightclub.POJO.Player;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 27/9/2016.
 */

public class AddPlayerDialogFragment extends DialogFragment {

    public static final String TAG = "AddPlayerDialogFragment";

    @BindView(R.id.name_wrapper) TextInputLayout nameWrapper;
    @BindView(R.id.name_edit) EditText nameEdit;
    @BindView(R.id.level_wrapper) TextInputLayout levelWrapper;
    @BindView(R.id.level_edit) EditText levelEdit;
    @BindView(R.id.init_wrapper) TextInputLayout initWrapper;
    @BindView(R.id.init_edit) EditText initEdit;
    @BindView(R.id.hp_wrapper) TextInputLayout hpWrapper;
    @BindView(R.id.hp_edit) EditText hpEdit;
    @BindView(R.id.create_player) Button createPlayerButton;

    public String name, levelString, hpString, init;
    public int level = 1, hp = 1;
    public static Player p;

    public AddPlayerDialogFragment() {
    }

    public static AddPlayerDialogFragment newInstance(String title, Player pOld) {
        AddPlayerDialogFragment frag = new AddPlayerDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        if (pOld != null) {
            p = pOld;
        } else {
            p = null;
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
        View view = inflater.inflate(R.layout.dialog_add_player, container);
        ButterKnife.bind(this, view);

        createPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    levelString = levelWrapper.getEditText().getText().toString();
                    init = initWrapper.getEditText().getText().toString();
                    hpString = hpWrapper.getEditText().getText().toString();
                    name = nameWrapper.getEditText().getText().toString();

                    if (TextUtils.isEmpty(name)) {
                        nameWrapper.setError("Name is empty!");
                    } else if (TextUtils.isEmpty(levelString)) {
                        levelWrapper.setError("Level is empty!");
                    } else if (TextUtils.isEmpty(init)) {
                        initWrapper.setError("Init Modifier is empty!");
                    } else if (TextUtils.isEmpty(hpString)) {
                        hpWrapper.setError("HP is empty!");
                    } else {
                        level = Integer.parseInt(levelString);
                        hp = Integer.parseInt(hpString);
                        if (!TextUtils.isDigitsOnly(levelString)) {
                            levelWrapper.setError("Not a number!");
                        } else if (level > 20) {
                            levelWrapper.setError("Level too high!");
                        } else if (level < 1) {
                            levelWrapper.setError("Level too low!");
                        } else if (!TextUtils.isDigitsOnly(hpString)) {
                            hpWrapper.setError("Not a number!");
                        } else if (hp < 1) {
                            hpWrapper.setError("HP too low!");
                        } else {
                            Player player = new Player();
                            player.setName(name);
                            player.setInitMod(init);
                            player.setLevel(level);
                            player.setHP(hp);
                            //player.setParty(partySpinner.getSelectedItem().toString());
                            ArrayList<ArrayList<Integer>> thresh = XPThresholdsSingleton.getInstance().getThresholds();
                            ArrayList<Integer> row = thresh.get(level-1);
                            player.setEasy(row.get(0));
                            player.setMed(row.get(1));
                            player.setHard(row.get(2));
                            player.setDeadly(row.get(3));
                            sendBackResult(player, p);
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
        String title = getArguments().getString("title", "Add Player");
        getDialog().setTitle(title);

        if (p != null) {
            nameWrapper.getEditText().setText(p.getName());
            levelWrapper.getEditText().setText(Integer.toString(p.getLevel()));
            hpWrapper.getEditText().setText(Integer.toString(p.getHP()));
            initWrapper.getEditText().setText(p.getInitMod());

            createPlayerButton.setText("Edit");
            getDialog().setTitle("Edit Player");
        } else {
            nameWrapper.getEditText().setText("");
            levelWrapper.getEditText().setText("");
            hpWrapper.getEditText().setText("");
            initWrapper.getEditText().setText("");

            createPlayerButton.setText("Create");
            getDialog().setTitle("New Player");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*SharedPreferences.Editor editor = partyNames.edit();
        editor.putStringSet("names", partiesSet);
        editor.apply();*/
    }

    public interface AddPlayerDialogListener {
        void onFinishAddDialog(Player player, Player oldPlayer);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(Player player, Player oldPlayer) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        AddPlayerDialogListener listener = (AddPlayerDialogListener) getTargetFragment();
        listener.onFinishAddDialog(player, oldPlayer);
        dismiss();
    }
}
