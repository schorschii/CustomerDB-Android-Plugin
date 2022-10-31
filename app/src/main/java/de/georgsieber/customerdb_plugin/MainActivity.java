package de.georgsieber.customerdb_plugin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String PACKAGE_NAME_CUSTOMER_DATABASE = "de.georgsieber.customerdb";

    private final static int PERMISSION_REQUEST = 5;
    private final static int BATTERY_REQUEST = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            ((TextView) findViewById(R.id.textViewVersion)).setText( pInfo.versionName );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_done:
                Intent intent = getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME_CUSTOMER_DATABASE);
                if(intent == null) {
                    // open play store
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME_CUSTOMER_DATABASE)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGE_NAME_CUSTOMER_DATABASE)));
                    }
                } else {
                    // start customer db
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshView() {
        if(arePhonePermissionsGranted()) {
            ((Button) findViewById(R.id.buttonGrantPermissions)).setText(getResources().getString(R.string.phone_call_log_already_granted));
            findViewById(R.id.buttonGrantPermissions).setEnabled(false);
        } else {
            findViewById(R.id.buttonGrantPermissions).setEnabled(true);
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            findViewById(R.id.buttonDisableBatteryOptimization).setVisibility(View.GONE);
        } else {
            if(areBatteryPermissionsGranted()) {
                ((Button) findViewById(R.id.buttonDisableBatteryOptimization)).setText(getResources().getString(R.string.battery_already_granted));
                findViewById(R.id.buttonDisableBatteryOptimization).setEnabled(false);
            } else {
                findViewById(R.id.buttonDisableBatteryOptimization).setEnabled(true);
            }
        }

        if(areBatteryPermissionsGranted() && arePhonePermissionsGranted()) {
            findViewById(R.id.textViewGrantPermissionsText).setVisibility(View.GONE);
        } else {
            findViewById(R.id.textViewGrantPermissionsText).setVisibility(View.VISIBLE);
        }
    }
    private boolean arePhonePermissionsGranted() {
        boolean permissionsAlreadyGranted = true;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            permissionsAlreadyGranted = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
                permissionsAlreadyGranted = false;
        }
        return permissionsAlreadyGranted;
    }
    private boolean areBatteryPermissionsGranted() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(getPackageName());
        }
    }

    public void askPermissions(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CALL_LOG
                    }, PERMISSION_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    }, PERMISSION_REQUEST);
        }
    }
    public void askBatteryOptimizations(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if(!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent, BATTERY_REQUEST);
            }
        }
    }

    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };
    public void askVendorOptimizations(View v) {
        for(Intent intent : POWERMANAGER_INTENTS) {
            if(getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    startActivityForResult(intent, BATTERY_REQUEST);
                    return;
                } catch(Exception ignored) {}
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_need_for_action))
                .setMessage(getString(R.string.no_vendor_specific_tool_found))
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        findViewById(R.id.buttonDisableVendorOptimization).setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        refreshView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshView();
    }
}
