package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;

// 컨트롤러의 서블릿 종속성을 제거하기 위해 Model을 직접 만들고 View 이름까지 전달하는 객체가 필요하다
public class ModelView {
    // 뷰의 이름(경로)
    private String viewName;
    // 모델 객체
    private Map<String, Object> model = new HashMap<>();

    // 객체를 생성하는 동시에 뷰의 이름을 받아오고, 그 후 모델 데이터 또한 받아온다.
    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
