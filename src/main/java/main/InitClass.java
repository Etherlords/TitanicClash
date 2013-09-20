package main;

import net.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitClass {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static PrintStream print = System.out;

    public static void main(String[] argv) throws IOException, InterruptedException
    {
	    ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
	    HelloService helloService = context.getBean(HelloService.class);
	    Server gameServer = context.getBean(Server.class);

	    System.out.println(helloService.sayHello());
    }

}
