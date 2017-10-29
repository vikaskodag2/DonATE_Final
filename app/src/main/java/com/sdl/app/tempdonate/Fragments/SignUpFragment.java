package com.sdl.app.tempdonate.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Activities.LoginActivity;
import com.sdl.app.tempdonate.Retrofit.MSG;
import com.sdl.app.tempdonate.Retrofit.User;
import com.sdl.app.tempdonate.R;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdl.app.tempdonate.R.id.btn_signup;
import static com.sdl.app.tempdonate.R.id.link_login;
import static com.sdl.app.tempdonate.R.id.signUpProgress;
import static com.sdl.app.tempdonate.R.id.signUp_city;
import static com.sdl.app.tempdonate.R.id.signUp_email;
import static com.sdl.app.tempdonate.R.id.signUp_mob;
import static com.sdl.app.tempdonate.R.id.signUp_name;
import static com.sdl.app.tempdonate.R.id.signUp_password;
import static com.sdl.app.tempdonate.R.id.signUp_reEnterPassword;

public class SignUpFragment extends Fragment{

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText reEnterPasswordText;
    private EditText mobileText;
    private EditText cityText;
    private Button signupButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        nameText = (EditText) view.findViewById(signUp_name);
        emailText = (EditText) view.findViewById(signUp_email);
        passwordText = (EditText) view.findViewById(signUp_password);
        reEnterPasswordText = (EditText) view.findViewById(signUp_reEnterPassword);
        mobileText = (EditText) view.findViewById(signUp_mob);
        cityText = (EditText) view.findViewById(signUp_city);
        signupButton = (Button) view.findViewById(btn_signup);
        loginLink = (TextView) view.findViewById(link_login);
        progressBar = (ProgressBar) view.findViewById(signUpProgress);

        hideProgressBar();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_login, LoginFragment.newInstance())
                        .commit();
            }
        });

        return view;
    }

    private void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        savetoServerDB();
    }

    private boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        String mobile = mobileText.getText().toString();
        String city = cityText.getText().toString();

        if (name.length() < 2) {
            nameText.setError("At least 2 characters must be present");
            nameText.requestFocus();
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            emailText.requestFocus();
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("Cannot be empty");
            passwordText.requestFocus();
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Passwords Do not match");
            reEnterPasswordText.requestFocus();
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        if (!(isValidMobile(mobile))) {
            mobileText.setError("Not a valid mobile number");
            mobileText.requestFocus();
            valid = false;
        } else {
            mobileText.setError(null);
        }

        if (city.length() < 2) {
            cityText.setError("At least 2 characters must be present");
            cityText.requestFocus();
            valid = false;
        } else {
            cityText.setError(null);
        }

        return valid;
    }

    private boolean isValidMobile(String mobile) {
        boolean valid;
        if (!Pattern.matches("[a-zA-Z]+", mobile)) {
            if (mobile.length() != 10)
                valid = false;
            else
                valid = true;
        } else
            valid = false;
        return valid;
    }

    private void savetoServerDB() {

        showProgressBar();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String mobile = mobileText.getText().toString();
        String city = cityText.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);

        User user = new User(name, email, password, mobile, city);
        Call<MSG> signupCall = service.userSignUp(user);

        signupCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
                hideProgressBar();

                if (response.body().getSuccess()) {
                    Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(), "Account already exists!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                hideProgressBar();
                t.printStackTrace();
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onSignupFailed() {
        signupButton.setEnabled(true);
    }

    private void showProgressBar() {
        nameText.setEnabled(false);
        emailText.setEnabled(false);
        passwordText.setEnabled(false);
        reEnterPasswordText.setEnabled(false);
        mobileText.setEnabled(false);
        cityText.setEnabled(false);
        signupButton.setEnabled(false);
        loginLink.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        nameText.setEnabled(true);
        emailText.setEnabled(true);
        passwordText.setEnabled(true);
        reEnterPasswordText.setEnabled(true);
        mobileText.setEnabled(true);
        cityText.setEnabled(true);
        signupButton.setEnabled(true);
        loginLink.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
}
