package ru.ulpfr.pension_brms;

import ru.ulpfr.pension_brms.gui.MainWindow;
import javax.swing.SwingUtilities;

public class Application {

	public static void main(String[] args) {
		//Скачиваем конфигурационные файлы приложения
		/**/
		
		// Cоздаем окно в потоке обработки событий
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow window = MainWindow.getInstance(); // создаём главное окно приложения
				window.showPanels();
				
			}
		});
		

	}

}
