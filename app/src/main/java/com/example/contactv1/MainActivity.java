package com.example.contactv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static ContactListAdapter adapter;
    static List<Contact> arrayList;
    static Intent intentAdd;
    static RecyclerView rvList;
    static Intent intent;
    static boolean isRunning = true;
    static MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        System.out.println("Trang thai onCreate");
        rvList = findViewById(R.id.rv_list);

        // Tạo 1 list view hiển thị nội dung trong mảng các Content
        if (isRunning) {
            db = new MyDatabase(this);
            arrayList = new ArrayList<>();
            arrayList = db.getAllContacts();
            adapter = new ContactListAdapter(this, arrayList);
            isRunning = false;
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);

        // Tạo 1 intent để chuyển sang activity add_contact
        intentAdd = new Intent(this, AddContactActivity.class);
        FloatingActionButton btnAddContact = findViewById(R.id.fab_add_contact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAdd);
            }
        });

        // Nhận intent của các Activity khác gửi về
        intent = getIntent();
        intentHandler(intent);


    }

    void intentHandler(Intent intent) {
        intentAddHandler(intent);
        intentEditHandler(intent);
        intentDeleteHandler(intent);

        arrayList.clear();
        arrayList.addAll(db.getAllContacts());
        adapter.notifyDataSetChanged();
    }

    void intentAddHandler(Intent intent) {
        Bundle bundle = intent.getBundleExtra("contactPackage");

        if (bundle != null) {
            Contact newContact = (Contact) bundle.getSerializable("contact");
            db.addContact(newContact);
            getIntent().removeExtra("contact");
        }
    }

    void intentEditHandler(Intent intent) {
        Contact contactEdited = (Contact) intent.getSerializableExtra("ContactEdit");
        int pos = intent.getIntExtra("PosEdit", -1);

        if (contactEdited != null && pos != -1) {
            System.out.println("Thực hiện chỉnh sửa");
//            arrayList.set(pos, contactEdited);
            db.updateContact(contactEdited.getId(), contactEdited);
            getIntent().removeExtra("PosEdit");
            getIntent().removeExtra("ContactEdit");
        }
    }

    void intentDeleteHandler(Intent intent) {
        int posDelete = intent.getIntExtra("PosDelete", -1);
        if (posDelete != -1) {
            String id = arrayList.get(posDelete).getId();
            db.deleteContact(id);
            getIntent().removeExtra("PosDelete");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Cài menu là search view
        getMenuInflater().inflate(R.menu.search_view, menu);
        // Lấy đối tượng searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Search View lắng nghe thay đổi text
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
