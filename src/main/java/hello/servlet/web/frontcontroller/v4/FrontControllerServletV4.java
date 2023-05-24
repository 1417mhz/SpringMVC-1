package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// v4 패키지 하위에서 발생하는 모든 요청을 이 서블릿에서 받아들인다.
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    /** 프론트 컨트롤러가 모델까지 만들어 파라미터로 전달하도록 함으로써
     *  각 컨트롤러에서 MyView 객체를 생성하고 반환하는 과정을 없앤다
     */

    // key: 매핑 URI, value: 호출 될 컨트롤러
    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    // URI 매핑
    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    // 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출한다
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV4.service");

        // requestURI 를 조회하여 실제 호출할 컨트롤러를 controllerMap 에서 찾는다
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) { // 찾는 컨트롤러가 없을 경우 404 상태 코드 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        // request 객체에서 가져온 데이터를 Map에 담는다
        Map<String, String> paramMap = createParamMap(request);

        // Model 생성
        // 컨트롤러에서 모델 객체에 값을 담으면 여기에 그대로 담겨있게 된다
        Map<String, Object> model = new HashMap<>();

        // 실제 컨트롤러를 호출하고 viewName을 리턴받는다
        String viewName = controller.process(paramMap, model);

        // MyView 객체를 생성하면서 viewPath를 초기화함
        // viewPath = /WEB-INF/views/논리path.jsp
        MyView view = viewResolver(viewName);

        // MyView 객체를 이용해 뷰 렌더링
        view.render(model, request, response);
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
