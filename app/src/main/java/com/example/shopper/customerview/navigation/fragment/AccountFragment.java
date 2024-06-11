package com.example.shopper.customerview.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.shopper.R;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayout YourOrder;
    private LinearLayout Changepass;
    private LinearLayout Help;
    private LinearLayout Profile;

    private  LinearLayout Logout;
    private TextView txtFullName, txtUserID;
    private BottomNavigationCustomerActivity bottomNavigationCustomActivity;
    private ShapeableImageView imgAvt;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomerActivity) getActivity();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        YourOrder = view.findViewById(R.id.l1);
        Changepass = view.findViewById(R.id.l4);
        Help = view.findViewById(R.id.l2);
        Profile = view.findViewById(R.id.l3);

        Logout = view.findViewById(R.id.l5);
        txtFullName = view.findViewById(R.id.user_name);
        imgAvt = view.findViewById(R.id.img_avt_account);
        // Inflate the layout for this fragment

        setOnCLickHelp();
        setOnClickChangePass();
        setOnClickLogOut();
        setOnClickProfile();
        if (currentUser != null) {
            String userID = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("USERS").document(userID);

            userRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Toast.makeText(getContext(), "Can't get data từ Firestore. Try later!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Lấy thông tin người dùng từ Firestore
                    String fullName = documentSnapshot.getString("fullName");
                    String avatarURL = documentSnapshot.getString("avatar");
                    txtFullName.setText(fullName);
                    if (avatarURL != null && !avatarURL.isEmpty()) {
                        Picasso.get().load(avatarURL).into(imgAvt);
                    } else {
                    }
                }
            });
        }
        setOnClick();
        return view;
    }


    private void setOnClickProfile() {
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoProfile();
            }
        });
    }
    private void setOnCLickHelp() {
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoCLickHelp();
            }
        });
    }
    private void setOnClickChangePass() {
        Changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoChangePass();
            }
        });
    }
    private void setOnClickLogOut() {
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference= FirebaseFirestore.getInstance().
                        collection("USERS").document(FirebaseAuth.getInstance().getUid());
                documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
                bottomNavigationCustomActivity.gotoLogOut();

            }
        });
    }

    private void setOnClick() {

        YourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoOrderActivity();
            }
        });
    }
}
