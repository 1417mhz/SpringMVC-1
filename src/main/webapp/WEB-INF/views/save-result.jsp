<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공!
<ul style="border: solid black;">
<%--    모델에 저장한 member 객체를 꺼내려면 문법이 굉장히 복잡해진다 --%>
<%--    JSP는 이 문제를 해결하기 위해 간결한 문법을 제공한다 - ${} --%>
<%--    ${member.~} 를 이용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다 --%>
  <li>Id = ${member.id}</li>
  <li>Username = ${member.username}</li>
  <li>Age = ${member.age}</li>
</ul>

<a href="/index.html">메인</a>
</body>
</html>
