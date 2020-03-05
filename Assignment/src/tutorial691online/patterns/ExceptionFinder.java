package tutorial691online.patterns;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaModelException;

import tutorial691online.handlers.SampleHandler;
import tutorial691online.visitors.CatchClauseVisitor;
import tutorial691online.visitors.OverCatchVisitor;
import tutorial691online.visitors.Throw1ClauseVisitor;
import tutorial691online.visitors.ThrowsClauseVisitor;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

public class ExceptionFinder {
	HashMap<MethodDeclaration, String> suspectMethods = new HashMap<>();
	HashMap<MethodDeclaration, String> throwMethods = new HashMap<>();
	HashMap<MethodDeclaration, String> catchMethods = new HashMap<>();
	HashMap<MethodDeclaration, String> kitchenSinkMethods = new HashMap<>();

	public void findExceptions(IProject project) throws JavaModelException {
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
		
		for (IPackageFragment mypackage : packages) {
			
			for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
				// AST node
				CompilationUnit parsedCompilationUnit = parse(unit);
				
				// overcatch
				//OverCatchVisitor catVisitor = new OverCatchVisitor();
				//parsedCompilationUnit.accept(catVisitor);
				
				//Pattern 1
				// do method visit here and check stuff
				CatchClauseVisitor exceptionVisitor = new CatchClauseVisitor();

				parsedCompilationUnit.accept(exceptionVisitor);
				// Give detail of detection
				getMethodsWithTargetCatchClauses(exceptionVisitor);
				
				//Pattern 3: overcatch
				OverCatchVisitor overCatchVisitor = new OverCatchVisitor();
				parsedCompilationUnit.accept(overCatchVisitor);
				getMethodsWithTargetTryClauses(overCatchVisitor);

				parsedCompilationUnit.accept(exceptionVisitor);				

				// Give detail of detection
				getMethodsWithTargetCatchClauses(exceptionVisitor);
				
				// get Kitchen sink anti-pattern here
				//ThrowsClauseVisitor throwUncheckedException = new ThrowsClauseVisitor();
				//parsedCompilationUnit.accept(throwUncheckedException);
				
				// Give detail of detection for Kitchen sink anti-patter
				//getMethodsWithTargetThrowClauses(throwUncheckedException);
				
				//Pattern 2 : Kitchen Sink
				// get Kitchen sink anti-pattern here
				Throw1ClauseVisitor throwUncheckedException1 = new Throw1ClauseVisitor();
				parsedCompilationUnit.accept(throwUncheckedException1);
				
				getMethodsWithTargetThrow1Clauses(throwUncheckedException1);

			}
		}
	}

	private void getMethodsWithTargetThrowClauses(ThrowsClauseVisitor throwUncheckedException) {
		// TODO Auto-generated method stub
		for(TryStatement tryStatements: ThrowsClauseVisitor.getTryStatements()) {
			kitchenSinkMethods.put(findMethodForTry(tryStatements), "Throwing the Kitchen Sink");
		}
		
	}
	
	private void getMethodsWithTargetThrow1Clauses(Throw1ClauseVisitor throwUncheckedException) {
		// TODO Auto-generated method stub
		for(MethodInvocation methodInvocationStatement: Throw1ClauseVisitor.getmethodInvocationStatements()) {
			kitchenSinkMethods.put(findMethodForThrow1(methodInvocationStatement), "Throwing the Kitchen Sink");
		}
		
	}

	private void getMethodsWithTargetCatchClauses(CatchClauseVisitor catchClauseVisitor) {
		for(CatchClause throwStatement: catchClauseVisitor.getThrowStatements()) {
			//suspectMethods.put(findMethodForThrow(throwStatement), "throwStatement");
			throwMethods.put(findMethodForThrow(throwStatement), "LogThrow");
		}
		
	}
	
	
	private void getMethodsWithTargetTryClauses(OverCatchVisitor overCatchVisitor) {
		for(CatchClause catchblock: overCatchVisitor.getCatchBlocks()) {
			//suspectMethods.put(findMethodForCatch(catchblock), "Over-Catch");
			catchMethods.put(findMethodForCatch(catchblock), "Over-Catch");
		}
	}
	
	private ASTNode findParentMethodDeclaration(ASTNode node) {
			if(node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
				return node.getParent();
			} else {
				return findParentMethodDeclaration(node.getParent());
			}
	}

	private ASTNode findParentMethodInvocation(ASTNode node) {
		if(node.getParent().getNodeType() == ASTNode.METHOD_INVOCATION) {
			return node.getParent();
		} else {
			return findParentMethodInvocation(node.getParent());
		}
	}
	
	private MethodDeclaration findMethodForThrow(CatchClause throwStatement) {
		return (MethodDeclaration) findParentMethodDeclaration(throwStatement);
	}

	private MethodDeclaration findMethodForCatch(CatchClause catchStatement) {
		return (MethodDeclaration) findParentMethodDeclaration(catchStatement);
	}

	private MethodDeclaration findMethodForTry(TryStatement tryClause) {
		return (MethodDeclaration) findParentMethodTryDeclaration(tryClause);
	}
	
	
	private ASTNode findParentMethodTryDeclaration(ASTNode node) {
		if(node != null && node.getParent() != null ) {
			if(node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
				return node.getParent();
			} else {
				return findParentMethodTryDeclaration(node.getParent());
			}
			}else
				return null;
	}
	
	private MethodDeclaration findMethodForThrow1(MethodInvocation methodInvoc ) {
		return (MethodDeclaration) findParentMethodThrow1Declaration(methodInvoc);
	}
	
	
	private ASTNode findParentMethodThrow1Declaration(ASTNode node) {
		// TODO Auto-generated method stub
		if(node != null && node.getParent() != null ) {
		if(node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
			return node.getParent();
		} else {
			return findParentMethodThrow1Declaration(node.getParent());
		}
		}else
			return null;
	}

	public HashMap<MethodDeclaration, String> getSuspectMethods() {
		return suspectMethods;
	}

	public void printExceptions() {
		for (MethodDeclaration declaration : suspectMethods.keySet()) {
			String type = suspectMethods.get(declaration);
			SampleHandler.printMessage(String.format("The following method suffers from the %s anti-pattern", type));
			SampleHandler.printMessage(declaration.toString());
		}

		for (MethodDeclaration declaration : throwMethods.keySet()) {
			String type = throwMethods.get(declaration);
			SampleHandler.printMessage(String.format("The following method suffers from the Throw & Log anti-pattern: %s", type));
			SampleHandler.printMessage(declaration.toString());
		}
		for (MethodDeclaration declaration : catchMethods.keySet()) {
			String type = catchMethods.get(declaration);
			SampleHandler.printMessage(String.format("The following method suffers from the Over-Catch anti-pattern: %s", type));
			SampleHandler.printMessage(declaration.toString());
		}

		for (MethodDeclaration declaration : kitchenSinkMethods.keySet()) {
			String type = kitchenSinkMethods.get(declaration);
			SampleHandler.printMessage(String.format("The following method suffers from the %s anti-pattern", type));
			if (declaration != null) {
				SampleHandler.printMessage(declaration.toString());
			}			
		}
		
		
		SampleHandler.printMessage(String.format("Throw & Log anti-pattern Detected Count: %s", throwMethods.size()));
		SampleHandler.printMessage(String.format("Over-Catch anti-pattern Detected Count: %s", catchMethods.size()));
		SampleHandler.printMessage(String.format("Throwing the Kitchen Sink anti-pattern Detected Count: %s", kitchenSinkMethods.size()));
	}

	public static CompilationUnit parse(ICompilationUnit unit) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}
