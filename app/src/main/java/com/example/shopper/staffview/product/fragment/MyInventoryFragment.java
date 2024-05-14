package com.example.shopper.staffview.product.fragment;

import static android.content.Context.SEARCH_SERVICE;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.adapter.MyInventoryAdapter;
import com.example.shopper.staffview.product.model.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyInventoryFragment#} factory method to
 * create an instance of this fragment.
 */
public class MyInventoryFragment extends Fragment implements Filterable {

    private RecyclerView recyclerView;
    private MyInventoryAdapter adapter;
    private SearchView searchView;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_inventory, container, false);

        recyclerView = view.findViewById(R.id.RCV_My_inventory);
        searchView = view.findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        adapter = new MyInventoryAdapter(productList, getContext());
        adapter.setHideButtonClickListener(new MyInventoryAdapter.HideButtonClickListener() {
            @Override
            public void onHideButtonClick(int position) {
                adapter.updateProductStatus(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        loadProductsFromFirestore();
        SearchTrending();
        return view;
    }

    private void SearchTrending() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void loadProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference sanphamRef = db.collection("PRODUCT");
        sanphamRef.whereEqualTo("state", "Inventory")
                .whereGreaterThan("warehouse", 0)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Xử lý lỗi nếu cần thiết
                        return;
                    }
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String tenSP = document.getString("productName");
                        String maSP = document.getString("productId");
                        String moTaSP = document.getString("description");
                        List<String> color = (List<String>) document.get("productColor");
                        List<String> size = (List<String>) document.get("productSize");
                        String trangthai = document.getString("state");
                        int price = document.getLong("productPrice") != null ? document.getLong("productPrice").intValue() : 0;
                        int warehouse = document.getLong("warehouse") != null ? document.getLong("warehouse").intValue() : 0;
                        int sold = document.getLong("sold") != null ? document.getLong("sold").intValue() : 0;
                        int love = document.getLong("love") != null ? document.getLong("love").intValue() : 0;
                        int view = document.getLong("quanity") != null ? document.getLong("quanity").intValue() : 0;


                        // Kiểm tra xem trường "HinhAnhSP" có dữ liệu không
                        if (document.contains("imageUrl")) {
                            Object hinhAnhSPValue = document.get("imageUrl");

                            // Kiểm tra nếu giá trị là một List<String>
                            if (hinhAnhSPValue instanceof List) {
                                List<String> hinhAnhSPList = (List<String>) hinhAnhSPValue;
                                // Lấy địa chỉ ảnh đầu tiên trong mảng
                                String hinhAnhSP = hinhAnhSPList != null && !hinhAnhSPList.isEmpty() ? hinhAnhSPList.get(0) : "";
                                // Tạo đối tượng Product và thiết lập các thuộc tính
                                Product product = new Product(hinhAnhSP, tenSP, price, warehouse, sold, love, view, maSP);
                                product.setDescription(moTaSP);
                                product.setProductSize(size);
                                product.setProductColor(color);
                                product.setState(trangthai);

                                // Thêm đối tượng Product vào danh sách
                                productList.add(product);
                            } else if (hinhAnhSPValue instanceof String) {
                                // Xử lý khi giá trị là một String
                                String hinhAnhSP = (String) hinhAnhSPValue;
                                // Tạo đối tượng Product và thiết lập các thuộc tính
                                Product product = new Product(hinhAnhSP, tenSP, price, warehouse, sold, love, view, maSP);
                                product.setDescription(moTaSP);
                                product.setProductSize(size);
                                product.setProductColor(color);
                                product.setState(trangthai);
                                // Thêm đối tượng Product vào danh sách
                                productList.add(product);
                            } else {
                                // Xử lý trường hợp giá trị không phải là List<String> hoặc String
                                // Nếu trường "HinhAnhSP" có giá trị khác kiểu List<String> hoặc String,
                                // bạn có thể xử lý tại đây theo ý muốn.
                            }
                        } else {
                            // Xử lý trường hợp không có dữ liệu trong trường "HinhAnhSP"
                            // Nếu không có giá trị hoặc giá trị là null, bạn có thể xử lý tại đây theo ý muốn.
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }


    @Override
    public Filter getFilter() {
        return null;
    }


}
