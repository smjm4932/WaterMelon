package controller;

//FileInputStream -> ��û��ɾ ��ϵ� ������ �о���̱� ���ؼ�
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

public class ControllerAction extends HttpServlet{
	
    //��ɾ�� ��ɾ� ó��Ŭ������ ������ ����
    private Map commandMap = new HashMap();
    
	//������ ����� ������ �ʱ�ȭ �۾�->������
    public void init(ServletConfig config) throws ServletException {
    	
  //��ο� �´� CommandPro.properties������ �ҷ���
    String props = config.getInitParameter("propertyConfig"); // �ܺ��� �Ű������� �޾ƿ�
    System.out.println("�ҷ��°��="+props);
    
  //��ɾ�� ó��Ŭ������ ���������� ������
  //Properties��ü ����
    Properties pr = new Properties();
    FileInputStream f = null;//���Ϻҷ��ö� 
    
        try {
           //CommandPro.properties������ ������ �о��
        	f=new FileInputStream(props);
           
        	//������ ������ Properties�� ����
        	pr.load(f);
        	
        }catch(IOException e){
          throw new ServletException(e);
        }finally{
        if(f!=null) try{f.close();}catch(IOException ex){}	
        }
        	
     //��ü�� �ϳ��� ������ �� ��ü������ Properties
     //��ü�� ����� ��ü�� ����
     Iterator keyiter = pr.keySet().iterator();
     
     while(keyiter.hasNext()){ // ������ �����Ͱ� �����ϴ��� üũ
       //��û�� ��ɾ ���ϱ�����
       String command = (String)keyiter.next();
       System.out.println("command="+command);
       //��û�� ��ɾ�(Ű)�� �ش��ϴ� Ŭ�������� ����
       String className=pr.getProperty(command);
       System.out.println("className="+className);
       
       try{
       //�� Ŭ������ ��ü�� ���������� �޸𸮿� �ε�
       Class commandClass = Class.forName(className);
       System.out.println("commandClass="+commandClass);
       //Ŭ������.newInstance() => ��ü�� ���� �� �ִ�.
       Object commandInstance = commandClass.newInstance();
       System.out.println
              ("commandInstance="+commandInstance); // Ŭ������@�ּҰ�
      
       //Map��ü commandMap�� ����
       commandMap.put(command, commandInstance);
       System.out.println("commandMap="+commandMap);
       
            } catch (ClassNotFoundException e) {
                throw new ServletException(e);
            } catch (InstantiationException e) {
                throw new ServletException(e);
            } catch (IllegalAccessException e) {
                throw new ServletException(e);
            }
        }//while
    }

    public void doGet(//get����� ���� �޼ҵ�
                     HttpServletRequest request, 
                     HttpServletResponse response)
    throws ServletException, IOException {
    	    requestPro(request,response);
    }

    protected void doPost(//post����� ���� �޼ҵ�
                     HttpServletRequest request, 
                     HttpServletResponse response)
    throws ServletException, IOException {
    	    requestPro(request,response);
    }

    //�ÿ����� ��û�� �м��ؼ� �ش� �۾��� ó��
    private void requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String view=null; // ��û��ɾ ���� �̵��� �������� �̸�����
    	// list.do=action.ListAction->��ü
    	 //ListAction com=null; ListAction com=new ListAction();
    	CommandAction com=null; /// ��� �ڽ�Ŭ������ ��ü�� �θ������� ��ȯ
    	// CommandAction com=new ListAction(); ��ü����ȯ(���δ� �ڽ�)
    	// CommandAction com=new WriteFormAction();
    	try {
    		//��û��ɾ� �и� -> list.jsp
    		String command=request.getRequestURI();
    		System.out.println("request.getRequestURI()=>"+request.getRequestURI());
    		System.out.println("request.getContextPath()=>"+request.getContextPath());
    		// JspBoard2/list.do						/JspBoard
    		if(command.indexOf(request.getContextPath())==0) {
    			command=command.substring(request.getContextPath().length()); // substring(10)
    			System.out.println("�������� command=>"+command); // /list.do
    		} 
    		//��û��ɾ� -> /list.do->action.ListAction ��ü
    		com=(CommandAction)commandMap.get(command); //get(Ű��(��û��ɾ�))
    		System.out.println("com=>"+com); //action.ListAction@ �ּҰ�
    		view=com.requestPro(request, response);
    		System.out.println("view=>"+view); // /list.jsp
    	}catch(Throwable e) {
    		throw new ServletException(e); // ��������ó��
    	}
    	// ������ ��û��ɾ �ش��ϴ� view�� �����͸� ������Ű�鼭 �̵�
    	RequestDispatcher dispatcher=request.getRequestDispatcher(view); // /list.jsp
    	dispatcher.forward(request,  response);
    	
    }
}













