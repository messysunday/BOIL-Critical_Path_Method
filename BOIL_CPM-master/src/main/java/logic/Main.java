package logic;
import ui.MainUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try{
            //stworzenie GUI
            //napisana metoda do stworzenia gui
            SwingUtilities.invokeLater(Main::createGUI);
            //MainLogic mainLogic = new MainLogic();
            //mainLogic.test();

        } catch (Exception e) {
            String message = "Something went wrong!\n";
            if (e.getMessage() != null) {
                message += e.getMessage();
                message += "\n";
            }
            message += "Please try again!\n";
            JOptionPane.showMessageDialog(new JFrame(), message);
        }
    }
    private static void createGUI() {
        MainUI ui = new MainUI(); //klasa stworzona za pomoca kreatora GUI
        JPanel root = ui.getRootPanel(); //pobranie panelu
        JFrame frame = new JFrame(); //ramka na panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ustawienie zamykania ramki - guzik close
        frame.setContentPane(root); //dodanie panelu do ramki
        frame.pack(); //"spakowanie" ramki - dopasowanie jej rozmiaru do okienka
        frame.setSize(750, 750);
        frame.setLocationRelativeTo(null); //wysrodkowanie okienka na ekranie
        frame.setVisible(true); //wyswietlanie ui
    }
}