package com.hanze.recipe.ui.login;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hanze.recipe.MainActivity;
import com.hanze.recipe.R;
import com.hanze.recipe.ServerConnectionPost;
import com.hanze.recipe.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText usernameEditText = inf.findViewById(R.id.usernameField);
        final EditText emailEditText = inf.findViewById(R.id.username);
        final EditText passwordEditText = inf.findViewById(R.id.password);
        final TextView errorMessageTextView = inf.findViewById(R.id.errorMessage);
        final Button registerButton = inf.findViewById(R.id.register);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                checkRegisterEntryData(usernameEditText,emailEditText,passwordEditText,errorMessageTextView,registerButton);
            }   //ignore
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                //ignore
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                checkRegisterEntryData(usernameEditText,emailEditText,passwordEditText,errorMessageTextView,registerButton);
            }   //ignore
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            //ignore
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                checkRegisterEntryData(usernameEditText,emailEditText,passwordEditText,errorMessageTextView,registerButton);
            }   //ignore
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            //ignore
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterData(usernameEditText.getText().toString(),emailEditText.getText().toString(), passwordEditText.getText().toString(), errorMessageTextView);
            }
        });
        return inf;

    }

    void checkRegisterEntryData(EditText usernameEditText, EditText emailEditText, EditText passwordEditText, TextView errorMessageTextView, Button registerButton ){
        boolean usernameFormat = false;
        boolean emailFormat = false;
        boolean passwordFormat = false;
        //username regels
        if(usernameEditText.getText().toString().length() == 0){
            errorMessageTextView.setText("Gebruikersnaam is vereist.");
            errorMessageTextView.setVisibility(View.VISIBLE);
        }else{
            errorMessageTextView.setText("");
            usernameFormat = true;
            errorMessageTextView.setVisibility(View.INVISIBLE);
        }

        //email regels
        if(isEmailValid(emailEditText.getText().toString())==false){
            errorMessageTextView.setText("Geen geldige email meegegeven.");
            errorMessageTextView.setVisibility(View.VISIBLE);
        }else{
            errorMessageTextView.setText("");
            emailFormat = true;
            errorMessageTextView.setVisibility(View.INVISIBLE);
        }

        //wachtwoord regels
        if(passwordEditText.getText().toString().length() <= 5){
            errorMessageTextView.setText("Het wachtwoord moet minstens 6 tekens lang zijn.");
            errorMessageTextView.setVisibility(View.VISIBLE);
        }else{
            errorMessageTextView.setText("");
            passwordFormat = true;
            errorMessageTextView.setVisibility(View.INVISIBLE);
        }

        if(usernameFormat && emailFormat && passwordFormat){
            registerButton.setEnabled(true);
        }else{
            registerButton.setEnabled(false);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    } // end of TextWatcher (email)

    private void sendRegisterData(String username, String email, String password, TextView errorText) {
        try {
            ServerConnectionPost sc = new ServerConnectionPost(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                JSONObject res = sc.fetchRegister(username,email,password,new URL("http://192.168.8.49:3000/register/") );
                Log.d("request", String.valueOf(res));
                if(res == null){
                    Log.d("requestEx","ex");
                    errorText.setText("Gebruikersnaam of email is al in gebruik.");
                    errorText.setVisibility(View.VISIBLE);
                }else{
                    errorText.setVisibility(View.INVISIBLE);
                    Log.d("register" , String.valueOf(res));
                    updateUiWithUser(username);
                    LoginFragment.loggin = true;
                    //register was succes
                    //MenuItem menuItem = inf.findViewById(R.id.usernameField);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
}