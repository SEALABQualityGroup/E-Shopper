package it.univaq.ing.web.util;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


public class SyntheticModes {
	protected Logger logger = Logger.getLogger(SyntheticModes.class.getName());
	protected static Random random = new Random();

	public static void injectLatency(List<String> syntheticModes){
        int requestClass = random.nextInt(syntheticModes.size());
		int latency  = Integer.parseInt(syntheticModes.get(requestClass));
		
        if (latency > 0) {
			try {
				Thread.sleep(latency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}