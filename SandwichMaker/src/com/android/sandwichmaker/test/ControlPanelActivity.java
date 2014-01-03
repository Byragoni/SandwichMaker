package com.android.sandwichmaker.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.sandwichmaker.MainActivity;
import com.android.sandwichmaker.R;
import com.android.sandwichmaker.data.InfoFromData;
import com.android.sandwichmaker.data.ReadableDataFormat;

/**
 * Created by uttamb on 12/6/13.
 */
public class ControlPanelActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_panel);

      findViewById(R.id.reports).setOnClickListener(this);
      findViewById(R.id.phi).setOnClickListener(this);
      findViewById(R.id.general_app).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.reports:
              ReadableDataFormat rdf =  new ReadableDataFormat(ControlPanelActivity.this);

                rdf.generateReports();
                break;
            case R.id.phi:
                InfoFromData ifd = new InfoFromData(ControlPanelActivity.this);
                ifd.updateLocationInfo();
                break;

            case R.id.general_app:
                Intent intent = new Intent().setClass(this, MainActivity.class);
                startActivity(intent);
                break;
        }


    }
}
