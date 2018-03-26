<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<section class="col-md-12">
			<div class="row">
			<form action="${prefix}ajustes" enctype="multipart/form-data" role="form" class="form-register" method="POST">
					<div class="text-center">
					<h2>
						Ajustes Usuario 
					</h2>
	</div>
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-12">
							<div class="form-group">
								<input type="file" name="avatar" id="avatar" 
									class="form-control input-lg label_better" placeholder="Ruta" tabindex="1">
					
							</div>
						</div>
					</div>
				 	<div class="form-group">
						<input type="email" name="email" id="email"
							class="form-control input-lg label_better" placeholder="${email}" tabindex="2">
					</div>
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-12">
							<div class="form-group">
								<input type="password" name="pass" id="password" 
									class="form-control input-lg label_better" placeholder="Contrase�a"
									tabindex="3">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12 col-md-3"></div>
						<div class="col-xs-12 col-md-6">
							<input type="submit" value="Cambiar"
								class="btn btn-primary btn-block btn-lg" tabindex="4">
						</div>
					</div>
				</form>
			</div>
		</section>
	</div>
</section>
<!-- /.container -->

<%@ include file="../fragments/footer.jspf"%>