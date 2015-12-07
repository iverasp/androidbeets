package no.iegget.androidbeets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import no.iegget.androidbeets.R;
import no.iegget.androidbeets.utils.Global;


public class PreferencesFragment extends PreferenceFragment {

    private ListPreference mOfflineQualityPreference;
    private ListPreference mStreamingQualityPreference;
    private EditTextPreference mAddressPreference;
    private EditTextPreference mPortPreference;
    private EditTextPreference mUsernamePreference;
    private EditTextPreference mPasswordPreference;
    private SwitchPreference mCellularPreference;


    public static PreferencesFragment newInstance() {
        PreferencesFragment fragment = new PreferencesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(Global.PREFERENCES_KEY, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        mOfflineQualityPreference = (ListPreference) getPreferenceManager().findPreference("offline_quality_key");
        mStreamingQualityPreference = (ListPreference) getPreferenceManager().findPreference("streaming_quality_key");
        mAddressPreference = (EditTextPreference) getPreferenceManager().findPreference("server_address_key");
        mPortPreference = (EditTextPreference) getPreferenceManager().findPreference("server_port_key");
        mUsernamePreference = (EditTextPreference) getPreferenceManager().findPreference("server_username_key");
        mPasswordPreference = (EditTextPreference) getPreferenceManager().findPreference("server_password_key");
        mCellularPreference = (SwitchPreference) getPreferenceManager().findPreference("offline_cellular_key");

        mOfflineQualityPreference.setSummary(sharedPref.getString(Global.OFFLINE_QUALITY, ""));
        mStreamingQualityPreference.setSummary(sharedPref.getString(Global.STREAMING_QUALITY, ""));
        mAddressPreference.setSummary(sharedPref.getString(Global.SERVER_URL, ""));
        mPortPreference.setSummary(sharedPref.getString(Global.SERVER_PORT, ""));
        mUsernamePreference.setSummary(sharedPref.getString(Global.SERVER_USERNAME, ""));

        mPasswordPreference.setSummary(
                (sharedPref.getString(Global.SERVER_PASSWORD, "").isEmpty()) ? "Not set" : "Set");

        mCellularPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putBoolean(Global.DOWNLOAD_CELLULAR, (boolean) newValue);
                editor.commit();
                return false;
            }
        });

        mAddressPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.SERVER_URL, (String) newValue);
                editor.commit();
                preference.setSummary((String) newValue);
                return false;
            }
        });

        mPortPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.SERVER_PORT, (String) newValue);
                editor.commit();
                preference.setSummary((String) newValue);
                return false;
            }
        });

        mUsernamePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.SERVER_USERNAME, (String) newValue);
                editor.commit();
                preference.setSummary((String) newValue);
                return false;
            }
        });

        mPasswordPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.SERVER_PASSWORD, (String) newValue);
                editor.commit();
                preference.setSummary(((String) newValue).isEmpty() ? "Not set" : "Set");
                return false;
            }
        });

        mOfflineQualityPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.OFFLINE_QUALITY, (String) newValue);
                editor.commit();
                preference.setSummary((String) newValue);
                return false;
            }
        });

        mStreamingQualityPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(Global.STREAMING_QUALITY, (String) newValue);
                editor.commit();
                preference.setSummary((String) newValue);
                return false;
            }
        });
    }

}
