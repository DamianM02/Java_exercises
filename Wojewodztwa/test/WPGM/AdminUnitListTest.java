package WPGM;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminUnitListTest {

    @Test
    void testK() throws Exception {
        AdminUnitList list = new AdminUnitList();
        list.read("admin-units.csv");

        list.filter(a->a.name.startsWith("K")).sortInplaceByArea().list(System.out);

    }
    @Test
    void powiatymalopolskie() throws Exception {
        AdminUnitList list = new AdminUnitList();
        list.read("admin-units.csv");

        list.filter(a->a.adminLevel==6).filter(a->a.parent.name.equals("województwo małopolskie")).sortInplaceByName().list(System.out);
    }

}