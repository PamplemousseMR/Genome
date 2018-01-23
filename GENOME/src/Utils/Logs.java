package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class Logs {
		
	public static final String s_LOGS_FILE_NAME = "log.txt";

	private static BufferedWriter m_file = null;
		
	public static void initializeLog() {
		if(m_file == null) {
			File file = new File(s_LOGS_FILE_NAME);
			try {
				file.createNewFile();
				m_file = new BufferedWriter(new FileWriter(file));
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void finalizeLog() {
		try {
			if(m_file != null) 
				m_file.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String message) {
		if(m_file != null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] info : ";
			try {
				m_file.write(header + " " + message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public static void warning(String message) {
		if(m_file != null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] warning : ";
			try {
				m_file.write(header + " " + message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public static void exception(Exception exception) {
		if(m_file != null) {
			StringWriter errors = new StringWriter();
			exception.printStackTrace(new PrintWriter(errors));
			
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String header =  "["+stackTraceElements[2].getFileName()+"{ "+ stackTraceElements[2].getClassName()+" : "+ stackTraceElements[2].getMethodName()+"("+ stackTraceElements[2].getLineNumber()+") } ] Exception : ";
			try {
				m_file.write(header + " " + errors.toString() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
