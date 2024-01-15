<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">
					<c:out value="${isEdit ? 'User Edit Confirm' : 'User Create Confirm'}" />
				</h1>
			</div>

			<c:if test="${isEdit}">
			<form action="${pageContext.request.contextPath}/users/confirm/${user.id}" method="POST" class="d-flex flex-column gap-3">
			</c:if>
			<c:if test="${!isEdit}">
			<form action="${pageContext.request.contextPath}/users/confirm" method="POST" class="d-flex flex-column gap-3">
			</c:if>
				<div>
					<img src="${pageContext.request.contextPath}${user.profile}"
					class="d-block rounded-circle border mx-auto mb-3" alt="User Avartar"
					width="100" height="100">
					<input type="text" id="profile" name="profile" value="${user.profile}" readonly class="form-control-plaintext" hidden>
				</div>
				<div class="row">
					<label for="name" class="col-sm-4 col-form-label">Name</label>
					<div class="col-sm-8">
						<input type="text" id="name" name="name" value="${user.name}" readonly class="form-control-plaintext">
					</div>
				</div>
				<div class="row">
					<label for="email" class="col-sm-4 col-form-label">Email</label>
					<div class="col-sm-8">
						<input type="email" id="email" name="email" value="${user.email}" readonly class="form-control-plaintext">
					</div>
				</div>
				<c:if test="${!isEdit}">
				<div class="row">
					<label for="password" class="col-sm-4 col-form-label">Password</label>
					<div class="col-sm-8">
						<input type="password" id="password" name="password" value="${user.password}" readonly class="form-control-plaintext" >
					</div>
				</div>
				</c:if>
				<div class="row">
					<label for="type" class="col-sm-4 col-form-label">Type</label>
					<div class="col-sm-8">
						<input type="hidden" id="type" name="type" value="${user.type.getCode()}" readonly class="form-control-plaintext">
						<p class="form-control-plaintext">${user.type}</p>
					</div>
				</div>
				<div class="row">
					<label for="phone" class="col-sm-4 col-form-label">Phone</label>
					<div class="col-sm-8">
						<input type="tel" id="phone" name="phone" value="${user.phone}" readonly class="form-control-plaintext">
					</div>
				</div>
				<div class="row">
					<label for="dob" class="col-sm-4 col-form-label">Date of Birth</label>
					<div class="col-sm-8">
						<input type="date" id="dob" name="dob" value="${user.dob}" readonly class="form-control-plaintext">
					</div>
				</div>
				<div class="row">
					<label for="address" class="col-sm-4 col-form-label">Address</label>
					<div class="col-sm-8">
						<textarea id="address" name="address" rows="4" readonly class="form-control-plaintext">${user.address}</textarea>
					</div>
				</div>
				<button type="submit" class="btn btn-primary w-100 mt-2"><c:out value="${isEdit ? 'Update' : 'Create'}" /></button>
				<button type="button" class="btn btn-outline-primary w-100 mt-2">Cancel</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
