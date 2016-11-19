/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import panelSetup.DatesPanel;

/**
 *
 * @author maxim
 */

/*
  Данный класс строит json строку
 
  This class builds a json string
 */
public class BuilderJSON {

    private String jsonFormat;

    public BuilderJSON(JPanel inputPanel) {

        JScrollPane x = (JScrollPane) inputPanel.getComponent(0);
        JViewport viewport = x.getViewport();
        JTable balanceTable = (JTable) viewport.getView();

        DatesPanel datePanel = (DatesPanel) inputPanel.getComponent(1);
        JTable dateTable = datePanel.getTable();

        JScrollPane x2 = (JScrollPane) inputPanel.getComponent(2);
        JViewport viewport2 = x2.getViewport();
        JTable symbolTable = (JTable) viewport2.getView();

        String balance = balanceTable.getModel().getValueAt(0, 0).toString();
        Map<String, String> assetsList = buildAssetsList(symbolTable);
        String dateFrom = buildDate(dateTable, "From");
        String dateTo = buildDate(dateTable, "To");

        jsonFormat = buildJsonFormat(balance, dateFrom, dateTo, assetsList);
 
    }

    private Map<String, String> buildAssetsList(JTable table1) {

        Map<String, String> assetsList = new HashMap<>();

        for (int count = 0; count < table1.getModel().getRowCount(); count++) {

            String symbol = table1.getValueAt(count, 0).toString();
            String percent = table1.getValueAt(count, 1).toString();

            if (!symbol.equals("") && !percent.equals("")) {
                assetsList.put(symbol, percent);
            }

        }

        return assetsList;

    }

    private String buildDate(JTable table, String FromTo) {

        int row = 0;

        if (FromTo.equals("From")) {
            row = 0;
        }
        if (FromTo.equals("To")) {
            row = 1;
        }

        String date = "";
        String day = "0";
        String month = "0";
        String year = "";

        int dayTemp = Integer.parseInt(table.getModel().getValueAt(row, 1).toString());
        if (dayTemp < 10) {
            day += String.valueOf(dayTemp);
        } else {
            day = String.valueOf(dayTemp);
        }

        int monthTemp = Integer.parseInt(table.getModel().getValueAt(row, 2).toString());
        if (monthTemp < 10) {
            month += String.valueOf(monthTemp);
        } else {
            month = String.valueOf(monthTemp);
        }

        year = table.getModel().getValueAt(row, 3).toString();

        date = year + month + day;

        return date;
    }

    private String buildJsonFormat(String balance, String dateFrom, String dateTo, Map<String, String> assetsList) {

        String jsonFormat = "";

        jsonFormat += "{\"dateFrom\":\"" + dateFrom + "\","
                + "\"dateTo\":\"" + dateTo + "\","
                + "\"balance\":\"" + balance + "\",";

        String arrays = "\"array\":[";

        for (Map.Entry<String, String> entry : assetsList.entrySet()) {

            String symbol = entry.getKey();
            String percent = entry.getValue();

            arrays += "{\"stockName\":\"" + symbol + "\",\"stockPercent\":\"" + percent + "\"},";

        }

        if (arrays != null && arrays.length() > 0 && arrays.charAt(arrays.length() - 1) == ',') {
            arrays = arrays.substring(0, arrays.length() - 1);
        }

        if (assetsList.entrySet().toString().equals("[]")) {
            arrays += "{\"stockName\":\"\",\"stockPercent\":\"\"}";
        }

        jsonFormat += arrays;
        jsonFormat += "]}";

        return jsonFormat;

    }

    public String getJsonFormat() {
        return jsonFormat;
    }

}
