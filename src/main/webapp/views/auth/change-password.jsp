<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">Change Password</h1>
			</div>

			<form action="${pageContext.request.contextPath}/change-password" method="POST" class="d-flex flex-column gap-3">
				<div>
					<label for="oldPassword" class="form-label">Old Password</label>
					<input type="password" id="oldPassword" name="oldPassword" class="form-control" value="${fu.old('oldPassword')}" >
					<fe:error error="oldPassword" />
				</div>
				<div class="mt-4">
					<label for="newPassword" class="form-label">New Password</label>
					<input type="password" id="newPassword" name="newPassword" class="form-control" value="${fu.old('newPassword')}" >
					<fe:error error="newPassword" />
				</div>
				<div>
					<label for="confirmPassword" class="form-label">Confirm Password</label>
					<input type="password" id="confirmPassword" name="confirmPassword" class="form-control" value="${fu.old('confirmPassword')}" >
					<fe:error error="confirmPassword" />
				</div>

				<button type="submit" class="btn btn-primary w-100 mt-2">Confirm</button>
				<button type="button" class="btn btn-outline-primary w-100 mt-2">Clear</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
