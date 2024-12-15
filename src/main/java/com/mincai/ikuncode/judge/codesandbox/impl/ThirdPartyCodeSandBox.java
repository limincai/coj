package com.mincai.ikuncode.judge.codesandbox.impl;

import com.mincai.ikuncode.judge.codesandbox.CodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 调用第三方代码沙箱
 * @author limincai
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
