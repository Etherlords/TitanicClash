package main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitClass {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static PrintStream print = System.out;

	static Logger log = Logger.getLogger(InitClass.class.getName());

    public static void main(String[] argv) throws IOException, InterruptedException
    {
	    ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
    }

}
