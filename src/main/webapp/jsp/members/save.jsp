<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");

    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    성공!
    <ul style="border: solid black;">
        <li>Id = <%=member.getId()%></li>
        <li>Username = <%=member.getUsername()%></li>
        <li>Age = <%=member.getAge()%></li>
    </ul>

    <a href="/index.html">메인</a>
</body>
</html>
