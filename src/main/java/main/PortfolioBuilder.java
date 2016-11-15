package main;

import charts.LineChart;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

/**
 * Created by maxim on 19.04.16.
 */

/*
  Класс, который формирует портфель, исходя из баланса и веса каждой акции
   
  THis class creates the portfolio based on the balance and weight of each ticker
*/

public class PortfolioBuilder {

    double balance;
    ArrayList<String> tickers;
    Map<String, Double> percentWeight;
    Map<String, Map> quotesList;
    Set<Date> dates;
    ArrayList<ArrayList> calculatedChanges = new ArrayList<>();
    Map<Date, Double> result;
    private Map<String, String> statistic = new TreeMap();
    JPanel statisticsPanel;

    public PortfolioBuilder(double balance, ArrayList<String> tickers, Map<String, Double> percentWeight, Map<String, Map> quotesList, JPanel statisticsPanel) {

        this.balance = balance;
        this.tickers = tickers;
        this.percentWeight = percentWeight;
        this.quotesList = quotesList;
        this.statisticsPanel = statisticsPanel;

        result = buildPortfolio();
        buildChart(result);
        updateStatisticPanel();

    }

    public void updateStatisticPanel() {

        statistic.put("MaxDD", calculateDD(result));

        JScrollPane b = (JScrollPane) statisticsPanel.getComponent(0);

        JViewport viewport = b.getViewport();
        JTable s = (JTable) viewport.getView();
 
        TableModel x = s.getModel();

        x.setValueAt(statistic.get("IB"), 0, 1);
        x.setValueAt(statistic.get("FB"), 1, 1);
        x.setValueAt(statistic.get("IBdate") + " - " + statistic.get("FBdate"), 2, 1);
        x.setValueAt(statistic.get("PG"), 3, 1);
        x.setValueAt(statistic.get("MaxDD"), 4, 1);
        x.setValueAt(statistic.get("MaxDDperiod"), 5, 1);

    }

    /* 
      Метод выполняет все вычисления
      Method that does all the calculations
    */
    
    public Map<Date, Double> buildPortfolio() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dates = makeIntersection();

        final List<Date> ds = Lists.newArrayList();

        for (Date d : dates) {
            ds.add(d);
        }

        for (String e : tickers) { //Производим вычисления для каждого тикера
            calculate(e);          //Do calctulactions for each ticker
        }

        int sz = dates.size();

        final Map<Date, Double> results = Maps.newTreeMap();

        ArrayList<Double> temp = new ArrayList<Double>();

        for (int i = 0; i < sz; ++i) {

            double price = 0;

            for (ArrayList e : calculatedChanges) {
                price = price + (double) e.get(i); //Для каждой итерации (даты) складываем данные каждого changes из calculatedChanges
            }                                      //For each iteration (date) sum the data of each of the changes calculatedChanges
            results.put(ds.get(i), price);

            if (i == 0) {
                String q = String.format("%1$,.0f", price);
                statistic.put("IB", q);
                statistic.put("IBdate", dateFormat.format(ds.get(i)));
                temp.add(0, price);
            }
            if (i == sz - 1) {
                String q = String.format("%1$,.0f", price);
                statistic.put("FB", q);
                statistic.put("FBdate", dateFormat.format(ds.get(i)));
                temp.add(1, price);
            }

        }

        double percentGain = (temp.get(1) / temp.get(0)) * 100 - 100;

        statistic.put("PG", new BigDecimal(percentGain).setScale(1, RoundingMode.UP).doubleValue() + "%");

        return results;
    }

    //Исторические даты для каждого тикера могут отличаеться, поэтому сравниваем их между собой,
    //и заносим в dates только те даты, которые есть у каждого тикера
    
    //Historical dates for each ticker may vary, so compare them to each other
    //and add in dates, only those dates that each ticker have
    
    public Set<Date> makeIntersection() {

        ArrayList dateSets = new ArrayList();

        Map<String, Map> map = quotesList;
        for (Map.Entry<String, Map> entry : map.entrySet()) {
            dateSets.add(entry.getValue().keySet()); //Добавляем все dataItems в dateSets
        }                                            //Add all dataItems in dateSets

        Set<Date> dates = (Set<Date>) dateSets.get(0);

        for (int i = 0; i < dateSets.size(); i++) {
            dates = Sets.intersection(dates, (Set<Date>) dateSets.get(i)); //Добавляем только совпадающие даты в dates
        }                                                                  //Add only matching dates into dates

        return dates;
    }

    //Метод, который вычисляет как исторически изменялась сумма денег, вложенная в этот 
    
    //A method that calculates how historically changed the amount of money invested in this asset
    
    public void calculate(String symbol) {

        final ArrayList<Double> changes = new ArrayList<>();
        double money = balance * percentWeight.get(symbol) / 100;
        Map<Date, Double> quotes = quotesList.get(symbol);

        Date firstDate = dates.iterator().next();
        double start = quotes.get(firstDate);

        for (Date d : dates) {
         
            double c = quotes.get(d);
            double price = (c / start) * money; //Формула

            changes.add(price);

        }

        calculatedChanges.add(changes);

    }

    //Метод, вычисляющий максимальную просадку 
    
    //Method that calculates the maximum drawdown
    
    public String calculateDD(Map<Date, Double> result) {

        double DD = 0d;
        double todayValue = 0d;
        double MDD = 0d;
        double peak = -99999d;
        Date a = null;
        String ddInfo = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Map.Entry<Date, Double> entry : result.entrySet()) {

            todayValue = entry.getValue();

            if (todayValue > peak) {

                peak = todayValue;
                a = entry.getKey();
            }

            DD = 100.0 * (peak - todayValue) / peak;

            if (DD > MDD) {

                MDD = DD;

                double ddRounded = new BigDecimal(MDD).setScale(1, RoundingMode.UP).doubleValue() * -1;

                ddInfo = ddRounded + "% ";
                statistic.put("MaxDDperiod", dateFormat.format(a) + " - " + dateFormat.format(entry.getKey()));

            }
        }

        return ddInfo;

    }

    //Метод возвращаюзий ArrayList формата "Тикер - Его вес в портфеле"
    
    //Method that returns the ArrayList in the format of "Ticker - Its weight in the portfolio"
    
    public ArrayList<String> getTicekrsAndWeights() {

        ArrayList<String> tickersANDpercentWeights = new ArrayList<String>();

        for (String e : tickers) {

            tickersANDpercentWeights.add(e + " " + percentWeight.get(e).intValue());

        }

        return tickersANDpercentWeights;

    }

    //Создаем график, добавляем на graphPanel
    
    //Create a chart, than add it to graphPanel
    
    public void buildChart(Map<Date, Double> dataItems) {

        LineChart chart = new LineChart(dataItems, "PortfolioBuilder", getTicekrsAndWeights().toString());

        GUI.graphPanel.removeAll();
        GUI.graphPanel.add(LineChart.chartPanel, BorderLayout.CENTER);
        GUI.graphPanel.validate();

    }

}
