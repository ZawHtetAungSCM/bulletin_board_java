<%@ page import="com.mtm.bulletin_board.utils.FormUtils" %>
<%@ page isELIgnored="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/lib/form-error" prefix="fe" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bulletin Board</title>

<!-- Include Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/bootstrap/css/bootstrap.css">

<!-- Main CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body class="d-flex flex-column" style="min-height: 100vh;">
<%
	FormUtils fu = new FormUtils(request);
	request.setAttribute("fu",fu);
%>