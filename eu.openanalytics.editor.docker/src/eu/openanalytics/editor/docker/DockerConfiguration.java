package eu.openanalytics.editor.docker;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import eu.openanalytics.editor.docker.assist.CompletionProcessor;
import eu.openanalytics.editor.docker.scanner.DockerCommentScanner;
import eu.openanalytics.editor.docker.scanner.DockerInstructionScanner;
import eu.openanalytics.editor.docker.scanner.DockerPartitionScanner;

public class DockerConfiguration extends SourceViewerConfiguration {

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr;

		dr = new DefaultDamagerRepairer(new DockerCommentScanner());
		reconciler.setDamager(dr, DockerPartitionScanner.TYPE_COMMENT);
		reconciler.setRepairer(dr, DockerPartitionScanner.TYPE_COMMENT);

		dr = new DefaultDamagerRepairer(new DockerInstructionScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant ca = new ContentAssistant();
		IContentAssistProcessor cap = new CompletionProcessor();
		ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
		ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		return ca;
	}
}
