package com.limo.ezTelephone;

import static android.provider.CallLog.Calls.OUTGOING_TYPE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Date;

import android.util.Log;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class Page2 extends Fragment {
    private xAdapter mAdapter;
    private Boolean get_success = true;
    Intent intent_call = MainActivity.intent_call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new xAdapter(getActivity());

        Cursor cursor = MainActivity.cursor;
        Log.v("CALL2", Arrays.toString(cursor.getColumnNames())); // 項目名一覧
        Log.v("CALL2", "Num = " + cursor.getCount()); // 取得件数

        try {
            if (cursor.moveToFirst()) {
                do {
                    // 履歴種別
                    @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(("type")));
                    if (type == OUTGOING_TYPE) continue;
                    // 電話番号
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(("number")));
                    if (number.equals("")) continue;
                    // タイムスタンプ
                    @SuppressLint("Range") Date date = new Date(cursor.getLong(cursor.getColumnIndex("date")));
                    // 名前
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(("name")));


                    // アダプタに追加
                    mAdapter.add(new teleLog(type, name, number, date));

                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Toast.makeText(getActivity(),"履歴取得に失敗しました。",Toast.LENGTH_SHORT).show();
            get_success = false;
        }
    }

    @Override
    public void onViewCreated (@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView tv2 = view.findViewById(R.id.textView2);
        ListView lv = view.findViewById(R.id.rx_listview);

        if (get_success){
            tv2.setVisibility(View.GONE);
        }else{
            tv2.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }

        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener((parent, view1, position, id) -> {
            ListView listView = (ListView) parent;
            teleLog tl = (teleLog) listView.getItemAtPosition(position);
            startActivity(intent_call.setData(
                    Uri.parse("tel:"+tl.getNumber())
            ));
        });
        lv.setOnItemLongClickListener((parent, view2, position, id) -> {
            // new AdapterView.OnItemLongClickListener() {
            // public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            teleLog tl = (teleLog) listView.getItemAtPosition(position);
            Toast.makeText(getActivity(),"タップすると「"+tl.getNumber()+"」へダイアルします",Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page2, container, false);
    }
}
