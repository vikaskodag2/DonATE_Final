package com.sdl.app.tempdonate.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdl.app.tempdonate.Permissions.MarshmallowPermissions;
import com.sdl.app.tempdonate.R;
import com.sdl.app.tempdonate.Retrofit.APIClient;
import com.sdl.app.tempdonate.Retrofit.APIService;
import com.sdl.app.tempdonate.Retrofit.MSG;
import com.sdl.app.tempdonate.Retrofit.Profile;
import com.sdl.app.tempdonate.Retrofit.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private ImageButton editButton;
    private Button saveButton;
    private EditText editName, editEmail, editPhone, editCity;
    private ImageView profileImageView;
    private ProgressBar progressBar;
    private String token,profpic,userChosenTask;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editName = (EditText) view.findViewById(R.id.profile_name);
        editEmail = (EditText) view.findViewById(R.id.profile_email);
        editPhone = (EditText) view.findViewById(R.id.profile_phone);
        editCity = (EditText) view.findViewById(R.id.profile_city);
        editButton = (ImageButton) view.findViewById(R.id.profile_editbtn);
        saveButton = (Button) view.findViewById(R.id.profile_savebtn);
        profileImageView = (ImageView) view.findViewById(R.id.profile_image);
        progressBar = (ProgressBar) view.findViewById(R.id.profile_progressbar);

        progressBar.setVisibility(View.GONE);

        sharedPreferences = this.getActivity().getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        profpic = sharedPreferences.getString("profpic", "");

        if(profpic.equals("") || profpic == null) {
            profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_profile_picture));
        }
        else {
            profileImageView.setImageBitmap(decodeBase64(profpic));
        }

        APIService service = APIClient.getClient().create(APIService.class);

        Call<List<Profile>> profileCall = service.userProfile(token);

        profileCall.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                editName.setText(response.body().get(0).getName());
                editEmail.setText(response.body().get(0).getEmail());
                editPhone.setText(response.body().get(0).getPhoneNo());
                editCity.setText(response.body().get(0).getCity());
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                editName.setText("");
                editPhone.setText("");
                editCity.setText("");
                Toast.makeText(getActivity(), "Could not update your profile! Internet Connection was not established", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setEnabled(true);
                editPhone.setEnabled(true);
                editCity.setEnabled(true);
                saveButton.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.INVISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                    Toast.makeText(getActivity(), "Invalid data entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveToServer();

                editName.setEnabled(false);
                editPhone.setEnabled(false);
                editCity.setEnabled(false);
                editButton.setEnabled(true);
                saveButton.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.VISIBLE);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        return view;
    }

    private void saveToServer() {
        showpBar();

        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();
        String city = editCity.getText().toString();

        APIService service = APIClient.getClient().create(APIService.class);

        User user = new User(name, null, null, phone, city);
        Call<List<MSG>> updateCall = service.userUpdate(user, token);

        updateCall.enqueue(new Callback<List<MSG>>() {
            @Override
            public void onResponse(Call<List<MSG>> call, Response<List<MSG>> response) {
                hidepBar();

                if (response.body().get(0).getSuccess()) {
                    Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Profile not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MSG>> call, Throwable t) {
                hidepBar();
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();
        String city = editCity.getText().toString();

        if (name.length() < 2) {
            editName.setError("At least 2 characters must be present");
            editName.requestFocus();
            valid = false;
        } else {
            editName.setError(null);
        }

        if (!isValidMobile(phone)) {
            editPhone.setError("Not a valid phone number");
            editPhone.requestFocus();
            valid = false;
        } else {
            editPhone.setError(null);
        }

        if (city.length() < 2) {
            editCity.setError("Not a valid city");
            editCity.requestFocus();
            valid = false;
        } else {
            editCity.setError(null);
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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Remove Picture", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean StorageResult= MarshmallowPermissions.checkStoragePermission(getActivity());
                boolean CameraResult = MarshmallowPermissions.checkCameraPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChosenTask="Take Photo";
                    if(CameraResult)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask="Choose from Library";
                    if(StorageResult)
                        galleryIntent();
                } else if(items[item].equals("Remove Picture")) {
                    removePicture();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(getCropIntent(intent), REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getCropIntent(intent), "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MarshmallowPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(getCropIntent(data));
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(getCropIntent(data));
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profpic", encodeTobase64(bm));
        editor.apply();
        profileImageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profpic", encodeTobase64(thumbnail));
        editor.apply();
        profileImageView.setImageBitmap(thumbnail);
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private Intent getCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        return intent;
    }

    private void removePicture() {
        profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_profile_picture));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(profpic!=null || profpic.equalsIgnoreCase("")) {
            editor.putString("profpic", "");
            editor.apply();
        }
    }

    private void showpBar() {
        editName.setEnabled(false);
        editPhone.setEnabled(false);
        editCity.setEnabled(false);
        saveButton.setEnabled(false);
        editButton.setEnabled(false);
        profileImageView.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidepBar() {
        editButton.setEnabled(true);
        profileImageView.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
}
