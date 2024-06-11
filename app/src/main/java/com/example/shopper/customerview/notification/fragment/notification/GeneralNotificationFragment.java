package com.example.shopper.customerview.notification.fragment.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.adapter.notification.GeneralNotificationAdapter;
import com.example.shopper.customerview.notification.model.Voucher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralNotificationFragment extends Fragment {
    private List<Voucher> voucherList;
    private GeneralNotificationAdapter generalAdapter;
    private RecyclerView recyclerViewNotifications;
    public GeneralNotificationFragment() {
        // Required empty public constructor
    }


    public static GeneralNotificationFragment newInstance(String param1, String param2) {
        GeneralNotificationFragment fragment = new GeneralNotificationFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_general_notification, container, false);

        recyclerViewNotifications = rootView.findViewById(R.id.RCV_general);
        recyclerViewNotifications.setLayoutManager(new GridLayoutManager(rootView.getContext(), 1));
        voucherList = new ArrayList<>();
        Collections.sort(voucherList, new Comparator<Voucher>() {
            @Override
            public int compare(Voucher voucher1, Voucher voucher2) {
                // Use the compareTo method of Timestamp to compare two timestamps.
                return voucher1.getThoigian().compareTo(voucher2.getThoigian());
            }
        });
        generalAdapter = new GeneralNotificationAdapter(voucherList, rootView.getContext());
        recyclerViewNotifications.setAdapter(generalAdapter);
        return rootView;
    }
}