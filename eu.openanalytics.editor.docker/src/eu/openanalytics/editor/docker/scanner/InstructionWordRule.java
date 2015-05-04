package eu.openanalytics.editor.docker.scanner;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class InstructionWordRule extends WordRule {

	public static final String[] INSTRUCTIONS = {
		"ADD", "CMD", "COPY", "ENTRYPOINT", "ENV", "EXPOSE",
		"FROM", "LABEL", "MAINTAINER", "ONBUILD", "RUN", "USER", "VOLUME", "WORKDIR"
	};
	
	public InstructionWordRule(IToken instructionToken) {
		super(new InstructionWordDetector(), Token.UNDEFINED, true);
		for (String instruction: INSTRUCTIONS) addWord(instruction, instructionToken);
	}

	private static class InstructionWordDetector implements IWordDetector {

		@Override
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}

		@Override
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

	}
}