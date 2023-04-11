package WM;

import java.util.ArrayList;
import java.util.List;

public class Sum extends Node {

    List<Node> args = new ArrayList<>();

    Sum(){}

    Sum(Node n1, Node n2){
        args.add(n1);
        args.add(n2);
    }


    Sum add(Node n){

        args.add(n);
        return this;
    }

    Sum add(double c){
        //if (c==0) return this;
        args.add(new Constant(c));
        return this;
    }

    Sum add(double c, Node n) {
        //if(c==0 || n.isZero()) return this;
        Node mul = new Prod(c,n);
        args.add(mul);
        return this;
    }

    @Override
    double evaluate() {
        double result =0;
        for (Node i : args){
            result+=i.evaluate();
        }
        return sign*result;
    }

    int getArgumentsCount(){return args.size();}

    public String toString(){
        if(isZero()) return "0";
        this.simplify();
        StringBuilder b =  new StringBuilder();
        if(sign<0)b.append("-(");
        boolean a= false;
        for ( Node i : args){
            if(i.isZero()) continue;
            if (a) b.append(" + ");
            a=true;
            b.append(i.toString());
        }

        if(sign<0)b.append(")");
        return b.toString();
    }


    @Override
    Node diff(Variable var) {
        Sum r = new Sum();
        for(Node n:args){
            if(!n.diff(var).isZero()) r.add(n.diff(var));
        }
        return r;
    }

    @Override
    boolean isZero() {
        for (Node i : args){
            if (i.isZero()==false) return false;
        }
        return true;
    }

    Sum simplify(){
        double d=0;
        for(int j=0; j<getArgumentsCount(); ){
            if(args.get(j).getClass().isAssignableFrom(Constant.class)){
                d+=args.get(j).evaluate();
                args.remove(j);
                continue;
            }else if (args.get(j).getClass().isAssignableFrom(Sum.class)){
                Sum a=(Sum) args.get(j);
                a.simplify();
                args.addAll(a.args);
                args.remove(j);
                continue;
            }else if (args.get(j).getClass().isAssignableFrom(Prod.class) ){
                Prod a=(Prod) args.get(j);
                a.simplify();
                args.remove(j);
                args.add(j, a);

            }
            j++;
        }
        if(d!=0) args.add(0, new Constant(d));
        return this;
    }


}
