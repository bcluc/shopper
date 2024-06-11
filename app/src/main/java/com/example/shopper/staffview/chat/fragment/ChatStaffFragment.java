package com.example.shopper.staffview.chat.fragment;

import static android.content.Context.SEARCH_SERVICE;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shopper.R;
import com.example.shopper.authentication.model.User;
import com.example.shopper.staffview.chat.adapter.ChatFragmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatStaffFragment#} factory method to
 * create an instance of this fragment.
 */
public class ChatStaffFragment extends Fragment implements AdapterView.OnItemSelectedListener, Filterable {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    Button btn_back;

    //ImageView mimageviewofuser;
    SearchView searchView;
    ChatFragmentAdapter adapterChatFragment;

    //FirestoreRecyclerAdapter<User,NoteViewHolder> chatAdapter;
    ProgressDialog progressDialog;
    ArrayList<User> userArrayList;
    RecyclerView mrecyclerview;

    Query query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_staff, container, false);
        mrecyclerview = v.findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        userArrayList = new ArrayList<>();
        adapterChatFragment = new ChatFragmentAdapter(getContext(), userArrayList);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        EventInitListener();

        mrecyclerview.setAdapter(adapterChatFragment);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView = v.findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapterChatFragment.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterChatFragment.getFilter().filter(s);
                return false;
            }
        });
        return v;

    }

    private void EventInitListener() {
        progressDialog.setTitle("Loading data...");
        progressDialog.show();
        firebaseFirestore.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressDialog.dismiss();
                userArrayList.clear();
                for (DocumentSnapshot d : task.getResult()) {
                    User object = d.toObject(User.class);
                    if (!object.getUserId().equals(firebaseAuth.getUid())) userArrayList.add(object);
                    adapterChatFragment.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public Filter getFilter() {
        return null;
    }

}