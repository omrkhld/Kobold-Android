package omrkhld.com.koboldfightclub.MonsterList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrkhld.com.koboldfightclub.R;

/**
 * Created by Omar on 22/10/2016.
 */

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialogFragment";

    @BindView(R.id.size_spinner) Spinner sizeSpinner;
    @BindView(R.id.type_spinner) Spinner typeSpinner;
    @BindView(R.id.alignment_spinner) Spinner alignmentSpinner;
    @BindView(R.id.mincr_spinner) Spinner minSpinner;
    @BindView(R.id.maxcr_spinner) Spinner maxSpinner;
    @BindView(R.id.filter_button) Button filterButton;
    @BindView(R.id.clear_button) Button clearButton;

    public SharedPreferences filters;
    public int sizePos, typePos, alignmentPos, minPos, maxPos;

    public FilterDialogFragment() {
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        filters = getActivity().getSharedPreferences(getString(R.string.pref_filters), 0);
        sizePos = filters.getInt("sizePos", 0);
        typePos = filters.getInt("typePos", 0);
        alignmentPos = filters.getInt("alignmentPos", 0);
        minPos = filters.getInt("minPos", 0);
        maxPos = filters.getInt("maxPos", 33);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container);
        ButterKnife.bind(this, view);

        sizeSpinner.setSelection(sizePos);
        typeSpinner.setSelection(typePos);
        alignmentSpinner.setSelection(alignmentPos);
        minSpinner.setSelection(minPos);
        maxSpinner.setSelection(maxPos);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeSpinner.setSelection(0);
                typeSpinner.setSelection(0);
                alignmentSpinner.setSelection(0);
                minSpinner.setSelection(0);
                maxSpinner.setSelection(33);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = filters.edit();
                editor.putInt("sizePos", sizeSpinner.getSelectedItemPosition());
                editor.putInt("typePos", typeSpinner.getSelectedItemPosition());
                editor.putInt("alignmentPos", alignmentSpinner.getSelectedItemPosition());
                editor.putInt("minPos", minSpinner.getSelectedItemPosition());
                editor.putInt("maxPos", maxSpinner.getSelectedItemPosition());
                editor.apply();
                String selectedAlignment = alignmentSpinner.getSelectedItem().toString();
                if (selectedAlignment.equals("Any Chaotic")) {
                    selectedAlignment = "C";
                } else if (selectedAlignment.equals("Any Evil")) {
                    selectedAlignment = "E";
                } else if (selectedAlignment.equals("Any Good")) {
                    selectedAlignment = "G";
                } else if (selectedAlignment.equals("Any Lawful")) {
                    selectedAlignment = "L";
                } else if (selectedAlignment.equals("Any Neutral")) {
                    selectedAlignment = "N";
                } else if (selectedAlignment.equals("Chaotic Evil")) {
                    selectedAlignment = "CE";
                } else if (selectedAlignment.equals("Chaotic Good")) {
                    selectedAlignment = "CG";
                } else if (selectedAlignment.equals("Chaotic Neutral")) {
                    selectedAlignment = "CN";
                } else if (selectedAlignment.equals("Lawful Evil")) {
                    selectedAlignment = "LE";
                } else if (selectedAlignment.equals("Lawful Good")) {
                    selectedAlignment = "LG";
                } else if (selectedAlignment.equals("Lawful Neutral")) {
                    selectedAlignment = "LN";
                } else if (selectedAlignment.equals("Neutral Evil")) {
                    selectedAlignment = "NE";
                } else if (selectedAlignment.equals("Neutral Good")) {
                    selectedAlignment = "NG";
                } else if (selectedAlignment.equals("Neutral")) {
                    selectedAlignment = "*N";
                }  else if (selectedAlignment.equals("Non-Chaotic")) {
                    selectedAlignment = "!C";
                } else if (selectedAlignment.equals("Non-Evil")) {
                    selectedAlignment = "!E";
                } else if (selectedAlignment.equals("Non-Good")) {
                    selectedAlignment = "!G";
                } else if (selectedAlignment.equals("Non-Lawful")) {
                    selectedAlignment = "!L";
                } else if (selectedAlignment.equals("Unaligned")) {
                    selectedAlignment = "U";
                }

                sendBackFilters(sizeSpinner.getSelectedItem().toString(),
                        typeSpinner.getSelectedItem().toString(),
                        selectedAlignment,
                        minSpinner.getSelectedItem().toString(),
                        maxSpinner.getSelectedItem().toString());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Filter");
    }

    public interface FilterDialogListener {
        void onFiltered(String size, String type, String alignment, String min, String max);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackFilters(String size, String type, String alignment, String min, String max) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        FilterDialogListener listener = (FilterDialogListener) getTargetFragment();
        listener.onFiltered(size, type, alignment, min, max);
        dismiss();
    }
}
