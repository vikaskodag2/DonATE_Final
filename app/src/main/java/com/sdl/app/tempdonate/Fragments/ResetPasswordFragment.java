package com.sdl.app.tempdonate.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.MSG;
import com.sdl.app.tempdonate.Retrofit.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String token;

    EditText editPasswordText;
    EditText editReEnterPasswordText;
    Button resetPasswordButton;
    ProgressBar resetProgressBar;

    public ResetPasswordFragment() {
    }


    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences("FORGOT_PREF", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        if(token.equalsIgnoreCase("") || token==null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_login, LoginFragment.newInstance())
                    .commit();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        editPasswordText = (EditText) view.findViewById(R.id.reset_pwd);
        editReEnterPasswordText = (EditText) view.findViewById(R.id.reset_reEnterPassword);
        resetPasswordButton = (Button) view.findViewById(R.id.reset_btn);
        resetProgressBar = (ProgressBar) view.findViewById(R.id.ResetProgress);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        return view;
    }

    private void resetPassword() {

        if(!validate()) {
            onResetFailed();
            return;
        }

        updateToServerDB();
    }

    private void updateToServerDB() {

        showpBar();

        String password = editPasswordText.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);

        User user = new User(null, null, password, null, null);

        Call<List<MSG>> userCall = service.updatePassword(user, token);

        userCall.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                hidepBar();

                if(response.body().get(0).getSuccess()) {
                    Toast.makeText(getContext(), "Password has been reset!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_login, LoginFragment.newInstance())
                            .commit();
                }
                else {
                    Toast.makeText(getContext(), "Password could not be updated. Try again!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_login, LoginFragment.newInstance())
                            .commit();
                }
            }
            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onResetFailed() {
        resetPasswordButton.setEnabled(true);
    }


    private boolean validate() {
        boolean valid = true;

        String password = editPasswordText.getText().toString().trim();
        String reEnterPassword = editReEnterPasswordText.getText().toString().trim();

        if(password.length() <6) {
            editPasswordText.setError("Password must be at least 6 characters");
            editPasswordText.requestFocus();
            valid = false;
        }

        if(!(password.equals(reEnterPassword))) {
            editReEnterPasswordText.setError("Both passwords must match!");
            editReEnterPasswordText.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void showpBar() {
        editPasswordText.setEnabled(false);
        editReEnterPasswordText.setEnabled(false);
        resetPasswordButton.setEnabled(false);
        resetProgressBar.setVisibility(View.VISIBLE);
    }

    private void hidepBar() {
        editPasswordText.setEnabled(true);
        editReEnterPasswordText.setEnabled(true);
        resetPasswordButton.setEnabled(true);
        resetProgressBar.setVisibility(View.GONE);
    }

}
