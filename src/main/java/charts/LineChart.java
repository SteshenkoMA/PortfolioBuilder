package charts;

/**
 * Created by maxim on 12.05.16.
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

/* 
   Класс, который отвечает за отрисовку графика

   This class is used to display a сhart
 */

public class LineChart extends ApplicationFrame {

    public static ChartPanel chartPanel;
    
    //Создаем график из данных, добавляем на chartPanel
    //Builds chart from data, adds to chartPanel

    public LineChart(Map<Date, Double> dataItems, String applicationTitle, String chartTitle) {
        super(applicationTitle);

        TimeSeriesCollection dataset = createDataset(dataItems);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, null, null, dataset, false, true, false);
        customizeChart(chart);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(0, 0));
        
        setContentPane(chartPanel);

    }

    //Обрабатываем данные 
    //Process the data
    
    private TimeSeriesCollection createDataset(Map<Date, Double> dataItems) {
        TimeSeries s1 = new TimeSeries("Price");

        Map<Date, Double> data = dataItems;

        for (Map.Entry<Date, Double> entry : data.entrySet()) {
            Date key = entry.getKey();
            Double value = entry.getValue();

            s1.add(new Millisecond(key), value);
     
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);

        return dataset;
    }

    //Настраиваем особенности отображения графика
    //Customize the appearance of chart
    
    private void customizeChart(JFreeChart chart) {

        XYPlot plot = (XYPlot) chart.getPlot();

        XYToolTipGenerator xyToolTipGenerator = new XYToolTipGenerator() {
            public String generateToolTip(XYDataset dataset, int series, int item) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Number x = dataset.getX(series, item);
                Number y = dataset.getY(series, item);
    
                String balance = String.format("%1$,.0f", y.doubleValue());

                return (sdf.format(x) + " " + balance);

            }
        };

        XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
        render.setBaseToolTipGenerator(xyToolTipGenerator);

        plot.setRenderer(render);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        
        axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy")); //MM->01, MMM->Jan
        axis.setAutoRange(true);

    }

}
