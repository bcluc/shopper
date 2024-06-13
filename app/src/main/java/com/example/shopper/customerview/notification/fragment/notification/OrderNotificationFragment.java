package com.example.shopper.customerview.notification.fragment.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.adapter.notification.OrderNotificationAdapter;
import com.example.shopper.customerview.notification.model.Voucher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderNotificationFragment extends Fragment {

    private List<Voucher> voucherList;
    private OrderNotificationAdapter orderNotificationAdapter;
    private RecyclerView recyclerViewNotifications;

    public OrderNotificationFragment() {
        // Required empty public constructor
    }

    public static OrderNotificationFragment newInstance(String param1, String param2) {
        OrderNotificationFragment fragment = new OrderNotificationFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_order_notification, container, false);

        recyclerViewNotifications = rootView.findViewById(R.id.RCV_order_notification);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));

        voucherList = new ArrayList<>();
        Collections.sort(voucherList, new Comparator<Voucher>() {
            @Override
            public int compare(Voucher o1, Voucher o2) {
                return - o1.getThoigian().compareTo(o2.getThoigian());
            }
        });
        orderNotificationAdapter = new OrderNotificationAdapter(voucherList, requireContext());

        recyclerViewNotifications.setAdapter(orderNotificationAdapter);
        return rootView;
    }
}