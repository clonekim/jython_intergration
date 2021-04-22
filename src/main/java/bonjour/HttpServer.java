package bonjour;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.Servlet;

public class HttpServer {

    private Server server;

    private ServerConnector connector;

    private ServletContextHandler context;


    public HttpServer() {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("server");
        server = new Server(threadPool);
        connector = new ServerConnector(server);
        context = new ServletContextHandler();

        server.addConnector(connector);
        server.setHandler(context);
    }


    void setPort(int port) {
        this.connector.setPort(port);
        this.connector.setHost("0.0.0.0");
    }

    void setClassPathResource(String name) {
        this.context.setBaseResource(Resource.newClassPathResource(name));
        this.context.setWelcomeFiles(new String[]{ "index.html" });
    }


    void listen() throws Exception {
        this.server.start();
        this.server.join();
    }


    void setContextPath(String path) {
        this.context.setContextPath(path);
        ServletHolder defaultServlet = context.addServlet(DefaultServlet.class, path);
        defaultServlet.setInitParameter("dirAllowed", "false");
    }


    void addServlet(Class<? extends Servlet> servlet, String pathSpec) {
        this.context.addServlet(servlet, pathSpec);
    }

    public static void main(String[] args) throws Exception {

        HttpServer server = new HttpServer();
        server.setPort(8080);
        server.setClassPathResource("/public");
        server.setContextPath("/");
        server.addServlet(JythonServlet.class, "/interpreter");
        server.listen();
    }

}
