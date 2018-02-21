<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../fragments/column-left.jspf"%>
		<section class="col-md-7">
			<article class="articulo">
				<header> Titulo de un articulo producido </header>
				<section>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
						Vivamus faucibus elementum ligula, et lobortis diam condimentum
						et. Nunc vitae quam est. In tempor arcu eu metus rhoncus tempus.
						Cras ex ligula, dapibus ac rhoncus eu, sagittis nec erat. Donec
						egestas imperdiet maximus. Donec bibendum vel libero eget iaculis.
						Curabitur nunc lectus, euismod vitae imperdiet non, ornare nec
						lorem. Vivamus cursus tellus ut dolor laoreet, a rutrum tellus
						posuere. Duis et lectus a libero aliquet iaculis eget vitae est.
						Class aptent taciti sociosqu ad litora torquent per conubia
						nostra, per inceptos himenaeos. Curabitur tempus scelerisque purus
						sed consequat. Mauris luctus risus mauris.</p>
					<p>Nulla eu ullamcorper ligula, ac lobortis nisl. Donec id
						auctor lectus. Maecenas mattis, enim et tempus suscipit, orci eros
						placerat magna, quis blandit massa magna at arcu. Nunc feugiat vel
						ex facilisis volutpat. Suspendisse vitae elit suscipit, interdum
						velit non, maximus quam. Suspendisse nec sem lacinia, gravida arcu
						fringilla, tristique nulla. Fusce in erat quis purus dignissim
						scelerisque ut eget leo. Vestibulum accumsan, justo ut bibendum
						elementum, nibh nibh iaculis sapien, sit amet vulputate dui sapien
						id quam. Praesent vel metus neque. Curabitur sit amet est ante.
						Nam dictum metus pellentesque maximus sodales. Mauris volutpat
						rutrum pellentesque. Mauris vitae volutpat massa. Vestibulum
						iaculis elementum accumsan. Donec dapibus luctus auctor.</p>
					<p>
						Nunc suscipit augue quis lacus fermentum, in aliquet felis rutrum.
						Pellentesque habitant morbi tristique senectus et netus et
						malesuada fames ac turpis egestas. Cras ut arcu dignissim, luctus
						tortor vitae, pharetra velit. Pellentesque consectetur neque vel
						mauris tempus vehicula. <span class="">Donec laoreet, justo
							ut pretium accumsan, purus risus faucibus est, in feugiat mauris
							nibh ac ex. Sed ac lorem et lectus mattis placerat. Nullam diam
							odio, ultrices nec nisi ut,</span> hendrerit faucibus quam. Morbi auctor
						placerat lorem eu porttitor. Donec faucibus quam lectus, vel
						tincidunt risus auctor nec. Phasellus neque neque, rutrum non
						tristique quis, tincidunt cursus mi. Interdum et malesuada fames
						ac ante ipsum primis in faucibus.
					</p>
					<p>Morbi sit amet condimentum sapien. Pellentesque volutpat
						aliquam nunc ut laoreet. Sed leo urna, pellentesque at commodo
						non, vehicula eu ante. Morbi vulputate ornare cursus. Ut congue
						sem urna, ornare convallis turpis semper ut. Vivamus consectetur,
						odio et lacinia rutrum, sapien odio porttitor odio, quis interdum
						dolor purus ac felis. Aenean quis rutrum neque, at accumsan purus.
						Aenean volutpat tortor nec nunc vestibulum fringilla. Curabitur
						dolor magna, rutrum et eros et, aliquet sagittis urna. Nulla nec
						feugiat justo. Maecenas odio est, sollicitudin ut pretium eget,
						pretium in odio.</p>
					<p>Morbi ut metus quis leo blandit imperdiet. Duis cursus purus
						sed velit tempor, vitae auctor nisi mollis. Duis hendrerit neque
						eros, eget posuere tortor lobortis eu. Mauris feugiat malesuada
						elit quis dapibus. Morbi nibh augue, dignissim eget scelerisque a,
						dignissim et diam. Suspendisse placerat in justo consequat
						porttitor. Quisque euismod justo eget dolor cursus, non facilisis
						risus consecteture.</p>
					<span onclick="changeseleccionActiva()">Seleccionar parrafo</span>
					<form method="post" action="#">
						<div class="col-md-10">
							<textarea class="seleccion form-control" rows="8"></textarea>
						</div>
						<input type="submit" value="Comentar">
					</form>
				</section>
			</article>
			<ul id="pagination-demo" class="pagination-sm"></ul>
		</section>
		<%@ include file="../fragments/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../fragments/footer.jspf"%>


<c:forEach items="${actividades}" var="a">
<p>${a.estado} ${a.user.name} ${a.updatedAt}</p>
</c:forEach>
