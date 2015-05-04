package eu.openanalytics.editor.docker.projectbuild;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import eu.openanalytics.editor.docker.Activator;

public class DockerFileListener implements IResourceChangeListener, IResourceDeltaVisitor, IStartup {

	private static final String DOCKER_FILE_EXT = "dockerfile";

	@Override
	public void earlyStartup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			try {
				event.getDelta().accept(this);
			} catch (CoreException e) {
				Activator.log(IStatus.ERROR, "Dockerfile auto-nature: failed to process resource delta", e);
			}
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();
		if (res.getType() == IResource.FILE) {
			String fileName = ((IFile) res).getName().toLowerCase();
			if (fileName.equals(DOCKER_FILE_EXT) || fileName.endsWith("." + DOCKER_FILE_EXT)) {
				//TODO Only when the last dockerfile is deleted
				if (delta.getKind() == IResourceDelta.REMOVED) triggerRemoveNature(res.getProject());
				else triggerAddNature(res.getProject());
			}
		}
		return true;
	}

	private void triggerAddNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		for (String nature: natures) {
			if (nature.equals(DockerProjectNature.ID)) return;
		}
		
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = DockerProjectNature.ID;

		IStatus status = project.getWorkspace().validateNatureSet(newNatures);
		if (status.getCode() == IStatus.OK) {
			scheduleUpdateProjectJob(project, newNatures);
		} else {
			Activator.log(IStatus.ERROR, "Dockerfile auto-nature: cannot add nature: " + status.getMessage(), null);
		}
	}

	private void triggerRemoveNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		boolean hasNature = false;
		for (String nature: natures) {
			if (nature.equals(DockerProjectNature.ID)) hasNature = true;
		}
		if (!hasNature) return;
		
		String[] newNatures = new String[natures.length - 1];
		int newIndex = 0;
		for (int i=0; i<natures.length; i++) {
			if (natures[i].equals(DockerProjectNature.ID)) continue;
			newNatures[newIndex++] = natures[i];
		}

		IStatus status = project.getWorkspace().validateNatureSet(newNatures);
		if (status.getCode() == IStatus.OK) {
			scheduleUpdateProjectJob(project, newNatures);
		} else {
			Activator.log(IStatus.ERROR, "Dockerfile auto-nature: cannot remove nature: " + status.getMessage(), null);
		}
	}
	
	private void scheduleUpdateProjectJob(IProject project, final String[] newNatures) {
		Job job = new Job("Modifying project nature") {
			protected IStatus run(IProgressMonitor monitor) {
				if (monitor.isCanceled()) return Status.CANCEL_STATUS;
				monitor.beginTask(getName(), 10);
				
				try {
					IProjectDescription description = project.getDescription();
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
					Activator.log(IStatus.INFO, "Dockerfile auto-nature: nature update for project " + project.getName(), null);
				} catch (CoreException e) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to modify project nature", e);
				}
				
				monitor.done();
				return Status.OK_STATUS;
			};
		};
		job.setSystem(true);
		job.schedule();
	}
}