package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// v2 패키지 하위에서 발생하는 모든 요청을 이 서블릿에서 받아들인다.
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

    // key: 매핑 URI, value: 호출 될 컨트롤러
    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    // URI 매핑
    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    // 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출한다
    // 각각의 컨트롤러는 MyView 객체를 생성해서 반환하기만 하면 된다.
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV2.service");

        // requestURI 를 조회하여 실제 호출할 컨트롤러를 controllerMap 에서 찾는다
        String requestURI = request.getRequestURI();
        ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) { // 찾는 컨트롤러가 없을 경우 404 상태 코드 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        MyView view = controller.process(request, response);
        view.render(request, response);

    }

}
