package omrkhld.com.koboldfightclub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MonsterListFragment extends Fragment {

    public static final String TAG = "MonsterListFragment";
    private OnListFragmentInteractionListener mListener;
    private Realm realm;
    public RealmResults<Monster> results;
    public String[] conditions = {"", "", "", "0", "30"};

    @BindView(R.id.list_view) RecyclerView list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MonsterListFragment() {
    }

    @SuppressWarnings("unused")
    public static MonsterListFragment newInstance() {
        MonsterListFragment fragment = new MonsterListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monster_list, container, false);
        ButterKnife.bind(this, view);
        list.addItemDecoration(new DividerItemDecoration(getActivity()));

        realm = Realm.getDefaultInstance();
        try {
            loadJsonFromStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RealmQuery<Monster> query = realm.where(Monster.class);
        results = query.findAllAsync();

        // Set the adapter
        list.setAdapter(new MonsterRecyclerViewAdapter(getActivity(), results));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmQuery<Monster> query = realm.where(Monster.class);
        results = query.findAllAsync();
        list.setAdapter(new MonsterRecyclerViewAdapter(getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_monsterlist, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RealmQuery<Monster> query = realm.where(Monster.class);
                try {
                    query.equalTo("cr", Float.parseFloat(newText));
                } catch(NumberFormatException e) {
                    query.contains("name", newText, Case.INSENSITIVE);
                    query.or().contains("size", newText, Case.INSENSITIVE);
                    query.or().contains("type", newText, Case.INSENSITIVE);
                    query.or().contains("tag", newText, Case.INSENSITIVE);
                }

                updateRecyclerView(query);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.info:
                showInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadJsonFromStream() throws IOException {
        realm.beginTransaction();
        InputStream stream = getActivity().getAssets().open("Monsters.json");
        try {
            realm.createAllFromJson(Monster.class, stream);
            realm.commitTransaction();
        } catch (IOException e) {
            realm.cancelTransaction();
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private RealmResults<Monster> buildQuery(RealmQuery<Monster> query) {
        query.contains("size", conditions[0], Case.INSENSITIVE)
                .contains("type", conditions[1], Case.INSENSITIVE)
                .contains("alignment", conditions[2], Case.INSENSITIVE)
                .greaterThanOrEqualTo("cr", Float.parseFloat(conditions[3]))
                .lessThanOrEqualTo("cr", Float.parseFloat(conditions[4]));

        return query.findAllAsync();
    }

    private void updateRecyclerView(RealmQuery<Monster> query) {
        results = buildQuery(query);
        list.setAdapter(new MonsterRecyclerViewAdapter(getActivity(), results));
        list.getAdapter().notifyDataSetChanged();
    }

    private void showInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.newInstance("Group Info");
        infoDialogFragment.show(fm, "fragment_dialog_info");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Monster item);
    }
}
