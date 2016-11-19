package parsers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//Данный класс переходит на http://ichart.finance.yahoo.com, выбирает нужную котировку, считывает ее исторические данные и формирует коллекцию quotesList, из который будут запрашиваться исторические данные
//This class goes on http://ichart.finance.yahoo.com selects the desired quotation, it reads the historical data and forms collection quotesList of which will be requested historical data
public class ParserQuotes {

    private Map<String, Map> quotesList = new TreeMap<>();

    public ParserQuotes(ArrayList<String> tickers, GregorianCalendar start, GregorianCalendar end) {

        //Считываем исторические данные каждого тикера
        //Read the historical data for each ticker
        for (String symbol : tickers) {
            makeQuoteData(symbol, start, end);
        }
    }

    public Map<String, Map> getQuotesList() {
        return quotesList;
    }

    //makeQuoteArray - этот метод считывает данные тикера с заданного url и заносит данные в quotesList
    //makeQuoteArray - this method reads the Ticker data from the specified url and enters the data into quotesList
    public void makeQuoteData(String symbol, GregorianCalendar start, GregorianCalendar end) {

        Map<Date, Double> dataItem = new TreeMap<>();

        try {

            String strUrl = "http://ichart.finance.yahoo.com/table.csv?s=" + symbol
                    + "&a=" + start.get(Calendar.MONTH)
                    + "&b=" + start.get(Calendar.DAY_OF_MONTH)
                    + "&c=" + start.get(Calendar.YEAR)
                    + "&d=" + end.get(Calendar.MONTH)
                    + "&e=" + end.get(Calendar.DAY_OF_MONTH)
                    + "&f=" + end.get(Calendar.YEAR)
                    + "&g=d&ignore=.csv";

            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            DateFormat df = new SimpleDateFormat("y-M-d");

            //Считываем построчно csv, заданный в url. Нужные нам данные разделены зяпятой
            //Read csv line by line, specified in the url. We need the data separated zapatos
            String inputLine;
            in.readLine();
            while ((inputLine = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(inputLine, ",");

                Date date = df.parse(st.nextToken());
                double open = Double.parseDouble(st.nextToken());
                double high = Double.parseDouble(st.nextToken());
                double low = Double.parseDouble(st.nextToken());
                double close = Double.parseDouble(st.nextToken());
                double volume = Double.parseDouble(st.nextToken());
                double adjClose = Double.parseDouble(st.nextToken());

                //Заносим дату и соответствущую ей котировку в dataItem
                //Write date and matching her quote in the dataItem
                dataItem.put(date, adjClose); //Нас интересует только Adjusted closing price
                //We are interested only in Adjusted closing price

            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Добавляем называние тикера и его исторические данные в коллекцию
        //Add the name of the Ticker and its historical data collection
        quotesList.put(symbol, dataItem);

    }

}
