package kultprosvet.com.calculator;

import android.content.Context;

public class Calculator {
    private static Calculator sCalcInstance;
    private String mCurrentValue;
    private String mHiddenValue;
    private String mScreenResult;

    private boolean mCommaClicked;
    private Operations mOperation;
    private Context mContext;

    private Calculator(Context context) {
        mCurrentValue = Const.EMPTY;
        mHiddenValue = Const.EMPTY;
        mScreenResult = Const.EMPTY;

        mOperation = null;
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

    public String getScreenResult() {
        return mScreenResult;
    }

    public void setCurrentValue(String value) {
        mCurrentValue = value;
    }

    private void numberClicked(int number){
        if( number == Const.ZERO_VALUE
                && !mCommaClicked
                && mCurrentValue.length() == 1
                && String.valueOf(mCurrentValue.charAt(0)).equals(Const.ZERO)) {
            mScreenResult = mCurrentValue;
        } else {
            // entering integer numbers
            if (!mCommaClicked) {
                if(mCurrentValue.equals(Const.ZERO)) {
                    // replace 0 to entered value
                    mCurrentValue = Const.EMPTY + number;
                } else {
                    mCurrentValue = mCurrentValue + number;
                }
                mScreenResult = mCurrentValue;
            } else {
                // entering decimal numbers
                if ( !mCurrentValue.contains(Const.COMMA)) {
                    mCurrentValue = mCurrentValue + Const.COMMA + number;
                } else {
                    mCurrentValue = mCurrentValue + number;
                }
                mScreenResult = mCurrentValue;
            }
        }
    }

    /**
     * clear all fields
     */
    public void clearScreenClicked()  {
        mCurrentValue = Const.EMPTY;
        mHiddenValue = Const.EMPTY;
        mScreenResult = Const.EMPTY;
        mOperation = null;
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
        if( !mHiddenValue.equals(Const.EMPTY) && !mCurrentValue.equals(Const.EMPTY)) {
            equalsClicked();
            mHiddenValue = Const.EMPTY;
            mCurrentValue = Const.EMPTY;
            return;
        }
        mHiddenValue = mCurrentValue;
        mCurrentValue = Const.EMPTY;
        mScreenResult = Const.EMPTY;
        mCommaClicked = false;
    }

    public void equalsClicked() {
        double hiddenValDouble = Double.parseDouble(mHiddenValue);
        double currentValDouble = Double.parseDouble(mCurrentValue);

        if (mOperation == Operations.MULTIPLY){
            mCurrentValue = String.valueOf( hiddenValDouble * currentValDouble);
        }
        if (mOperation == Operations.DELIM){
            if (mCurrentValue.equals(Const.ZERO)) {
                // division by zero
                nanExeptionReport();
            } else {
                mCurrentValue = String.valueOf( hiddenValDouble / currentValDouble);
            }
        }
        if (mOperation == Operations.PLUS){
            mCurrentValue = String.valueOf( hiddenValDouble + currentValDouble);
        }
        if (mOperation == Operations.MINUS){
            mCurrentValue = String.valueOf( hiddenValDouble - currentValDouble);
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
        if(String.valueOf(mCurrentValue.charAt(0)).equals(Const.OPER_MINUS)) {
            mCurrentValue = mCurrentValue.substring(1, (mCurrentValue.length() - 1));
        } else {
            mCurrentValue = Const.OPER_MINUS + mCurrentValue;
        }
        mScreenResult = mCurrentValue;
    }

    private void commaClicked() {
        // it is possible to enter decimal
        mCommaClicked = true;
    }
}
