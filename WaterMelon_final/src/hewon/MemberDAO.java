package hewon;

//DB�� �����ؼ� ���ϴ� �����͸� �˻�,�Է�,����,������Ű�����ؼ�
//�ʿ���ϴ� �޼��带 ����->ȣ�� �� �� �ֵ��� ����� Ŭ����
//1.DBConnectionMgr--->MemberDAO�� �޼��� ȣ��(has a ����)

import java.sql.*;//DB
import java.util.*;//�ڷᱸ��->Vector,ArrayList,,,

public class MemberDAO {
	// 1.DBConnectionMgr��ü�� ����(�������)
	private DBConnectionMgr pool = null; // getConnection(),freeConnection()

	// �������� ������ ��� �ʿ��� �������
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";// �����ų SQL���� ����

	// 2.�����ڸ� ���ؼ� ��ü�� ������ ����
	public MemberDAO() {
		try {
			pool = DBConnectionMgr.getInstance();
			System.out.println("pool=>" + pool);
		} catch (Exception e) {
			System.out.println("DB���� ����->" + e);// e.toString()
		}
	}// ������
		// 1)�䱸�м��� ���� ȸ���α����� üũ�� ���ִ� �޼��� �ʿ�->
		// ����) public ��ȯ�� �޼����(�ڷ���(String) 2��)
		// select id,passwd from member where id='nup' and passwd='1234';

