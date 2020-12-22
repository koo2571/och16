package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommandProcess;


//@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//    /updateForm.do service.UpdateFormAction
    private Map<String, Object> commandMap = new HashMap<String, Object>();   
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		//web.xml에서 propertyConfig에 해당하는 init-param 의 값을 읽어옴
		String props = config.getInitParameter("config");
		System.out.println("Controller init props ->"+props);
		//명령어와 처리클래스의 매핑정보를 저장할 Properties객체 생성 ( '='기준 key, value 값을 지정)
		Properties pr = new Properties();
		FileInputStream f= null;
		try {
			String configFilePath = config.getServletContext().getRealPath(props);
			System.out.println("Controller init configFilePath ->"+configFilePath);
			f = new FileInputStream(configFilePath);
			pr.load(f);
		} catch (IOException e) {
			throw new ServletException(e);
		}finally {
			if(f != null) try {
				f.close();
			}catch(IOException ex) {}
		}
		//Iterator객체는 Enumeration객체를 확장시킨 개념의 객체
		Iterator keyIter = pr.keySet().iterator();
		//객체를 하나씩 꺼내서 그 객체명으로 properties객체에 저장된 객체에 접근
		while(keyIter.hasNext()) {
			String command = (String)keyIter.next(); // /updateForm.do
			String className = pr.getProperty(command); //service.UpdateFormAction
			// UpdateFormAction ufa = new UpdateFormAction();
			try {
				Class commandClass = Class.forName(className);//해당문자열을 클래스로 만든다
				Object commandInstance = commandClass.newInstance();//해당클래스의 객체를 생성
				commandMap.put(command, commandInstance);//Map 객체인 commandMap에 객체 저장
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestPro(request, response);
	}
	
	//사용자의 요청을 분석해서 해당 작업을 처리
	private void requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException{
		String view = null;
		CommandProcess com = null;
		String command = request.getRequestURI();
		try {
			System.out.println("requestPro command 1 ->"+command); // /och16/list.do
			//System.out.println(request.getContextPath()); // /och16
			//System.out.println(command.indexOf(request.getContextPath())); // 0
			//if(command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
			//}
			com = (CommandProcess)commandMap.get(command);
			System.out.println("requestPro command 2 ->"+command);
			System.out.println("requestPro com ->"+com);
			view = com.requestPro(request, response);               //updateForm.jsp
			System.out.println("requestPro view->"+view);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
	
	
}
