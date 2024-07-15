package common.method;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;


public class Searcher {
	public static void focusInput(JTextField textFieldSearch) {
		textFieldSearch.setText("Search");
		textFieldSearch.setForeground(Color.LIGHT_GRAY);
		textFieldSearch.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (textFieldSearch.getText().isEmpty()) {
					textFieldSearch.setText("Search");
					textFieldSearch.setForeground(Color.LIGHT_GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (textFieldSearch.getText().equals("Search")) {
					textFieldSearch.setText("");
					textFieldSearch.setForeground(Color.BLACK);
				}
			}
		});
	}

}