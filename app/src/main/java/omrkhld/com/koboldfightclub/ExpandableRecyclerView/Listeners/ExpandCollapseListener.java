package omrkhld.com.koboldfightclub.ExpandableRecyclerView.Listeners;

import omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup;

/**
 * Created by Omar on 10/10/2016.
 */

public interface ExpandCollapseListener {

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the {@link ExpandableGroup}
     * @param itemCount the total number of children in the {@link ExpandableGroup}
     */
    void onGroupExpanded(int positionStart, int itemCount);

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the {@link omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup}
     * @param itemCount the total number of children in the {@link omrkhld.com.koboldfightclub.ExpandableRecyclerView.Models.ExpandableGroup}
     */
    void onGroupCollapsed(int positionStart, int itemCount);
}