/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.EventQueue;

/**
 *
 * @author maxim
 */

public class Main {

    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new GUI().displayGUI();
            }
        };
        EventQueue.invokeLater(runnable);
    }
};
