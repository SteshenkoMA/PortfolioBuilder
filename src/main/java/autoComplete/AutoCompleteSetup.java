package autoComplete;

import autoComplete.AutoComplete;
import java.awt.*;
import java.awt.event.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.event.*;

/* 
   Данный класс используется для работы с JComboBox, который содержит в себе список тикеров из AutoComplete.showVariants 
   и появляется при вводе символов в JTextField symbol

   This class is used to work with a JComboBox that contains the Ticker list from AutoComplete.showVariants and
   appears when entering characters in JTextField symbol
 */

public class AutoCompleteSetup {

    private static boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public static void setupAutoComplete(final JTextField txtInput) {

        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        final JComboBox cbInput = new JComboBox(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        setAdjusting(cbInput, false);

        cbInput.setSelectedItem(null);

        /* 
           Данный Listener при выборе тикера обрезает строчку, отбраcывая полное название тикера, 
           помещает полученное значение в txtInput
        
           When you select a Ticker, this Listener cuts off the line, dropping the full name of the Ticker, 
           and puts the value in txtInput
         */
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAdjusting(cbInput)) {
                    if (cbInput.getSelectedItem() != null) {
                        String st = cbInput.getSelectedItem().toString();
                        String selectedTicker = st.substring(0, st.indexOf(' '));
                        txtInput.setText(selectedTicker);
                    }
                }
            }
        });

        /* 
           Данный Listener отвечает за использование клавиш Enter, Space, Esc, Up и Down
           1) Enter - помещает выбранный тикер в txtInput
           2) Ecs - закрывает всплывающее окно
           3) Space - выбирает первый тикер из списка
           4) Up и Down - позволяет перемещаться по списку тикеров, оставляя курсор и фокус на txtInput
        
           This Listener is responsible for using the keys Enter, Space, Esc, Up and Down
           1) Enter - places the selected Ticker in txtInput
           2) Ecs - closes pop-up window
           3) Space - selects the first symbol from the list
           4) Up and Down keys - allows you to navigate through the list of tickers, leaving the cursor and focus to txtInput
         */
        txtInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                setAdjusting(cbInput, true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (cbInput.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String st = cbInput.getSelectedItem().toString();
                        String selectedTicker = st.substring(0, st.indexOf(' '));
                        txtInput.setText(selectedTicker);
                        cbInput.setPopupVisible(false);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                }
                setAdjusting(cbInput, false);
            }
        });

        /* 
           Данный DocumentListener отслеживает изменения в txtInput
           и при каждом изменении выполняет метод updateList
        
           This DocumentListener tracks changes in txtInput
           and performs the method updateList at each change 
         */
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {

                updateList();
            }

            public void removeUpdate(DocumentEvent e) {

                updateList();
            }

            public void changedUpdate(DocumentEvent e) {

                updateList();
            }

            /* 
               Метод updateList:
               1) запускает на выполнение AutoComplete.showVariants 
               2) добавляет список тикеров в model класс JComboBox,
                  которое появляется при вводе символов в JTextField stockSymbol 
                  и предлагает список тикеров для выбора
            
               UpdateList method:
               1) starts to perform AutoComplete.showVariants 
               2) adds a list of tickers to model class JComboBox
                  that appears when you start typing in the JTextField stockSymbol 
                  and prompts a list to select tickers
             */
            private void updateList() {
                setAdjusting(cbInput, true);

                model.removeAllElements();

                String input = txtInput.getText();

                if (!input.isEmpty()) {

                    AutoComplete.showVariants(input);

                    JSONArray ra = AutoComplete.tickersList;
                    try {
                        for (int i = 0; i < ra.length(); ++i) {
                            JSONObject item = ra.getJSONObject(i);
                            String s = "";
                            s += item.getString("symbol") + " ";
                            s += item.getString("name");

                            model.addElement(s);

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }

                cbInput.updateUI();

                try {
                    cbInput.setPopupVisible(model.getSize() > 0);
                } catch (Exception e) {
                    // e.printStackTrace();
                }

                setAdjusting(cbInput, false);
            }
        });

        txtInput.setLayout(new BorderLayout());
        txtInput.add(cbInput, BorderLayout.SOUTH);

    }

}
