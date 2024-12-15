package com.mincai.ikuncode.judge.codesandbox;

import com.mincai.ikuncode.judge.codesandbox.impl.ExampleCodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.impl.RemoteCodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工程
 *
 * @author limincai
 */
public class CodeSandBoxFactory {

    /**
     * 返回代码沙箱实例，默认返回示例代码沙箱
     */
    public static CodeSandBox newInstance(String codeSandBoxType) {
        switch (codeSandBoxType) {
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            case "example":
            default:
                return new ExampleCodeSandBox();
        }
    }
}
