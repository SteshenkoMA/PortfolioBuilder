/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panelSetup;

/**
 *
 * @author maxim
 */
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

/*
 Класс, который настраивает панель в выбором дат

 The class, which configures the date choice panel
 */
public class DatesPanel extends JPanel {

    private ArrayList<String> daysList;
    private ArrayList<String> monthsList;
    private ArrayList<String> yearsList;
    private String[] days;
    private String[] months;
    private String[] yearsStart;
    private JTable table;

    public JTable getTable() {
        return table;
    }

    public static int getCurrentDay() {
        Calendar calendar = java.util.Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        Calendar calendar = java.util.Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(Calendar.MONTH);
    }

    public static int getCurrentYear() {
        Calendar calendar = java.util.Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new java.util.Date());
        return calendar.get(java.util.Calendar.YEAR);
    }

    public DatesPanel() {
        super(new GridBagLayout());

        table = new JTable(new MyTableModel());
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                table.getSelectionModel().clearSelection();;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(true);

        buildDates();

        setUpSportColumn(table, table.getColumnModel().getColumn(1), days);
        setUpSportColumn(table, table.getColumnModel().getColumn(2), months);
        setUpSportColumn(table, table.getColumnModel().getColumn(3), yearsStart);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.00;
        c.weighty = 1.00;

        add(scrollPane, c);

    }

    public void setUpSportColumn(JTable table,
            TableColumn sportColumn, String[] dates) {

        buildDates();

        JComboBox comboBox = new JComboBox(dates);

        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);

    }

    class MyTableModel extends AbstractTableModel {

        private String[] columnNames = {"Date",
            "Day",
            "Month",
            "Year"};
        private Object[][] data = {
            {"Start",
                1, 1, 1970},
            {"End",
                getCurrentDay(), getCurrentMonth() + 1, getCurrentYear()},};

        public final Object[] longValues = {"", "", "", ""};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {

            data[row][col] = value;
            fireTableCellUpdated(row, col);

        }

    }

    private void buildDates() {

        daysList = new ArrayList<String>();
        for (int a = 1; a <= 31; a++) {
            daysList.add(Integer.toString(a));
        }
        days = daysList.toArray(new String[daysList.size()]);

        monthsList = new ArrayList<String>();
        for (int a = 1; a <= 12; a++) {
            monthsList.add(Integer.toString(a));
        }
        months = monthsList.toArray(new String[monthsList.size()]);

        yearsList = new ArrayList<String>();
        for (int a = 1970; a <= getCurrentYear(); a++) {
            yearsList.add(Integer.toString(a));
        }
        yearsStart = yearsList.toArray(new String[yearsList.size()]);
        Collections.reverse(yearsList);

    }

}
