<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">
					<c:out value="${isEdit ? 'Post Edit Confirm' : 'Post Create Confirm'}" />
				</h1>
			</div>

			<c:if test="${isEdit}">
			<form action="${pageContext.request.contextPath}/posts/confirm/${post.id}" method="POST" class="d-flex flex-column gap-3">
			</c:if>
			<c:if test="${!isEdit}">
			<form action="${pageContext.request.contextPath}/posts/confirm" method="POST" class="d-flex flex-column gap-3">
			</c:if>
				<div class="row">
					<label for="title" class="col-sm-4 col-form-label">Title</label>
					<div class="col-sm-8">
						<input type="text" id="title" name="title" value="${post.title}" readonly class="form-control-plaintext">
					</div>
				</div>
				<div class="row">
					<label for="description" class="col-sm-4 col-form-label">Description</label>
					<div class="col-sm-8">
						<textarea id="description" name="description" rows="4" readonly class="form-control-plaintext">${post.description}</textarea>
					</div>
				</div>
				<div class="row">
					<label for="status" class="col-sm-4 col-form-label">Status</label>
					<div class="col-sm-8 form-switch">
						<input id="status" name="status" ${post.status.getCode() == 1 ? 'checked' : ''} class="form-check-input" type="checkbox" role="switch" onclick="return false;" style="margin-top: 11px">
					</div>
				</div>
				<button type="submit" class="btn btn-primary w-100 mt-2"><c:out value="${isEdit ? 'Update' : 'Create'}" /></button>
				<button type="button" class="btn btn-outline-primary w-100 mt-2">Cancel</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
