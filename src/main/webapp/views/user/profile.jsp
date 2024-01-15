<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">User Profile</h1>
			</div>
			<div  class="d-flex flex-column gap-3">
				<div>
					<img src="${pageContext.request.contextPath}${user.profile}"
					class="d-block rounded-circle border mx-auto mb-3" alt="User Avartar"
					width="100" height="100">
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Name</span>
					<div class="col-sm-8">
						<p>${user.name}</p>
					</div>
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Email</span>
					<div class="col-sm-8">
						<p>${user.email}</p>
					</div>
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Type</span>
					<div class="col-sm-8">
						<p>${user.type}</p>
					</div>
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Phone</span>
					<div class="col-sm-8">
						<p>${user.phone}</p>
					</div>
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Date of Birth</span>
					<div class="col-sm-8">
						<p>${user.dob}</p>
					</div>
				</div>
				<div class="row">
					<span class="col-sm-4 fw-semibold">Address</span>
					<div class="col-sm-8">
						<p>${user.address}</p>
					</div>
				</div>
				<c:if test="${auth.isAdmin() || auth.id == user.id}">
				<a href="${pageContext.request.contextPath}/users/edit/${user.id}" class="btn btn-outline-primary w-100 mt-2">Edit</a>
				</c:if>
			</div>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
