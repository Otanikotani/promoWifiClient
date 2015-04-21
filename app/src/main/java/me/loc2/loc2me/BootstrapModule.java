package me.loc2.loc2me;

import android.accounts.AccountManager;
import android.content.Context;

import me.loc2.loc2me.authenticator.ApiKeyProvider;
import me.loc2.loc2me.authenticator.BootstrapAuthenticatorActivity;
import me.loc2.loc2me.authenticator.LogoutService;
import me.loc2.loc2me.core.BootstrapService;
import me.loc2.loc2me.core.OfferLoaderService;
import me.loc2.loc2me.core.OfferStorageService;
import me.loc2.loc2me.core.PostFromAnyThreadBus;
import me.loc2.loc2me.core.RestAdapterRequestInterceptor;
import me.loc2.loc2me.core.RestErrorHandler;
import me.loc2.loc2me.core.WifiScanService;
import me.loc2.loc2me.core.TimerService;
import me.loc2.loc2me.core.UserAgentProvider;
import me.loc2.loc2me.ui.BootstrapTimerActivity;
import me.loc2.loc2me.ui.FilterFragment;
import me.loc2.loc2me.ui.MainActivity;
import me.loc2.loc2me.ui.NavigationDrawerFragment;
import me.loc2.loc2me.ui.OfferActivity;
import me.loc2.loc2me.ui.OfferListFragment;
import me.loc2.loc2me.ui.UserActivity;
import me.loc2.loc2me.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                NavigationDrawerFragment.class,
                OfferActivity.class,
                OfferListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                FilterFragment.class,
                TimerService.class,
                WifiScanService.class,
                OfferStorageService.class,
                OfferLoaderService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
//                .setEndpoint(Constants.Http.URL_BASE)
                .setEndpoint("http://promowifi.herokuapp.com")
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

//    @Provides
//    WifiReceiver provideWifiReceiver(){
//        return new WifiReceiver();
//    }
}