	public boolean loginCheck(String id, String passwd) {
		// 1.DB����
		boolean check = false;
		// 2.�����ų SQL->��ȯ�� ó��
		try {
			con = pool.getConnection();
			System.out.println("con=>" + con);
			sql = "select id,passwd from member where  id=? and passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			check = rs.next();// �����Ͱ� ����->true, ������ ->false
		} catch (Exception e) {
			System.out.println("logCheck()���� ��������->" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return check;
		// 3.DB��������
	}

	// 2)ȸ������->�ߺ� id�� üũ�� ���ִ� �޼��尡 �ʿ�
	// select id from member where id='nup';
	public boolean checkId(String id) {
		// 1.DB����
		boolean check = false;
		// 2.�����ų SQL->��ȯ�� ó��
		try {
			con = pool.getConnection();
			sql = "select id from member where  id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			check = rs.next();// �����Ͱ� ����->true, ������ ->false
		} catch (Exception e) {
			System.out.println("checkId()���� ��������->" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return check;
		// 3.DB��������
	}

	// 3)�����ȣ->�����ȣ�� �˻�->�ڵ����� �Է�
	// sql>
	public Vector<ZipcodeDTO> zipcodeRead(String area3) {
		Vector<ZipcodeDTO> vecList = new Vector();

		try {
			con = pool.getConnection();
			// select * from zipcode where area3 like '����3��%';
			sql = "select * from zipcode where area3 like '" + area3 + "%'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// ã�� ���ڵ尡 �Ѱ� if(rs.next()) �Ѱ��̻� ->while(rs.next())
			while (rs.next()) {
				// vector or ZipcodeDTO����(rs.getXXX(�ʵ��))->�ʵ庰�� ����,��������
				ZipcodeDTO tempZipcode = new ZipcodeDTO();
				tempZipcode.setZipcode(rs.getString("zipcode"));// "142-098"
				tempZipcode.setArea1(rs.getString("area1"));// "����"
				tempZipcode.setArea2(rs.getString("area2"));
				tempZipcode.setArea3(rs.getString("area3"));
				tempZipcode.setArea4(rs.getString("area4"));
				// vecor or ArrayList�� ��� ����
				vecList.add(tempZipcode);
			}
		} catch (Exception e) {
			System.out.println("zipcodeRead()�������=>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vecList;
	}

	// 4)ȸ������->insert into member values(?,?,?,?~);=>��ȯ�� 1,0
	// MemberDTO->1.���̺��� �ʵ庰�� ����,��� 2.�޼����� �Ű������� ��ȯ������ ���
	public boolean memberInsert(MemberDTO mem) {// ������ �Է��� �� ��ü��

		boolean check = false;// ȸ������ ��������
		// 2.�����ų SQL->��ȯ�� ó��
		try {
			con = pool.getConnection();
			// ----Ʈ����� ó��-----------------------------
			con.setAutoCommit(false);// �ڵ� commit�� �ϵ� ���ϵ��� ����
			// -------------------------------------------------
			sql = "insert into member values(?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem.getMem_id()); // (1,"test")
			pstmt.setString(2, mem.getMem_passwd());
			pstmt.setString(3, mem.getMem_name());
			pstmt.setString(4, mem.getMem_email());
			pstmt.setString(5, mem.getMem_phone());
			pstmt.setString(6, mem.getMem_zipcode());
			pstmt.setString(7, mem.getMem_address());
			pstmt.setString(8, mem.getMem_job());

			int insert = pstmt.executeUpdate();// ��ȯ 1 (����), 0 (����)
			System.out.println("insert(������ �Է�����)=>" + insert);
			con.commit();// ���������� insert �۵��ȴ�.
			if (insert > 0) {// if(insert==1)
				check = true;// ������ ����Ȯ��
			}
		} catch (Exception e) {
			System.out.println("memberInsert()���� ��������->" + e);
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return check;
	}

	// 5)ȸ������->Ư�� ȸ�� ã��->nup
	// ����)select * from member where id='nup';
	public MemberDTO getMember(String mem_id) {

		MemberDTO mem = null;// id���� �ش�Ǵ� ���ڵ� �Ѱ��� ������ ��ü����

		try {
			con = pool.getConnection();
			sql = "select * from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			// ã�� ���ڵ尡 �Ѱ� if(rs.next()) �Ѱ��̻� ->while(rs.next())
			if (rs.next()) {
				mem = new MemberDTO();
				// ã�� �ʵ尪->Setter Method����->���� ���->Getter Method
				mem.setMem_id(rs.getString("id")); // <%=mem.getMem_id()%>
				mem.setMem_passwd(rs.getString("passwd"));
				mem.setMem_name(rs.getString("name"));
				mem.setMem_phone(rs.getString("phone"));
				mem.setMem_zipcode(rs.getString("zipcode"));
				mem.setMem_address(rs.getString("address"));
				mem.setMem_email(rs.getString("e_mail"));
				mem.setMem_job(rs.getString("job"));
			}
		} catch (Exception e) {
			System.out.println("getMember()�������=>" + e);// �α����Ϸ� �ۼ�->���
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return mem;
	}

	// 6)ã�� �����͸� ����->ȸ�����԰� ����
	// update ���̺�� set �ʵ��=��,,, where ���ǽ�
	public boolean memberUpdate(MemberDTO mem) {

		boolean check = false;// ȸ������ ��������
		// 2.�����ų SQL->��ȯ�� ó��
		try {
			con = pool.getConnection();
			// ----Ʈ����� ó��-----------------------------
			con.setAutoCommit(false);// �ڵ� commit�� �ϵ� ���ϵ��� ����
			// -------------------------------------------------
			sql = "update member set passwd=?,name=?,e_mail=?,phone=?," + " zipcode=?,address=?, job=?  where  id=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem.getMem_passwd());
			pstmt.setString(2, mem.getMem_name());
			pstmt.setString(3, mem.getMem_email());
			pstmt.setString(4, mem.getMem_phone());
			pstmt.setString(5, mem.getMem_zipcode());
			pstmt.setString(6, mem.getMem_address());
			pstmt.setString(7, mem.getMem_job());
			pstmt.setString(8, mem.getMem_id());

			int update = pstmt.executeUpdate();// ��ȯ 1 (����), 0 (����)
			System.out.println("update(������ ��������)=>" + update);
			con.commit();// ���������� update �۵��ȴ�.
			if (update == 1) {
				check = true;// ��������
			}
		} catch (Exception e) {
			System.out.println("memberUpdate()���� ��������->" + e);
		} finally {
			pool.freeConnection(con, pstmt);// rs�� ����.(select�� �ƴ�)
		}
		return check;
	}

	// 7)ȸ�� Ż��->�����ų sql������ �ִ�
	// select passwd from member where id=?
	// ����)delete from member where id='nup';
	public int deleteMember(String id, String passwd) {
		String dbpasswd = "";// DB�󿡼� ã�� ��ȣ�� ����
		int x = -1;// ȸ��Ż�� ����

		try {
			con = pool.getConnection();
			con.setAutoCommit(false);// Ʈ�����ó��
			// 1.id���� �ش��ϴ� ��ȣ���� ã��
			pstmt = con.prepareStatement("select passwd from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			// ��ȣ�� �����Ѵٸ�
			if (rs.next()) {
				dbpasswd = rs.getString("passwd");// rs.getString(1);
				System.out.println("dbpasswd=>" + dbpasswd);
				// dbpasswd(DB�󿡼� ã����ȣ)==passwd(���󿡼� �Է��� ��)
				if (dbpasswd.equals(passwd)) {
					pstmt = con.prepareStatement("delete from member where id=?");
					pstmt.setString(1, id);
					int delete = pstmt.executeUpdate();
					System.out.println("delete(ȸ��Ż�� ��������)=>" + delete);
					con.commit();
					x = 1;// ȸ��Ż�� ����
				} else {
					x = 0;// ȸ��Ż�� ����->��ȣ�� Ʋ�����
				}
			} /*
				 * else { x=-1;//��ȣ�� �������� ���� ��� }
				 */
		} catch (Exception e) {
			System.out.println("deleteMember() ȣ���=>" + e); // con.rollback();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return x;// 1,0,-1
	}

	// 8)ȸ������Ʈ �� ����¡ ó��->�Խ����� �۸�Ϻ��� �� ����¡ ó��
	// select * from member ->getMemberList()
	// public ArrayList<MemberDTO> getMemberList(){}
	public ArrayList<MemberDTO> getMemberList() {

		ArrayList<MemberDTO> arrList = new ArrayList();

		try {
			con = pool.getConnection();
			sql = "select * from member";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDTO tempMember = new MemberDTO();
				tempMember.setMem_id(rs.getString("id"));
				tempMember.setMem_passwd(rs.getString("passwd"));
				tempMember.setMem_name(rs.getString("name"));
				tempMember.setMem_email(rs.getString("e_mail"));
				tempMember.setMem_zipcode(rs.getString("zipcode"));
				tempMember.setMem_address(rs.getString("address"));
				tempMember.setMem_job(rs.getString("job"));
				arrList.add(tempMember);
			}
		} catch (Exception e) {
			System.out.println("getMemberList()�޼��� ���� ���� =>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return arrList;
	}
}
