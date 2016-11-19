/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panelSetup;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author maxim
 */

/*
  __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ 
 |mainPanel                                   |
 | ―― ―― ―― ―― ―― ―― ――  ―― ―― ―― ―― ―― ―― ―― |
 ||leftPanel           ||rightPanel          ||
 || ―― ―― ―― ―― ―― ――  || ―― ―― ―― ―― ―― ――  ||
 |||inputPanel       | |||graphPanel       | ||
 |||                 | |||                 | ||
 |||__ __ __ __ __ __| |||__ __ __ __ __ __| ||              
 || ―― ―― ―― ―― ―― ――  || ―― ―― ―― ―― ―― ――  ||
 |||portfoliosPanel  | |||statisticsPanel  | ||
 |||                 | |||                 | ||
 |||__ __ __ __ __ __| |||__ __ __ __ __ __| ||
 ||__ __ __ __ __ __ __||__ __ __ __ __ __ __||
 |__ __ __ __ __ __ __ __ __ __ __ __ __ __ __|
        
 */
public class StatisticPanelSetup {

    JPanel statisticsPanel;

    public StatisticPanelSetup(JPanel statisticsPanel) {

        this.statisticsPanel = statisticsPanel;

        setupStatisticsPanel();

    }

    private void setupStatisticsPanel() {

        Object rowData[][] = {{"Initial balance", ""},
        {"Final balance", ""},
        {"Period", ""},
        {"Percent gain", ""},
        {"MaxDD", ""},
        {"MaxDD period", ""}
        };

        Object columnNames[] = {"Metric", "Result"};
        JTable table = new JTable(
                rowData, columnNames);

        table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                table.getSelectionModel().clearSelection();;
            }
        });

        table.setPreferredScrollableViewportSize(new Dimension(0, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(1, 0, 0, 0);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 0.8;

        statisticsPanel.add(scrollPane, c);

    }

}
