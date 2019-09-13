package it.univaq.ing.products.util;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.cloud.sleuth.Span;

public class Experiment{
	protected Logger logger = Logger.getLogger(Experiment.class.getName());

	private static void doInjection(int requestClass, List<String> latencyInjections){
		int latency  = Integer.parseInt(latencyInjections.get(requestClass));
		if (latency > 0) {
			try {
				Thread.sleep(latency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}

	public static void injectLatency(Span span, List<String> latencyInjections){
		String experiment = span.getBaggageItem("experiment");
		if (experiment != null){
			int requestClass = Integer.valueOf(experiment);
			doInjection(requestClass, latencyInjections);
		}
	}

}