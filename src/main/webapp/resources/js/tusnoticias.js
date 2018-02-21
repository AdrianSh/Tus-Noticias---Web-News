/*
 *  Edicion de post
 */
try {
	var selecciones = [];
	var seleccionActiva = false;

	$(function() {
		$(document.body).bind('mouseup', function(e) {
			var selection;

			if (window.getSelection) {
				selection = window.getSelection();
			} else if (document.selection) {
				selection = document.selection.createRange();
			}

			// selection.toString() !== '' && console.log('"' +
			// selection.toString() + '" was selected at ' + e.pageX + '/' +
			// e.pageY);

			if (!exist(selection.toString()) && seleccionActiva) {
				selecciones.push({
					'pageX' : e.pageX,
					'pageY' : e.pageY,
					'seleccion' : selection.toString()
				});
				updateSeleccion();
			}
		});
	});
	function updateSeleccion() {
		$(".seleccion").html = '';
		$.each(selecciones, function(key, value) {
			if (this['seleccion'] != '')
				$(".seleccion").append('"...' + this['seleccion'] + '..."' /*
																			 * <span
																			 * class="deleteSeleccion"
																			 * onclick="deleteSeleccion(' +
																			 * "'" +
																			 * btoa(this['seleccion']) +
																			 * "'" +
																			 * ')">x</span>,
																			 */);
		});
		changeseleccionActiva();
	}
	function deleteSeleccion(valuestring) {
		/*
		 * valuestring = atob(valuestring); $.each(selecciones, function(key,
		 * value) { if (this['seleccion'] == valuestring) this['seleccion'] =
		 * ''; }); updateSeleccion();
		 */
	}
	function exist(valuestring) {
		encontrado = false;
		$.each(selecciones, function(key, value) {
			if (this['seleccion'] == valuestring) {
				encontrado = true;
			}
		});
		return encontrado;
	}
	function changeseleccionActiva() {
		if (seleccionActiva)
			seleccionActiva = false;
		else
			seleccionActiva = true;
	}
} catch (err) {
	console.log(err.message);
}

/*
 * Basico
 */
$('#pagination-demo').twbsPagination({
	totalPages : 35,
	visiblePages : 7,
	onPageClick : function(event, page) {
		// $('.articulo').text('Page ' + page);
		$('.articulo').text($('.articulo').html);
	}
});
$(".category").click(function() {
	$("article.articulo").text("Cambiar con json ajax");
	$("a.list-group-item.category.active").toggleClass("active");
	$(this).toggleClass("active");
});

$(document).ready(function() {
	$(".btn-pref .btn").click(function() {
		$(".btn-pref .btn").removeClass("btn-primary").addClass("btn-default");
		// $(".tab").addClass("active"); // instead of this do the below
		$(this).removeClass("btn-default").addClass("btn-primary");
	});
});

$(function() {
	$('.button-checkbox')
			.each(
					function() {

						// Settings
						var $widget = $(this), $button = $widget.find('button'), $checkbox = $widget
								.find('input:checkbox'), color = $button
								.data('color'), settings = {
							on : {
								icon : 'glyphicon glyphicon-check'
							},
							off : {
								icon : 'glyphicon glyphicon-unchecked'
							}
						};

						// Event Handlers
						$button.on('click',
								function() {
									$checkbox.prop('checked', !$checkbox
											.is(':checked'));
									$checkbox.triggerHandler('change');
									updateDisplay();
								});
						$checkbox.on('change', function() {
							updateDisplay();
						});

						// Actions
						function updateDisplay() {
							var isChecked = $checkbox.is(':checked');

							// Set the button's state
							$button.data('state', (isChecked) ? "on" : "off");

							// Set the button's icon
							$button
									.find('.state-icon')
									.removeClass()
									.addClass(
											'state-icon '
													+ settings[$button
															.data('state')].icon);

							// Update the button's color
							if (isChecked) {
								$button.removeClass('btn-default').addClass(
										'btn-' + color + ' active');
							} else {
								$button.removeClass('btn-' + color + ' active')
										.addClass('btn-default');
							}
						}

						// Initialization
						function init() {

							updateDisplay();

							// Inject the icon if applicable
							if ($button.find('.state-icon').length == 0) {
								$button.prepend('<i class="state-icon '
										+ settings[$button.data('state')].icon
										+ '"></i>');
							}
						}
						init();
					});
});

$("input.label_better").label_better({
	position : "top", // This will let you define the position where the label
						// will appear when the user clicked on the input
						// fields. Acceptable options are "top", "bottom",
						// "left" and "right". Default value is "top".
	animationTime : 250, // This will let you control the animation speed
							// when the label appear. This option accepts value
							// in milliseconds. The default value is 500.
	easing : "ease-in-out", // This option will let you define the CSS easing
							// you would like to see animating the label. The
							// option accepts all default CSS easing such as
							// "linear", "ease" etc. Another extra option is you
							// can use is "bounce". The default value is
							// "ease-in-out".
	offset : 0, // You can add more spacing between the input and the label.
				// This option accepts value in pixels (without the unit). The
				// default value is 20.
	hidePlaceholderOnFocus : true
// The default placeholder text will hide on focus
});