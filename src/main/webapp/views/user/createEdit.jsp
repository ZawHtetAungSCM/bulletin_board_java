<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">
					<c:out value="${isEdit ? 'User Edit' : 'User Create'}" />
				</h1>
			</div>

			<c:if test="${isEdit}">
			<form action="${pageContext.request.contextPath}/users/edit/${user.id}" method="POST" enctype="multipart/form-data" class="d-flex flex-column gap-3">
			</c:if>
			<c:if test="${!isEdit}">
			<form action="${pageContext.request.contextPath}/users/create" method="POST" enctype="multipart/form-data" class="d-flex flex-column gap-3">
			</c:if>

				<div>
					<label for="name" class="form-label">Name</label>
					<input type="text" id="name" name="name" class="form-control" value="${fu.old('name', user.name)}" >
					<fe:error error="name" />
				</div>
				<div>
					<label for="email" class="form-label">Email</label>
					<input type="email" id="email" name="email" class="form-control" value="${fu.old('email', user.email)}" ${isEdit ? 'readonly': '' }>
					<fe:error error="email" />
				</div>
				<c:if test="${!isEdit}">
				<div>
					<label for="password" class="form-label">Password</label>
					<input type="password" id="password" name="password" class="form-control">
					<fe:error error="password" />
				</div>
				<div>
					<label for="confirmPassword" class="form-label">Confirm Password</label>
					<input type="password" id="confirmPassword" name="confirmPassword" class="form-control">
					<fe:error error="confirmPassword" />
				</div>
				</c:if>
				<div>
					<label for="type" class="form-label">Type</label>
					<select class="form-select" id="type" name="type" ${isEdit ? auth.isAdmin() ? '': 'disabled' : '' } >
						<option selected disabled>Select user type</option>
						<option value="1" ${fu.old('type', user.type.getCode()) eq '1' ? 'selected' : ''}>User</option>
						<option value="2" ${fu.old('type', user.type.getCode()) eq '2' ? 'selected' : ''}>Admin</option>
					</select>
					<fe:error error="type" />
				</div>
				<div>
					<label for="phone" class="form-label">Phone</label>
					<input type="tel" id="phone" name="phone" class="form-control" value="${fu.old('phone', user.phone)}">
					<fe:error error="phone" />
				</div>
				<div>
					<label for="dob" class="form-label">Date of Birth</label>
					<input type="date" id="dob" name="dob" class="form-control" value="${fu.old('dob', user.dob)}">
					<fe:error error="dob" />
				</div>
				<div>
					<label for="address" class="form-label">Address</label>
					<textarea class="form-control" id="address" name="address" rows="2">${fu.old('address', user.address)}</textarea>
					<fe:error error="address" />
				</div>
				<div>
					<label for="profile" class="form-label">Profile</label>
					<input type="file" id="profile" name="profile" class="form-control" accept="image/*" onchange="preview()">
					<fe:error error="profile" />
					<img id="imagePreview" src="${pageContext.request.contextPath}${user.profile}" class="d-block rounded-3 border mt-2" alt="User Profile" width="100" height="100">
				</div>
				<button type="submit" class="btn btn-primary w-100 mt-2">Confirm</button>
				<button type="button" class="btn btn-outline-primary w-100 mt-2">Clear</button>
				<c:if test="${isEdit && auth.id == user.id}">
				<a href="${pageContext.request.contextPath}/change-password" class="link-primary text-center link-offset-1 mt-4">Change Password</a>
				</c:if>
			</form>
		</div>
	</div>
</div>

 <script>
    function preview() {
    	var inputElement = event.target;
		if (inputElement.files && inputElement.files[0]) {
			// Update the src attribute
			 imagePreview.src = URL.createObjectURL(inputElement.files[0]);
		}
    }
</script>

<%@ include file="/Layout/app-footer.jsp"%>
