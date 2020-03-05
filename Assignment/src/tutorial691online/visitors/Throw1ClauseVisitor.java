package tutorial691online.visitors;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

import tutorial691online.handlers.SampleHandler;

import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class Throw1ClauseVisitor extends ASTVisitor  {
	private static HashSet<MethodInvocation> methodInvocationStatement = new HashSet<>();
	
	public boolean visit(MethodInvocation node) {
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("throwBlock");
		node.accept(methodInvocationVisitor);
		
		if(methodInvocationVisitor.getNumberofCheckedException() > 1) {
			methodInvocationStatement.add(node);
		}

		return super.visit(node);
	}
	public static HashSet<MethodInvocation> getmethodInvocationStatements() {
		return methodInvocationStatement;
	}
}
