package com.example.shopper.customerview.notification.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.adapter.order.CustomerOrderAdapter;
import com.example.shopper.staffview.order.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerWaitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerWaitFragment extends Fragment {

    private String currentUserId;
    private List<Order> orderList;
    private CustomerOrderAdapter orderAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public CustomerWaitFragment() {
        // Required empty public constructor
    }


    public static CustomerWaitFragment newInstance(String param1, String param2) {
        CustomerWaitFragment fragment = new CustomerWaitFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_wait, container, false);
        // Kết nối tới Firestore

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        RecyclerView recyclerView = view.findViewById(R.id.RCV_wait_Customer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Kết nối tới Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Truy van
        // Truy vấn collection "DONHANG"

        CollectionReference donHangRef = db.collection("ORDER");
        donHangRef.whereEqualTo("state", "Wait")
                .whereEqualTo("userId", firebaseUser.getUid())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    // Xử lý kết quả truy vấn
                    orderList = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Đọc dữ liệu từ documentSnapshot
                        Order order = documentSnapshot.toObject(Order.class);
                        orderList.add(order);
                    }

                    // Khởi tạo adapter và gán nó cho RecyclerView
                    orderAdapter = new CustomerOrderAdapter(orderList);
                    orderAdapter.refresh();
                    recyclerView.setAdapter(orderAdapter);
                });

        return view;
    }
}