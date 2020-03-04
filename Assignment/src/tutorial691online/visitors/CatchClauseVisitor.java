package tutorial691online.visitors;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;

public class CatchClauseVisitor extends ASTVisitor{

//	private HashSet<CatchClause> dummyCatches = new HashSet<>();
//	private HashSet<CatchClause> emptyCatches = new HashSet<>();
	private HashSet<CatchClause> throwStatements = new HashSet<>();
	
	@Override
	public boolean visit(CatchClause node) {
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("LogCatchSwitch");
		node.accept(methodInvocationVisitor);
		
//		if(isEmptyException(node)) {
//			emptyCatches.add(node);
//		}
//		
//		if(node.getBody().statements().size() == methodInvocationVisitor.getLogPrintDefaultStatements() && !emptyCatches.contains(node)){
//			dummyCatches.add(node);
//		}
		
		if(isfirstPatternException(node)) {
			throwStatements.add(node);
		}

		return super.visit(node);
	}
	
	private boolean isfirstPatternException(CatchClause node) {
		int throwCounter = 0;
		int logCounter = 0;
		
		List<Statement> blockStatements = node.getBody().statements();
		
		for(Statement statement : blockStatements) {
			if(statement.toString().contains("throw")) {
				throwCounter++;
			}
			if(statement.toString().contains("log")) {
				logCounter++;
			}
			if(throwCounter>0 && logCounter>0) {
				System.out.println(throwStatements);
				return true;
			}
		}
		
		
		return false;
	}

//	public HashSet<CatchClause> getDummyCatches() {
//		return dummyCatches;
//	}
//	
//	public HashSet<CatchClause> getEmptyCatches() {
//		return emptyCatches;
//	}

	public HashSet<CatchClause> getThrowStatements() {
		return throwStatements;
	}
	
	private boolean isEmptyException(CatchClause node) {
		
		return node.getBody().statements().isEmpty();
	}
	
}
