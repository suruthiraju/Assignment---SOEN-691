package tutorial.example;

import java.io.IOException;
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
	
	
	public void bar()   {

		try{

			foo();

		}catch(Exception e){

			e.printStackTrace();

		}

	}

	private void foo() throws IOException{

		throw new IOException();

	}
	
	public void callException1() {
		try {
			throwExceptions();
		}catch (InvalidPathException ex) {
			// empty block
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
