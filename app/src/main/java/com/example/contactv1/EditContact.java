package com.example.contactv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class EditContact extends AppCompatActivity {
    Contact contact;
    EditText edtName, edtMobile;
    ImageView civAvatar;
    Button btnDelete;
    static Intent intentSendMainActivity;
    static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        setUpToolbar();

        edtName = findViewById(R.id.edt_edit_name);
        edtMobile = findViewById(R.id.edt_edit_mobile);
        civAvatar = findViewById(R.id.civ_edit_avatar);
        btnDelete = findViewById(R.id.btn_delete);

        // Nạp đối tượng cần chỉnh sửa
        contact = (Contact) getIntent().getSerializableExtra("contact");
        position = getIntent().getIntExtra("position", -1);
        edtName.setText(contact.getmName());
        edtMobile.setText(contact.getmMobile());

        if (contact.getmAvatar() != null) {
            byte[] bytes = contact.getmAvatar();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            civAvatar.setImageBitmap(bmp);

        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentSendMainActivity.putExtra("PosDelete", position);
                startActivity(intentSendMainActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void setUpToolbar() {
        Toolbar tb = findViewById(R.id.toolbarEdit);
        setSupportActionBar(tb);

        // Nhấn Cancel Icon để trở về MainActivity
        tb.setNavigationIcon(R.drawable.ic_close_black_24dp);
        intentSendMainActivity = new Intent(this, MainActivity.class);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentSendMainActivity);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_check: {
                if (isValidated()) {
                    // Chỉnh sửa đối tượng va Nhấn Check Icon để
                    String mName = edtName.getText().toString();
                    String mPhone = edtMobile.getText().toString();
                    contact.setmName(mName);
                    contact.setmMobile(mPhone);

                    System.out.println(contact);
                    intentSendMainActivity.putExtra("ContactEdit", contact);
                    intentSendMainActivity.putExtra("PosEdit", position);

                    startActivity(intentSendMainActivity);
                }
                break;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean isValidated() {
        String txt1 = edtName.getText().toString();
        String txt2 = edtMobile.getText().toString();
        return (txt1 != "" && txt2 != "");
    }
}
