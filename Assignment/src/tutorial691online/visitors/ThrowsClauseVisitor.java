package tutorial691online.visitors;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Statement;

import tutorial691online.handlers.SampleHandler;

import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;

public class ThrowsClauseVisitor extends ASTVisitor  {
	private static HashSet<TryStatement> tryStatements = new HashSet<>();
	
	@Override
	public boolean visit(TryStatement node) {
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("TryBlock");
		node.accept(methodInvocationVisitor);
		SampleHandler.printMessage("Number" + methodInvocationVisitor.getNumberofCheckedException());
		
		if(methodInvocationVisitor.getNumberofCheckedException() > 1) {
			tryStatements.add(node);
		}
		
		return super.visit(node);
	}
	public static HashSet<TryStatement> getTryStatements() {
		return tryStatements;
	}
}
