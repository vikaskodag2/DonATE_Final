package com.sdl.app.tempdonate.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
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
import com.sdl.app.tempdonate.Retrofit.OTP;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotFragment extends Fragment {

    String email;

    private ProgressBar forgotProgress;
    private EditText forgotEmail;
    private Button generateOTP;
    private AppCompatButton buttonConfirm;
    private EditText editTextConfirmOtp;
    private ProgressBar OTPprogressbar;
    private AlertDialog alertDialog;

    public ForgotFragment() {
        // Required empty public constructor
    }

    public static ForgotFragment newInstance() {
        ForgotFragment fragment = new ForgotFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        forgotProgress = (ProgressBar) view.findViewById(R.id.ForgotProgress);
        forgotEmail = (EditText) view.findViewById(R.id.frgt_email);
        generateOTP = (Button) view.findViewById(R.id.frgt_btn);


        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateOTP();
            }
        });
        return view;
    }

    private void generateOTP() {

        if(!validate()) {
            onOTPFailed();
        }

        validateFromServerDB();
        return;
    }

    private void validateFromServerDB() {

        showpBar();

        email = forgotEmail.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);

        Call<List<MSG>> userCall = service.userForgot(email);

        userCall.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                hidepBar();

                if(response.body().get(0).getSuccess()) {
                    //Creating a LayoutInflater object for the dialog box
                    LayoutInflater li = LayoutInflater.from(getContext());
                    //Creating a view to get the dialog box
                    View confirmDialog = li.inflate(R.layout.dialog_otp, null);

                    //Initializing confirm button fo dialog box and edittext of dialog box
                    buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
                    editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
                    OTPprogressbar = (ProgressBar) confirmDialog.findViewById(R.id.OTPProgress);

                    //Creating an AlertDialog builder
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    //Adding our dialog box to the view of alert dialog
                    alert.setView(confirmDialog);

                    //Creating an alert dialog
                    alertDialog = alert.create();

                    //Displaying the alert dialog
                    alertDialog.show();

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!validateOTP())
                                return;

                            confirmOTPFromServer();
                            return;
                        }
                    });
                } else {
                    Toast.makeText(getContext(), response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                hidepBar();
                t.printStackTrace();
                Toast.makeText(getContext(), "Check your Internet connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void confirmOTPFromServer() {

        showOTPBar();

        String verifyOTP = editTextConfirmOtp.getText().toString().trim();

        APIService service = APIClient.getClient().create(APIService.class);

        OTP otp = new OTP(verifyOTP, email);
        Call<List<MSG>> userCall = service.OTPVerify(otp);

        userCall.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                hideOTPBar();

                if(response.body().get(0).getSuccess()) {
                    alertDialog.dismiss();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FORGOT_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().get(0).getToken());
                    editor.apply();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_login, ResetPasswordFragment.newInstance())
                            .commit();
                }
                else {
                    editTextConfirmOtp.setError(response.body().get(0).getMessage());
                    editTextConfirmOtp.requestFocus();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                hideOTPBar();
                Toast.makeText(getContext(), "Check your Internet Conection!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private boolean validateOTP() {
        boolean valid = true;

        String otp = editTextConfirmOtp.getText().toString().trim();

        if(!Pattern.compile("([0-9]{6})").matcher(otp).matches()) {
            editTextConfirmOtp.setError("Not a valid OTP");
            editTextConfirmOtp.requestFocus();
            valid = false;
        } else {
            editTextConfirmOtp.setError(null);
        }
        return valid;
    }

    private void onOTPFailed() {
        Toast.makeText(getActivity(), "Invalid email entered!", Toast.LENGTH_SHORT).show();
        forgotEmail.setEnabled(true);
        generateOTP.setEnabled(true);
    }


    private boolean validate() {
        boolean valid = true;

        String email = forgotEmail.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotEmail.setError("Enter a valid email address");
            forgotEmail.requestFocus();
            valid = false;
        } else {
            forgotEmail.setError(null);
        }
        return valid;
    }

    private void showpBar() {
        forgotEmail.setEnabled(false);
        generateOTP.setEnabled(false);
        forgotProgress.setVisibility(View.VISIBLE);
    }

    private void hidepBar() {
        forgotEmail.setEnabled(true);
        generateOTP.setEnabled(true);
        forgotProgress.setVisibility(View.GONE);
    }

    private void showOTPBar() {
        editTextConfirmOtp.setEnabled(false);
        buttonConfirm.setEnabled(false);
        OTPprogressbar.setVisibility(View.VISIBLE);
    }

    private void hideOTPBar() {
        editTextConfirmOtp.setEnabled(true);
        buttonConfirm.setEnabled(true);
        OTPprogressbar.setVisibility(View.GONE);
    }
}
