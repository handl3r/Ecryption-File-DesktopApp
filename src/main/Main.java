package main;

import main.ui.DesktopUI;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DesktopUI desktopUI = new DesktopUI();
            }
        });

    }
}
