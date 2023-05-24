package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    // 생성자 호출과 동시에
    public FrontControllerServletV5() {
        // 핸들러 매핑
        initHandlerMappingMap();
        // 핸들러 어댑터 매핑
        initHandlerAdapters();
    }

    // 핸들러 매핑
    private void initHandlerMappingMap() {
        // v3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // v4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    // 핸들러 어댑터 매핑
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV5.service");

        // 1) 핸들러(컨틀롤러에서 명칭 변경)를 찾아온다
        Object handler = getHandler(request);
        if (handler == null) { // 찾는 핸들러가 없을 경우 404 상태 코드 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return;
        }

        // 2) 핸들러 어댑터를 찾아온다
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        // 3, 4, 5) 핸들러 어댑터를 통해 실제 핸들러를 호출하고 ModelView 객체를 리턴받는다
        //          v4의 경우 어댑터에서 ModelView 객체를 생성하고 리턴한다
        ModelView mv = adapter.handle(request, response, handler);

        // 논리 path를 꺼낸다
        String viewName = mv.getViewName();
        // 6, 7) MyView 객체를 생성하면서 viewPath를 초기화함
        //       viewPath = /WEB-INF/views/논리path.jsp
        MyView view = viewResolver(viewName);

        // 8) MyView 객체를 이용해 뷰 렌더링
        view.render(mv.getModel(), request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        // requestURI 를 조회하여 실제 호출할 컨트롤러를 controllerMap 에서 찾는다
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            // 어댑터들 중에 해당 타입의 핸들러를 호환하는 어댑터가 있을 경우 리턴
            if (adapter.support(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("* handler adapter를 찾을 수 없습니다. handler = " + handler);
    }

    private MyView viewResolver(String viewName) {
        // 논리 path를 물리 뷰 path로 수정하여 생성자에 전달하고, 생성된 MyView 객체를 반환
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}
