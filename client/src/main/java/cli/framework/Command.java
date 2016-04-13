package cli.framework;

import java.util.List;

public abstract class Command<T> {

	abstract public String identifier();
	abstract public void execute() throws Exception;
	abstract public String describe();

	public boolean shouldContinue() { return true; }  // default implementation
	public void load(List<String> args) {  }          // default implementation


	protected Shell<T> shell;

	public void withShell(Shell<T> shell) { this.shell = shell;   }

	public boolean process(List<String> args) throws Exception {
		try { load(args); }
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		execute();
		return shouldContinue();
	}

}
