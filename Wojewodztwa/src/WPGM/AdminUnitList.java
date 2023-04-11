package WPGM;

import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.function.Predicate;


public class AdminUnitList {
    List<AdminUnit> units = new ArrayList<>();






    /**
     * Czyta rekordy pliku i dodaje do listy
     * @param filename nazwa pliku
     */

    public void read(String filename) throws Exception {

        CSVReader a = new CSVReader(filename, ",", true);
        Map<Long, AdminUnit> mapa = new HashMap<>();
        Map<AdminUnit, Long> mapa1=new HashMap<>();

        //int i=0;
        while(a.next() /*&& i<10000*/){
            AdminUnit b= new AdminUnit();
            b.name=a.get("name");
            b.adminLevel=a.getInt("admin_level");
            b.area=a.getDouble("area");

            //b.density=a.getDouble("density");
            //-------------------------------------------
            b.population=a.getDouble("population");
            b.density=a.getDouble("density");


            //----------------------------------------------


            for (int j=1; j<5; j++){
                if(!a.isMissing("x" + j) && !a.isMissing("y"+1))
                    b.bbox.addPoint(a.getDouble("x" + j), a.getDouble("y"+j));

            }


            units.add(b);
            //i++;
            //System.out.println(b.density);
            mapa.put(a.getLong(0), b);
            if (!a.get(1).equals(""))
                mapa1.put(b, a.getLong(1));

        }

        for( AdminUnit j: units){
            j.parent=mapa.get(mapa1.get(j));
            if(j.parent!=null) j.parent.children.add(j);

        }

        for (AdminUnit j:units) {
            j.fixMissingValues();
        }

    }






    /**
     * Wypisuje zawartość korzystając z AdminUnit.toString()
     * @param out
     */
    void list(PrintStream out) throws FileNotFoundException, UnsupportedEncodingException {

        for (AdminUnit i: units ){
            out.println( i.toString() );
        }
    }
    /**
     * Wypisuje co najwyżej limit elementów począwszy od elementu o indeksie offset
     * @param out - strumień wyjsciowy
     * @param offset - od którego elementu rozpocząć wypisywanie
     * @param limit - ile (maksymalnie) elementów wypisać
     */
    void list(PrintStream out,int offset, int limit ) throws Exception {
        if (offset>= units.size()) throw new Exception("Zly indeks");

        for (int i= offset; i<offset+limit && i<units.size(); i++){
            out.println(units.get(i).toString() );
        }

    }

    /**
     * Zwraca nową listę zawierającą te obiekty AdminUnit, których nazwa pasuje do wzorca
     * @param pattern - wzorzec dla nazwy
     * @param regex - jeśli regex=true, użyj finkcji String matches(); jeśli false użyj funkcji contains()
     * @return podzbiór elementów, których nazwy spełniają kryterium wyboru
     */
    AdminUnitList selectByName(String pattern, boolean regex){
        AdminUnitList ret = new AdminUnitList();
        // przeiteruj po zawartości units
        // jeżeli nazwa jednostki pasuje do wzorca dodaj do ret

        if (regex){
            for (AdminUnit i : units){
                if(i.name.matches(pattern)) ret.units.add(i);
            }
        }else{
            for (AdminUnit i : units){
                if(i.name.contains(pattern)) ret.units.add(i);
            }

        }
        


        return ret;
    }

    //---------------------------------------------------------------------
    AdminUnitList getNeighbors(AdminUnit unit, double maxdistance)  {
        AdminUnitList a = new AdminUnitList();


        if(unit.adminLevel<=7){
            for (AdminUnit i : units){
                if(i!=unit && unit.adminLevel==i.adminLevel && unit.bbox.intersects(i.bbox)){
                    a.units.add(i);
                }
            }
        }else {
            for (AdminUnit i : units) {
                if (i != unit && unit.adminLevel == i.adminLevel && unit.bbox.distanceTo(i.bbox) < maxdistance) {
                    a.units.add(i);
                }
            }

        }
            ///////////////////////////////////\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\




        return a;
    }

    AdminUnitList getNeighbors2(AdminUnit unit, double maxdistance)  {



        AdminUnit Polska = new AdminUnit();
        Polska.parent=null;

        for (AdminUnit i : units) {
            if (i.adminLevel == 4) {
                Polska.children.add(i);
                i.parent = Polska;
            }
        }


        AdminUnitList b=new AdminUnitList();

        if(unit.parent==null) return b;
        for( AdminUnit i : getNeighbors(unit.parent, maxdistance).units) {
            if(i.adminLevel==unit.adminLevel){

                if(unit.adminLevel<=7 && unit.bbox.intersects(i.bbox)){
                    b.units.add(i);
                }else if(unit.bbox.distanceTo(i.bbox)<=maxdistance){
                    b.units.add(i);
                }

            }else{

            }


        }


        return b;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByName(){
        class Com implements Comparator<AdminUnit> {

            @Override
            public int compare(AdminUnit o1, AdminUnit o2) {
                return Collator.getInstance(new Locale("pl", "PL")).compare(o1.name, o2.name);
            }

        }

        units.sort(new Com());
        return this;
    }

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByArea(){


        units.sort(new  Comparator<AdminUnit> (){
            @Override
            public int compare(AdminUnit o1, AdminUnit o2) {
                return Double.compare(o1.area, o2.area);
            }
        });
        return this;
    }

    /**
     * Sortuje daną listę jednostek (in place = w miejscu)
     * @return this
     */
    AdminUnitList sortInplaceByPopulation(){

        units.sort((o1, o2)->Double.compare(o1.population, o2.population));
        return this;
    }
//----------------------------------------------------------
    AdminUnitList sortInplace(Comparator<AdminUnit> cmp){
        units.sort(cmp);
        return this;
    }

    AdminUnitList sort(Comparator<AdminUnit> cmp){
        // Tworzy wyjściową listę
        // Kopiuje wszystkie jednostki
        // woła sortInPlace

        AdminUnitList a = new AdminUnitList();
        a.units.addAll(this.units);
        a.sortInplace(cmp);
        return a;
    }

    //----------------------------------------------
    /**
     *
     * @param pred referencja do interfejsu Predicate
     * @return nową listę, na której pozostawiono tylko te jednostki,
     * dla których metoda test() zwraca true
     */
    AdminUnitList filter(Predicate<AdminUnit> pred){
        AdminUnitList a = new AdminUnitList();
        for(AdminUnit i : units){
            if(pred.test(i)){
                a.units.add(i);
            }
        }
        return a;
    }

    AdminUnitList filter(Predicate<AdminUnit> pred, int limit){
        AdminUnitList a = new AdminUnitList();

        int licznik =1;
        for(AdminUnit i : units){
            if(pred.test(i)){
                a.units.add(i);
                if(licznik==limit) break;
                licznik++;
            }
        }
        return a;
    }

    /**
     * Zwraca co najwyżej limit elementów spełniających pred począwszy od offset
     * Offest jest obliczany po przefiltrowaniu
     * @param pred - predykat
     * @param - od którego elementu
     * @param limit - maksymalna liczba elementów
     * @return nową listę
     */
    AdminUnitList filter(Predicate<AdminUnit> pred, int offset, int limit){
        AdminUnitList a = this.filter(pred);
        AdminUnitList b=new AdminUnitList();
        for (int i=offset; i<a.units.size() && i<limit+offset; i++){
            b.units.add(a.units.get(i));
        }
        return b;
    }
}
