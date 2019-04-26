package it.univaq.ing.categories.util;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.cloud.sleuth.Span;

public class Experiment{
	protected Logger logger = Logger.getLogger(Experiment.class.getName());

	public static void injectLatency(Span span, List<String> latencyInjections){
        int requestClass = Integer.valueOf(span.getBaggageItem("experiment"));
        int latency  = Integer.parseInt(latencyInjections.get(requestClass));
        if (latency > 0) {
			try {
				Thread.sleep(latency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}