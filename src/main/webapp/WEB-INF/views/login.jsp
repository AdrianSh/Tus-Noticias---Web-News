<%@ include file="../fragments/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../fragments/column-left.jspf"%>
			<section class="col-md-7">
				<h2>
					Inicia sesi�n!
				</h2>
				<form action="${prefix}login" role="form" class="form-register" method="POST">
					<input type="hidden" id="source" name="source"
						value="/" />
					
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="text" name="login" id="login" 
									class="form-control input-lg label_better" placeholder="Alias" tabindex="1">
							</div>
						</div>
					</div>
						
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="password" name="pass" id="pass" 
									class="form-control input-lg label_better" placeholder="Contrase�a" tabindex="1">
							</div>
						</div>
					</div>	
					
					<p><a href="${prefix}olvidopass">�Olvidaste tu contrase�a?</a></p>
					
					<div class="row">
						<div class="col-xs-12 col-md-3"></div>
						<div class="col-xs-12 col-md-6">
							<input type="submit" value="Iniciar sesi�n"
								class="btn btn-primary btn-block btn-lg" tabindex="7">
						</div>

					</div>
				</form>		
								
			</section>
		<%@ include file="../fragments/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../fragments/footer.jspf"%>