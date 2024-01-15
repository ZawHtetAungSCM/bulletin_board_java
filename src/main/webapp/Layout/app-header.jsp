<header>
	<nav class="navbar navbar-expand-lg bg-primary text-white" data-bs-theme="dark">
		<div class="container-md">
			<a class="navbar-brand d-inline-flex align-items-center" href="${pageContext.request.contextPath}/home">
				<img src="${pageContext.request.contextPath}/assets/img/logo-white.svg" alt="App Logo" width="36"
					height="36" class="d-inline-block">
				<div class="d-inline-flex flex-column h-6 lh-1 ms-2">
					<span class="fw-bold">
						Bulletin
					</span>
					<span class="fw-light fs-6">
						Board
					</span>
				</div>
			</a>
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto">
					<c:if test="${auth.isAdmin()}">
					<li class="nav-item">
						<a class="nav-link" href="${pageContext.request.contextPath}/users">Users</a>
					</li>
					</c:if>
					<li class="nav-item">
						<div class="nav-item dropdown me-2">
							<button class="nav-link" data-bs-toggle="dropdown" aria-expanded="false"
								data-bs-offset="10,20">
								Posts
							</button>
							<ul class="dropdown-menu">
								<li><a class="dropdown-item" href="${pageContext.request.contextPath}/posts/create">Create New</a></li>
								<li>
									<hr class="dropdown-divider">
								</li>
								<li><a class="dropdown-item" href="${pageContext.request.contextPath}/posts/upload-csv">Upload CSV</a></li>
								<li>
									<form action="${pageContext.request.contextPath}/posts/download-csv" method="GET" >
										<button type="submit" class="dropdown-item">Download CSV</button>
        							</form>
								</li>
							</ul>
						</div>
					</li>
				</ul>
				<div class="d-flex">
					<div class="nav-item dropdown">
						<button class="nav-link dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"
							data-bs-offset="10,20">
							<span>${auth.user.name}</span>
							<img src="${pageContext.request.contextPath}${auth.user.profile}"
								class="d-inline-block rounded-circle border border-secondary border-2 ms-2" alt="User Avartar"
								width="40" height="40">
						</button>
						<ul class="dropdown-menu dropdown-menu-end">
							<li>
								<a class="dropdown-item" href="${pageContext.request.contextPath}/users/profile/${auth.id}">Profile</a>
							</li>
							<li>
								<hr class="dropdown-divider">
							</li>
							<li>
								<button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#logoutConfirmModal" >Logout</button>
							</li>
						</ul>
					</div>
				</div>
			</div>
	</nav>
</header>

<div class="modal fade" id="logoutConfirmModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
        <form action="${pageContext.request.contextPath}/logout" method="POST" >
			<div class="modal-header">
				<h1 class="modal-title fs-5">Account Logout</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				Are you sure you want to logout?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
				<button type="submit" class="btn btn-primary">Logout</button>
			</div>
        </form>
    </div>
  </div>
</div>
