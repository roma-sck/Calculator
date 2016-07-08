package kultprosvet.com.calculator;

import android.content.Context;
import android.databinding.ObservableField;

public class Calculator {
    private static Calculator sCalcInstance;
    private String mCurrentValue;
    private String mHiddenValue;
    private boolean mCommaClicked;
    private Operations mOperation;
    private Context mContext;
    public final ObservableField<String> observableMiniDisplayResult = new ObservableField<>();
    public final ObservableField<String> observableResult = new ObservableField<>();

    private Calculator(Context context) {
        mCurrentValue = Const.EMPTY;
        mHiddenValue = Const.EMPTY;
        observableMiniDisplayResult.set(Const.EMPTY);
        observableResult.set(Const.EMPTY);
        mOperation = null;
        mContext = context;
    }

    public static Calculator getInstance(Context context) {
        if (sCalcInstance == null) {
            sCalcInstance = new Calculator(context);
        }
        return sCalcInstance;
    }

    public String getScreenResult() {
        return observableResult.get();
    }

    protected void numberClicked(int number){
        observableMiniDisplayResult.set(Const.EMPTY);
        if( number == Const.ZERO_VALUE
                && !mCommaClicked
                && mCurrentValue.length() == Const.ONE_VALUE
                && String.valueOf(mCurrentValue.charAt(Const.ZERO_VALUE)).equals(Const.ZERO)) {
            observableResult.set(mCurrentValue);
        } else {
            // entering integer numbers
            if (!mCommaClicked) {
                if(mCurrentValue.equals(Const.ZERO) || mCurrentValue.equals(Const.ZERO_DOUBLE)) {
                    // replace 0 or 0.0 to entered value
                    mCurrentValue = Const.EMPTY + number;
                } else {
                    mCurrentValue = mCurrentValue + number;
                }
                observableResult.set(mCurrentValue);
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
                observableResult.set(mCurrentValue);
            }
        }
    }

    protected void operatorClicked(String oper) {
        observableMiniDisplayResult.set(Const.EMPTY);
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
        mCurrentValue = Const.EMPTY;
        observableResult.set(Const.EMPTY);
        mCommaClicked = false;
    }

    protected void equalsClicked() {
        if( !mHiddenValue.equals(Const.EMPTY) && mOperation != null && !mCurrentValue.equals(Const.EMPTY)) {
            setMiniDisplayResult();
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
            observableResult.set(mCurrentValue);
            mCurrentValue = Const.EMPTY;
            mOperation = null;
            mCommaClicked = false;
        }
    }

    private void setMiniDisplayResult() {
        String strOperation = Const.EMPTY;
        switch (mOperation) {
            case MULTIPLY : strOperation = Const.OPER_MULT;
                break;
            case DELIM : strOperation = Const.OPER_DELIM;
                break;
            case PLUS : strOperation = Const.OPER_PLUS;
                break;
            case MINUS : strOperation = Const.OPER_MINUS;
                break;
        }
        observableMiniDisplayResult.set(mHiddenValue + Const.SPACE
                + strOperation + Const.SPACE + mCurrentValue  + Const.SPACE );
    }

    public String getMiniDisplayResult() {
        return observableMiniDisplayResult.get();
    }

    private void nanExceptionReport() {
        clearScreenClicked();
        MainActivity.showExceptionDialog(mContext);
    }

    /**
     * clear all fields
     */
    protected void clearScreenClicked()  {
        mCurrentValue = Const.EMPTY;
        mHiddenValue = Const.EMPTY;
        observableResult.set(Const.EMPTY);
        observableMiniDisplayResult.set(Const.EMPTY);
        mOperation = null;
        mCommaClicked = false;
    }

    /**
     * method changed the sign +/- of entered value
     */
    protected void toggleChanged() {
        observableMiniDisplayResult.set(Const.EMPTY);
        if(observableResult.get().length() != Const.ZERO_VALUE && !observableResult.get().equals(Const.ZERO)) {
            // after equals clicked, if change sign of result value
            mCurrentValue = observableResult.get();
        }
        if(mCurrentValue.length() != Const.ZERO_VALUE && !mCurrentValue.equals(Const.ZERO)) {
            if (String.valueOf(mCurrentValue.charAt(Const.ZERO_VALUE)).equals(Const.OPER_MINUS)) {
                mCurrentValue = mCurrentValue.substring(Const.ONE_VALUE, (mCurrentValue.length()));
            } else {
                mCurrentValue = Const.OPER_MINUS + mCurrentValue;
            }
            observableResult.set(mCurrentValue);
        }
    }

    protected void commaClicked() {
        observableMiniDisplayResult.set(Const.EMPTY);
        // it is possible to enter decimal
        mCommaClicked = true;
        if(observableResult.get().length() != Const.ZERO_VALUE && !observableResult.get().equals(Const.ZERO)) {
            // after equals clicked, if want to enter desimal after result
            mCurrentValue = observableResult.get();
        }
    }

    protected void deleteClicked() {
        observableMiniDisplayResult.set(Const.EMPTY);
        if ( observableResult.get().length() > Const.ZERO_VALUE && !observableResult.get().equals(Const.ZERO)
                || observableResult.get().length() > Const.ZERO_VALUE && !observableResult.get().equals(Const.EMPTY)) {
            // after equals clicked, if delete last number of result value
            mCurrentValue = observableResult.get();
        }
        if ( mCurrentValue.length() > Const.ZERO_VALUE && !mCurrentValue.equals(Const.ZERO)
                || mCurrentValue.length() > Const.ZERO_VALUE && !mCurrentValue.equals(Const.EMPTY)) {
            // delete last number
            mCurrentValue = mCurrentValue.substring(
                    Const.ZERO_VALUE, mCurrentValue.length()- Const.ONE_VALUE);
            observableResult.set(mCurrentValue);
            mHiddenValue = Const.EMPTY;
        }
    }
}
