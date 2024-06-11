package com.example.shopper.staffview.report.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FinancialReport extends AppCompatActivity {

    private EditText startDay, endDay;
    private Button view_report, btn_today;
    private TextView doanhthu, sodonhang;
    private FirebaseFirestore firestore;
    private ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_report);

        firestore = FirebaseFirestore.getInstance();

        startDay = findViewById(R.id.edtStartDate);
        endDay = findViewById(R.id.edtEndDate);
        view_report = findViewById(R.id.view_report);
        doanhthu = findViewById(R.id.doanhthu);
        sodonhang = findViewById(R.id.sodonhang);
        doanhthu.setText("0");
        sodonhang.setText("0");
        ic_back = findViewById(R.id.imgbtn_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinancialReport.this.finish();
            }
        });
        btn_today = findViewById(R.id.btn_today);
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select Date Range Report")
                        .build();
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                datePicker.addOnPositiveButtonClickListener(
                        (MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
                            if (selection != null) {
                                Long startDate = selection.first;
                                Long endDate = selection.second;

                                // Convert the start date and end date to a readable format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                                if (startDate != null) {
                                    Calendar startCalendar = Calendar.getInstance();
                                    startCalendar.setTimeInMillis(startDate);
                                    String formattedStartDate = sdf.format(startCalendar.getTime());
                                    startDay.setText(formattedStartDate);
                                }

                                if (endDate != null) {
                                    Calendar endCalendar = Calendar.getInstance();
                                    endCalendar.setTimeInMillis(endDate);
                                    String formattedEndDate = sdf.format(endCalendar.getTime());
                                    endDay.setText(formattedEndDate);
                                }
                            }
                        });
            }
        });
        // Trong phương thức view_report.setOnClickListener
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateStr = startDay.getText().toString();
                String endDateStr = endDay.getText().toString();
                Log.d("StartDay", " is: " + startDateStr);
                Log.d("EndDay", " is: " + endDateStr);
                // Nếu chỉ nhập ngày bắt đầu
                if (!startDateStr.isEmpty() && endDateStr.isEmpty()) {

                    // Thực hiện truy vấn với ngày bắt đầu
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = new Date(); // Sử dụng ngày hiện tại làm ngày kết thúc
                        getDeliveredOrdersAndCalculateRevenue(startDate, endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // Nếu chỉ nhập ngày kết thúc
                else if (startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                    // Thực hiện truy vấn với ngày kết thúc
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    try {
                        Date startDate = new Date(0); // Sử dụng ngày 0 làm ngày bắt đầu (ngày 01/01/1970)
                        Date endDate = dateFormat.parse(endDateStr);
                        getDeliveredOrdersAndCalculateRevenue(startDate, endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // Nếu nhập cả 2 ngày bắt đầu và ngày kết thúc
                else if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                    // Thực hiện truy vấn với cả hai ngày
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);
                        if (startDate.compareTo(endDate) == 0) {
                            getDeliveredOrdersAndCalculateRevenue(startDate, endDate);
                        } else {
                            getDeliveredOrdersAndCalculateRevenue(startDate, endDate);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // Nếu không nhập cả 2 ngày bắt đầu và ngày kết thúc
                else {
                    Toast.makeText(FinancialReport.this, "Please enter start date and end date", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

    private void getDeliveredOrdersAndCalculateRevenue(Date startDate, Date endDate) {
        Timestamp startTimestamp = new Timestamp(startDate);
        Timestamp endTimestamp = new Timestamp(endDate);
        // If endDay is equal to startDay, set the end time to the end of the day
        // Loại bỏ giờ, phút, giây từ Timestamp
        startTimestamp = removeTimeFromTimestamp(startTimestamp);
        endTimestamp = setEndTimeFromtimestamp(endTimestamp);
        firestore.collection("ORDER")
                .whereEqualTo("state", "Delivered")
                .whereGreaterThanOrEqualTo("orderDate", startTimestamp)
                .whereLessThanOrEqualTo("orderDate", endTimestamp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            long totalRevenue = 0;
                            int orderCount = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assume you have a field "TongTien" for the total amount in each order
                                Long orderRevenue = document.getLong("totalBill");
                                if (orderRevenue != null) {
                                    totalRevenue += orderRevenue;
                                    orderCount++;
                                }
                            }
                            // Display the total revenue and order count in respective TextViews
                            doanhthu.setText(String.valueOf(totalRevenue));
                            sodonhang.setText(String.valueOf(orderCount));
                        } else {
                            Toast.makeText(FinancialReport.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private Timestamp removeTimeFromTimestamp(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getSeconds() * 1000); // Chuyển đổi giây thành mili giây
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long seconds = calendar.getTimeInMillis() / 1000; // Chuyển đổi lại thành giây
        return new Timestamp(seconds, 0);
    }
    private Timestamp setEndTimeFromtimestamp(Timestamp timestamp)
    {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(timestamp.getSeconds() * 1000); // Convert seconds to milliseconds
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999); // Set milliseconds to 999 for end of day

        long seconds = endCalendar.getTimeInMillis() / 1000; // Convert back to seconds
        return new Timestamp(seconds, 0);
    }
}