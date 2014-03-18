package com.findwise.thesis.feeder.monitor;

/**
 *
 * @author Nawar Alkurdi
 */
public class MonitorInformation {

    private long totalMemory, freeMemory, maxMemory;
    private long totalMemorySize, freePhysicalMemorySize, usedMemory;
    private long totalDiskSize, freeDiskSize, usedDiskSize;
    private double cpuRatio;
    private int totalThread;

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public long getTotalMemorySize() {
        return totalMemorySize;
    }

    public void setTotalMemorySize(long totalMemorySize) {
        this.totalMemorySize = totalMemorySize;
    }

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getTotalDiskSize() {
        return totalDiskSize;
    }

    public void setTotalDiskSize(long totalDiskSize) {
        this.totalDiskSize = totalDiskSize;
    }

    public long getFreeDiskSize() {
        return freeDiskSize;
    }

    public void setFreeDiskSize(long freeDiskSize) {
        this.freeDiskSize = freeDiskSize;
    }

    public long getUsedDiskSize() {
        return usedDiskSize;
    }

    public void setUsedDiskSize(long usedDiskSize) {
        this.usedDiskSize = usedDiskSize;
    }

    public double getCPURatio() {
        return cpuRatio;
    }

    public void setCPURatio(double cpuRatio) {
        this.cpuRatio = cpuRatio;
    }

    public int getTotalThread() {
        return totalThread;
    }

    public void setTotalThread(int totalThread) {
        this.totalThread = totalThread;
    }
}