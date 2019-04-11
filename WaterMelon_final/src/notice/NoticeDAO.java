package notice;

//DB���
import java.sql.*;

//ArrayList,List�� ����ϱ� ���ؼ�
import java.util.*;

public class NoticeDAO {

	//DB ���� ��� �ʿ��� ������� ����
	private DBConnectionMgr pool=null; 
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	private String sql="";//�����ų SQL���� ����
	
	//������ ���ؼ� ����
	public NoticeDAO() {
		try {
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool=>"+pool);
		}catch(Exception e) {
			System.out.println("DB���ӿ���=>"+e);
		}
	}
	 
	
	//���ڵ� ������ ����.
	public int getNoticeCount() { 
		int x=0;//���ڵ尹��
		
		try {
			con=pool.getConnection();
			System.out.println("con="+con);
			sql="select count(*) from notice";  
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) { 
				x=rs.getInt(1);                    
			}
		}catch(Exception e) {
			System.out.println("getNoticeCount()�޼��� ��������"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	// �� ��Ϻ���
	public List getNotice(int start,int end) {
		List noticeList=null; 
		
		try {
			con=pool.getConnection();
			sql="select * from notice order by n_Num desc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, start-1);
			pstmt.setInt(2, end); 
			rs=pstmt.executeQuery();
			if(rs.next()) {
				noticeList=new ArrayList(end);
				do {
					NoticeDTO article=makeNotice();
					noticeList.add(article);
				}while(rs.next());
			}
		}catch(Exception e) {
			System.out.println("getNotice()�޼��� ��������"+e);
		}finally {
			pool.freeConnection(con, pstmt,rs);
		}
		return noticeList;
	}
	
	
	//����¡ ó���� ���� ���ִ� �޼��� �ۼ�
	public Hashtable pageList(String pageNum, int count) {
		
		Hashtable<String,Integer> pgList = new Hashtable<String, Integer>();
		
		int pageSize=6;
		int blockSize=3;
		
		if(pageNum==null) {
			pageNum="1";
		}
		int currentPage=Integer.parseInt(pageNum);//����������->nowPage
	    //���۷��ڵ��ȣ ->limit ?,?
	    //                  (1-1)*10+1=1,(2-1)*10+1=11,(3-1)*10+1=21
	    int startRow=(currentPage-1)*pageSize+1;		
	    int endRow=currentPage*pageSize;//1*10=10,2*10=20,3*10=30
	    int number=0; // beginPerPage -> �������� �����ϴ� �� ó���� ������ �Խù� ��ȣ
	    System.out.println("���� ���ڵ��(count)=>"+count);
	    number=count-(currentPage-1)*pageSize;
	    System.out.println("�������� number=>"+number);
	    
	  //�� ��������, ����, ���������� ���
	    int pageCount=count/pageSize+(count%pageSize==0?0:1);
	       //2.���������� 
	       //���� �������� ���->10->10���,3->3�� ���
	       int startPage=0;//1,2,3,,,,10 [������ 10],11,12,,,,,20
	       if(currentPage%blockSize!=0){ //1~9,11~19,21~29,,,
	    	   startPage=currentPage/blockSize*blockSize+1;
	       }else{ //10%10 (10,20,30,40~)
	    	   //             ((10/10)-1)*10+1=1
	    	  startPage=((currentPage/blockSize)-1)*blockSize+1; 
	       }
	       int endPage=startPage+blockSize-1;//1+10-1=10
	       System.out.println("startPage="+startPage+",endPage=>"+endPage);
	       //������ �����ؼ� ��ũ�ɾ ���
	       if(endPage > pageCount) endPage=pageCount;//������������=����������
	       
	     //~DAO -> �������� ������ ���õ� �ڵ� -> �׼�Ŭ������ ���� -> view(jsp)�� �������
	       pgList.put("pageSize", pageSize);
	       pgList.put("blockSize", blockSize);
	       pgList.put("currentPage", currentPage);
	       pgList.put("startRow", startRow);
	       pgList.put("endRow", endRow);
	       pgList.put("count", count);
	       pgList.put("number", number);
	       pgList.put("startPage", startPage);
	       pgList.put("endPage", endPage);
	       pgList.put("pageCount", pageCount);
	    
	    return pgList;
	}
	
	
	
	
	
	
	//---------------------------�ߺ��� ���ڵ� �ѹ��� ���� �� �ִ� �޼���----
	private NoticeDTO makeNotice() throws Exception{
		NoticeDTO article=new NoticeDTO();
		article.setN_Num(rs.getInt("n_Num"));
		article.setN_subject(rs.getString("n_subject"));
		article.setN_content(rs.getString("n_content"));
		article.setWritedate(rs.getTimestamp("writedate"));
		article.setReadCount(rs.getInt("readCount"));
		return article;
	}
	
	
}
