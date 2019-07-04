import ghidra.base.project.GhidraProject;

import ghidra.app.util.headless.HeadlessScript;

/*
 * Create a shared project
 *
 * analyzeHeadless . empty -postScript CreateProject.java <new_repo_name> -deleteProject -noanalysis
 *
*/

public class CreateProject extends HeadlessScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        if (args.length == 0) {
          System.err.println("Please specify a repo name!");
          return;
        }

        setServerCredentials("root", System.getenv("GHIDRA_PASS"));
        GhidraProject.getServerRepository(System.getenv("GHIDRA_SERVER_HOST"), 13100, args[0], true);
    }
}