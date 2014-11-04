package net.vikev.android.plates.activities;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.drawer.AbstractNavDrawerActivity;
import net.vikev.android.plates.drawer.NavDrawerActivityConfiguration;
import net.vikev.android.plates.drawer.NavDrawerAdapter;
import net.vikev.android.plates.drawer.NavDrawerItem;
import net.vikev.android.plates.drawer.NavMenuItem;
import net.vikev.android.plates.drawer.NavMenuSection;
import net.vikev.android.plates.fragments.IntentIntegrator;
import net.vikev.android.plates.fragments.IntentResult;
import net.vikev.android.plates.fragments.MainFragment;
import net.vikev.android.plates.fragments.SettingsFragment;
import net.vikev.android.plates.fragments.TestFragment;
import net.vikev.android.plates.fragments.ScanningFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AbstractNavDrawerActivity {
	public static String code,format;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		 IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		 if (scanningResult != null) {
			 code = scanningResult.getContents();
			 format = scanningResult.getFormatName();
			 
			}
		 else{
			 Toast toast = Toast.makeText(MyApplication.getAppContext(), "No scan data received!", Toast.LENGTH_SHORT);
		        toast.show();
			}
		}
    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] { 
                NavMenuSection.create(100, "Main"),
                NavMenuItem.create(101, "Home", "navdrawer_home", true, this),
                NavMenuSection.create(200, "Settings"),
                NavMenuItem.create(201, "Server settings", "navdrawer_settings", true, this),
                NavMenuSection.create(300, "QuickTest(to be removed)"),
                NavMenuItem.create(301, "Test", "navdrawer_settings", true, this) ,
        		NavMenuSection.create(400, "Scanner"),
        		NavMenuItem.create(401, "Scanner", "reader_fragment", true, this) };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(new NavDrawerAdapter(this, R.layout.navdrawer_item, menu));
        
        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch ((int) id) {
        case 101:
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
            break;
        case 201:
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
            break;
        case 301:
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TestFragment()).commit();
            break;
    	case 401:
    		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ScanningFragment()).commit();
        break;
    	}
    }
}