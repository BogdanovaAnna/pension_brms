package ru.ulpfr.pension_brms;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.gui.MainWindow.TABS;
import ru.ulpfr.pension_brms.managers.InputDataManager;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;

public class Application {

	public static void main(String[] args) {
		
		// Cоздаем окно в потоке обработки событий
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				//Создаём главное окно приложения
				MainWindow window = MainWindow.getInstance();
				window.setActiveTab(TABS.XML);
				
				//Инициируем конфигурационные файлы приложения
				InputDataManager.getInstance().parseConstantsCSV();
				InputDataManager.getInstance().parseInputVarsJSON();

				//Активация Логера log4j2
				BasicConfigurator.configure();
			}
		});
		

	}

}
