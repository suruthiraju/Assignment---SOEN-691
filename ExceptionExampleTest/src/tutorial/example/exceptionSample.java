package tutorial.example;

import java.nio.BufferOverflowException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class exceptionSample {
	static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("exception anti-pattern test example");
	}
	
	public void callException1() {
		try {
			throwExceptions();
		}catch (InvalidPathException ex) {
			// empty block
		}
	}
	
	public void callException2() {
		try {
			throwExceptions();
		}catch (BufferOverflowException ex) {
			ex.printStackTrace();
			System.out.println("I am dummy exception");
		}
	}
	
	public void callException3() {
		try {
			throwExceptions();
		}catch (BufferOverflowException ex) {
			ex.printStackTrace();
			System.out.println("I am dummy exception");
			return ;
		}
	}
	
	public static void throwExceptions() {
		FileSystem fileSystem = FileSystems.getDefault();
		fileSystem.getPath("File System to handle.", new String[] {});
		throw new InvalidPathException(null, null);
	}
	
	public static void firstPatternSample() {
		try {
			throwExceptions();
		}catch(Exception e){
			logger.setLevel(Level.INFO);
			throw new InvalidPathException(null, null);
		}
	}
	
	public static void firstPatternSecSample() {
		try {
			throwExceptions();
		}catch(Exception e){
			logger.setLevel(Level.INFO);
			throw new ArithmeticException("/ by zero");
		}
	}

}
