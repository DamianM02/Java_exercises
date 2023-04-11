package WPGM;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdminUnit {
    String name="";
    int adminLevel=-1;
    double population=-1;
    double area=-1;
    double density=-1;
    AdminUnit parent;
    BoundingBox bbox = new BoundingBox();



    List<AdminUnit> children = new ArrayList<>();

    @Override
    public String toString(){

        StringBuilder b = new StringBuilder();
        b.append(name +" ");
        if (adminLevel!=-1)
            b.append(adminLevel+" ");
        else
            b.append("[adminLevel = null] ");

        if(population!=-1)
            b.append(population+" ");
        else
            b.append("[population = null] ");


        if(area!=-1)
            b.append(area+" ");
        else
            b.append("[area = null] ");

        if(density!=-1)
            b.append(density+" ");
        else
            b.append("[density = null] ");


        if(parent!=null)
            b.append(parent.name +" ");
        else
            b.append("[parent = null] ");

        b.append(bbox.toString());

        return b.toString();
    }


    AdminUnit fixMissingValues(){
        if(density==-1){
            if(parent!=null) {
                if (parent.density == -1) {
                    parent.fixMissingValues();

                }
                density=parent.density;
            }
        }

        if(population==-1){
            population=area*density;
        }

        return this;
    }
}
