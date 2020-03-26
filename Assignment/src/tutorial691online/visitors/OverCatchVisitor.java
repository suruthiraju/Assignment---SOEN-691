package tutorial691online.visitors;

import java.util.*;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;

import tutorial691online.handlers.SampleHandler;

//
public class OverCatchVisitor extends ASTVisitor {
	HashMap<String, String> overCatchStatements = new HashMap<String, String>();
	private HashSet<CatchClause> catchStatements = new HashSet<>();

	@Override
	public boolean visit(CatchClause node) {
		TryStatement tryStatement = (TryStatement) node.getParent();
		// SampleHandler.printMessage("TryyyyBlock:::::"+tryStatement.getBody().toString());

		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("TryBlock");
		tryStatement.accept(methodInvocationVisitor);

		ASTNode parentNode = findParentMethod(tryStatement);
		String parentMethodName = new String();
		if (parentNode.getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration parentMethod = (MethodDeclaration) parentNode;
			parentNode = parentMethod.getBody();
			parentMethodName = getMethodNameWithoutBinding(parentMethod, true);
			// SampleHandler.printMessage("parentMethodName:::::" + parentMethodName);
		}

		SingleVariableDeclaration exceptionType = node.getException();
		ITypeBinding catchException = exceptionType.getType().resolveBinding();

		// SampleHandler.printMessage("Catch Exception Type:::::" + catchException);

		//// ITypeBinding catchExceptionType =
		//// catchMethodInvocationVisitor.getExceptionType();
		// SampleHandler.printMessage("Catch Exception Type:::::" + catchException);

		ITypeBinding invokedMethodException = methodInvocationVisitor.getExceptionType();
		// SampleHandler.printMessage("Invoked Method Exception Type:::::"
		// +invokedMethodException);

		if (isThirdPatternException(catchException, invokedMethodException)) {
//			overCatchStatements.put(node.getStartPosition()+"", 
//					"catch exception type in Catch Block: " + catchException.getQualifiedName() + 
//					", invoked method's exception type in Try Block: " + invokedMethodException.getQualifiedName() );

			catchStatements.add(node);
			// SampleHandler.printMessage(catchStatements.toString());
			if(invokedMethodException != null) {
//////				SampleHandler.printMessage("Over-Catch! METHOD" + parentMethodName
//////						+ "->" + "Exception type in CATCH:"
//////						+ catchException.getQualifiedName() + ",INVOKED METHOD's exception type in TRY:"
//////						+ invokedMethodException.getQualifiedName());
			}
		}
		
		return super.visit(node);
	}

	public HashSet<CatchClause> getCatchBlocks() {
		return catchStatements;
	}

	private boolean isThirdPatternException(ITypeBinding catchException, ITypeBinding invokedMethodException) {
		if(invokedMethodException != null) {
			if (IsSuperType(catchException, invokedMethodException)) {
				return true;
			} else {
				return false;
			}
		}else {
			return false;
		}
		
	}

	public static ASTNode findParentMethod(ASTNode node) {

		int parentNodeType = node.getParent().getNodeType();

		if (parentNodeType == ASTNode.METHOD_DECLARATION) {
			return node.getParent();
		}
		if (parentNodeType == ASTNode.INITIALIZER) {
			return node.getParent();
		}
		if (parentNodeType == ASTNode.TYPE_DECLARATION) {
			return node.getParent();
		}
		if (parentNodeType == ASTNode.METHOD_DECLARATION) {
			return node.getParent();
		}

		return findParentMethod(node.getParent());
	}

	public static String getMethodNameWithoutBinding(MethodDeclaration method, boolean quotes) {

		String methodName = new String();

		methodName = ((quotes) ? "\"" : "") + method.getName().toString();
		methodName += "(";

		for (Object param : method.parameters()) {
			SingleVariableDeclaration svParam = (SingleVariableDeclaration) param;
			methodName += svParam.getType().toString() + ",";
		}
		methodName += ")" + ((quotes) ? "\"" : "");

		methodName = methodName.replace(",)", ")");

		return methodName;

	}

	public HashMap<String, String> getOverCatchStatements() {
		return overCatchStatements;
	}

	/**
	 * Recursively find if the given subtype is a supertype of the reference type.
	 * 
	 * @param subtype       type to evaluate
	 * @param referenceType initial tracing reference to detect the super type
	 */
	public static Boolean IsSuperType(ITypeBinding subType, ITypeBinding referenceType) {

		if (subType == null || referenceType == null || referenceType.getQualifiedName().equals("java.lang.Object"))
			return false;

		if (subType.isEqualTo(referenceType.getSuperclass()))
			return true;
		
		
		return IsSuperType(subType, referenceType.getSuperclass());
	}
}
