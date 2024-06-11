package com.example.shopper.staffview.promotion.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.promotion.model.ReadStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddMyPromotion extends AppCompatActivity {

    private static final int REQUEST_CODE_PROMOTION_IMAGE = 1;
    private static final int REQUEST_CODE_THONG_BAO_IMAGE = 2;
    private ImageButton btn_back;
    private EditText name, detail, minimum, kind, rate;
    private EditText start_day, end_day;
    private Timestamp start, end;
    private FirebaseFirestore db_khuyenmai;
    private Button btn_add_new;
    private ImageView image_promotion, image_tb;
    private Uri imageUri; // Để lưu trữ đường dẫn hình ảnh đã chọn
    private String imageUrl;
    private final ActivityResultLauncher<PickVisualMediaRequest> kmLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(AddMyPromotion.this, "No image was selected.", Toast.LENGTH_SHORT).show();
            } else {
                imageUri = o;
                Glide.with(AddMyPromotion.this)
                        .load(imageUri)
                        .into(image_promotion);

                // Gọi hàm uploadImageToFirebase() để tải ảnh khuyến mãi lên Firebase Storage
                //uploadImageToFirebase();
            }
        }
    });
    private Uri image_tb_uri;
    private String hinhanhTB;
    private final ActivityResultLauncher<PickVisualMediaRequest> tbLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(AddMyPromotion.this, "No image was selected.", Toast.LENGTH_SHORT).show();
            } else {
                image_tb_uri = o;
                Glide.with(AddMyPromotion.this)
                        .load(image_tb_uri)
                        .into(image_tb);

                // Gọi hàm uploadImageThongBaoToFirebase() để tải ảnh thông báo lên Firebase Storage
                //uploadImageThongBaoToFirebase();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_promotion);
        btn_back = findViewById(R.id.btn_back);
        image_promotion = findViewById(R.id.imageView);
        name = findViewById(R.id.tenKM);
        detail = findViewById(R.id.chitiet_km);
        minimum = findViewById(R.id.dontoi_thieu);
        kind = findViewById(R.id.loaikhuyenmai);
        rate = findViewById(R.id.tile);
        db_khuyenmai = FirebaseFirestore.getInstance();
        image_tb = findViewById(R.id.image_thongbao);
        start_day = findViewById(R.id.start);
        end_day = findViewById(R.id.end);
        image_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });
        image_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng ImagePicker để chọn hình ảnh
                kmLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });

        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMyPromotion.this, MyPromotion.class);
                startActivity(intent);
            }
        });

        btn_add_new = findViewById(R.id.btn_add_new);
        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageThongBaoToFirebase();
                uploadImageToFirebase();
                savePromotionDataToFirestore();
                createReadStatusDocument();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

    private void showDatePickerDialog(final boolean isStart) {
        // Lấy ngày hiện tại để hiển thị trên DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Khi người dùng chọn ngày tháng năm, lưu giá trị vào biến tương ứng
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        int selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
                        int selectedMinute = selectedCalendar.get(Calendar.MINUTE);

                        // Đặt giờ và phút cho biến start hoặc end tùy thuộc vào trường hợp
                        if (isStart) {
                            selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            selectedCalendar.set(Calendar.MINUTE, selectedMinute);
                            start = new Timestamp(selectedCalendar.getTime());
                            start_day.setText(formatTimestamp(start));
                        } else {
                            selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            selectedCalendar.set(Calendar.MINUTE, selectedMinute);
                            end = new Timestamp(selectedCalendar.getTime());
                            end_day.setText(formatTimestamp(end));
                        }
                    }
                },
                year, month, day
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private String formatTimestamp(Timestamp timestamp) {
        // Phương thức này để định dạng Timestamp thành chuỗi ngày tháng năm giờ phút
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }

    private void uploadImageThongBaoToFirebase() {
        if (image_tb_uri != null) {
            // Tạo tên duy nhất cho hình ảnh thông báo (ví dụ: "thongbao_image_2023_07_20_15_30_45.jpg")
            String fileName = "notify_image_" + System.currentTimeMillis() + ".jpg";

            // Tham chiếu đến Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("notifyImages").child(fileName);

            // Chuyển đổi hình ảnh thành mảng byte để tải lên
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_tb_uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] imageData = baos.toByteArray();

                // Tải lên hình ảnh thông báo lên Firebase Storage
                UploadTask uploadTask = storageRef.putBytes(imageData);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy đường dẫn tới hình ảnh thông báo đã tải lên
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Khi tải lên thành công, bạn có thể lưu đường dẫn downloadUrl vào biến hinhanhTB
                                hinhanhTB = downloadUrl.toString();
                                Log.d("UploadImage", "Image URL (ThongBao): " + hinhanhTB);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu tải lên thất bại
                        Log.e("UploadImage", "Upload failed: " + e.getMessage()); // Log the upload failure for debugging
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Hiển thị thông báo nếu chưa chọn ảnh thông báo
            Toast.makeText(AddMyPromotion.this, "Please pick notify Image", Toast.LENGTH_SHORT).show();
        }
    }


    // Phương thức để tải lên hình ảnh lên Firebase Storage
