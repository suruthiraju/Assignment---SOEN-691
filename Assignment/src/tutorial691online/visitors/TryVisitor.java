package tutorial691online.visitors;

import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

import tutorial691online.handlers.SampleHandler;

public class TryVisitor extends ASTVisitor {
	private static HashSet<TryStatement> tryStatements = new HashSet<>();
	private static int tryBlockCount = 0;
	private static int tryBlockLOC = 0;
	
	@Override
	public boolean visit(TryStatement node){
		tryBlockCount++;
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("TryBlock");
		node.accept(methodInvocationVisitor);
		
		if(methodInvocationVisitor.getNumberofCheckedException() > 1) {
			tryStatements.add(node);
		}
		
		List<Statement> bodyStatements = node.getBody().statements();
		//SampleHandler.printMessage("nodeeeee:" + node);
		for (Statement st : bodyStatements) {
			//SampleHandler.printMessage("Satatementttttt:" + st.toString());
			tryBlockLOC++;
		}

		//SampleHandler.printMessage("Commentttttt:" + getCommentLineCount(bodyStatements));
		
		return super.visit(node);
	}
	public static HashSet<TryStatement> getTryStatements() {
		return tryStatements;
	}
	
	public static int getTryBlockCount() {
		return tryBlockCount;
	}
	
	public static int getTryBlockLOC() {
		return tryBlockLOC;
	}
	
}
