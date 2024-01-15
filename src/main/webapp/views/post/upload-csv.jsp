<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container py-4 px-2 mb-5 flex-grow-1">
        <div class="card rounded-4 mx-auto" style="width: 100%; max-width: 420px">
		<div class="p-4">
			<div class="mb-4">
				<h1 class="text-primary h-6 fw-bold text-center">Upload CSV File</h1>
			</div>
			<form action="${pageContext.request.contextPath}/posts/upload-csv" method="POST" enctype="multipart/form-data" class="d-flex flex-column gap-3">
				<div>
					<label for="file" class="form-label">File</label>
					<input type="file" id="file" name="file" class="form-control" accept=".csv">
					<fe:error error="file" />
				</div>
				<button type="submit" class="btn btn-primary w-100 mt-2">Import</button>
			</form>
		</div>
	</div>
</div>

<%@ include file="/Layout/app-footer.jsp"%>
