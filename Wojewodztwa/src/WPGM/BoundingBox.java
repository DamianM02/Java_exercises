package WPGM;

public class BoundingBox {
    double xmin=-1000;
    double ymin=-1000;
    double xmax=1000;
    double ymax=1000;

    //---------------------------------------------------------

    /**
     * Powiększa BB tak, aby zawierał punkt (x,y)
     * @param x - współrzędna x
     * @param y - współrzędna y
     */
    void addPoint(double x, double y){

        if (isEmpty()){
            xmin=x;
            xmax=x;
            ymin=y;
            ymax=y;
        }else{
            if(x<xmin) xmin=x;
            else if(x>xmax) xmax=x;

            if(y<ymin) ymin=y;
            else if(y>ymax) ymax =y;
        }



    }
    //----------------------------------------------------------
    /**
     * Sprawdza, czy BB zawiera punkt (x,y)
     * @param x
     * @param y
     * @return
     */
    boolean contains(double x, double y){
        if( x>=xmin && x<=xmax && y>=ymin && y<=ymax) return true;
        return false;
    }
    //-----------------------------------------------------------
    /**
     * Sprawdza czy dany BB zawiera bb
     * @param bb
     * @return
     */
    boolean contains(BoundingBox bb){
        if(bb.xmin>=xmin && bb.xmax<=xmax && bb.ymin>=ymin && bb.ymax<=ymax) return true;
        return false;
    }
    //-------------------------------------------------------------
    /**
     * Sprawdza, czy dany BB przecina się z bb
     * @param bb
     * @return
     */
    boolean intersects(BoundingBox bb){
        if((bb.xmin<xmax && bb.xmin>xmin) || (bb.xmax<xmax && bb.xmax>xmin)){
            if((bb.ymin<ymin && bb.ymax>ymin) || bb.ymin<ymax && bb.ymax>ymax) return true;
        }



        return false;
    }
    //--------------------------------------------------------------
    /**
     * Powiększa rozmiary tak, aby zawierał bb oraz poprzednią wersję this
     * @param bb
     * @return
     */
    BoundingBox add(BoundingBox bb){
        addPoint(bb.xmin, bb.ymin);
        addPoint(bb.xmin, bb.ymax);
        addPoint(bb.xmax, bb.ymin);
        addPoint(bb.xmax, bb.ymax);
        return this;
    }
    //---------------------------------------------------------------
    /**
     * Sprawdza czy BB jest pusty
     * @return
     */
    boolean isEmpty(){
        if(xmin!=-1000 && xmax!=1000 && ymin!=-1000 && ymax!=1000) return false;
        return true;
    }
    //------------------------------------------------------------------
    /**
     * Oblicza i zwraca współrzędną x środka
     * @return if !isEmpty() współrzędna x środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterX() {
        if(!isEmpty()){
            return (xmax + xmin)/2;
        }else{
            return Double.NaN;
        }
    }
    //------------------------------------------------------------------
    /**
     * Oblicza i zwraca współrzędną y środka
     * @return if !isEmpty() współrzędna y środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterY()  {
        if(!isEmpty()){
            return (ymax + ymin)/2;
        }else{
            return Double.NaN;
        }
    }
    //-------------------------------------------------------------------

    /**
     * Oblicza odległość pomiędzy środkami this bounding box oraz bbx
     * @param bbx prostokąt, do którego liczona jest odległość
     * @return if !isEmpty odległość, else wyrzuca wyjątek lub zwraca maksymalną możliwą wartość double
     * Ze względu na to, że są to współrzędne geograficzne, zamiast odległości użyj wzoru haversine
     * (ang. haversine formula)
     *
     * Gotowy kod można znaleźć w Internecie...
     */
    double distanceTo(BoundingBox bbx)  {
        if (!isEmpty() && !bbx.isEmpty()){
            return haversine(getCenterX(), getCenterY(), bbx.getCenterX(), bbx.getCenterY());
        }else {
            return Double.MAX_VALUE;
        }
    }

    static double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    //-------------------------------------------------------------
    public String toString (){
        if(isEmpty()) return null;
        StringBuilder b = new StringBuilder();
        b.append(" " + xmin);
        b.append(" "+ ymin);
        b.append(" " + xmax);
        b.append(" " + ymax);
        return b.toString();
    }

}
