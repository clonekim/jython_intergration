package bonjour;


import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JythonServlet extends HttpServlet {

    static {
        //https://github.com/jython/frozen-mirror/blob/master/registry 참고
        System.setProperty("python.import.site", "false");
        System.setProperty("python.cachedir.skip", "true");
        System.setProperty("python.path", ".");
    }

    static class JythonObjectFactory {
        private static JythonObjectFactory instance = null;


        protected JythonObjectFactory() {
        }

        public static JythonObjectFactory getInstance(){
            if(instance == null){
                instance = new JythonObjectFactory();
            }

            return instance;

        }


        public Object createObject(Object interfaceType, String moduleName){

            PythonInterpreter interpreter = new PythonInterpreter();
            interpreter.exec(String.format("from %s import %s", moduleName, moduleName));


            PyObject pyObject = interpreter.get(moduleName);

            try {

                PyObject newObj = pyObject.__call__();

                return newObj.__tojava__(Class.forName(interfaceType.toString().substring(interfaceType.toString().indexOf(" ") + 1)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

    }



    JythonObjectFactory jythonObjectFactory = JythonObjectFactory.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CodeMaker codeMaker = (CodeMaker) jythonObjectFactory.createObject(CodeMaker.class, req.getParameter("module"));

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        try(PrintWriter writer = resp.getWriter()) {
            writer.println(codeMaker.version());
        }

    }
}
