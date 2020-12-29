package com.example.cuoiki2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cuoiki2.database.UserDatabase;

public class UpdateActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnUpdateUser;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtUsername=findViewById(R.id.edt_username);
        edtAddress=findViewById(R.id.edt_address);
        btnUpdateUser=findViewById(R.id.btn_update_user);

        user = (User) getIntent().getExtras().get("objectUser");

        if (user!= null){
            edtUsername.setText(user.getUsername());
            edtAddress.setText(user.getAddress());
        }
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String strUsername= edtUsername.getText().toString().trim();
        String strAddress= edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }
        //update user
        user.setUsername(strUsername);
        user.setAddress(strAddress);

        UserDatabase.getInstance(this).userDAO().updateUser(user);

        Toast.makeText(this,"update successfully",Toast.LENGTH_SHORT).show();

        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK,intentResult);
        finish();
    }
}