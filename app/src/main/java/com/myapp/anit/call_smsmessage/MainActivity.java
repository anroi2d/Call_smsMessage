package com.myapp.anit.call_smsmessage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    EditText edtName, edtPhone;
    Button btnSave;
    ListView lvContact;
    //danh sach contact dua vao listview
    ArrayList<MyContact> arrContact = new ArrayList<>();
    ArrayAdapter<MyContact> adapter = null;
    MyContact mySelectContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doGetFormWidget();
        doAddEvent();
    }

    private void doAddEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSaveContact();
            }
        });

        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //luu vet contact duoc chon trong listview
                mySelectContact = arrContact.get(position);
                return false;
            }
        });
    }

    private void doSaveContact() {
        MyContact ct = new MyContact();
        ct.setName(edtName.getText() + "");
        ct.setPhone(edtPhone.getText() + "");
        arrContact.add(ct);
        adapter.notifyDataSetChanged();
    }

    private void doGetFormWidget() {
        btnSave = (Button) findViewById(R.id.btnSaveContact);
        edtName = (EditText) findViewById(R.id.editName);
        edtPhone = (EditText) findViewById(R.id.editPhone);
        lvContact = (ListView) findViewById(R.id.lvContact);
        //tao doi tuong adapter
        adapter = new ArrayAdapter<MyContact>(this, android.R.layout.simple_list_item_1, arrContact);
        //gan adapter vao listview
        lvContact.setAdapter(adapter);
        //thiet lap contextmenu cho listview
        registerForContextMenu(lvContact);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.phonecontextmenu, menu);
        menu.setHeaderTitle("Call- Sms");
        menu.getItem(0).setTitle("Call to " + mySelectContact.getPhone());
        menu.getItem(1).setTitle("Send sms to " + mySelectContact.getPhone());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuCall:
                domakeCall();
                break;
            case R.id.mnuSms:
                domakeSMS();
                break;
            case R.id.mnuRemove:
                arrContact.remove(mySelectContact);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void domakeSMS() {
        Intent i = new Intent(this, MySMSActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("CONTACT", mySelectContact);
        i.putExtra("DATA", b);
        startActivity(i);

    }

    private void domakeCall() {
        Uri uri = Uri.parse("tel:" + mySelectContact.getPhone());
        Intent i = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }
}
