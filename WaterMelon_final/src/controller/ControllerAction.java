package controller;

//FileInputStream -> 요청명령어가 등록된 파일을 읽어들이기 위해서
import java.io.FileInputStream;
import java.io.IOException;
//Map,Properties
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import action.CommandAction;

public class ControllerAction extends HttpServlet {

	// 명령어와 명령어 처리클래스를 쌍으로 저장
	private Map commandMap = new HashMap();

	// 서블릿을 실행시 서블릿의 초기화 작업->생성자
	public void init(ServletConfig config) throws ServletException {

		// 경로에 맞는 CommandPro.properties파일을 불러옴
		String props = config.getInitParameter("propertyConfig");// 외부의 매개변수를 받아옴
		System.out.println("불러온경로=" + props);

		// 명령어와 처리클래스의 매핑정보를 저장할
		// Properties객체 생성
		Properties pr = new Properties();
		FileInputStream f = null;// 파일불러올때

		try {
			// CommandPro.properties파일의 내용을 읽어옴
			f = new FileInputStream(props);

			// 파일의 정보를 Properties에 저장
			pr.load(f);

		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ex) {
				}
		}

		// 객체를 하나씩 꺼내서 그 객체명으로 Properties
		// 객체에 저장된 객체를 접근
		Iterator keyiter = pr.keySet().iterator();

		while (keyiter.hasNext()) {// 꺼내올 데이터가 존재하는지 체크
			// 요청한 명령어를 구하기위해
			String command = (String) keyiter.next();
			System.out.println("command=" + command); // /list.do
			// 요청한 명령어(키)에 해당하는 클래스명을 구함
			String className = pr.getProperty(command);// getProperty(키명)
			System.out.println("className=" + className);//

			try {
				// 그 클래스의 객체를 얻어오기위해 메모리에 로드
				Class commandClass = Class.forName(className);
				System.out.println("commandClass=" + commandClass);
				// 클래스명.newInstance()=>객체를 얻어올 수 있다.
				Object commandInstance = commandClass.newInstance();
				System.out.println("commandInstance=" + commandInstance);// 클래스명@주소값

				// Map객체 commandMap에 저장
				commandMap.put(command, commandInstance);// 키명(요청명령어),실질적인 객체저장
				System.out.println("commandMap=" + commandMap);

			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			} catch (InstantiationException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			}
		} // while
	}

	public void doGet(// get방식의 서비스 메소드
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request, response);
	}

	protected void doPost(// post방식의 서비스 메소드
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request, response);
	}

	// 시용자의 요청을 분석해서 해당 작업을 처리
	private void requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String view = null;// 요청명령어에 따라서 이동할 페이지의 이름저장
		// /list.do=action.ListAction->객체
		// ListAction com=null; ListAction com=new ListAction();
		CommandAction com = null;// 어떠한 자식클래스의 객체라도 부모형으로 변환
		// CommandAction com=new ListAction(); 객체형변환(내부는 자식)
		// CommandAction com=new WriteFormAction();
		try {
			// 요청명령어분리->list.jsp
			String command = request.getRequestURI();
			System.out.println("request.getRequestURI()=>" + request.getRequestURI());
			System.out.println("request.getContextPasssth()=>" + request.getContextPath());
			// /JspBoard2/list.do /JspBoard
			if (command.indexOf(request.getContextPath()) == 0) {
				command = command.substring(request.getContextPath().length());// substring(10)
				System.out.println("실질적인 command=>" + command);// /list.do
			}
			// 요청명령어-> /list.do->action.ListAction객체
			com = (CommandAction) commandMap.get(command);// get(키명(요청명령어))
			System.out.println("com=>" + com);// action.ListAction@주소값
			view = com.requestPro(request, response);
			System.out.println("view=>" + view);// /list.jsp
		} catch (Throwable e) {
			throw new ServletException(e);// 서블릿예외처리
		}
		// 위에서 요청명령어에 해당하는 view로 데이터를 공유시키면서 이동
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);// /list.jsp
		dispatcher.forward(request, response);
	}
}
