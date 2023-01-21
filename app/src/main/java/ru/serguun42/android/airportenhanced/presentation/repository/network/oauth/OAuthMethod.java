package ru.serguun42.android.airportenhanced.presentation.repository.network.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.navigation.Navigation;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.presentation.view.OAuthFragment;

public class OAuthMethod {
    public final static String STARTING_PAGE = "https://airport.serguun42.ru/auth";
    public final static String CATCHING_PAGE = "https://airport.serguun42.ru/auth/success";

    public static void startAuthentication(MainActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putString(OAuthFragment.STARTING_URL_EXTRA_KEY, STARTING_PAGE);
        Navigation.findNavController(activity.binding.navHostFragment).navigate(R.id.action_flightsList_to_oauth, bundle);
    }

    public WebViewClient oauthExecute(MainActivity mainActivity) {
        SharedPreferences sharedPref = mainActivity.getSharedPreferences(
                mainActivity.getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);

        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().contains(CATCHING_PAGE)) {
                    String token = Uri.parse(request.getUrl().toString()).getQueryParameter("token");

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(mainActivity.getString(R.string.credentials_token_key), token);
                    editor.apply();

                    Navigation.findNavController(mainActivity.binding.navHostFragment).popBackStack();

                    return false;
                }
                view.loadUrl(request.getUrl().toString());

                return true;
            }
        };
    }
}
