package org.example;

import org.example.persistence.JsonStorageService;
import org.example.persistence.StorageService;
import org.example.service.GyarManager;
import org.example.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        GyarManager gyarManager = new GyarManager(200);
        StorageService storageService = new JsonStorageService();
        ConsoleMenu menu = new ConsoleMenu(gyarManager, storageService);

        menu.indit();
    }
}