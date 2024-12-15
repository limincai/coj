package com.mincai.ikuncode.judge.codesandbox;

import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理类
 *
 * @author limincai
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {


    private CodeSandBox codeSandBox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱执行信息：{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
