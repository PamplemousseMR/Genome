package Main;

import GUI.MainFrame;
import Utils.Logs;
import Utils.Options;

public final class GENOME {
	
	/**
	 * Function call at the begin of the program
	 */
	public static void initializeProgram() {
		Logs.initializeLog();
		Logs.info("Log initialized");
		Options.initializeOptions();
		Logs.info("Options initialized");
	}
	
	/**
	 * Function call at the end of the program
	 */
	public static void finalizeProgram() {
		Logs.info("Options finalized");
		Options.finalizeOptions();
		Logs.info("Log finalized");
		Logs.finalizeLog();
	}

	public static void main(String[] args) {
		initializeProgram();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				finalizeProgram();
			}
		});
		
		// Write main under this comment 
		Logs.info("Begin");
		new MainFrame();
		Logs.info("End");
	}

}
