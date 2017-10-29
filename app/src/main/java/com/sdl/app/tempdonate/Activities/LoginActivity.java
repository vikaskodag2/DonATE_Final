package com.sdl.app.tempdonate.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sdl.app.tempdonate.Fragments.LoginFragment;
import com.sdl.app.tempdonate.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_login, LoginFragment.newInstance())
                .commit();
    }
}

