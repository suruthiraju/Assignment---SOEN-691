package tutorial691astexample.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import tutorial691astexample.visitors.MethodDeclarationVisitor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class DetectMethodDeclaration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		detectInProjects(projects);
		SampleHandler.printMessage("DONE DETECTING");
		return null;
	}
	
	// provide java source code to parse
	private void detectInProjects(IProject[] projects) {
		for (IProject project : projects) {
			SampleHandler.printMessage("DETECTING IN: " + project.getName());
			IPackageFragment[] packages;
			try {
				packages = JavaCore.create(project).getPackageFragments();
				for (IPackageFragment mypackage : packages) {
					findMethods(mypackage);
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void findMethods(IPackageFragment packageFragment) throws JavaModelException {
		for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {
			CompilationUnit parsedCompilationUnit = parse(unit);	// Create AST from source code project

			// do method visit here and check stuff
			MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();  // implement your own visitor
			parsedCompilationUnit.accept(methodDeclarationVisitor);
			// 
			printMethodInfo(methodDeclarationVisitor);
		}
	}
	
	private static void printMethodInfo(MethodDeclarationVisitor methodDeclarationVisitor) {
		
		SampleHandler.printMessage(String.format("The number of method in the project is %s ", methodDeclarationVisitor.getMethodCount()));
	}
	
	@SuppressWarnings("deprecation")
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}
