<div class="d-flex justify-content-end mt-4">
    <nav>
        <ul class="pagination">
        <li class="page-item ${currentPage > 1 ? '' : 'disabled'}"><a class="page-link" href="?page=${currentPage - 1}">Previous</a></li>
        <c:forEach var="page" begin="1" end="${totalPages}" step="1">
            <c:choose>
                <c:when test="${page == currentPage}">
                    <li class="page-item active"><a class="page-link" href="?page=${page}">${page}</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link" href="?page=${page}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}"><a class="page-link" href="?page=${currentPage + 1}">Next</a></li>
        </ul>
    </nav>
</div>