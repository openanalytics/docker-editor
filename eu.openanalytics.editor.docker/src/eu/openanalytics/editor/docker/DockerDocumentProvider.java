package eu.openanalytics.editor.docker;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import eu.openanalytics.editor.docker.scanner.DockerPartitionScanner;

public class DockerDocumentProvider extends FileDocumentProvider {
	
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			DockerPartitionScanner scanner = new DockerPartitionScanner();
			IDocumentPartitioner partitioner = new FastPartitioner(scanner, DockerPartitionScanner.ALLOWED_CONTENT_TYPES);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}