package it.univaq.ing.web.util;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.cloud.sleuth.Span;


public class SyntheticNoise {
	protected Logger logger = Logger.getLogger(SyntheticNoise.class.getName());
	protected static Random random = new Random();

	public static void doInjection(int requestClass, List<String> syntheticNoise){
		int latency  = Integer.parseInt(syntheticNoise.get(requestClass));

        if (latency > 0) {
			try {
				if (random.nextInt() % 2 == 0){
					Thread.sleep(latency);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void injectLatency(Span span, List<String> syntheticNoise){
		String experiment = span.getBaggageItem("experiment");
		if (experiment != null){
			int requestClass = Integer.valueOf(experiment);
			doInjection(requestClass, syntheticNoise);
		}
	}

}