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
import net.vikev.android.plates.fragments.NewScaleAddFragment;
import net.vikev.android.plates.fragments.SettingsFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AbstractNavDrawerActivity {
    public static String code, format;
    // TO BE DELETED

    public static boolean first = true;

    // DELETING OVER
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
        }

        MyApplication.restartWebServerScaleRetrieverService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            MainActivity.code = scanningResult.getContents();
            MainActivity.format = scanningResult.getFormatName();
            try {
                ((EditText) findViewById(R.id.Barcode)).setText(MainActivity.code);
            } catch (Exception e) {
            }
            try {
                ((EditText) findViewById(R.id.editText_scaleMac)).setText(MainActivity.code);
            } catch (Exception e) {
            }

        } else {
            Toast toast = Toast.makeText(MyApplication.getAppContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] { NavMenuSection.create(100, "Main"),
                NavMenuItem.create(101, "Home", "navdrawer_home", true, this), NavMenuSection.create(200, "Settings"),
                NavMenuItem.create(102, "New Scale", "fragment_scale_add", true, this),
                NavMenuItem.create(201, "Server settings", "navdrawer_settings", true, this) };

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
            case 102:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new NewScaleAddFragment()).commit();
                break;
            case 201:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
                break;
        }
    }
}