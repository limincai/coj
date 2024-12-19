package com.mincai.ikuncode.judge.codesandbox.utils;

import com.mincai.ikuncode.judge.codesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author limincai
 */
@Slf4j
public class ProcessUtil {


    public static ExecuteMessage compileCode(Process compileProcess) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        BufferedReader errorBufferedReader = null;
        InputStream errorInputStream = null;
        try {
            int exitValue = 0;
            inputStream = compileProcess.getInputStream();
            exitValue = compileProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue == 0) {
                StringBuilder compileResultStr = new StringBuilder("");
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileResultStr.append(compileOutputLine).append("\n");
                }
                executeMessage.setMessage(compileResultStr.toString());
            } else {
                StringBuilder compileResultStr = new StringBuilder("");
                bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream(), "GBK"));
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileResultStr.append(compileOutputLine).append("\n");
                }
                executeMessage.setMessage(compileResultStr.toString());
                errorInputStream = compileProcess.getErrorStream();
                StringBuilder compileErrorResultStr = new StringBuilder("");
                errorBufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    compileErrorResultStr.append(errorCompileOutputLine).append("\n");
                }
                executeMessage.setErrorMessage(compileErrorResultStr.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                bufferedReader.close();
                if (errorInputStream != null) {
                    errorInputStream.close();
                }
                if (errorBufferedReader != null) {
                    errorBufferedReader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            compileProcess.destroy();
        }
        return executeMessage;
    }

    public static ExecuteMessage runCode(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        InputStream errorInputStream = null;
        BufferedReader errorBufferedReader = null;
        try {
            if (args != null) {
                inputArgs(runProcess, args, outputStream, outputStreamWriter);
            }
            setTime(executeMessage, runProcess);
            setRunResult(runProcess, executeMessage, inputStream, bufferedReader, errorInputStream,
                    errorBufferedReader);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (errorInputStream != null) {
                    errorInputStream.close();
                }
                if (errorBufferedReader != null) {
                    errorBufferedReader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            runProcess.destroy();
        }
        return executeMessage;
    }

    private static void setTime(ExecuteMessage executeMessage, Process process) throws InterruptedException {
        long start = 0L;
        long end = 0L;
        if (process.isAlive()) {
            start = System.currentTimeMillis();
            process.waitFor();
            end = System.currentTimeMillis();
        }
        executeMessage.setTime((end - start));
    }

    private static void inputArgs(Process runProcess, String args, OutputStream outputStream,
                                  OutputStreamWriter outputStreamWriter) throws IOException {
        outputStream = runProcess.getOutputStream();

        outputStreamWriter = new OutputStreamWriter(outputStream);

        outputStreamWriter.write(args + "\n");
        outputStreamWriter.flush();
    }

    private static void setRunResult(Process runProcess, ExecuteMessage executeMessage, InputStream inputStream,
                                     BufferedReader bufferedReader, InputStream errorInputStream, BufferedReader errorBufferReader)
            throws IOException {
        inputStream = runProcess.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder runOutputStringBuilder = new StringBuilder();
        String runOutputLine;
        while ((runOutputLine = bufferedReader.readLine()) != null) {
            runOutputStringBuilder.append(runOutputLine);
        }
        executeMessage.setMessage(runOutputStringBuilder.toString());

        errorInputStream = runProcess.getErrorStream();
        bufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
        StringBuilder errorRunStringBuilder = new StringBuilder();
        String errorRunOutputLine;
        while ((errorRunOutputLine = bufferedReader.readLine()) != null) {
            errorRunStringBuilder.append(errorRunOutputLine);
        }
        executeMessage.setErrorMessage(errorRunStringBuilder.toString());
    }
}
