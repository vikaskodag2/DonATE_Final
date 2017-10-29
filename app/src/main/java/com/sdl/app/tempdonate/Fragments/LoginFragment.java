package com.sdl.app.tempdonate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.sdl.app.tempdonate.Activities.HomeActivity;
import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.MSG;
import com.sdl.app.tempdonate.Retrofit.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdl.app.tempdonate.R.id.content_login;

public class LoginFragment extends Fragment {

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;
    private TextView frgtPwdLink;
    private ProgressBar progressBar;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailText = (EditText) view.findViewById(R.id.input_email);
        passwordText = (EditText) view.findViewById(R.id.input_password);
        loginButton = (Button) view.findViewById(R.id.btn_login);
        signupLink = (TextView) view.findViewById(R.id.link_signup);
        frgtPwdLink = (TextView) view.findViewById(R.id.frgt_pwd);
        progressBar = (ProgressBar) view.findViewById(R.id.loginProgress);

        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Move to signUp Fragment
                new SignUpFragment();
                getActivity().getSupportFragmentManager()
                      .beginTransaction()
                      .replace(content_login, SignUpFragment.newInstance())
                      .addToBackStack(null)
                      .commit();
            }
        });

        frgtPwdLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to forgot password Fragment
                new ForgotFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(content_login, ForgotFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void login() {

        if(!validate()) {
            onLoginFailed();
            return;
        }

        validateFromServerDB();
    }

    private void validateFromServerDB() {

        showpBar();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);


        User user = new User(null,email,password,null,null);
        Call<List<MSG>> userCall = service.userLogIn(user);

        userCall.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                hidepBar();

                if(response.body().get(0).getSuccess()) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().get(0).getToken());
                    editor.apply();
                    Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), HomeActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                hidepBar();
                t.printStackTrace();
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoginFailed() {
        Toast.makeText(getActivity(), "Invalid data entered!", Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            emailText.requestFocus();
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("Password cannot be empty");
            passwordText.requestFocus();
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }

    private void showpBar() {
        emailText.setEnabled(false);
        passwordText.setEnabled(false);
        loginButton.setEnabled(false);
        signupLink.setEnabled(false);
        frgtPwdLink.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidepBar() {
        emailText.setEnabled(true);
        passwordText.setEnabled(true);
        loginButton.setEnabled(true);
        signupLink.setEnabled(true);
        frgtPwdLink.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
}
