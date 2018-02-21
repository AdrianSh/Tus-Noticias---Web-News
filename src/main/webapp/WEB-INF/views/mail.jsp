<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
			<%@ include file="../fragments/column-left.jspf"%>
		<section class="col-md-7">
			<div>
				<form method="POST" action="mailto:">
					<div> Su nueva contraseña es ${nuevo} </div> 
				</form>
			</div>
		</section>
		<%@ include file="../fragments/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->
<%@ include file="../fragments/footer.jspf"%>