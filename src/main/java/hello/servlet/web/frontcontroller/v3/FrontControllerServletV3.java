package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// v2 패키지 하위에서 발생하는 모든 요청을 이 서블릿에서 받아들인다.
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    // key: 매핑 URI, value: 호출 될 컨트롤러
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    // URI 매핑
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    // 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출한다
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        // requestURI 를 조회하여 실제 호출할 컨트롤러를 controllerMap 에서 찾는다
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) { // 찾는 컨트롤러가 없을 경우 404 상태 코드 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        // request 객체에서 가져온 데이터를 Map에 담는다
        Map<String, String> paramMap = createParamMap(request);

        // 실제 컨트롤러를 호출하고 ModelView 객체를 리턴받는다
        // ModelView 객체에는 논리 path, 모델(paramMap)이 담겨있다
        ModelView mv = controller.process(paramMap);

        // 논리 path를 꺼낸다
        String viewName = mv.getViewName();
        // MyView 객체를 생성하면서 viewPath를 초기화함
        // viewPath = /WEB-INF/views/논리path.jsp
        MyView view = viewResolver(viewName);

        // MyView 객체를 이용해 뷰 렌더링
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        // 논리 path를 물리 뷰 path로 수정하여 생성자에 전달하고, 생성된 MyView 객체를 반환
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        // request 객체로 넘어온 데이터를 반복문을 통해 Map에 담아 반환한다
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }

}