private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Tạo tên duy nhất cho hình ảnh (ví dụ: "promotion_image_2023_07_20_15_30_45.jpg")
            String fileName = "promotion_image_" + System.currentTimeMillis() + ".jpg";

            // Tham chiếu đến Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("promotionImages").child(fileName);

            // Chuyển đổi hình ảnh thành mảng byte để tải lên
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] imageData = baos.toByteArray();

                // Tải lên hình ảnh lên Firebase Storage
                UploadTask uploadTask = storageRef.putBytes(imageData);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy đường dẫn tới hình ảnh đã tải lên
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Khi tải lên thành công, bạn có thể sử dụng đường dẫn downloadUrl để lưu trữ trong imageUrl
                                imageUrl = downloadUrl.toString();
                                Log.d("UploadImage", "Image URL: " + imageUrl);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu tải lên thất bại
                        Log.e("UploadImage", "Upload failed: " + e.getMessage()); // Log the upload failure for debugging
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Hiển thị thông báo nếu chưa chọn hình ảnh
            Toast.makeText(AddMyPromotion.this, "Please pick promotion images", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePromotionDataToFirestore() {
        // Lấy thông tin khuyến mãi từ các EditText (name, detail, minimum, kind, rate, start, end)
        String promotionName = name.getText().toString();
        String promotionDetail = detail.getText().toString();
        String kindValue = kind.getText().toString();

        // Kiểm tra xem trường minimum và rate có chưa dữ liệu và có chứa giá trị số hợp lệ không
        String minimumStr = minimum.getText().toString();
        String rateStr = rate.getText().toString();

        if (minimumStr.isEmpty() || rateStr.isEmpty()) {
            Toast.makeText(AddMyPromotion.this, "Please fill all information", Toast.LENGTH_SHORT).show();
            return;
        }

        int minimumValue;
        double rateValue;
        try {
            minimumValue = Integer.parseInt(minimumStr);
            rateValue = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(AddMyPromotion.this, "Please enter minium value and rated", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo một Map chứa thông tin khuyến mãi để thê


        // Kiểm tra xem ngày bắt đầu và ngày kết thúc đã được chọn hay chưa
        if (start == null || end == null) {
            // Hiển thị thông báo nếu ngày bắt đầu hoặc kết thúc chưa được chọn
            Toast.makeText(AddMyPromotion.this, "Please select start date and end", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra ràng buộc ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu
        if (end.compareTo(start) < 0) {
            // Hiển thị thông báo nếu ngày kết thúc nhỏ hơn ngày bắt đầu
            Toast.makeText(AddMyPromotion.this, "End date must after start date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUrl == null) {
            Toast.makeText(AddMyPromotion.this, "You need to add promotion images", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hinhanhTB == null) {
            Toast.makeText(AddMyPromotion.this, "You need to add notify images", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo một Map chứa thông tin khuyến mãi để thêm vào Firestore
        Map<String, Object> promotionData = new HashMap<>();
        promotionData.put("promotionName", promotionName);
        promotionData.put("promotionDetail", promotionDetail);
        promotionData.put("miniumValue", minimumValue);
        promotionData.put("promotionType", kindValue);
        promotionData.put("ratedValue", rateValue);
        promotionData.put("startDate", start);
        promotionData.put("endDate", end);
        promotionData.put("promotionImg", imageUrl); // Lưu đường dẫn hình ảnh vào Firestore
        promotionData.put("notifyImg", hinhanhTB);


        // Thực hiện thêm dữ liệu vào collection "KHUYENMAI" trong Firestore
        db_khuyenmai.collection("PROMOTION")
                .add(promotionData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Lấy ID của document sau khi thêm dữ liệu thành công
                        String maKM = documentReference.getId();
                        // Thêm ID của document vào trường "MaKM"
                        db_khuyenmai.collection("PROMOTION").document(maKM)
                                .update("promotionId", maKM) // Cập nhật trường "MaKM" với giá trị ID
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Xử lý thành công
                                        Toast.makeText(AddMyPromotion.this, "Add new promotion successfully", Toast.LENGTH_SHORT).show();

                                        // Chuyển về màn hình danh sách khuyến mãi sau khi thêm thành công
                                        Intent intent = new Intent(AddMyPromotion.this, MyPromotion.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý lỗi nếu có khi cập nhật trường "MaKM"
                                        Toast.makeText(AddMyPromotion.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        db_khuyenmai.collection("CUSTOMNOTIFY")
                                .whereEqualTo("notifyType", kindValue) // Compare with "kindValue"
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        String maTB = document.getId(); // MaTB from the matching document
                                        Boolean read = false;
                                        Date currentTime = new Date();

                                        Timestamp timestamp = new Timestamp(currentTime);
                                        // Create notification data
                                        Map<String, Object> thongBao = new HashMap<>();
                                        thongBao.put("notifyId", maTB);
                                        thongBao.put("promotionId", maKM);
                                        thongBao.put("read", read);
                                        thongBao.put("timeStamp", timestamp);
                                        // Add the notification to "THONGBAO" collection
                                        db_khuyenmai.collection("SENDNOTIFY")
                                                .add(thongBao)
                                                .addOnSuccessListener(documentSetTB -> {
                                                    String thongBaoId = documentSetTB.getId();
                                                    thongBao.put("sendNotifyId", thongBaoId);
                                                    documentSetTB.set(thongBao)
                                                            .addOnSuccessListener(aVoid -> {
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Xử lý lỗi khi cập nhật tài liệu "THONGBAODONHANG"
                                                            });
                                                    // Notification added successfully
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle the failure if adding notification fails
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Handle the failure if querying "MATHONGBAO" fails
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu có khi thêm dữ liệu vào Firestore
                        Toast.makeText(AddMyPromotion.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createReadStatusDocument() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 1. Lấy danh sách mã ND từ COLLECTION "NGUOIDUNG"
        CollectionReference nguoiDungCollection = db.collection("USERS");
        Task<QuerySnapshot> getMaNDTask = nguoiDungCollection.get();

        getMaNDTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot maNDQuerySnapshot) {
                        List<String> maNDList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : maNDQuerySnapshot) {
                            String maND = document.getString("userId");
                            if (maND != null) {
                                maNDList.add(maND);
                            }
                        }

                        // 2. Lấy danh sách ID từ COLLECTION "THONGBAO"
                        CollectionReference thongBaoCollection = db.collection("SENDNOTIFY");
                        Task<QuerySnapshot> getTBIdTask = thongBaoCollection.get();

                        getTBIdTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot tbIdQuerySnapshot) {
                                        List<String> tbIdList = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : tbIdQuerySnapshot) {
                                            String tbId = document.getId();
                                            tbIdList.add(tbId);
                                        }

                                        // 3. Tạo các document mới trong COLLECTION "READSTATUS"
                                        CollectionReference readStatusCollection = db.collection("READSTATUS");
                                        List<Task<DocumentReference>> tasks = new ArrayList<>();
                                        for (String maND : maNDList) {
                                            for (String tbId : tbIdList) {
                                                ReadStatus readStatus = new ReadStatus(maND, false, tbId);
                                                Task<DocumentReference> addTask = readStatusCollection.add(readStatus);
                                                tasks.add(addTask);
                                            }
                                        }

                                        // Wait for all tasks to complete
                                        Tasks.whenAll(tasks)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // All documents added successfully.
                                                        // You can perform any additional actions here if needed.
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle error when adding documents.
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý khi có lỗi xảy ra khi lấy danh sách ID từ COLLECTION "THONGBAO"
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi có lỗi xảy ra khi lấy danh sách mã ND từ COLLECTION "NGUOIDUNG"
                    }
                });
    }
}