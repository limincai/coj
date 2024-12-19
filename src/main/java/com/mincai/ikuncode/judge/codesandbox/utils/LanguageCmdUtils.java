package com.mincai.ikuncode.judge.codesandbox.utils;

import java.io.File;

public class LanguageCmdUtils {

    private static final String JAVA_COMPILE = "javac -encoding utf-8 %s";

    // private static final String JAVA_RUN = "java -Xmx512m -Dfile.encoding=UTF-8 -cp %s
    // Main";

    private static final String JAVA_RUN = "java -Xmx512m -Dfile.encoding=UTF-8 -cp %s:%s  Main";

    private static final String PYTHON_RUN = "python3 %s";

    private static final String C_PLUS_PLUS_COMPILE = "g++ %s -o %s/a.exe";

    private static final String C_PLUS_PLUS_RUN = "%s/a.exe";

    private static final String C_COMPILE = "gcc %s -o %s/a.exe";

    private static final String C_RUN = "%s/a.exe";

    private static final String JAVA_Docker_RUN = "docker exec -i  %s java -Dfile.encoding=UTF-8 -cp %s Main";

    private static final String PYTHON_Docker_RUN = "docker exec -i %s python3 %s";

    private static final String C_PLUS_PLUS_Docker_RUN = "docker exec -i %s %s/a.exe";

    private static final String C_Docker_RUN = "docker exec -i %s %s/a.exe";

    public static String getCompileCMD(String userCodePath, String userCodeParent, String language) {
        switch (language) {
            case "java":
                return String.format(JAVA_COMPILE, userCodePath);
            case "c++":
                return String.format(C_PLUS_PLUS_COMPILE, userCodePath, userCodeParent);
            case "python":
                return null;
            case "c":
                return String.format(C_COMPILE, userCodePath, userCodeParent);
            default:
                return null;
        }
    }

    public static String getRunCMD(String userCodeParent, String language, String containId) {
        switch (language) {
            case "java":
                return String.format(JAVA_Docker_RUN, containId, userCodeParent);
            case "c++":
                return String.format(C_PLUS_PLUS_Docker_RUN, containId, userCodeParent);
            case "python":
                return String.format(PYTHON_Docker_RUN, containId, userCodeParent + File.separator + "main.py");
            case "c":
                return String.format(C_Docker_RUN, containId, userCodeParent);
            default:
                return null;
        }
    }

    // public static String getRunCMD(String userCodePath,String userCodeParent,String
    // language) {
    // switch (language) {
    // case "java":
    //// return String.format(JAVA_RUN, userCodeParent, SECURITY_MANAGER_PATH,
    // SECURITY_MANAGER_CLASSNAME);
    // return String.format(JAVA_RUN,userCodeParent);
    // case "c++":
    // return String.format(C_PLUS_PLUS_RUN,userCodeParent);
    // case "python":
    // return String.format(PYTHON_RUN,userCodePath);
    // case "c":
    // return String.format(C_RUN,userCodeParent);
    // default:
    // return null;
    // }
    // }

}
