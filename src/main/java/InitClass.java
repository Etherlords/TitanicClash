import net.Server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitClass {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static PrintStream print = System.out;

    public static void main(String[] argv) throws IOException, InterruptedException
    {
        new InitClass();
    }

    public InitClass() throws IOException, InterruptedException
    {
        Server gameServer = new Server();


    }

}
