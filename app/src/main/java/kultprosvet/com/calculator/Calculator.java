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
            case Const.DELETE:
                deleteClicked();
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
                && mCurrentValue.length() == Const.ONE_VALUE
                && String.valueOf(mCurrentValue.charAt(Const.ZERO_VALUE)).equals(Const.ZERO)) {
            mScreenResult = mCurrentValue;
        } else {
            // entering integer numbers
            if (!mCommaClicked) {
                if(mCurrentValue.equals(Const.ZERO) || mCurrentValue.equals(Const.ZERO_DOUBLE)) {
                    // replace 0 or 0.0 to entered value
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
                    if(mCurrentValue.equals(Const.EMPTY)) {
                        // if current value epty - add zero at first position (0.9 instead .9)
                        mCurrentValue = Const.ZERO + number;
                    } else {
                        mCurrentValue = mCurrentValue + number;
                    }
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
        if( !mCurrentValue.equals(Const.EMPTY)) {
            mHiddenValue = mCurrentValue;
        }
        mScreenResult = Const.EMPTY;
        mCommaClicked = false;
    }

    public void equalsClicked() {
        if( !mHiddenValue.equals(Const.EMPTY) && !mCurrentValue.equals(Const.EMPTY)) {
            double hiddenValDouble = Double.parseDouble(mHiddenValue);
            double currentValDouble = Double.parseDouble(mCurrentValue);

            if (mOperation == Operations.MULTIPLY) {
                mCurrentValue = String.valueOf(hiddenValDouble * currentValDouble);
            }
            if (mOperation == Operations.DELIM) {
                if (mCurrentValue.equals(Const.ZERO)) {
                    // division by zero
                    nanExceptionReport();
                } else {
                    mCurrentValue = String.valueOf(hiddenValDouble / currentValDouble);
                }
            }
            if (mOperation == Operations.PLUS) {
                mCurrentValue = String.valueOf(hiddenValDouble + currentValDouble);
            }
            if (mOperation == Operations.MINUS) {
                mCurrentValue = String.valueOf(hiddenValDouble - currentValDouble);
            }
            mHiddenValue = mCurrentValue;
            mScreenResult = mCurrentValue;
            mCurrentValue = Const.EMPTY;
            mOperation = null;
            mCommaClicked = false;
        }
    }

    private void nanExceptionReport() {
        clearScreenClicked();
        MainActivity.showExceptionDialog(mContext);
    }

    /**
     * method changed the sign +/- of entered value
     */
    public void toggleChanged() {
        if(mCurrentValue.length() != Const.ZERO_VALUE && !mCurrentValue.equals(Const.ZERO)) {
            if (String.valueOf(mCurrentValue.charAt(Const.ZERO_VALUE)).equals(Const.OPER_MINUS)) {
                mCurrentValue = mCurrentValue.substring(Const.ONE_VALUE, (mCurrentValue.length()));
            } else {
                mCurrentValue = Const.OPER_MINUS + mCurrentValue;
            }
            mScreenResult = mCurrentValue;
        }
    }

    private void commaClicked() {
        // it is possible to enter decimal
        mCommaClicked = true;
    }

    private void deleteClicked() {
        if ( mCurrentValue.length() > Const.ZERO_VALUE && !mCurrentValue.equals(Const.ZERO)
                || mCurrentValue.length() > Const.ZERO_VALUE && !mCurrentValue.equals(Const.EMPTY)) {
            // delete last number
            mCurrentValue = mCurrentValue.substring(
                    Const.ZERO_VALUE, mCurrentValue.length()- Const.ONE_VALUE);
            mScreenResult = mCurrentValue;
            mHiddenValue = Const.EMPTY;
        }
    }
}
