package ru.ulpfr.pension_brms;

import ru.ulpfr.pension_brms.gui.MainWindow;
import ru.ulpfr.pension_brms.managers.InputDataManager;

import javax.swing.SwingUtilities;

public class Application {

	public static void main(String[] args) {
		
		// Cоздаем окно в потоке обработки событий
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				//Инициируем конфигурационные файлы приложения
				InputDataManager.getInstance().parseInputVarsJSON();
				InputDataManager.getInstance().parseConstantsCSV();
				
				// создаём и отображаем главное окно приложения
				MainWindow window = MainWindow.getInstance();
				window.showPanels();
			}
		});
		

	}

}
