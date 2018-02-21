package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class CloseListener implements ActionListener, MenuListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		//super.dispose();
		System.exit(0);
	}

	@Override
	public void menuSelected(MenuEvent e) {
		System.exit(0);
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		System.exit(0);
		
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		System.exit(0);
		
	}

}
