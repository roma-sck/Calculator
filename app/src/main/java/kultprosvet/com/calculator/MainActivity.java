package kultprosvet.com.calculator;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button1) Button mButton1;
    @BindView(R.id.button2) Button mButton2;
    @BindView(R.id.button3) Button mButton3;
    @BindView(R.id.button4) Button mButton4;
    @BindView(R.id.button5) Button mButton5;
    @BindView(R.id.button6) Button mButton6;
    @BindView(R.id.button7) Button mButton7;
    @BindView(R.id.button8) Button mButton8;
    @BindView(R.id.button9) Button mButton9;
    @BindView(R.id.button0) Button mButton0;
    @BindView(R.id.result_screen) TextView mScreenView;
    private final static String SAVED_VALUE_KEY = "double_value";
    private String mDisplayedValue;

    private Calculator mCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null){
            mDisplayedValue = savedInstanceState.getString(SAVED_VALUE_KEY);

            mScreenView.setText(String.valueOf(mDisplayedValue));
        }
        mCalc = Calculator.getInstance(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_VALUE_KEY, mDisplayedValue);
    }

    /**
     * set onClick for all buttons
     * @param btn clicked button
     */
    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9, R.id.button0, R.id.button_equals,
            R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_delim,
            R.id.button_clear, R.id.button_delete, R.id.button_toggle, R.id.button_comma})
    public void onButtonClick(Button btn) {
        String btnText = btn.getText().toString();
        if(mDisplayedValue != null) {
            // set current value after screen rotation
            mCalc.setCurrentValue(mDisplayedValue);
        }
        mCalc.calculate(btnText);
        // get calculated result and shows it on screen
        mDisplayedValue = mCalc.getScreenResult();
        setScreenView(mDisplayedValue);
    }

    private void setScreenView(String value) {
        if(value.equals(Const.EMPTY)) {
            mScreenView.setText(String.valueOf(Const.ZERO));
        } else {
            mScreenView.setText(String.valueOf(value));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                showAboutDialog();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_about)
                .setMessage(R.string.dialog_msg_about)
                .create()
                .show();
    }

    protected static void showExceptionDialog(Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.nan_exception_dialog_title)
                .setMessage(R.string.nan_exception_dialog_message)
                .setPositiveButton(R.string.nan_exception_dialog_ok_text, null)
                .show();
    }
}
