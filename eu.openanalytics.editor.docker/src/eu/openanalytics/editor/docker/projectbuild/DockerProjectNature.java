package eu.openanalytics.editor.docker.projectbuild;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import eu.openanalytics.editor.docker.Activator;

public class DockerProjectNature implements IProjectNature {

	public static final String ID = Activator.PLUGIN_ID + ".dockerProjectNature";
	
	private IProject project;
	
	@Override
	public void configure() throws CoreException {
		// No additional configuration required.
	}

	@Override
	public void deconfigure() throws CoreException {
		// No additional cleanup required.
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
