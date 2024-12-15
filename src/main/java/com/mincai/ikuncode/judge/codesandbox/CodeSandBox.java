package com.mincai.ikuncode.judge.codesandbox;

import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author limincai
 */
public interface CodeSandBox {

    /**
     * 执行代码
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
