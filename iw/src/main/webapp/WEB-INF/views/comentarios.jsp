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
					<p>Nunc suscipit augue quis lacus fermentum, in aliquet felis
						rutrum. Pellentesque habitant morbi tristique senectus et netus et
						malesuada fames ac turpis egestas. Cras ut arcu dignissim, luctus
						tortor vitae, pharetra velit. Pellentesque consectetur neque vel
						mauris tempus vehicula. Donec laoreet, justo ut pretium accumsan,
						purus risus faucibus est, in feugiat mauris nibh ac ex. Sed ac
						lorem et lectus mattis placerat. Nullam diam odio, ultrices nec
						nisi ut, hendrerit faucibus quam. Morbi auctor placerat lorem eu
						porttitor. Donec faucibus quam lectus, vel tincidunt risus auctor
						nec. Phasellus neque neque, rutrum non tristique quis, tincidunt
						cursus mi. Interdum et malesuada fames ac ante ipsum primis in
						faucibus.</p>
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