package tutorial691online.visitors;


import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import tutorial691online.handlers.SampleHandler;

public class MethodInvocationVisitor extends ASTVisitor{
	
	private static String[] LogMethods = {"log", "info", "warn", "error", "trace", "debug", "fatal"}; // "log statement"
    private static String[] PrintMethods = {"println", "print"}; // "print statement"
	private static String[] DefaultMethods = {"printStackTrace"}; // display statement
	private static String[] ThrowMethods = {"throw"}; // "throw statement"
	
	private int logPrintDefaultStatements = 0;
	private int thrownStatements = 0;
	private int numberofCheckedException =0;
	private String statementAccordingToVisitorType;
	
	public MethodInvocationVisitor(String statement) {
		this.statementAccordingToVisitorType = statement;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		
		// log statement inside catch
		if(this.statementAccordingToVisitorType == "LogCatchSwitch"){  
			String nodeName = node.getName().toString();
			SampleHandler.printMessage("Invoked nodeName::::::" + nodeName);
			if (IsLoggingStatement(nodeName) || IsDefaultStatement(nodeName) || IsPrintStatement(nodeName)) {
				logPrintDefaultStatements += 1;
			}
			
			if(IsLoggingStatement(nodeName) && IsThrownStatement(nodeName)) {
				
				thrownStatements += 1;
			}
		}
		if(this.statementAccordingToVisitorType == "TryBlock") {
			IMethodBinding methodNode = node.resolveMethodBinding();
			if(methodNode != null) {
				ITypeBinding[] exceptionBinding = methodNode.getExceptionTypes();
				SampleHandler.printMessage("Invoked Method Name::::::" + methodNode.getName());	
				SampleHandler.printMessage("Number of exception::::::" + methodNode.getExceptionTypes().length);
				for(ITypeBinding exception : exceptionBinding) {
					SampleHandler.printMessage("exception::::::" + exception.getName());
					numberofCheckedException++;
				}
			}
		}
		if(this.statementAccordingToVisitorType == "throwBlock") {
			IMethodBinding methodNode = node.resolveMethodBinding();
			if(methodNode != null) {
				ITypeBinding[] exceptionBinding = methodNode.getExceptionTypes();
				SampleHandler.printMessage("Invoked Method Name::::::" + methodNode.getName());	
				SampleHandler.printMessage("Number of exception::::::" + methodNode.getExceptionTypes().length);
				for(ITypeBinding exception : exceptionBinding) {
					SampleHandler.printMessage("exception::::::" + exception.getName());
					numberofCheckedException++;
				}
			}
		}	
		return super.visit(node);
	}
    // To check whether an invocation is a Throw statement
    private static boolean IsThrownStatement(String statement)
    {
        if (statement == null) return false;
        for (String logmethod : ThrowMethods)
        {
            if (statement.indexOf(logmethod) > -1)
            {
                return true;
            }
        }
        return false;
    }
    
    // To check whether an invocation is a logging statement
    private static boolean IsLoggingStatement(String statement)
    {
        if (statement == null) return false;
        for (String logmethod : LogMethods)
        {
            if (statement.indexOf(logmethod) > -1)
            {
                return true;
            }
        }
        return false;
    }
    
    /// To check whether an invocation is a default statement
	private static boolean IsDefaultStatement(String statement)
	{
        if (statement == null) return false;
        for (String defaultmethod : DefaultMethods)
        {
            if (statement.indexOf(defaultmethod) > -1)
            {
                return true;
            }
        }
        return false;
    }
	
    /// To check whether an invocation is a print statement
	private static boolean IsPrintStatement(String statement)
	{
        if (statement == null) return false;
        for (String defaultmethod : PrintMethods)
        {
            if (statement.indexOf(defaultmethod) > -1)
            {
                return true;
            }
        }
        return false;
    }
	
	public int getLogPrintDefaultStatements() {
		return logPrintDefaultStatements;
	}
	
	public int getThrownStatements() {
		return thrownStatements;
	}
	
	public int getNumberofCheckedException() {
		return numberofCheckedException;
	}
}
