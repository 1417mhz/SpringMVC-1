package hello.servlet.web.frontcontroller.v1;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// v1 패키지 하위에서 발생하는 모든 요청을 이 서블릿에서 받아들인다.
@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    // key: 매핑 URI, value: 호출 될 컨트롤러
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    // URI 매핑
    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    // 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출한다
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        // requestURI 를 조회하여 실제 호출할 컨트롤러를 controllerMap 에서 찾는다
        String requestURI = request.getRequestURI();
        ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) { // 찾는 컨트롤러가 없을 경우 404 상태 코드 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }
        controller.process(request, response);

    }

}
