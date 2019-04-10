package action;

import java.sql.Timestamp;//추가할 부분(시간)

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//추가

import accounts.AccountsDAO;
import accounts.AccountsDTO;

public class SignUpAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub

		// 한글처리
		request.setCharacterEncoding("utf-8");
		// BoardDTO->Setter Method(5)+hidden (4)
		// BoardDAO 객체 필요
		AccountsDTO account = new AccountsDTO();
		account.setId(request.getParameter("id"));
		account.setPassword(request.getParameter("password"));
		account.setNickname(request.getParameter("nickname"));
		account.setName(request.getParameter("name"));
		account.setEmail(request.getParameter("email"));
		account.setAdmin(0);

		AccountsDAO mInsert = new AccountsDAO();
		boolean check = mInsert.memberInsert(account);

		if (check=false) {
			return "/sign-in-up/SignUpErr.jsp";
		}

		return "/sign-in-up/SignUp.jsp";
	}
}
