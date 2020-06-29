package com.hanze.recipe.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnection;
import com.hanze.recipe.ServerConnectionPost;
import com.hanze.recipe.fragments.HomeFragment;
import com.hanze.recipe.fragments.RecipeFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    public static boolean loggin = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginStatusChanged(loggin);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        final TextView errorMessageTextView = view.findViewById(R.id.errorMessage);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(true);
                if (loginFormState.getUsernameError() != null) {
                    //usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                Log.d("Login data", String.valueOf(loginResult));
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    //updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                sendLoginData(usernameEditText.getText().toString(), passwordEditText.getText().toString(), errorMessageTextView);
            }
        });
    }

    private void sendLoginData(String username, String password, TextView errorText) {
        try {
            ServerConnectionPost sc = new ServerConnectionPost(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                JSONObject res = sc.fetchLogin(username,password,new URL("http://192.168.8.49:3000/login/") );
                if(res == null){
                    errorText.setVisibility(View.VISIBLE);
                }else{
                    errorText.setVisibility(View.INVISIBLE);
                    updateUiWithUser(username);
                    loggin = true;
                    loginStatusChanged(loggin);
                    //Button button =view.findViewById(R.id.login);
                    //button.setText("Log out");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loginStatusChanged(boolean loggedin){
        MainActivity mainActivity = MainActivity.getInstance();
        Menu menu = mainActivity.nv.getMenu();
        MenuItem menuItemRegister = menu.findItem(R.id.registerMenu);
        MenuItem menuItemLogin = menu.findItem(R.id.loginMenu);
        menuItemRegister.setVisible(!loggedin);
        if(loggedin) {
            menuItemLogin.setTitle(R.string.logout);
        }else
            menuItemLogin.setTitle(R.string.login);

    }

    private void updateUiWithUser(String username) {
        String welcome = getString(R.string.welcome) + " " + username;
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
        Fragment homeFragment = new HomeFragment();
        MainActivity.getInstance().changeFragment(homeFragment);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
}