package com.example.cuoiki2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuoiki2.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MYREQUESTCODE = 10;
    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnAddUser;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private List<User> listUser;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();

        userAdapter = new UserAdapter(new UserAdapter.IClickItem() {
            @Override
            public void UpdateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void DeleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        listUser=new ArrayList<>();
        userAdapter.setData(listUser);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    //logic search
                    searchingUser();
                }
                return false;
            }
        });

    loadData();
    }

    private void initUi(){
        edtUsername=findViewById(R.id.edt_username);
        edtAddress=findViewById(R.id.edt_address);
        btnAddUser=findViewById(R.id.btn_add_user);
        rcvUser=findViewById(R.id.rcv_user);
        edtSearch=findViewById(R.id.edt_search);
    }
    private void addUser(){
        String strUsername= edtUsername.getText().toString().trim();
        String strAddress= edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        User user = new User(strUsername,strAddress);

        if(isUserExist(user)){
            Toast.makeText(this,"already exist",Toast.LENGTH_SHORT).show();
            return;
        }
        UserDatabase.getInstance(this).userDAO().insertUSer(user);
        Toast.makeText(this,"thanh cong",Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");
        hideSoftKeyboard();

        loadData();
    }
    public void hideSoftKeyboard(){
        try {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    private void loadData(){
        listUser=UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(listUser);
    }
    private boolean isUserExist(User user){
        List<User> list= UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list != null && !list.isEmpty();
    }
    private void clickUpdateUser(User user){
        Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objectUser",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,MYREQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MYREQUESTCODE && resultCode== Activity.RESULT_OK){
            loadData();
        }
    }

    private void clickDeleteUser(User user){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("You sure ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(MainActivity.this,"Delete successfully",Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("no", null)
                .show();
    }

    private void searchingUser(){
        String strKeyword= edtSearch.getText().toString().trim();
        listUser= new ArrayList<>();
        listUser= UserDatabase.getInstance(this).userDAO().searchUser(strKeyword);
        userAdapter.setData(listUser);
        hideSoftKeyboard();
    }
}