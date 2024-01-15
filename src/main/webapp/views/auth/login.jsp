<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>

<div class="d-flex align-items-center justify-content-center" style="height: 100vh;">
	<div class="bg-white border rounded-5 mx-2" style="width: 100%; max-width: 420px">
		<div class="p-5">
			<div class="mb-5">
				<h1 class="text-primary h-2 fw-bold">Login</h1>
				<p class="fs-6 text-secondary">Enter your account to sign in Bulletin Board.</p>
			</div>

			<form action="${pageContext.request.contextPath}/login" method="POST" class="d-flex flex-column gap-3">
				<div>
					<label for="email" class="form-label">Email</label>
					<input type="email" id="email" name="email" class="form-control" value="${fu.old('email')}" >
					<fe:error error="email" />
				</div>
				<div>
					<label for="password" class="form-label">Password</label>
					<input type="password" id="password" name="password" class="form-control">
					<fe:error error="password" />
				</div>
				<%-- <a href="#" class="link-primary link-offset-1">Forget Password</a> --%>
				<button type="submit" class="btn btn-primary w-100 mt-2">Login</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/auth-footer.jsp"%>
