package me.loc2.loc2me.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import me.loc2.loc2me.Injector;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public class Loc2meFragmentActivity extends ActionBarActivity {

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        Views.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
}
