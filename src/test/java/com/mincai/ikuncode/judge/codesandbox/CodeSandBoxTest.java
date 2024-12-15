package com.mincai.ikuncode.judge.codesandbox;

import com.mincai.ikuncode.constant.CodeSandBoxType;
import com.mincai.ikuncode.constant.QuestionSubmitLanguage;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

/**
 * @author limincai
 */
@SpringBootTest
class CodeSandBoxTest {

    @Test
    void executeCode() {
        CodeSandBox codeSandBox = new CodeSandBoxProxy(CodeSandBoxFactory.newInstance(CodeSandBoxType.THIRD_PARTY));
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest
                .builder()
                .code("123")
                .inputList(Collections.singletonList("1 2"))
                .language(QuestionSubmitLanguage.JAVA)
                .build();
        codeSandBox.executeCode(executeCodeRequest);
    }
}