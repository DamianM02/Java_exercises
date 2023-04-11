package WM;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Constant extends Node {
    double value;
    Constant(double value){
        this.sign = value<0?-1:1;
        this.value = value<0?-value:value;
    }


    @Override
    double evaluate() {
        return sign*value;
    }

    @Override
    public String toString() {
        if(isZero()) return "0";
        String sgn=sign<0?"-":"";
//        return sgn+Double.toString(value);
        DecimalFormat format = new DecimalFormat("0.#####",new DecimalFormatSymbols(Locale.US));
        return sgn+format.format(value);
    }

    @Override
    Node diff(Variable var) {
        return new Constant(0);
    }

    @Override
    boolean isZero(){
        if (value==0) return true;
        return false;
    }
}