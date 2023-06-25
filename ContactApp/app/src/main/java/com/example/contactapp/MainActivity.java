package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtName, edtNumber;
    Button btnInsert, btnDelete, btnUpdate, btnQuery ;
    String DB_PATH_SUFFIX = "/databases/"; //Thư mục chứa csdl trong đt <Mặc định>
    SQLiteDatabase database=null;//Tên csdl
    String DATABASE_NAME="contact.db"; //Tên file csdl
    //Khai báo listView
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    //Test
    ArrayList<String> dsTinhThanh=new ArrayList<>();
    private void addControls() {

        lv=findViewById(R.id.lv);
//        dsTinhThanh.addAll(Arrays.asList(getResources().getStringArray()));
        myadapter=new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,dsTinhThanh
        );
        lv.setAdapter(myadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //La
        MenuItem menuItem=menu.findItem(R.id.mnu_cart);
        //Lay search view ra
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            //Tim kiem toi dau su dung toi do
            @Override
            public boolean onQueryTextChange(String s) {
                myadapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);

        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);
        database=openOrCreateDatabase("contact.db",MODE_PRIVATE, null);
        // Tạo các tham số cho ListView
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        processCopy();//Gọi hàm cop csdl từ thư mục assets vào thư mục database trong bộ nhớ đt
         //Truy vấn CSDL và cập nhật hiển thị lên Listview
        database=openOrCreateDatabase("contact.db",MODE_PRIVATE, null);
        Cursor c = database.query("dbcontact", null,
                null, null,null, null, null);
        String data = "";
        c.moveToFirst();//Di chuyển con trỏ về bản ghi đầu tiên
        while (c.isAfterLast() == false) // Nếu không phải là bản ghi cuối cùng
        {
            data = c.getString(1) + "\n" + c.getString(0);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                insert();

                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();
                ContentValues myValues = new ContentValues();
                myValues.put("number", number);
                myValues.put("name", name);

                String msg = "";
                if (database.insert("dbcontact", null , myValues) ==  -1){
                    msg = "Thêm thất bại!";
                } else {
                    msg = "Bạn đã thêm thành công!";
                }
                Toast.makeText(MainActivity.this,msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                int n = database.delete("dbcontact", "name = ?", new String[]{name});
                String msg = "";
                if (n == 0){
                    msg = "Không có bản ghi nào bị xóa";
                }else {
                    msg = n + "Xóa thành công!";
                }
                Toast.makeText(MainActivity.this, msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();
                ContentValues myValues = new ContentValues();
                myValues.put("number", number);
                myValues.put("name", name);
                int n = database.update("dbcontact",myValues,"name = ?", new String[]{name});
                String msg = "";
                if (n == 0){
                    msg = "Không có bản ghi nào được cập nhật.";
                } else {
                    msg = n + " bản ghi đã được cập nhật";
                }
                Toast.makeText(MainActivity.this, msg ,Toast.LENGTH_SHORT).show();
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist.clear();
                processCopy();//Gọi hàm cop csdl từ thư mục assets vào thư mục database trong bộ nhớ đt
                //Truy vấn CSDL và cập nhật hiển thị lên Listview
                database=openOrCreateDatabase("contact.db",MODE_PRIVATE, null);
                Cursor c = database.query("dbcontact", null,
                        null, null,null, null, null);
                String data = "";
                c.moveToFirst();//Di chuyển con trỏ về bản ghi đầu tiên
                while (c.isAfterLast() == false) // Nếu không phải là bản ghi cuối cùng
                {
                    data = c.getString(1) + "\n" + c.getString(0);
                    mylist.add(data);
                    c.moveToNext();
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                String[] values = item.split("\n");
                Intent intent = new Intent(MainActivity.this, Info.class);
                //Lấy dữ liệu
                String name = values[0];
                String number = values[1];
                //Đóng gói vào bundle
                Bundle mybundle = new Bundle();//đối tượng dùng để chứa dữ liệu trước khi đưa vào intent
                //Đưa dữ liệu vào Bundle
                mybundle.putString("name", name);
                mybundle.putString("number", number);
                //Đưa Bundle vào Intent
                intent.putExtra("mybundle", mybundle);
                //Khởi động intent
                startActivity(intent);
            }
        });
    }

    //Hàm copy csdl Sqlite từ thư mục assets vào thư mục cài đặt ứng dụng
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {

        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
