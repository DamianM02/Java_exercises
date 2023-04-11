package WM;

import com.sun.tools.jconsole.JConsoleContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Prod extends Node {
    List<Node> args = new ArrayList<>();

    Prod(){}

    Prod(Node n1){
        args.add(n1);
    }
    Prod(double c){
        args.add(new Constant(c));
    }

    Prod(Node n1, Node n2){

        args.add(n1);
        args.add(n2);

    }
    Prod(double c, Node n){


        args.add(new Constant(c));
        args.add(n);


    }



    Prod mul(Node n){

        args.add(n);
        return this;
    }

    Prod mul(double c){
        args.add(new Constant(c));

        return this;
    }


    @Override
    double evaluate() {
        double result =1;
        for ( Node i : args){
            result*= i.evaluate();
        }
        // oblicz iloczyn czynników wołąjąc ich metodę evaluate
        return sign*result;
    }
    int getArgumentsCount(){return args.size();}



    @Override
    public String toString(){
        if(isZero()){

            return "0";
        }

        this.simplify();
        StringBuilder b =  new StringBuilder();

        if(sign<0)b.append("-");
        boolean a= false;
        for ( Node i : args){
            if(i.isZero()) continue;
            if (a) b.append("*");
            a=true;
            if (i.getClass().isAssignableFrom(Sum.class) || i.getSign()<0){
                b.append("(");
                b.append(i.toString());
                b.append(")");
            }else{
                b.append(i.toString());
            }

        }

        return b.toString();
    }

    Node diff(Variable var) {
        Sum r = new Sum();
        for(int i=0;i<args.size();i++){
            Prod m= new Prod();
            for(int j=0;j<args.size();j++){
                Node f = args.get(j);
                if(j==i)m.mul(f.diff(var));
                else m.mul(f);
            }
            if(!m.isZero()) r.add(m);
        }
        return r;
    }

    @Override
    boolean isZero() {
        for (Node i : args) {
            if (i.isZero()) return true;
        }
        return false;
    }

    Prod simplify(){
        double d=1;
        for (int j=0; j<getArgumentsCount(); ){
            if(args.get(j).getClass().isAssignableFrom(Constant.class)){
                d*=((Constant) args.get(j) ).value * args.get(j).sign;
                args.remove(j);
                continue;
            }else if(args.get(j).getClass().isAssignableFrom(Prod.class)){
                Prod a=(Prod) args.get(j);
                a.simplify();
                args.addAll(a.args);

                args.remove(j);
                continue;
            }else if(args.get(j).getClass().isAssignableFrom(Sum.class) ){

                Sum a=(Sum) args.get(j);
                a.simplify();
                args.remove(j);
                args.add(j, a);

            }
            j++;
        }

        if(d!=1){
            args.add(0, new Constant(d));
        }
        return this;

    }



}
