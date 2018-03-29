package GUI;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import Utils.Options;

public class OpenFileSystemListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String userHomePath = System.getProperty("user.dir");
		File userHome = new File(userHomePath);
		// uses the corect path separator for the OS

		File videos = new File(userHome,"./Results" ); // "."+ File.separator +Options.getResultDirectory()  
		try {
			Desktop.getDesktop().open(videos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

	}}
