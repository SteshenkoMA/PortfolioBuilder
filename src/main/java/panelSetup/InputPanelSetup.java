/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panelSetup;

import fileIO.PortfolioList;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import autoComplete.AutoCompleteSetup;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import main.PortfolioBuilder;
import parsers.BuilderJSON;
import parsers.ParserJSON;
import parsers.ParserQuotes;

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

public class InputPanelSetup {

    JPanel inputPanel;
    JPanel portfoliosPanel;
    JPanel statisticsPanel;
    JTextField stockSymbol;
    String jsonFormat;

    public InputPanelSetup(JPanel inputPanel, JPanel portfoliosPanel, JPanel statisticsPanel, JTextField stockSymbol, String jsonFormat) {

        this.inputPanel = inputPanel;
        this.portfoliosPanel = portfoliosPanel;
        this.statisticsPanel = statisticsPanel;
        this.stockSymbol = stockSymbol;
        this.jsonFormat = jsonFormat;

        setupRedPanel();

    }

    private void setupRedPanel() {

        Object rowData[][] = {{"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""},
                              {"", ""}
        };
        
        Object columnNames[] = {"Stock symbols", "%"};
        JTable table = new JTable(new DefaultTableModel(rowData, columnNames));

        Object rowData1[][] = {{"10000"}};
        Object columnNames1[] = {"Balance"};
        JTable table1 = new JTable(rowData1, columnNames1);

        JScrollPane scrollPane1 = new JScrollPane(table1);
        table1.setPreferredScrollableViewportSize(new Dimension(0, 0));
        scrollPane1.setPreferredSize(new Dimension(0, 0));

        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(0, 0));
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        scrollPane.setPreferredSize(new Dimension(0, 0));

        stockSymbol = new JTextField();
        AutoCompleteSetup.setupAutoComplete(stockSymbol);
        setUpSportColumn(table, table.getColumnModel().getColumn(0));

        JButton button = new JButton("Add row");
        button.setPreferredSize(new Dimension(0, 0));

        JButton button2 = new JButton("Build");
        button2.setPreferredSize(new Dimension(0, 0));

        JButton button3 = new JButton("Save portfolio");
        button3.setPreferredSize(new Dimension(0, 0));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{"", ""});
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread t = new Thread() {
                    public void run() {

                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }

                        JScrollPane b = (JScrollPane) portfoliosPanel.getComponent(0);
                        JViewport viewport = b.getViewport();
                        JList list = (JList) viewport.getView();
                        list.clearSelection();

                        BuilderJSON a = new BuilderJSON(inputPanel);

                        jsonFormat = a.getJsonFormat();

                        ParserJSON item = new ParserJSON(jsonFormat);

                        ParserQuotes quotes = new ParserQuotes(item.getTickers(), item.getStart(), item.getEnd());

                        PortfolioBuilder portfolio = new PortfolioBuilder(item.getBalance(), item.getTickers(), item.getPercentWeight(), quotes.getQuotesList(), statisticsPanel);
        
                    }
                };
                t.start();

            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread t = new Thread() {
                    public void run() {

                        PortfolioList.readFile();
                        System.out.println("PortfolioList" + PortfolioList.getList());

                        if (jsonFormat != null) {

                            PortfolioList.addTo(jsonFormat);
                            PortfolioList.writeInFile();
   
                            JScrollPane a = (JScrollPane) portfoliosPanel.getComponent(0);

                            JViewport viewport = a.getViewport();
                            JList l = (JList) viewport.getView();
                            DefaultListModel model = (DefaultListModel) l.getModel();
                            model.addElement(jsonFormat);

                        }

                    }
                };
                t.start();

            }
        });

        TableRenderDemo newContentPane = new TableRenderDemo();
        newContentPane.setPreferredSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 0.20;
        c.weighty = 0.20;
        
        inputPanel.add(newContentPane, c);
            
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.50;
        c.weighty = 0.50;
        
        inputPanel.add(scrollPane, c);

        c.insets = new Insets(0, 0, 0, 1);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.07;
        c.weighty = 0.07;
        
        inputPanel.add(button, c);

        c.insets = new Insets(0, 0, 0, 1);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.07;
        c.weighty = 0.07;
        
        inputPanel.add(button2, c);

        c.insets = new Insets(1, 0, 0, 1);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.07;
        c.weighty = 0.07;
        
        inputPanel.add(button3, c);

        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

                int sum = 0;

                for (int count = 0; count < table.getModel().getRowCount(); count++) {

                    String percent = table.getModel().getValueAt(count, 1).toString();

                    if (!percent.equals("")) {

                        int percentInt = Integer.parseInt(percent);
                        sum += percentInt;
                        
                    }
                }
  
                table.getColumnModel().getColumn(1).setHeaderValue(sum + "%");
                table.getTableHeader().repaint();

            }
        });

        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                int tW = table.getWidth();
                int r = tW / 4;

                table.getColumnModel().getColumn(1).setMaxWidth(r);
                table.getColumnModel().getColumn(1).setPreferredWidth(r);
                table.getColumnModel().getColumn(1).setWidth(r);

            }
        });

        table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                table.getSelectionModel().clearSelection();

            }
        });

    }

    public void setUpSportColumn(JTable table,
            
        TableColumn sportColumn) {
        sportColumn.setCellEditor(new DefaultCellEditor(stockSymbol));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
        
    }

}
