package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class Logs {
		
	/**
	 * File's name
	 */
	private static final String s_LOGS_FILE_NAME = "log.txt";

	/**
	 * Buffer to write in file
	 */
	private static BufferedWriter s_file = null;
		
	/**
	 * Open the log file in order to write in it
	 */
	public static void initializeLog() {
		if(s_file == null) {
			File file = new File(s_LOGS_FILE_NAME);
			try {
				s_file = new BufferedWriter(new FileWriter(file));
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Close the log file
	 */
	public static void finalizeLog() {
		try {
			if(s_file != null) 
				s_file.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print a message in the log file
	 * @param _message the message to print
	 */
	public static void info(String _message) {
		if(s_file != null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] info : ";
			try {
				s_file.write(header + " " + _message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Print a message in the log file
	 * @param _message the message to print
	 */
	public static void warning(String _message) {
		if(s_file != null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] warning : ";
			try {
				s_file.write(header + " " + _message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Print a exception in the log file
	 * @param _exception the message to print
	 */
	public static void exception(Exception _exception) {
		if(s_file != null) {
			StringWriter errors = new StringWriter();
			_exception.printStackTrace(new PrintWriter(errors));
			
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] Exception : ";
			try {
				s_file.write(header + " " + errors.toString() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
