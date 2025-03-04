package com.MH.kershyApp.view.ui.ProfileUser;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.MH.kershyApp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import com.MH.kershyApp.Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileUserFragment extends Fragment {
    DatabaseReference reference;
    @BindView(R.id.imageView_user)
    CircleImageView imageViewUser;
    StorageReference mStorageRef;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Uri imageUri;
    View root;
    @BindView(R.id.edit_profile)
    ImageView editProfile;
    @BindView(R.id.textView_userName)
    TextView textViewUserName;
    @BindView(R.id.nameText)
    LinearLayout nameText;
    @BindView(R.id.editText_userName)
    EditText editTextUserName;
    @BindView(R.id.nameEdit)
    LinearLayout nameEdit;
    @BindView(R.id.textView_userEmail)
    TextView textViewUserEmail;
    @BindView(R.id.textView_userAddress)
    TextView textViewUserAddress;
    @BindView(R.id.textView_userPhone)
    TextView textViewUserPhone;
    @BindView(R.id.button_apply_cart)
    Button buttonApplyCart;
    @BindView(R.id.textEmail)
    LinearLayout textEmail;
    @BindView(R.id.textAddress)
    LinearLayout textAddress;
    @BindView(R.id.editText_userAddress)
    EditText editTextUserAddress;
    @BindView(R.id.editAddress)
    LinearLayout editAddress;
    @BindView(R.id.textPhone)
    LinearLayout textPhone;
    @BindView(R.id.editText_userPhone)
    EditText editTextUserPhone;
    @BindView(R.id.editPhone)
    LinearLayout editPhone;
    private StorageTask uploadTask;
    @BindView(R.id.v)
    View vi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile_user, container, false);
        ButterKnife.bind(this, root);
        onClickEditProfile();
        onClickApply();
        getDataFirebase();
        editImageUser();
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    // Open gallery from phone
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    // get extention
    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    // Upload image and update it in database(to convert image="default" to image="linkImage")
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.Uploading));
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference filereference = mStorageRef.
                    child(String.valueOf(System.currentTimeMillis())
                    + "." + getFileExtention(imageUri));
            uploadTask = filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        String mUri = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", mUri);
                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.Please_Choose_image, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Uploading...", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
    // To save user in formation in database..
    private void onClickApply() {
        buttonApplyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                String name = editTextUserName.getText().toString();
                String address = editTextUserAddress.getText().toString();
                String phone = editTextUserPhone.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Snackbar.make(v, R.string.enterName, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(address)) {
                    Snackbar.make(v, R.string.enterAddress, Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(phone)) {
                    Snackbar.make(v, R.string.enterPhone, Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("address", address);
                    hashMap.put("phone", phone);
                    hashMap.put("userName", name);
                    Toast.makeText(getActivity(), R.string.updateIsDone, Toast.LENGTH_SHORT).show();
                    reference.updateChildren(hashMap);
                }
                nameText.setVisibility(View.VISIBLE);
                textAddress.setVisibility(View.VISIBLE);
                textEmail.setVisibility(View.VISIBLE);
                textPhone.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.GONE);
                editAddress.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editTextUserName.getText().clear();
                editTextUserAddress.getText().clear();
                editTextUserPhone.getText().clear();
            }
        });
    }
    // To hide and show design to edit profile..
    private void onClickEditProfile() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setVisibility(View.GONE);
                textAddress.setVisibility(View.GONE);
                textEmail.setVisibility(View.GONE);
                textPhone.setVisibility(View.GONE);
                vi.setVisibility(View.GONE);
                buttonApplyCart.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.VISIBLE);
                editAddress.setVisibility(View.VISIBLE);
                editPhone.setVisibility(View.VISIBLE);
            }
        });
    }
    // To get User information from database..
    private void getDataFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()){
                    User users = dataSnapshot.getValue(User.class);
                    String nameUser = dataSnapshot.child("userName").getValue(String.class);
                    final String emailUser = dataSnapshot.child("email").getValue(String.class);
                    String phoneUser = dataSnapshot.child("phone").getValue(String.class);
                    String addressUser = dataSnapshot.child("address").getValue(String.class);
                    textViewUserName.setText(nameUser);
                    textViewUserEmail.setText(emailUser);
                    textViewUserPhone.setText(phoneUser);
                    textViewUserAddress.setText(addressUser);
                    if (users.getImageURL().equals("default")) {
                        imageViewUser.setImageResource(R.drawable.ic_man);
                    } else {
                        Glide.with(getActivity()).load(users.getImageURL()).into(imageViewUser);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editImageUser() {
        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

}