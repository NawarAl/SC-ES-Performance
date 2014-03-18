package com.findwise.thesis.feeder.monitor;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;

/**
 *
 * @author Nawar Alkurdi
 */
public class Monitor implements InterfaceMonitor {

    private final int cpuTime = 30;
    private final int percent = 100;
    private final int faultLength = 10;

    @Override
    public MonitorInformation getInfo() {
        int kb = 1024;
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;
        long freeMemoryDiskSize = 0;
        long totalDiskSize = 0;
        long usedDiskSize = 0;
        double cpuRatio = 0;

        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long totalMemorySize = os.getTotalPhysicalMemorySize() / kb;
        long freePhysicalMemorySize = os.getFreePhysicalMemorySize() / kb;
        long usedMemory = (os.getTotalPhysicalMemorySize() - os.getFreePhysicalMemorySize()) / kb;
        ThreadGroup parent = Thread.currentThread().getThreadGroup();
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        int totalThread = parent.activeCount();

        MonitorInformation info = new MonitorInformation();
        info.setCPURatio(cpuRatio);
        info.setFreeDiskSize(freeMemoryDiskSize);
        info.setFreeMemory(freeMemory);
        info.setFreePhysicalMemorySize(freePhysicalMemorySize);
        info.setMaxMemory(maxMemory);

        info.setTotalDiskSize(totalDiskSize);
        info.setTotalMemory(totalMemory);
        info.setTotalMemorySize(totalMemorySize);
        info.setTotalThread(totalThread);
        info.setUsedDiskSize(usedDiskSize);
        info.setUsedMemory(usedMemory);
        return info;
    }

    private static void freeResource(InputStream input, InputStreamReader inputReader, BufferedReader reader) throws IOException {
        if (input != null) {
            input.close();
        }
        if (inputReader != null) {
            inputReader.close();
        }
        if (reader != null) {
            reader.close();
        }
    }

    public double getCPUInfo() throws Exception {
        String cmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe "
                + "process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
        long[] readCPU1 = readCPU(Runtime.getRuntime().exec(cmd));
        Thread.sleep(cpuTime);
        long[] readCPU2 = readCPU(Runtime.getRuntime().exec(cmd));
        if (readCPU1 != null && readCPU2 != null) {
            long idelTime = readCPU2[0] - readCPU1[0];
            long busyTime = readCPU2[1] - readCPU1[1];
            return Double.valueOf(percent * (busyTime) / (busyTime + idelTime)).doubleValue();
        } else {
            return 0.0;
        }
    }

    private long[] readCPU(final Process pc) throws Exception {
        try {
            long[] returnCPUInfo = new long[2];
            pc.getOutputStream().close();

            InputStreamReader inputReader = new InputStreamReader(pc.getInputStream());
            LineNumberReader lineNumberInput = new LineNumberReader(inputReader);
            String readInput = lineNumberInput.readLine();
            if (readInput == null || readInput.length() < faultLength) {
                return null;
            }

            int captionID = readInput.indexOf("Caption");
            int commandID = readInput.indexOf("CommandLine");
            int readOperationCountID = readInput.indexOf("ReadOperationCount");
            int userModeTimeID = readInput.indexOf("UserModeTime");
            int kernelModeTimeID = readInput.indexOf("KernelModeTime");
            int writeOperationCountID = readInput.indexOf("WriteOperationCount");

            long idelTime = 0, kernelTime = 0, userTime = 0;

            while ((readInput = lineNumberInput.readLine()) != null) {
                if (readInput.length() < writeOperationCountID) {
                    continue;
                }

                String caption = readInput.substring(captionID, commandID - 1).trim();
                String command = readInput.substring(commandID, kernelModeTimeID - 1).trim();

                if (command.indexOf("wmic.exe") >= 0) {
                    continue;
                }

                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    idelTime += Long.valueOf(Bytes.substring(readInput, kernelModeTimeID, readOperationCountID - 1).trim()).longValue();
                    idelTime += Long.valueOf(Bytes.substring(readInput, userModeTimeID, writeOperationCountID - 1).trim()).longValue();
                    continue;
                }
                kernelTime += Long.valueOf(Bytes.substring(readInput, kernelModeTimeID, readOperationCountID - 1).trim()).longValue();
                userTime += Long.valueOf(Bytes.substring(readInput, userModeTimeID, writeOperationCountID - 1).trim()).longValue();
            }
            returnCPUInfo[0] = idelTime;
            returnCPUInfo[1] = kernelTime + userTime;
            return returnCPUInfo;
        } catch (Exception e) {
        } finally {
            try {
                pc.getInputStream().close();
            } catch (Exception e) {
            }
        }

        return null;
    }
}

class Bytes {

    public static String substring(String readInput, int startID, int endID) {
        byte[] b = readInput.getBytes();
        String sub = "";
        for (int i = startID; i <= endID; i++) {
            sub += (char) b[i];
        }
        return sub;
    }
}
