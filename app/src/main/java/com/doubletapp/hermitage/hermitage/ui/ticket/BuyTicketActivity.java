package com.doubletapp.hermitage.hermitage.ui.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import com.doubletapp.hermitage.hermitage.R;
import com.doubletapp.hermitage.hermitage.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyTicketActivity extends AppCompatActivity {

    @BindView(R.id.buy_ticket_toolbar)
    Toolbar mToolbar;

    public static void start(@NonNull Context context) {
        Intent starter = new Intent(context, BuyTicketActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        ButterKnife.bind(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ActivityUtils.replaceFragment(getSupportFragmentManager(),
                R.id.fragment_container,
                BuyTicketFragment.newInstance(),
                BuyTicketFragment.TAG,
                false);
    }
}
