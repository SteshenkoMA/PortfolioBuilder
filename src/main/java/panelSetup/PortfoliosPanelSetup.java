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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.GregorianCalendar;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import main.PortflioBuilder;
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
public class PortfoliosPanelSetup {

    JPanel portfoliosPanel;
    JPanel statisticsPanel;
    String jsonFormat;

    public PortfoliosPanelSetup(JPanel portfoliosPanel, JPanel statisticsPanel, String jsonFormat) {

        this.portfoliosPanel = portfoliosPanel;
        this.statisticsPanel = statisticsPanel;
        this.jsonFormat = jsonFormat;

        setupPortfoliosPanel();

    }

    private void setupPortfoliosPanel() {

        DefaultListModel listModel = new DefaultListModel();

        PortfolioList.readFile();

        for (int i = 0; i < PortfolioList.getList().size(); i++) {
            listModel.addElement(PortfolioList.getList().get(i));
        }

        JList list = new JList(listModel);
        list.setFocusable(false);
        list.setVisibleRowCount(5);

        final JPopupMenu popupMenu = new JPopupMenu();

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)
                        && !list.isSelectionEmpty()) {

                    popupMenu.removeAll();
                    int row = list.locationToIndex(me.getPoint());
                    popupMenu.add(list.getModel().getElementAt(row).toString());
                    popupMenu.show(list, me.getX(), me.getY());
                }
            }

            public void mouseReleased(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)
                        && !list.isSelectionEmpty()) {

                    popupMenu.removeAll();
                    int row = list.locationToIndex(me.getPoint());
                    popupMenu.add(list.getModel().getElementAt(row).toString());
                    popupMenu.show(list, me.getX(), me.getY());
                }
            }
        });

        Object rowData[][] = {{""}};
        Object columnNames[] = {"Portfolios"};
        JTable table = new JTable(
                rowData, columnNames);

        table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                table.getSelectionModel().clearSelection();;
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setPreferredSize(new Dimension(0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;
        c.weighty = 0.8;
        portfoliosPanel.add(scrollPane, c);

        JButton button1 = new JButton("Del");
        button1.setPreferredSize(new Dimension(0, 0));

        JButton button2 = new JButton("Build");
        button2.setPreferredSize(new Dimension(0, 0));

        c.insets = new Insets(0, 0, 0, 1);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.2;
        c.weighty = 0.2;
        portfoliosPanel.add(button1, c);

        c.insets = new Insets(0, 0, 0, 1);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.2;
        c.weighty = 0.2;
        portfoliosPanel.add(button2, c);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread t = new Thread() {
                    public void run() {

                        PortfolioList.getList().remove(list.getSelectedIndex());
                        PortfolioList.writeInFile();
                        listModel.remove(list.getSelectedIndex());

                    }
                };
                t.start();

            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread t = new Thread() {
                    public void run() {

                        String jsonFormat = list.getSelectedValue().toString();
                        ParserJSON item = new ParserJSON(jsonFormat);
                        GregorianCalendar today = new GregorianCalendar();
                        ParserQuotes quotes = new ParserQuotes(item.getTickers(), item.getStart(), today);
                        PortflioBuilder portfolio = new PortflioBuilder(item.getBalance(), item.getTickers(), item.getPercentWeight(), quotes.getQuotesList(), statisticsPanel);
                    }
                };
                t.start();

            }
        });

    }

}
