package com.example.shopper.customerview.notification.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.adapter.order.CheckProductOrderAdapter;
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
 * Use the {@link CustomerDeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerDeliveredFragment extends Fragment {

    private List<Order> orderList;
    private CheckProductOrderAdapter orderAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    public static CustomerDeliveredFragment newInstance(String param1, String param2) {
        CustomerDeliveredFragment fragment = new CustomerDeliveredFragment();
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
        View view = inflater.inflate(R.layout.fragment_customer_delivered, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        RecyclerView recyclerView = view.findViewById(R.id.RCV_delivered_customer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Truy van
        // Truy vấn collection "DONHANG"
        CollectionReference donHangRef = firebaseFirestore.collection("ORDER");
        donHangRef.whereEqualTo("state", "Delivered")
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
                    orderAdapter = new CheckProductOrderAdapter(orderList);
                    orderAdapter.refresh();
                    recyclerView.setAdapter(orderAdapter);
                });

        return view;
    }
}