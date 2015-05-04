package eu.openanalytics.editor.docker;

import org.eclipse.ui.editors.text.TextEditor;

public class DockerEditor extends TextEditor {

	public DockerEditor() {
		super();
		setSourceViewerConfiguration(new DockerConfiguration());
		setDocumentProvider(new DockerDocumentProvider());
	}
}
