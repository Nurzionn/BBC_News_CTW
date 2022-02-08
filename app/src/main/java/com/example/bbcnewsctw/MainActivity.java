package com.example.bbcnewsctw;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bbcnewsctw.databinding.ActivityMainBinding;
import com.example.bbcnewsctw.interfaces.OnClickLayoutListener;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements OnClickLayoutListener {

    private Bundle savedInstanceState;
    private ActivityMainBinding binding;
    private Executor executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClickBiometric(this);
        setupActionBarTitle(getString(R.string.news_title));
        this.savedInstanceState = savedInstanceState;
        verifyBiometrics();
    }

    /**
     * Used by children fragment to change the Action bar title
     * depending on the News Source that was request
     *
     * @param title - ActionBar Title
     */
    public void setupActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    /**
     * Verify if is biometric is available and config on device...
     * ... If yes, the biometric prompt appears
     * ... If not, app proceeds normally showing the first fragment
     */
    @SuppressLint("WrongConstant")
    private void verifyBiometrics() {
        boolean authenticateNeeded = false;
        executor = ContextCompat.getMainExecutor(this);
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ?
                biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL) :
                biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                authenticateNeeded = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                authenticateNeeded = false;
                break;
        }

        authenticateUser(authenticateNeeded);
    }

    private void authenticateUser(boolean authenticateNeeded) {
        if (authenticateNeeded) {
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    showFirstFragment();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(MainActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    if (errString.equals(getString(R.string.cancel)))
                        errString = getString(R.string.generic_error_user_canceled);
                    Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
                }
            });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.biometric_auth_title))
                    .setSubtitle(getString(R.string.fingerprint_dialog_touch_sensor))
                    .setNegativeButtonText(getString(R.string.cancel))
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else
            showFirstFragment();
    }

    private void showFirstFragment() {
        binding.biometricLinearLayout.setVisibility(View.GONE);
        Utils.setupAPIConnection();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(binding.fragmentContainerView.getId(), ArticlesListFragment.class, null)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onClickLayout(View view) {
        if (view == binding.biometricLinearLayout)
            authenticateUser(true);
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }
}