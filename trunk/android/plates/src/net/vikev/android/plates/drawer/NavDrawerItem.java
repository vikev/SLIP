package net.vikev.android.plates.drawer;

public interface NavDrawerItem {
	int getId();
    String getLabel();
    int getType();
    
    /**
     * Is clickable.
     * @return
     */
    boolean isEnabled();
    boolean updateActionBarTitle();
}
