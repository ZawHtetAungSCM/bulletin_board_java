<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/Layout/index.jsp"%>
<%@ include file="/Layout/app-header.jsp"%>

<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.8.0/css/bootstrap-datepicker.css" rel="stylesheet"/>

<div class="container-md py-4 mb-5">
  <div class="d-flex align-items-center justify-content-between mb-4">
	<form action="${pageContext.request.contextPath}/users" method="GET" class="d-flex" role="search">
		<input name="kw" type="search" class="form-control me-2" placeholder="Search" value="${searchKeyword}" />
		<input name="date" id="search-date" type="text" class="form-control me-2" placeholder="Created date" value="${searchDate}">
		<button class="btn btn-primary" type="submit">Search</button>
	</form>
    <a href="${pageContext.request.contextPath}/users/create" class="btn btn-primary">Add New</a>
  </div>

  <div class="card">
  	<div class="card-body overflow-auto">
	  <table class="table mb-0">
		  <thead>
		    <tr>
		      <th scope="col">#</th>
		      <th scope="col">Name</th>
		      <th scope="col">Email</th>
		      <th scope="col">Phone</th>
		      <th scope="col">Birth Date</th>
		      <th scope="col">Created User</th>
		      <th scope="col">Created Date</th>
		      <th scope="col">Action</th>
		    </tr>
		  </thead>
		  <tbody>
			<c:forEach var="user" items="${userList}">
			    <tr>
			      <th scope="row">${user.id}</th>
			      <td>${user.name}</td>
			      <td>${user.email}</td>
			      <td>${user.phone}</td>
			      <td>${user.dob}</td>
			      <td>
			      	<c:if test="${user.getCreatedUser() ne null}">
						${user.getCreatedUser().name}
					</c:if></td>
			      <td>
			      	<fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" />
			      </td>
			      <td>
			      	<div class="dropdown">
			            <button
			              class="btn btn-light rounded-circle d-inline-flex align-items-center justify-content-center p-1"
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
			              <li><a class="dropdown-item" href="${pageContext.request.contextPath}/users/edit/${user.id}">Edit</a></li>
			              <li>
							<button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#deleteUserModal" data-bs-userId="${user.id}" data-bs-username="${user.name}">Delete</button>
			              </li>
			            </ul>
			          </div>
			          </td>
			    </tr>
			</c:forEach>
		  </tbody>
		</table>
	  </div>
  </div>
	<c:if test="${fn:length(userList) > 0}">
	<%@ include file="/components/pagination.jsp"%>
	</c:if>

</div>

<div class="modal fade" id="deleteUserModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
        <form action="${pageContext.request.contextPath}/users/delete/" method="POST" >
			<div class="modal-header">
				<h1 class="modal-title fs-5">Deleting User</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<p>Delete User</p>
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
const deleteUserModal = document.getElementById('deleteUserModal')
if (deleteUserModal) {
  deleteUserModal.addEventListener('show.bs.modal', event => {

    const button = event.relatedTarget

    const userId = button.getAttribute('data-bs-userId')
    const username = button.getAttribute('data-bs-username')

    const modalForm = deleteUserModal.querySelector('.modal-content form');
    const modalBodyText = deleteUserModal.querySelector('.modal-body p');

	modalForm.action += userId;
    modalBodyText.textContent = "Going to delete user : *"+username+"*. This action cannot be undone. Are you sure?";
  })
}
</script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.8.0/js/bootstrap-datepicker.min.js"></script>
<script>
$('#search-date').datepicker({
    startView: 0,
    minViewMode: 0,
    maxViewMode: 2,
    multidate: true,
    multidateSeparator: "-",
    autoClose: true,
    beforeShowDay: highlightRange,
  }).on("changeDate", function(event) {
    var dates = event.dates,
        elem = $('#date');
    if (elem.data("selecteddates") == dates.join(",")) return;
    if (dates.length > 2) dates = dates.splice(dates.length - 1);
    dates.sort(function(a, b) { return new Date(a).getTime() - new Date(b).getTime() });
    elem.data("selecteddates", dates.join(",")).datepicker('setDates', dates);
  });

  function highlightRange(date) {
    var selectedDates = $('#date').datepicker('getDates');
    if (selectedDates.length === 2 && date >= selectedDates[0] && date <= selectedDates[1]) {
      return 'highlighted';
    }
    return '';
  }
</script>

<%@ include file="/Layout/app-footer.jsp"%>
