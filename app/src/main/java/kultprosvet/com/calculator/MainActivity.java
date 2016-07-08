package kultprosvet.com.calculator;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import kultprosvet.com.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Calculator mCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mCalc = Calculator.getInstance(this);
        binding.setCalc(mCalc);
        binding.setActivity(this);

//        if (savedInstanceState != null){
//            mDisplayedValue = savedInstanceState.getString(Const.SAVED_VALUE_KEY);
//            setScreenView(mDisplayedValue);
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Const.SAVED_VALUE_KEY, mCalc.getScreenResult());
    }

    /**
     * set onClick for all buttons
     * @param view - clicked button
     */
    public void onButtonClick(View view) {
        String btnText = ((Button)view).getText().toString();
        mCalc.calculate(btnText);
    }

//    private void setScreenView(String value) {
//        if(value == null || value.equals(Const.EMPTY)) {
//            mScreenView.setText(String.valueOf(Const.ZERO));
//        } else {
//            mScreenView.setText(String.valueOf(value));
//        }
//    }

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
