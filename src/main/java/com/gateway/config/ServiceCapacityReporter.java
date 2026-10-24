package com.gateway.config;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;

@Component
public class ServiceCapacityReporter implements InfoContributor {

    private final ObjectProvider<ApplicationInfoManager> applicationInfoManager;

    public ServiceCapacityReporter(ObjectProvider<ApplicationInfoManager> applicationInfoManager) {
        this.applicationInfoManager = applicationInfoManager;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("capacity", snapshot());
    }

    @Scheduled(fixedDelay = 15000)
    public void publishToEureka() {
        ApplicationInfoManager manager = applicationInfoManager.getIfAvailable();
        if (manager == null || manager.getInfo() == null) {
            return;
        }
        Map<String, Object> snap = snapshot();
        Map<String, String> metadata = new LinkedHashMap<>();
        snap.forEach((key, value) -> metadata.put(key, String.valueOf(value)));
        manager.registerAppMetadata(metadata);
    }

    static Map<String, Object> snapshot() {
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        long heapUsed = memory.getHeapMemoryUsage().getUsed();
        long heapMax = memory.getHeapMemoryUsage().getMax();
        long max = runtime.maxMemory();
        long used = runtime.totalMemory() - runtime.freeMemory();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("heap.used.mb", heapUsed / (1024 * 1024));
        data.put("heap.max.mb", heapMax / (1024 * 1024));
        data.put("heap.usage.percent", heapMax > 0 ? Math.round(heapUsed * 1000.0 / heapMax) / 10.0 : 0);
        data.put("jvm.used.mb", used / (1024 * 1024));
        data.put("jvm.max.mb", max / (1024 * 1024));
        data.put("jvm.free.mb", (max - used) / (1024 * 1024));
        data.put("cpu.cores", os.getAvailableProcessors());
        data.put("system.load", os.getSystemLoadAverage());
        if (os instanceof com.sun.management.OperatingSystemMXBean sunOs) {
            data.put("process.cpu.percent", Math.round(sunOs.getProcessCpuLoad() * 1000.0) / 10.0);
            data.put("system.cpu.percent", Math.round(sunOs.getCpuLoad() * 1000.0) / 10.0);
        }
        return data;
    }
}
