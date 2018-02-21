<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<section>
			<table>
				<tr>
					<th>Falta agregar de que la url ya sea porque la manda un
						administrador o porque el usuario consulta una tematica o algo por
						estilo</th>
				</tr>
				<tr>
					<th>Url</th>
					<th>Identificador</th>
					<th>Valor</th>
					<th>Elemento HTML</th>
				</tr>
				<c:forEach items="${periodicos}" var="b">
					<tr>
						<td>${b.url}</td>
						<td>${b.identificador}</td>
						<td>${b.identiValor}</td>
						<td>${b.contenidoHTML}</td>
					</tr>
				</c:forEach>
			</table>
		</section>

		<%@ include file="../fragments/column-left.jspf"%>
		<section class="col-md-7">
			<article class="articulo">
				<header> ${articulo.titulo} </header>
				<section>
					<p>${articulo.contenido}</p>
					<p>Fecha: ${articulo.fecha}</p>
					<p>Ranking: ${articulo.ranking}</p>
					<p>Autor: ${articulo.autor.login}</p>
				</section>
			</article>
			<section class="commentarios">
				<div class="row">
					<div class="col-sm-2 right">
						<div class="thumbnail">
							<img class="img-responsive user-photo"
								src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png">
						</div>
						<!-- /thumbnail -->
					</div>
					<!-- /col-sm-1 -->

					<div class="col-sm-8 right">
						<div class="panel panel-default">
							<div class="panel-heading">
								<strong>myusername</strong> <span class="text-muted">commented
									5 days ago</span>
							</div>
							<div class="panel-body">Panel content</div>
							<!-- /panel-body -->
						</div>
						<!-- /panel panel-default -->
					</div>
					<!-- /col-sm-5 -->
				</div>
				<div class="row">
					<div class="col-sm-2 left">
						<div class="thumbnail">
							<img class="img-responsive user-photo"
								src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png">
						</div>
						<!-- /thumbnail -->
					</div>
					<!-- /col-sm-1 -->

					<div class="col-sm-8 left">
						<div class="panel panel-default">
							<div class="panel-heading">
								<strong>myusername</strong> <span class="text-muted">commented
									5 days ago</span>
							</div>
							<div class="panel-body">Panel content</div>
							<!-- /panel-body -->
						</div>
						<!-- /panel panel-default -->
					</div>
				</div>
				<!--  Formulario de comentarios -->
				<div class="row">
					<div class="col-sm-2 left">
						<div class="thumbnail">
							<img class="img-responsive user-photo"
								src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png">
						</div>
						<!-- /thumbnail -->
					</div>
					<!-- /col-sm-1 -->

					<div class="col-sm-8 left">
						<div class="panel panel-default">
							<div class="panel-heading">
								<strong>myusername</strong> <span class="text-muted">commented
									5 days ago</span>
							</div>
							<form method="post" class="comment-form">
								<textarea class="panel-body">
								Contenido a publicar ...
								</textarea>
								<input type="submit" value="Comentar" class="btn btn-default">
							</form>
							<!-- /panel-body -->
						</div>
						<!-- /panel panel-default -->
					</div>
				</div>
				<!--  /Formulario de comentarios -->
			</section>
		</section>
		<%@ include file="../fragments/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../fragments/footer.jspf"%>