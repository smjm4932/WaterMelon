package action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
//�߰�
import notice.*;
import java.util.*;

//list.do=action.ListAction ����
public class ListAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
			String pageNum=request.getParameter("pageNum");
			
		    int count=0;//�ѷ��ڵ��
		    List noticeList=null;//ȭ�鿡 ����� ���ڵ带 ������ ����
		    
		    NoticeDAO dbPro=new NoticeDAO();
		    count=dbPro.getNoticeCount(); //sql������ ���� �޶�����.
		    System.out.println("���� �˻��� ���ڵ��(count)=>"+count);
		    
		    Hashtable<String,Integer> pgList=dbPro.pageList(pageNum,count);
		    
		    if(count > 0){
		    	System.out.println(pgList.get("startRow")+","+pgList.get("endRow"));
		    	noticeList=dbPro.getNotice(pgList.get("startRow"), pgList.get("pageSize")); //ù��° ���ڵ��ȣ,�ҷ��ð���
		    	                                                                       //endRow(X)
		    }else {
		    	noticeList=Collections.EMPTY_LIST; // �ƹ��͵� ���� �� list ��ü ��ȯ
		    }
		   
	
		//2. request ��ü�� ����
		    request.setAttribute("pgList", pgList); // ����¡ó�� 10������
		    request.setAttribute("articleList", noticeList); //${articleList}
		
		//3. �����ؼ� �̵��� �� �ֵ��� ����
		return "/notice.jsp"; // /board/list.jsp request.getAttribute("currentPage")=${currentPage}
	}

}
