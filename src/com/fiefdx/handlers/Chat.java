package com.fiefdx.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Servlet implementation class Chat
 */
@WebServlet("/chat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private volatile Configuration cfg;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chat() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
    	Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
    	cfg.setServletContextForTemplateLoading(
                getServletContext(), "WEB-INF/templates");
    	cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    	this.cfg = cfg;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        Template t = cfg.getTemplate("chat/chat.ftl");
        
        Map root = new HashMap();
        root.put("current_nav", new String("Chat"));
        root.put("scheme", new String("http"));
        root.put("locale", new String("en_US"));
        root.put("User", new String("breeze"));
        
        try (PrintWriter writer = response.getWriter()) {
            try {
				t.process(root, writer);
				// t.dump(System.out);
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
