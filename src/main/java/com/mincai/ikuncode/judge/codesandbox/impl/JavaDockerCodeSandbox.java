package com.mincai.ikuncode.judge.codesandbox.impl;

import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.judge.codesandbox.template.CodeSandboxTemplate;
import org.springframework.stereotype.Service;

/**
 * @author limincai
 */
@Service
public class JavaDockerCodeSandbox extends CodeSandboxTemplate {

    public JavaDockerCodeSandbox() {
        super();
        super.GLOBAL_CODE_FILE_NAME = "Main.java";
        super.ImageName = "openjdk:8-alpine";
        super.volumnName = "/java";
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }

}
