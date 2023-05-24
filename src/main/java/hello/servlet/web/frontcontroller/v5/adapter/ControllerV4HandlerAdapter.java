package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {

    // 어댑터가 핸들러 지원 여부 확인 (타입 확인)
    @Override
    public boolean support(Object handler) {
        // ControllerV4 타입의 핸들러(컨트롤러)가 넘어오면 true를 반환함
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        // handler = Member ~ ControllerV4
        // 핸들러를 캐스팅해 ControllerV4 타입으로 바꾼다
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request);
        HashMap<String, Object> model = new HashMap<>();
        // 실제 핸들러를 호출하고 viewName을 반환받는다
        String viewName = controller.process(paramMap, model);

        // 컨트롤러에서 ModelView 객체를 생성하지 않으므로 어댑터에서 그 역할을 대신한다
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;

    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        // request 객체로 넘어온 데이터를 반복문을 통해 Map에 담아 반환한다
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }

}
