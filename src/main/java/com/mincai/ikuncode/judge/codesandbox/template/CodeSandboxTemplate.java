package com.mincai.ikuncode.judge.codesandbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.mincai.ikuncode.judge.codesandbox.CodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.enums.QuestionExecuteResultEnum;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteMessage;
import com.mincai.ikuncode.judge.codesandbox.threads.TimeManagerThread;
import com.mincai.ikuncode.judge.codesandbox.utils.LanguageCmdUtils;
import com.mincai.ikuncode.judge.codesandbox.utils.ProcessUtil;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author limincai
 */
@Slf4j
public abstract class CodeSandboxTemplate implements CodeSandBox {

    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    protected String GLOBAL_CODE_FILE_NAME;

    public static final int TIME_OUT = 5000;

    private static String globalCodePathName;

    private String userCodeParentPath;

    private String userCodePath;

    protected String language;

    protected String containerId;

    protected DockerClient dockerClient;

    protected String ImageName;

    protected String volumnName;

    protected volatile Long memory;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        File userCodeFile = saveCodeFile(code);

        ExecuteMessage compileExecuteMessage = null;
        List<ExecuteMessage> runExecuteMessageList = null;
        try {
            compileExecuteMessage = compileFile(userCodeFile);
            startDocker();
            createContiner();
            runContainer();
            StatsCmd statsCmd = calculateMemory();
            while (memory < 0) {
                ;
            }
            runExecuteMessageList = runFile(inputList);
            stopContainer();
            removeContainer(statsCmd);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        ExecuteCodeResponse executeCodeResponse = getOutputResponse(runExecuteMessageList);

        boolean isDeleteFile = clearFile(userCodeFile);

        if (!isDeleteFile) {
            log.error("delete file error,userCodeFilePath={}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

    public void startDocker() {
        dockerClient = DockerClientBuilder.getInstance().build();
    }

    public void createContiner() throws Exception {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(ImageName);
        HostConfig hostConfig = new HostConfig();
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume(volumnName)));
        hostConfig.withCpuCount(1L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withMemory(100 * 1000 * 1000L);
        CreateContainerResponse createContainerResponse = containerCmd.withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        String responseId = createContainerResponse.getId();
        containerId = responseId;
    }

    public void runContainer() {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public StatsCmd calculateMemory() {
        memory = Long.MIN_VALUE;
        StatsCmd statsCmd = dockerClient.statsCmd(containerId);
        ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {

            @Override
            public void onStart(Closeable closeable) {

            }

            @Override
            public void onNext(Statistics statistics) {
                System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                memory = Math.max(statistics.getMemoryStats().getUsage(), memory);
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        statsCmd.exec(statisticsResultCallback);
        return statsCmd;
    }

    public void stopContainer() {
        try {
            Runtime.getRuntime().exec(String.format("docker stop -f %s", containerId));
            // dockerClient.stopContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeContainer(StatsCmd statsCmd) {
        try {
            Runtime.getRuntime().exec(String.format("docker rm -f %s", containerId));
            // dockerClient.removeContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        statsCmd.close();
    }

    public void removeContainer() {
        try {
            // Process exec = Runtime.getRuntime().exec(String.format("docker rm -f %s",
            // containerId));
            dockerClient.removeContainerCmd(containerId).exec();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File saveCodeFile(String code) {
        String userDir = System.getProperty("user.dir");
        globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_FILE_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    public ExecuteMessage compileFile(File userCodeFile) throws Exception {
        String compileCmd = LanguageCmdUtils.getCompileCMD(userCodeFile.getAbsolutePath(), userCodeParentPath,
                language);
        if (compileCmd == null) {
            return null;
        }
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtil.compileCode(compileProcess);
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误：\n错误的原因是：" + executeMessage.getErrorMessage());
            }
            return executeMessage;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<ExecuteMessage> runFile(List<String> inputList) throws IOException {
        long startMemory = memory;
        System.out.println("开始的内存---" + memory);
        List<ExecuteMessage> executeMessagesList = new ArrayList<>();
        if (inputList == null) {
            Runtime runtime = Runtime.getRuntime();
            String runCmd = LanguageCmdUtils.getRunCMD(volumnName, language, containerId);
            System.out.println(runCmd);
            Process runProcess = runtime.exec(runCmd);
            ExecuteMessage executeMessage = null;
            TimeManagerThread timeManagerThread = new TimeManagerThread(TIME_OUT);
            timeManagerThread.setProcess(runProcess);
            timeManagerThread.start();
            executeMessage = ProcessUtil.runCode(runProcess, null);
            executeMessage.setMemory(memory);
            executeMessagesList.add(executeMessage);
            if (timeManagerThread.IsTime_out()) {
                throw new RuntimeException("超时异常");
            }
            timeManagerThread.stop();
        } else {
            for (String inputArgs : inputList) {
                try {
                    Runtime runtime = Runtime.getRuntime();
                    String runCmd = LanguageCmdUtils.getRunCMD(volumnName, language, containerId);
                    System.out.println(runCmd);
                    Process runProcess = runtime.exec(runCmd);
                    ExecuteMessage executeMessage = null;
                    TimeManagerThread timeManagerThread = new TimeManagerThread(TIME_OUT);
                    timeManagerThread.setProcess(runProcess);
                    timeManagerThread.start();
                    executeMessage = ProcessUtil.runCode(runProcess, inputArgs);
                    System.out.println("结束的进程-----" + memory);
                    executeMessage.setMemory(memory);
                    executeMessagesList.add(executeMessage);
                    if (timeManagerThread.IsTime_out()) {
                        stopContainer();
                        removeContainer();
                        throw new RuntimeException("超时异常");
                    }
                    timeManagerThread.stop();
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return executeMessagesList;
    }

    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessagesList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        QuestionJudgeInfo judgeResult = new QuestionJudgeInfo();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0L;
        long maxMemory = 0L;
        for (ExecuteMessage executeMessage : executeMessagesList) {
            String errorMessageStr = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessageStr)) {
                executeCodeResponse.setMessage(QuestionExecuteResultEnum.RunException.getText());
                executeCodeResponse.setStatus(QuestionExecuteResultEnum.RunException.getValue());
                break;
            }
            Long time = executeMessage.getTime();
            Long memory = executeMessage.getMemory();
            maxTime = Math.max(time, maxTime);
            maxMemory = Math.max(maxMemory, memory);
            outputList.add(executeMessage.getMessage());
        }
        if (outputList.size() == executeMessagesList.size()) {
            executeCodeResponse.setMessage(QuestionExecuteResultEnum.ExecuteSuccess.getText());
            executeCodeResponse.setStatus(2);
        }
        judgeResult.setMemory(maxMemory / 1024 / 8);
        judgeResult.setTime(maxTime);
        executeCodeResponse.setOutputList(outputList);
        executeCodeResponse.setQuestionJudgeInfo(judgeResult);
        return executeCodeResponse;
    }

    public boolean clearFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("消除:" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    public ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        QuestionJudgeInfo judgeInfo = new QuestionJudgeInfo();
        judgeInfo.setMessage(e.getMessage());
        if (memory != null && memory > 0) {
            judgeInfo.setMemory(memory / 1024 / 8);
        }
        judgeInfo.setMessage(desensitizationStr(e.getMessage()));
        if (e.getMessage().contains("编译")) {
            executeCodeResponse.setMessage(QuestionExecuteResultEnum.CompileFailure.getText());
            executeCodeResponse.setStatus(QuestionExecuteResultEnum.CompileFailure.getValue());
        } else if (e.getMessage().contains("超时")) {
            executeCodeResponse.setMessage(QuestionExecuteResultEnum.OutOfTimeException.getText());
            executeCodeResponse.setStatus(QuestionExecuteResultEnum.OutOfTimeException.getValue());
        } else {
            executeCodeResponse.setMessage(QuestionExecuteResultEnum.RunException.getText());
            executeCodeResponse.setStatus(QuestionExecuteResultEnum.RunException.getValue());
        }
        executeCodeResponse.setQuestionJudgeInfo(new QuestionJudgeInfo());
        return executeCodeResponse;
    }

    public String desensitizationStr(String information) {
        if (information != null) {
            information = information.replaceAll(userCodePath, "");
            information = information.replaceAll(userCodeParentPath, "");
        }
        return information;
    }

}