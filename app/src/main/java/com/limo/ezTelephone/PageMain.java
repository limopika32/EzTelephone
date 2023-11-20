package com.limo.ezTelephone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import java.util.Optional;

public class PageMain extends Fragment {
    private final Button[] btn = new Button[12];
    private final int[] Rids = {
            R.id.button_1,R.id.button_2,R.id.button_3,
            R.id.button_4,R.id.button_5,R.id.button_6,
            R.id.button_7,R.id.button_8,R.id.button_9,
            R.id.button_A,R.id.button_0,R.id.button_B};
    private TextView call_addr;
    private final Handler guiThreadHandler = new Handler();
    final String latest_call = MainActivity.latest_call;
    Intent intent_call = MainActivity.intent_call;

    public PageMain() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("IntentReset")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        call_addr = view.findViewById(R.id.textView);
        call_addr.setMovementMethod(new ScrollingMovementMethod());

        // calling button
        Button btn_call = view.findViewById(R.id.button_call);
        // new View.OnClickListener() { public void onClick(View v) { ...
        btn_call.setOnClickListener(v -> {
            if ( ( ""+call_addr.getText() ).equals("") ){
                Toast.makeText(getActivity(),"ダイアルできません\n電話番号を入力してください",Toast.LENGTH_SHORT).show();
            } else {
                startActivity(intent_call.setData(
                        Uri.parse("tel:" + call_addr.getText())
                ));
            }
        });
        // redial
        btn_call.setOnLongClickListener(v -> {
            Toast.makeText(getActivity(),"「"+latest_call+"」へリダイアルします",Toast.LENGTH_SHORT).show();
            startActivity(intent_call.setData(
                    Uri.parse("tel:"+latest_call)
            ));
            // true -> onLongClick only, false -> onLongClick + onClick
            return true;
        });

        // delete button
        Button btn_del = view.findViewById(R.id.button_del);
        btn_del.setOnClickListener(v -> {
            String ad = "" + call_addr.getText();
            updateView(call_addr,""+
                    Optional.of(ad)
                            .filter(s -> s.length() != 0)
                            .map(s -> s.substring(0, s.length() - 1))
                            .orElse(ad)
            );
        });
        // all delete
        btn_del.setOnLongClickListener(v -> {
            updateView(call_addr,"");
            // true -> onLongClick only, false -> onLongClick + onClick
            return true;
        });

        // address book
        Button btn_adr = view.findViewById(R.id.button_address);
        btn_adr.setOnClickListener(v -> {
            startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"))
            );
            getActivity().finish();
        });

        // numpad buttons
        for (int i = 0; i < Rids.length; i++){
            btn[i] = view.findViewById(Rids[i]);
            final String tmp = "" + btn[i].getText();
            btn[i].setOnClickListener(v -> updateView(call_addr,""+call_addr.getText()+tmp));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pagemain, container, false);
    }

    private void updateView(TextView tv, final String tx){
        guiThreadHandler.post(() -> tv.setText(tx));
    }
}