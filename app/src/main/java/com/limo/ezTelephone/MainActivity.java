package com.limo.ezTelephone;

import static android.provider.CallLog.Calls.OUTGOING_TYPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CONTACTS = 123;
    private static final int REQUEST_CODE_READ_CALL_LOG = 122;
    protected static Cursor cursor;
    protected static String latest_call = null;
    protected static Intent intent_call = new Intent()
            .setAction(Intent.ACTION_DIAL)
            .setData(Uri.parse(""));

    private String TabName(int pos){
        switch (pos){
            case 0:
                return "ダイヤル";
            case 1:
                return "着信履歴";
            case 2:
                return "発信履歴";
        }
        return "PAGE"+(pos+1);
    }


    /**
     * ユーザ選択の結果を受けるコールバックメソッド
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("MainActivity", "API Level = " + Build.VERSION.SDK_INT + ": requestCode = " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // パーミッションの利用が許可されている

                } else {
                    // パーミッションの利用が許可されていない
                    Toast.makeText(this, "連絡先の利用が許可されていません", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_CODE_READ_CALL_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // パーミッションの利用が許可されている

                } else {
                    // パーミッションの利用が許可されていない
                    Toast.makeText(this, "通話履歴の利用が許可されていません", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void getPermission(){
        // パーミッションがアプリに付与されているか確認する
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // deny
            Log.v("MainActivity", "API Level = " + Build.VERSION.SDK_INT + ": パーミッションが付与されていない");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            Log.v("MainActivity", "API Level = " + Build.VERSION.SDK_INT + ": パーミッションが付与されている");
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.v("MainActivity", "API Level = " + Build.VERSION.SDK_INT + ": パーミッションが付与されていない");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CODE_READ_CALL_LOG);
        } else {
            Log.v("MainActivity", "API Level = " + Build.VERSION.SDK_INT + ": パーミッションが付与されている");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewPagerをアダプタに関連付ける
        ViewPager2 pager = findViewById(R.id.pager);
        TapPagerAdapter adapter = new TapPagerAdapter(this);
        pager.setAdapter(adapter);
        // TabLayoutとViewPagerを関連付ける
        TabLayout tabs = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText(TabName(position))
        ).attach();

        getPermission();

        // 通話ログの取得
        ContentResolver resolver = this.getContentResolver();
        cursor = resolver.query(
                CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER
        );

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 履歴種別
                    @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(("type")));
                    if (type != OUTGOING_TYPE) continue;

                    // 電話番号
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(("number")));
                    if (!number.equals("")){
                        latest_call = number;
                        break;
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            latest_call = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!cursor.isClosed()) cursor.close();
    }
}