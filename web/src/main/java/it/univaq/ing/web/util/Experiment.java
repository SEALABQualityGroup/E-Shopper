package it.univaq.ing.web.util;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.cloud.sleuth.Span;

public class Experiment{
	protected static Logger logger = Logger.getLogger(Experiment.class.getName());

	public static void injectLatency(Span span, List<String> latencyInjections){
        int requestClass = Integer.valueOf(span.getBaggageItem("experiment"));
        int latency  = Integer.parseInt(latencyInjections.get(requestClass));
        if (latency > 0) {
			try {
				logger.info("----------------------------");
				logger.info("latency:" + Integer.toString(latency));
				logger.info("----------------------------");
				Thread.sleep(latency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}