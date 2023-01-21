package ru.serguun42.android.airportenhanced.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.databinding.OauthFragmentBinding;
import ru.serguun42.android.airportenhanced.presentation.repository.network.oauth.OAuthMethod;

public class OAuthFragment extends Fragment {
    public final static String STARTING_URL_EXTRA_KEY = "oath_fragment_starting_url";
    private OauthFragmentBinding binding;
    private String startingUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startingUrl = getArguments().getString(STARTING_URL_EXTRA_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = OauthFragmentBinding.inflate(inflater, container, false);

        if (startingUrl != null && !startingUrl.isEmpty()) {
            CookieManager.getInstance().removeAllCookies(null);
            binding.oauthWeb.clearCache(false);

            WebSettings settings = binding.oauthWeb.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowContentAccess(true);
            settings.setDomStorageEnabled(true);

            binding.oauthWeb.setWebViewClient(new OAuthMethod().oauthExecute((MainActivity) getActivity()));
            binding.oauthWeb.loadUrl(startingUrl);
        }

        return binding.getRoot();
    }
}
