<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../fragments/column-left.jspf"%>
			<section class="col-md-7">
				<article class="articulo">
					<header>${e:forHtmlContent(mMensaje)}</header>
					<section>Si ya est�s registrado <a href="/tusnoticias/inicio_sesion">inicia sesi�n</a>, si no <a href="/tusnoticias/registro">reg�strate</a>!</section>
				</article>
			</section>
		<%@ include file="../fragments/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../fragments/footer.jspf"%>