/**
 * 
 */
package com.yage.research.tools.picselector;

import java.nio.file.Paths;

import com.yage.research.tools.picselector.ui.MainWindow;

/**
 * @author zhangya
 *
 */
public class Main {
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open(Paths.get("").toAbsolutePath().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
