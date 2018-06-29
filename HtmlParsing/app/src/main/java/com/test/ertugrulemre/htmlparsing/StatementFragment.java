package com.test.ertugrulemre.htmlparsing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementFragment extends Fragment {

    private TextView txt_Statement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_statement, container, false);
        // Inflate the layout for this fragment
        txt_Statement=(TextView) v.findViewById(R.id.text_Statement);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(DataEvent event) {
        txt_Statement.setText("Statement:"+" "+event.getStatement()+"\n\n\n"+"Address:"+" "+event.getAddress());

    }
}