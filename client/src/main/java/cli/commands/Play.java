package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Play extends Command<TCFPublicAPI> {

	private String fileName;

	@Override
	public String identifier() { return "play"; }

	@Override
	public void execute() throws Exception {
		InputStream stream = new FileInputStream(new File(fileName));
		shell.run(stream, true, 2);
	}

	@Override
	public void load(List<String> args) { fileName = args.get(0); }

	@Override
	public String describe() {
		return "Play commands stored in a given file (play FILENAME)";
	}

}
