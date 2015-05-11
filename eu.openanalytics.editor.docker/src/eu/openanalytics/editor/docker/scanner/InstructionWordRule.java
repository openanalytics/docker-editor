package eu.openanalytics.editor.docker.scanner;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

import eu.openanalytics.editor.docker.Activator;
import eu.openanalytics.editor.docker.util.AssetLoader;

public class InstructionWordRule extends WordRule {

	public static final String[] INSTRUCTIONS;
	
	private static final String INSTRUCTION_FILE_PATH = "assets/instructions.txt";
	private static final String INSTRUCTION_FILE_SEPARATOR = System.getProperty("line.separator");
	
	// Load instructions from the text file.
	static {
		String[] loadedInstructions = new String[0];
		try {
			byte[] contents = AssetLoader.loadAsset(INSTRUCTION_FILE_PATH);
			loadedInstructions = new String(contents).split(INSTRUCTION_FILE_SEPARATOR);
		} catch (IOException e) {
			Activator.log(IStatus.WARNING, "No content assist available: error while reading instruction file: " + INSTRUCTION_FILE_PATH, e);
		}
		INSTRUCTIONS = loadedInstructions;
	}
	
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