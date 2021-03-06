package eu.openanalytics.editor.docker.syntax;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import eu.openanalytics.editor.docker.Activator;
import eu.openanalytics.editor.docker.DockerEditor;

public class SyntaxReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

	private DockerEditor editor;
	private IDocument document;
	
	public SyntaxReconcilingStrategy(DockerEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		try {
			IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
			new SyntaxProblemReporter().checkAndApply(document, subRegion.getOffset(), subRegion.getLength(), resource);
		} catch (CoreException e) {
			Activator.log(IStatus.ERROR, "Failed to perform syntax check", e);
		} catch (BadLocationException e) {
			Activator.log(IStatus.ERROR, "Failed to perform syntax check", e);
		}
	}

	@Override
	public void reconcile(IRegion partition) {
		// Partition reconciling not supported.
	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		// Monitoring not supported.
	}

	@Override
	public void initialReconcile() {
		// Nothing to do.
	}
}
