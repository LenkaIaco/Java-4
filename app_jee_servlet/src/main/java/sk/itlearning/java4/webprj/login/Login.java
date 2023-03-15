package sk.itlearning.java4.webprj.login;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String title = "Current time";
		//web requesty su bud web request alebo odpoved nan. Dole je nasa odpoved. Ked pustime web server a deployneme nas servlet tak mame zadefinovany ze obsah co sa vygeneruje je cez Login class
		//MOZNOST 1: html priamo v java tirede ako tuto. Ale, v realite nepiseme html kod ako dole do javy ale napiseme len odkaz na source dokument( napr login.html) kt je tam naformatovany
//		resp.getWriter().append("<!DOCTYPE html>\n");
//		resp.getWriter().append("<html>\n");
//		resp.getWriter().append("<head>\n");
//		resp.getWriter().append("<title>");
//		resp.getWriter().append(title);
//		resp.getWriter().append("</title>\n");
//		resp.getWriter().append("</head>\n");
//		resp.getWriter().append("<h2 style='color:blue'>" + LocalDateTime.now().toString() + "</h2>");
//	
		
		//MOZNOST 2: v realite robime html kod uz ako tuto dole, nie priamo do javy ako dam hore:
		InputStream is2 = getServletContext().getResourceAsStream("/frontend/src/login.html");//relativna cesta k suboru. Povieme odkial WS ma nacitat co ma zobrazit ako odpoved na web request.
		String html = IOUtils.toString(is2, StandardCharsets.UTF_8.name());
		resp.getWriter().append(html);//getWriter urci co v responsi obsahovat, ale musi byt typu string takze 2 riadky hore sme museli najprv upravit z InputStreamu na String
		//teraz ked sme zmenili response, na Moznost 2 musimev Console stopnut(cerveny stvorcek) predosly Jetty Server.
		//Potom ideme do priecinku v PC: app_jee_servlet - target - a vymazeme subor servletwebapp.war(to je nasa vybuildovana appka mavenom, co obsahuje odpoved ktoru ma nas WS vratit na WB
		// ked je vymazany, dame v Project Explorer - app_jee_servlet - Run As.. - Maven Install(aby nam urobil novy servletwebapp.war)
		// ked je urobeny, dame  app_jee_servlet - Run As.. - bud Maven Build... - jetty:run //alebo cez skratky zel.trojuholnik: m2 - app?jee?servlet
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
