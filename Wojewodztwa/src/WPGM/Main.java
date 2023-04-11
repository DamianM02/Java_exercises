package WPGM;

import java.util.Comparator;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {

        AdminUnitList list = new AdminUnitList();
        list.read("admin-units.csv");
        //list.sortInplaceByPopulation().list(System.out);

























        /*
        AdminUnitQuery query = new AdminUnitQuery()
                .selectFrom(list)
                .where(a->a.area>1000)
                .or(a->a.name.startsWith("Sz"))
                .sort((a,b)->Double.compare(a.area,b.area))
                .limit(100);
        query.execute().list(System.out);
        */

    }
}