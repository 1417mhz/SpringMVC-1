package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* request.getParameter() 는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML Form 형식도 둘 다 지원한다. **/

        // 전체 파라미터 조회
        System.out.println("-- 전체 파라미터 조회 --");
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + " = " + request.getParameter(paramName)));
        System.out.println();

        // 단일 파라미터 조회
        System.out.println("-- 단일 파라미터 조회 --");
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        System.out.println("1. username = " + username);
        System.out.println("2. age = " + age);
        System.out.println();

        // 이름이 같은 복수 파라미터 조회
        System.out.println("-- 이름이 같은 복수 파라미터 조회 --");
        // 같은 키(username)의 값들을 가져옴
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name);
        }
        System.out.println();

        response.getWriter().write("OK");

    }
}
