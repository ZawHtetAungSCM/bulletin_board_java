<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5">
    <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">
					<c:out value="${isEdit ? 'Post Edit' : 'Post Create'}" />
				</h1>
			</div>

			<c:if test="${isEdit}">
			<form action="${pageContext.request.contextPath}/posts/edit/${post.id}" method="POST" class="d-flex flex-column gap-3">
			</c:if>
			<c:if test="${!isEdit}">
			<form action="${pageContext.request.contextPath}/posts/create" method="POST" class="d-flex flex-column gap-3">
			</c:if>
				<div>
					<label for="title" class="form-label">Title</label>
					<input type="text" id="title" name="title" class="form-control" value="${fu.old('title', post.title)}" >
					<fe:error error="title" />
				</div>
				<div>
					<label for="description" class="form-label">Description</label>
					<textarea class="form-control" id="description" name="description" rows="4">${fu.old('description', post.description)}</textarea>
					<fe:error error="description" />
				</div>
				<c:if test="${isEdit}">
				<div class="form-check form-switch">
					<input id="status" name="status" ${fu.old('status') eq 'on' ? 'checked' : ''} ${post.status.getCode() == 1 ? 'checked' : ''} class="form-check-input" type="checkbox" role="switch" >
					<label class="form-check-label" for="status">Status (Public)</label>
					<fe:error error="status" />
				</div>
				</c:if>
				<button type="submit" class="btn btn-primary w-100 mt-2">Confirm</button>
				<button type="button" class="btn btn-outline-primary w-100 mt-2">Clear</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
