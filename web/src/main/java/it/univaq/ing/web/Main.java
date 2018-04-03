package it.univaq.ing.web;

import java.net.SocketException;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Allow the servers to be invoke from the command-line. The jar is built with
 * this as the <code>Main-Class</code> in the jar's <code>MANIFEST.MF</code>.
 * 
 * @author LC
 */
public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws SocketException, ParseException {

		String eurekaIp = "registration" ;
		String dbServer = "web-db";
		String maxThreads = "500";
		String serverPort;

		String serverName = " ";

		

		// default config
		System.setProperty("server.tomcat.max-threads", maxThreads);
		System.setProperty("spring.datasource.max-active", String.valueOf(Integer.valueOf(maxThreads) * 5));
		System.setProperty("spring.datasource.max-idle",
				String.valueOf((int) Math.ceil(Integer.valueOf(maxThreads) * 3 / 4)));
		System.setProperty("spring.datasource.max-wait", "60000");
		System.setProperty("spring.datasource.min-idle",
				String.valueOf((int) Math.ceil(Integer.valueOf(maxThreads) / 4)));

		System.setProperty("eureka.ip", "registration");
		System.setProperty("datasource.url", dbServer);
		System.setProperty("server.address", "registration");

		serverName = "registration";
		System.setProperty("spring.config.name", serverName + "-server");
	}


	protected static void usage() {
		System.out.println("Usage: java -jar ... <server-name> [server-port]");
		System.out.println("     where server-name is 'registration', " + "'accounts' or 'web' and server-port > 1024");
	}

	public static Options options() {
		Options options = new Options();
		options.addOption("e", "eureka", true, "eureka server");
		options.addOption("db", "database", true, "database server");
		options.addOption("t", "maxThreads", true, "max threads");
		options.addOption("s", "serverName", true, "server name");
		options.addOption("p", "serverPort", true, "server port");
		options.addOption("h", "help", false, "help");
		return options;
	}

	public static void help() {
		System.out.println("e | eureka -> eureka server\n" + "db ! database -> database server\n"
				+ "t | maxThreads -> max threads\n" + "s | serverName -> server name\n"
				+ "p | serverPort -> server port\n" + "h | help -> help\n");
	}
}
