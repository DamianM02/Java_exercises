package WPGM;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    BufferedReader reader;
    String delimiter=",";
    boolean hasHeader=true;


    List<String> columnLabels = new ArrayList<>(); // nazwy kolumn w takiej kolejności, jak w pliku
    Map<String,Integer> columnLabelsToInt = new HashMap<>(); // odwzorowanie: nazwa kolumny -> numer kolumny


    String[]current;

    //-------------------------------------------------------------------------------

    /**
     *
     * @param filename - nazwa pliku
     * @param delimiter - separator pól
     * @param hasHeader - czy plik ma wiersz nagłówkowy
     */

    public CSVReader(String filename,String delimiter,boolean hasHeader) throws IOException {
        reader = new BufferedReader(new FileReader(filename) );
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if(hasHeader)parseHeader();
    }

    void parseHeader() throws IOException {
        // wczytaj wiersz
        String line = reader.readLine();
        if (line == null) {
            return;
        }
        // podziel na pola
        String[] header = line.split(delimiter);
        // przetwarzaj dane w wierszu
        for (int i = 0; i < header.length; i++) {
            // dodaj nazwy kolumn do columnLabels i numery do columnLabelsToInt
            columnLabels.add(header[i]);
            columnLabelsToInt.put(header[i], i);
        }
    }
//--------------------------------------------------------------------------------------

    boolean next()  throws IOException{
        // czyta następny wiersz, dzieli na elementy i przypisuje do current
        String line = reader.readLine();
        if (line == null) {
            return false;
        }

        current = line.split(delimiter);


        return true;
    }

    //----------------------------------------------------------------------------------
    CSVReader(String filename,String delimiter) throws IOException {
        this( new BufferedReader(new FileReader(filename) ), delimiter, true);
    }

    CSVReader(String filename) throws IOException {
        this( new BufferedReader(new FileReader(filename) ), ",", true);
    }

    public CSVReader(Reader reader, String delimiter, boolean hasHeader) throws IOException {
        this.reader = new BufferedReader(reader);
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if(hasHeader)parseHeader();
    }

    //konstruktor z innego
//-----------------------------------------------------------------
    List<String> getColumnLabels(){
        return columnLabels;
    }

    int getRecordLength(){
        return current.length;
    }

    boolean isMissing(int columnIndex)  {
        if(columnIndex<0 || columnIndex>= getRecordLength() || current==null) return true;
        if(current[columnIndex]==null || current[columnIndex].isEmpty() ) return true;
        return false;
    }

    boolean isMissing(String columnLabel) {
        if(!hasHeader) return true;
        if (columnLabelsToInt.get((columnLabel)) == null) return true;
        return isMissing(columnLabelsToInt.get((columnLabel)));

    }
//--------------------------------------------------
    String get(int columnIndex) {
        if(isMissing(columnIndex)) return "";
        return current[columnIndex];
    }

    String get(String columnLabel) {
        if (isMissing(columnLabel)) return "";
        return current[columnLabelsToInt.get(columnLabel)];
    }

    int getInt(int columnIndex)  {
        if(isMissing(columnIndex)) return -1;
        return Integer.parseInt(current[columnIndex]);
    }

    int getInt(String columnLabel)  {
        if (isMissing(columnLabel)) return -1;

        return Integer.parseInt(current[columnLabelsToInt.get(columnLabel)]);
    }

    long getLong(int columnIndex) {
        if(isMissing(columnIndex)) return -1;
        return Long.parseLong(current[columnIndex]);
    }

    long getLong(String columnLabel)  {
        if (isMissing(columnLabel)) return -1;
        return Long.parseLong(current[columnLabelsToInt.get(columnLabel)]);
    }

    double getDouble(int columnIndex)  {
        if(isMissing(columnIndex)) return -1;

        return Double.parseDouble(current[columnIndex]);
    }

    double getDouble(String columnLabel) {
        if (isMissing(columnLabel)) return -1;

        return Double.parseDouble(current[columnLabelsToInt.get(columnLabel)]);
    }

    //---------------------------------------------------------------------------------------------

    LocalTime getTime(int columnIndex, String format) throws Exception {
        if(columnIndex<0 || columnIndex>= getRecordLength()) throw new Exception("Invalid index.");
        return LocalTime.parse(current[columnIndex], DateTimeFormatter.ofPattern(format));
    }

    LocalTime getTime(String columnLabel, String format) throws Exception {
        if(!hasHeader) throw new Exception("File do not have header.");
        if(columnLabelsToInt.get(columnLabel)==null) throw new Exception("Null");
        return LocalTime.parse(current[columnLabelsToInt.get(columnLabel)], DateTimeFormatter.ofPattern(format));
    }



    LocalDate getDate(int columnIndex, String format) throws Exception {
        if(columnIndex<0 || columnIndex>= getRecordLength()) throw new Exception("Invalid index.");
        return LocalDate.parse(current[columnIndex], DateTimeFormatter.ofPattern(format));
    }

    LocalDate getDate(String columnLabel, String format) throws Exception {
        if(!hasHeader) throw new Exception("File do not have header.");
        if(columnLabelsToInt.get(columnLabel)==null) throw new Exception("Null");
        return LocalDate.parse(current[columnLabelsToInt.get(columnLabel)], DateTimeFormatter.ofPattern(format));
    }
}
