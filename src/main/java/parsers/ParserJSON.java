package parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by maxim on 05.06.16.
 */
//Класс, считывающий строку в формате JSON, которую мы передаем из GUI
//Class that reads a JSON-formatted string that we are passing from GUI
public class ParserJSON {

    private GregorianCalendar start;
    private GregorianCalendar end;
    private double balance;
    private ArrayList<String> tickers = new ArrayList<>();
    private Map<String, Double> percentWeight = new TreeMap<>();

    public ParserJSON(String jsonFormat) {
        parse(jsonFormat);
    }

    public GregorianCalendar getStart() {
        return start;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getTickers() {
        return tickers;
    }

    public Map<String, Double> getPercentWeight() {
        return percentWeight;
    }

    //Метод считывает строку и присваивает значения (start,end), balance, tickers, percentWeight
    //Method reads a string and assigns values (start,end) balance, tickers, percentWeight
    void parse(String jsonFormat) {

        try {
            JSONObject js = new JSONObject(jsonFormat);

            String dateFrom = js.getString("dateFrom");
            String dateTo = js.getString("dateTo");

            start = convertDateString(dateFrom);
            end = convertDateString(dateTo);

            String bal = js.getString("balance");
            balance = Double.parseDouble(bal);

            JSONArray array = js.getJSONArray("array");

            for (int i = 0; i < array.length(); ++i) {
                JSONObject item = array.getJSONObject(i);
                String stockName = item.getString("stockName");
                double stockPercent = item.getDouble("stockPercent");

                tickers.add(i, stockName);
                percentWeight.put(stockName, stockPercent);

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    //Метод, приводящий даты из строки к классу GregorianCalendar
    //Method that convert the date from the string to the GregorianCalendar class
    private GregorianCalendar convertDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar calender = new GregorianCalendar();
        calender.setTime(date);
        return calender;
    }

}
