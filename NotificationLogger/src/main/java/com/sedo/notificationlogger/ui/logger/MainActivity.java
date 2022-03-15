package com.sedo.notificationlogger.ui.logger;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sedo.notificationlogger.R;
import com.sedo.notificationlogger.data.models.LoggerModel;

public class MainActivity extends BaseActivity implements TransactionListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_logger_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TransactionListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(LoggerModel transaction) {
        TransactionActivity.start(this, transaction.getId());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}
