/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
   Данный класс отвечает за запись и чтение списка портфелей в PorfolioList.txt
   
   This class is responsible for writing and reading the list of portfolios in PorfolioList.txt
 */

public class PortfolioList {

    private static ArrayList<String> portfolios = new ArrayList<String>();

    public static void addTo(String jsonFormat) {
        portfolios.add(jsonFormat);
    }

    public static ArrayList getList() {
        return portfolios;
    }

    public static void writeInFile() {

        try {

            File file = new File("PorfolioList.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(portfolios);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readFile() {

        try {

            File file = new File("PorfolioList.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {

                ArrayList<String> a = (ArrayList<String>) ois.readObject();

                portfolios = a;

            } catch (ClassNotFoundException ex) {

            }
            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
