package by.aab.isp.dto.converter;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import org.springframework.stereotype.Component;

@Component
public class FormatUtil {

    private static final String[] BANDWIDTH_RANGES = {" Kb/s", " Mb/s", " Gb/s", " Tb/s"};

    public String formatBandwidth(int kbps) {
        if (kbps >= BANDWIDTH_UNLIMITED || kbps < 0) {
            return "unlimited";
        }
        double bandwidth = kbps;
        for (int i = 0; i < BANDWIDTH_RANGES.length - 1; i++) {
            if (bandwidth < 1024) {
                return bandwidth + BANDWIDTH_RANGES[i];
            }
            bandwidth = bandwidth / 1024;
        }
        return bandwidth + BANDWIDTH_RANGES[BANDWIDTH_RANGES.length - 1];
    }

    private static final String[] TRAFFIC_RANGES = {" B", " KB", " MB", " GB", " TB", " PB"};

    public String formatTraffic(long bytes) {
        if (bytes >= TRAFFIC_UNLIMITED || bytes < 0) {
            return "unlimited";
        }
        if (bytes < 1024) {
            return bytes + TRAFFIC_RANGES[0];
        }
        double traffic = (double) bytes / 1024;
        for (int i = 1; i < TRAFFIC_RANGES.length - 1; i++) {
            if (traffic < 1024) {
                return traffic + TRAFFIC_RANGES[i];
            }
            traffic = traffic / 1024;
        }
        return traffic + TRAFFIC_RANGES[TRAFFIC_RANGES.length - 1];
    }

}
