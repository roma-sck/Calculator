package kultprosvet.com.calculator;

import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView screenView;
    double currentValue=0;
    double hiddenValue=0;
    Operations operation;
    public enum Operations {PLUS,MINUS,DELIM,MULTIPLY}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","created");
        Button button1= (Button) findViewById(R.id.button1);
        Button button2= (Button) findViewById(R.id.button2);
        Button button3= (Button) findViewById(R.id.button3);
        Button button4= (Button) findViewById(R.id.button4);
        Button button5= (Button) findViewById(R.id.button5);
        Button button6= (Button) findViewById(R.id.button6);
        Button button7= (Button) findViewById(R.id.button7);
        Button button8= (Button) findViewById(R.id.button8);
        Button button9= (Button) findViewById(R.id.button9);
        Button button0= (Button) findViewById(R.id.button0);
        screenView=(TextView)findViewById(R.id.screen);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(5);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(6);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(7);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(8);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(9);
            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberClick(0);
            }
        });

        if (savedInstanceState!=null){
            Log.d("MainActivity","restored");
            currentValue=savedInstanceState.getDouble("value");
            screenView.setText(String.valueOf(currentValue));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","Activity started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","Activity stoped");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","paused");
    }

    private void numberClick(int number){
        currentValue=currentValue*10+number;
        screenView.setText(String.valueOf(currentValue));
    }

    public void onClickReset(View v){
        Log.d("MainActivity","Reset is pressed");
        new AlertDialog.Builder(this)
                .setTitle("INFO")
                .setMessage("Reser is done")
                .setPositiveButton("OK",null)
                .show();



        screenView.setText("0");
        currentValue=0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("MainActivity","saved instance state");
        super.onSaveInstanceState(outState);
        outState.putDouble("value",currentValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","destroyed");
    }

    public void onPlusClicked(View view) {
        if (hiddenValue!=0){
            onClickEquals(null);
            return;
        }
        hiddenValue=currentValue;
        currentValue=0;
        screenView.setText("0");
        operation=Operations.PLUS;
    }

    public void onClickEquals(View view) {
        if (operation==Operations.PLUS){
            currentValue=hiddenValue+currentValue;
        }
        hiddenValue=0;
        screenView.setText(String.valueOf(currentValue));

    }
}
