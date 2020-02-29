package tutorial691online.visitors;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;

import tutorial691online.handlers.SampleHandler;

/*
 * Class called OverCatchVisitor that extends ASTVisitor (a visitor for abstract syntax trees)
 * to use method of visit.
 * 
 */

public class OverCatchVisitor extends ASTVisitor {
	HashSet<TryStatement> tryStatement = new HashSet<>();

	ArrayList<String> catchStatement = new ArrayList<>();

	@Override
	public boolean visit(TryStatement node) {

		ArrayList<CatchClause> localExceptions = new ArrayList<>();
		/*
		 * Method catchClauses of class TryStatement returns the live ordered list of
		 * catch clauses for this try statement. The live list of catch clauses (element
		 * type: CatchClause) For each variable catchClause of type Object in the list
		 * of catch clauses (output from the node.catchClauses() method. )
		 */

		for (Object catchClause : node.catchClauses()) {
			/*
			 * Method accept inherited from class org.eclipse.jdt.core.dom.ASTNode it
			 * accepts the given visitor on a visit of the current node. it takes parameter:
			 * visitor (the visitor object of type ASTVisitor). it throws: IllegalArgumentException - if the
			 * visitor is null.
			 */
			((ASTNode) catchClause).accept(new ASTVisitor() {
				@Override
				public boolean visit(CatchClause exceptionNode) {

					localExceptions.add(exceptionNode);

					return super.visit(exceptionNode);
				}
			});

		}

		int count = 0;
		for (int i = 0; i < localExceptions.size(); i++) {

			for (int j = 0; j < localExceptions.size(); j++) {
				if (i == j) {
					continue;
				}
				try {
					// resolveBinding() method: Resolves and returns the binding for the exception type 
					// declared in this catchClause.
					//getQualifiedName() method: Returns the fully qualified name of this type element.
					
					String exceptionName1 = localExceptions.get(j).getException().getType().resolveBinding()
							.getQualifiedName().toString();
					String exceptionName2 = localExceptions.get(i).getException().getType().resolveBinding()
							.getQualifiedName().toString();
					
					// forName(String className) method: Returns the Class object associated with the class 
					// or interface with the given string name.
					
					Class exceptionClass1 = Class.forName(exceptionName1);
					Class exceptionClass2 = Class.forName(exceptionName2);

					/*
					 * The isAssignableFrom() method of java.lang.Class class is used to check if the 
					 * specified class’s object is compatible to be assigned to the instance of this Class.
					 * It will be compatible if both the classes are the same, or the specified class is a 
					 * superclass or superinterface. The method returns true if the specified class’s object 
					 * can be cast to the instance of this Class. It returns false otherwise.
					 */
					
					boolean overCatchFound = exceptionClass1.isAssignableFrom(exceptionClass2);
					if (overCatchFound) {
						System.out.println(
								"OverCatch Anti-pattern : " + exceptionClass1 + " isAssignableFrom " + exceptionClass2);
						catchStatement.add(exceptionClass1 + " -> " + exceptionClass2);
						count++;
					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block

				} catch (NullPointerException e) {
					// TODO Auto-generated catch block

				}
			}
		}

		return super.visit(node);
	}
	
	/*
	 * Method getcatchStatement 
	 * Returns catchStatement of type ArrayList<String>
	 */

	public ArrayList<String> getcatchStatement() {
		return catchStatement;
	}

}
