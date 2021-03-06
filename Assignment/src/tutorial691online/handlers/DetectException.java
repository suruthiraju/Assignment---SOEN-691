package tutorial691online.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaModelException;

import tutorial691online.patterns.ExceptionFinder;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class DetectException extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
			
		for(IProject project : projects) {
			SampleHandler.printMessage("DETECTING IN: " + project.getName());
			ExceptionFinder exceptionFinder = new ExceptionFinder();
			
			try {
				// find the exceptions and print the methods that contain the exceptions
				exceptionFinder.findExceptions(project);
				exceptionFinder.printExceptions();		
				
			} catch (JavaModelException e) {
				e.printStackTrace();
			}	
		}
		
		SampleHandler.printMessage("DONE DETECTING");
		return null;
	}
}