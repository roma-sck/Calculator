package kultprosvet.com.calculator;

import android.content.Context;

public class Calculator {
    private static Calculator sCalcInstance;
    private double mCurrentValue;
    private double mHiddenValue;
    private double mScreenResult;
    private boolean mCommaClicked;
    private Operations mOperation;
    private Context mContext;

    private Calculator(Context context) {
        mCurrentValue = Const.ZERO_VALUE;
        mHiddenValue = Const.ZERO_VALUE;
        mOperation = null;
        mScreenResult = Const.ZERO_VALUE;
        mContext = context;
    }

    public static Calculator getInstance(Context context) {
        if (sCalcInstance == null) {
            sCalcInstance = new Calculator(context);
        }
        return sCalcInstance;
    }

    public void calculate(String btnText) {
        switch (btnText) {
            case Const.ONE:
            case Const.TWO:
            case Const.THREE:
            case Const.FOUR:
            case Const.FIVE:
            case Const.SIX:
            case Const.SEVEN:
            case Const.EIGHT:
            case Const.NINE:
            case Const.ZERO:
                numberClicked(Integer.parseInt(btnText));
                break;
            case Const.COMMA:
                commaClicked();
                break;
            case Const.OPER_MULT:
            case Const.OPER_DELIM:
            case Const.OPER_PLUS:
            case Const.OPER_MINUS:
                operatorClicked(btnText);
                break;
            case Const.EQUALS:
                equalsClicked();
                break;
            case Const.CLEAR:
                clearScreenClicked();
                break;
            case Const.TOGGLE:
                toggleChanged();
                break;
        }
    }

    public double getScreenResult() {
        return mScreenResult;
    }

    public void setCurrentValue(double value) {
        mCurrentValue = value;
    }

    private void numberClicked(int number){
        if( !mCommaClicked) {
            mCurrentValue = mCurrentValue * Const.DOUBLE_VAL_COEFF + number;
            mScreenResult = mCurrentValue;
        } else {
            String currentStr = mCurrentValue + "";
            String resultStr;
            if(String.valueOf(currentStr.charAt(currentStr.length() - 1)).equals(Const.ZERO)) {
                resultStr = currentStr.substring(0, currentStr.length() - 2) + Const.COMMA + number;
            } else {
                resultStr = currentStr + number;
            }
            mCurrentValue = Double.parseDouble(resultStr);
            mScreenResult = mCurrentValue;
        }
    }

    /**
     * clear all fields
     */
    public void clearScreenClicked()  {
        mCurrentValue = Const.ZERO_VALUE;
        mHiddenValue = Const.ZERO_VALUE;
        mOperation = null;
        mScreenResult = Const.ZERO_VALUE;
        mCommaClicked = false;
    }

    public void operatorClicked(String oper) {
        switch (oper) {
            case Const.OPER_MULT: mOperation = Operations.MULTIPLY;
                break;
            case Const.OPER_DELIM: mOperation = Operations.DELIM;
                break;
            case Const.OPER_PLUS: mOperation = Operations.PLUS;
                break;
            case Const.OPER_MINUS: mOperation = Operations.MINUS;
                break;
        }
        mHiddenValue = mCurrentValue;
        mCurrentValue = Const.ZERO_VALUE;
        mScreenResult = Const.ZERO_VALUE;
        mCommaClicked = false;
    }

    public void equalsClicked() {
        if (mOperation == Operations.MULTIPLY){
            mCurrentValue = mHiddenValue * mCurrentValue;
        }
        if (mOperation == Operations.DELIM){
            if (mCurrentValue == Const.ZERO_VALUE) {
                // division by zero
                nanExeptionReport();
            } else {
                mCurrentValue = mHiddenValue / mCurrentValue;
            }
        }
        if (mOperation == Operations.PLUS){
            mCurrentValue = mHiddenValue + mCurrentValue;
        }
        if (mOperation == Operations.MINUS){
            mCurrentValue = mHiddenValue - mCurrentValue;
        }
        mHiddenValue = mCurrentValue;
        mScreenResult = mCurrentValue;
        mOperation = null;
        mCommaClicked = false;
    }

    private void nanExeptionReport() {
        clearScreenClicked();
        MainActivity.showExceptionDialog(mContext);
    }

    /**
     * method changed the sign +/- of entered value
     */
    public void toggleChanged() {
        String toggledValue = String.valueOf(mCurrentValue);
        if(String.valueOf(toggledValue.charAt(0)).equals(Const.OPER_MINUS)) {
            mCurrentValue = Double.parseDouble(toggledValue.substring(1, (toggledValue.length() - 1)) );
        } else {
            mCurrentValue = Double.parseDouble(Const.OPER_MINUS + toggledValue);
        }
        mScreenResult = mCurrentValue;
    }

    private void commaClicked() {
        // it is possible to enter decimal
        mCommaClicked = true;
    }
}
