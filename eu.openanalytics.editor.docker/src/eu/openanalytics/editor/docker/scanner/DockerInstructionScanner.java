package eu.openanalytics.editor.docker.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import eu.openanalytics.editor.docker.syntax.SyntaxColors;

public class DockerInstructionScanner extends RuleBasedScanner {

	public DockerInstructionScanner() {
		IToken instructionToken = new Token(new TextAttribute(SyntaxColors.getInstructionColor()));
		IToken stringToken = new Token(new TextAttribute(SyntaxColors.getStringColor()));
		IToken varToken = new Token(new TextAttribute(SyntaxColors.getVariableColor()));
		
		IRule[] rules = new IRule[4];
		rules[0] = new InstructionWordRule(instructionToken);
		rules[1] = new SingleLineRule("\"", "\"", stringToken, '\\');
		rules[2] = new SingleLineRule("\'", "\'", stringToken, '\\');
		rules[3] = new SingleLineRule("${", "}", varToken, '\\');
		
		setRules(rules);
	}
}
