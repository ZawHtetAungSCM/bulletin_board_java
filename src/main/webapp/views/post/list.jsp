<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<div class="container-md py-4 mb-5">
  <div class="d-flex align-items-center justify-content-between mb-4">
    <form action="${pageContext.request.contextPath}/home" method="GET" class="d-flex" role="search">
      <input class="form-control me-2" type="search" name="kw" placeholder="Search" aria-label="Search" value="${searchKeyword}" />
      <button class="btn btn-primary" type="submit">Search</button>
    </form>
    <a href="${pageContext.request.contextPath}/posts/create" class="btn btn-primary">Add New</a>
  </div>
  <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-4">
    <c:forEach var="post" items="${postList}">
      <div>
        <div class="card rounded-4">
          <div class="card-body position-relative">
            <h5 class="card-title line-clamp-2">${post.title}</h5>
            <p class="card-text line-clamp-4">${post.description}</p>

            <div class="d-flex">
				<img src="${pageContext.request.contextPath}${post.getCreatedUser().profile}"
				class="rounded-circle border border-secondary me-2" alt="User Avartar"
				width="36" height="36">
              <div class="d-flex flex-column justify-content-between">
                <a href="${pageContext.request.contextPath}/users/profile/${post.createdUserId}" class="lh-1 link-underline link-underline-opacity-0">
                  <span class="text-primary fw-semibold">${post.getCreatedUser().name}</span>
                </a>
                <small class="text-body-secondary" style="font-size: 12px">
                  <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd h:mm a" />
                  <span>&nbsp;&bull;&nbsp;</span>
					<c:if test="${post.isPublic()}">
					<span class="text-success">Public</span>
					</c:if>
					<c:if test="${!post.isPublic()}">
					<span class="text-danger">Private</span>
					</c:if>
                </small>
              </div>
			      </div>
            <c:if test="${auth.isAdmin() || auth.id == post.createdUserId}">
            <div class="position-absolute top-0 end-0 dropdown">
              <button
                class="btn btn-light rounded-circle d-inline-flex align-items-center justify-content-center p-1 m-2"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="1.5"
                  stroke="currentColor"
                  style="width: 24px"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
                  />
                </svg>
              </button>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <a class="dropdown-item" href="${pageContext.request.contextPath}/posts/edit/${post.id}">Edit</a>
                </li>
                <li>
                  <button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#deletePostModal" data-bs-postId="${post.id}">Delete</button>
                </li>
              </ul>
            </div>
            </c:if>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <c:if test="${fn:length(postList) > 0}">
  <%@ include file="/components/pagination.jsp"%>
  </c:if>
</div>

<div class="modal fade" id="deletePostModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
        <form action="${pageContext.request.contextPath}/posts/delete/" method="POST" >
			<div class="modal-header">
				<h1 class="modal-title fs-5">Deleting Post</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<p>Delete Post</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
				<button type="submit" class="btn btn-primary">Delete</button>
			</div>
        </form>
    </div>
  </div>
</div>

<script>
const deletePostModal = document.getElementById('deletePostModal')
if (deletePostModal) {
  deletePostModal.addEventListener('show.bs.modal', event => {

    const button = event.relatedTarget

    const postId = button.getAttribute('data-bs-postId')
    const username = button.getAttribute('data-bs-username')

    const modalForm = deletePostModal.querySelector('.modal-content form');
    const modalBodyText = deletePostModal.querySelector('.modal-body p');

	modalForm.action += postId;
  	modalBodyText.textContent = "Going to delete post id : "+postId+". This action cannot be undone. Are you sure?";
  })
}
</script>

<%@ include file="/Layout/app-footer.jsp"%>
