<%@ include file="../../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../../fragments/column-left.jspf"%>
		<section class="col-md-7" id="contenedorDeArts">
			<c:forEach items="${lastarticulos}" var="articulo">
				<article class="articulo">
					<header>
						<a href="${prefix}articulo/${e:forHtmlContent(articulo.id)}">${e:forHtmlContent(articulo.titulo)}</a><span
							class="fecha">${e:forHtmlContent(articulo.fecha)}</span>
					</header>
					<header class="opciones">
						<a
							href="${prefix}articulo/borrar/${e:forHtmlContent(articulo.id)}">
							<span class="glyphicon glyphicon-remove"></span>
						</a>
						<c:choose>
							<c:when test="${user.favoritos.contains(articulo)}">
								<a style="color: red;"
									href="${prefix}articulo/${e:forHtmlContent(articulo.id)}/favorito">
									<span class="glyphicon glyphicon-heart"></span>
								</a>
							</c:when>
							<c:otherwise>
								<a
									href="${prefix}articulo/${e:forHtmlContent(articulo.id)}/favorito">
									<span class="glyphicon glyphicon-heart"></span>
								</a>
							</c:otherwise>
						</c:choose>
						<a
							onclick="showFormAddTag($(this), ${e:forHtmlContent(articulo.id)})">
							<span class="glyphicon glyphicon-pushpin"></span>
						</a> <a
							onclick="showFormDeleteTag($(this), ${e:forHtmlContent(articulo.id)})">
							<span class="glyphicon glyphicon-scissors"></span>
						</a>

					</header>
					<section>
						<c:forEach items="${articulo.contenido}" var="paragraph">${e:forHtmlContent(paragraph)}</c:forEach>
					</section>
					<footer>
						<div class="ranking">${e:forHtmlContent(articulo.ranking)}</div>
						<div class="autor">${e:forHtmlContent(articulo.autor.login)}</div>
					</footer>
				</article>
				<section class="separadorDeArticulo"></section>
			</c:forEach>
			<!-- 
			Desactivado ya que no se piensa poner ajax para obtener los articulos por cuestiones
			de seguridad, ya sea que nos toca usar sql con limits, etc.
			<ul id="articulopag" class="pagination-sm"></ul>
			<script>
			$('#articulopag').twbsPagination({
				totalPages : 35,
				visiblePages : 7,
				onPageClick : function(event, page) {
					// $('.articulo').text('Page ' + page);
					$('.articulo').text($('.articulo').html);
				}
			});
			</script> -->
		</section>
		<%@ include file="../../fragments/column-right.jspf"%>
		<section class="col-md-10" style="text-align: center;">
			<a onclick="cargarArts()" href="#">Cargar mas articulos</a>
		</section>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../fragments/footer.jspf"%>

<script type="text/javascript">
	var articulos = $('.articulo');
	var articulosMostrados;
	var art = [];
	var contenedorDeArts = $('#contenedorDeArts');

	function removeArt(removeItem) {
		articulos = articulos.filter(function(e) {
			return !$(articulos[e]).is(removeItem);
		});
	}
	function addArt(art) {
		articulosMostrados = art;
	}
	function mostrarArts() {
		contenedorDeArts.html(contenedorDeArts.html()
				+ "<article class='articulo'>" + articulosMostrados
				+ "</article><section class='separadorDeArticulo'></section>");
	}
	if (articulos.length > 10) {
		cargarArts();
	}
	function cargarArts() {
		var i = 0;
		if (articulos.length > 0) {
			contenedorDeArts.html("");
			articulos.each(function(index, element) {
				if (i <= 10) {
					addArt($(this).html());
					removeArt($(this));
					i++;
					mostrarArts();
				} else {
					return false;
				}
			});
		}
	}
</script>