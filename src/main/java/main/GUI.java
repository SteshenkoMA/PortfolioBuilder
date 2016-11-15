/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author maxim
 */

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import panelSetup.PortfoliosPanelSetup;
import panelSetup.InputPanelSetup;
import panelSetup.StatisticPanelSetup;

/* 
   Класс, который отвечает за графический интерфейс

   This class which is responsible for graphical interface
 */

public class GUI {

    private JTextField stockSymbol;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel inputPanel;
    private JPanel portfoliosPanel;
    private JPanel statisticsPanel;
    public static JPanel graphPanel;
    String jsonFormat;
    private GridBagConstraints gbc;

    public GUI() {
        
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
    
    }

    public void displayGUI() {

        JFrame frame = new JFrame("Portfolio Builder");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout());
        rightPanel = new JPanel(new GridBagLayout());
        leftPanel = new JPanel(new GridBagLayout());
     
        inputPanel = getPanel();
        inputPanel.setLayout(new GridBagLayout());
        
        portfoliosPanel = getPanel();
        portfoliosPanel.setLayout(new GridBagLayout());
        
         JPanel aportfoliosPanel = getPanel();
        aportfoliosPanel .setLayout(new GridBagLayout());
   
        statisticsPanel = getPanel();
        statisticsPanel.setLayout(new GridBagLayout());

        graphPanel = getPanel();
        graphPanel.setLayout(new BorderLayout());
        
        
       JPanel oinputPanel = getPanel();
        oinputPanel.setLayout(new GridBagLayout());
        InputPanelSetup oiPanelSetup = new InputPanelSetup(oinputPanel, portfoliosPanel, statisticsPanel, stockSymbol, jsonFormat);
       JPanel qportfoliosPanel = getPanel();
        qportfoliosPanel.setLayout(new GridBagLayout());
 PortfoliosPanelSetup qportfoliosPanelSetup = new PortfoliosPanelSetup(qportfoliosPanel, statisticsPanel, jsonFormat);
        
         PortfoliosPanelSetup aportfoliosPanelSetup = new PortfoliosPanelSetup(portfoliosPanel, statisticsPanel, jsonFormat);
       StatisticPanelSetup statisticsPanelSetup = new StatisticPanelSetup(statisticsPanel);
        PortfoliosPanelSetup portfoliosPanelSetup = new PortfoliosPanelSetup(portfoliosPanel, statisticsPanel, jsonFormat);
        InputPanelSetup redPanelSetup = new InputPanelSetup(inputPanel, portfoliosPanel, statisticsPanel, stockSymbol, jsonFormat);


   
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
        
        addComp(leftPanel, inputPanel, 0, 0, 1, 1,
                GridBagConstraints.BOTH, 0.7, 0.7);
        addComp(leftPanel, portfoliosPanel, 0, 1, 1, 1,
                GridBagConstraints.BOTH, 0.3, 0.3);
        

        addComp(rightPanel, graphPanel, 0, 0, 1, 1,
                GridBagConstraints.BOTH, 0.7, 0.7);
        addComp(rightPanel, statisticsPanel, 0, 1, 1, 1,
                GridBagConstraints.BOTH, 0.3, 0.3);

        addComp(mainPanel, leftPanel, 0, 0, 1, 1,
                GridBagConstraints.BOTH, 0.3, 0.3);
        addComp(mainPanel, rightPanel, 1, 0, 1, 1,
                GridBagConstraints.BOTH, 0.7, 0.7);


        frame.setContentPane(mainPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);;
        frame.setVisible(true);
    }

    private void addComp(JPanel panel, JComponent comp,
                         int x, int y, int width, int height,
                         int fill, double weightx, double weighty) {
        
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;

        panel.add(comp, gbc);
        
    }

    private JPanel getPanel() {
        
        JPanel panel = new JPanel();
        panel.setOpaque(true);
    
        return panel;
    }

}
