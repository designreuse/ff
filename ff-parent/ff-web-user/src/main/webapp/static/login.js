angular.module('FundFinderUnsecured', ['pascalprecht.translate', 'ui.router', 'angular-ladda'])

// ================================================================================
//	CONSTANT
// ================================================================================
.constant("constants", {

})

// ================================================================================
//	CONFIG
// ================================================================================
.config(function($httpProvider, $translateProvider, $locationProvider, $sceProvider, constants) {  
	
	// disable SCE (Strict Contextual Escaping)
	$sceProvider.enabled(false);
	
	$locationProvider.html5Mode({ enabled: true, requireBase: false });
	
	// configure interceptors
	$httpProvider.interceptors.push(function($window, $location) {  
		var path = {
				request: function(config) {
					config.url = $window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) + config.url;
					return config;
				},
				response: function(response) {
					return response;
				}
			};
			return path;
		});
	
	// translations
	$translateProvider
		.translations('hr', {
			LBL_WELCOME: 'MojEUfond prijava',
			LBL_USERNAME: 'Korisničko ime',
			LBL_PASSWORD: 'Zaporka',
			LBL_PASSWORD_GUIDELINES: '(minimalno 8 znakova)',
			LBL_CONFIRM_PASSWORD: 'Potvrdi zaporku',
			LBL_RESET_PASSWORD: 'Resetiraj zaporku',
			LBL_REGISTRATION: 'Registracija',
			LBL_CANCEL: 'Otkaži',
			LBL_TERMS_OF_USE: 'Uvjeti korištenja',
			LBL_FIRST_NAME: 'Ime',
			LBL_LAST_NAME: 'Prezime',
			LBL_EMAIL: 'E-mail',
			LBL_COMPANY_NAME: 'Ime tvtke',
			LBL_LOADING: 'Učitavam MojEUfond',
			LBL_UNAUTHORIZED: 'Neautoriziran pristup',
			LBL_USER_ALREADY_REGISTERED: 'Korisnik je već registriran',
			
			BTN_LOGIN: 'Prijava',
			BTN_REGISTER: 'Registriraj',
			BTN_SEND: 'Pošalji',
			BTN_CLOSE: 'Zatvori',
			
			HDR_SUCCESS: 'Uspjeh',
			HDR_WARNING: 'Upozorenje',
			HDR_ERROR: 'Greška',
			
			LOGIN_BAD_CREDENTIALS: 'Prijava neuspješna, molimo pokušajte ponovo.',
			LOGIN_DISABLED: 'Vaš profil je deaktiviran. Za ponovnu aktivaciju obratite se na e-mail adresu: <a href="mailto:mojeufond@unicreditgroup.zaba.hr?Subject=Aktivacija korisnika" target="_top">mojeufond@unicreditgroup.zaba.hr</a>',
			
			MSG_REGISTRATION_SUCCESS: 'Vaš zahtjev za registracijom je zaprimljen. Na unesenu e-mail adresu zaprimit ćete link za dovršetak registracije.',
			MSG_REGISTRATION_ERROR: 'Nažalost, došlo je do pogreške.<p>Molimo kontaktirajte službu za korisnike.',
			MSG_REGISTRATION_CONFLICT: 'Korisnik s upisanom e-mail adresom već postoji u sustavu.',
				
			MSG_RESET_PASSWORD_SUCCESS: 'Nova zaporka je poslana na Vaš e-mail.',
			MSG_RESET_PASSWORD_ERROR: 'Nažalost, došlo je do pogreške.<p>Molimo kontaktirajte službu za korisnike.',
			MSG_RESET_PASSWORD_NOT_FOUND: 'Korisnik sa upisanim e-mailom nije pronađen.',
		});
	$translateProvider.preferredLanguage("hr");
})

// ================================================================================
//	CONTROLLER
// ================================================================================
.controller('Controller', function($rootScope, $scope, $http, $location, $window, $filter, $timeout, $log) {
	var $translate = $filter('translate');

	if ($window.location.hash.indexOf('logoutSuccess') != -1) {
		$window.location.hash = "";
	}
	
	$scope.login = function(isExternalFlow) {
		$http.post("/login.html", null, {
			params : { username: $scope.username, password: $scope.password }
		})
		.success(function(data, status, headers, config) {
			if (data.status == 'Disabled') {
				$scope.msg = $translate('LOGIN_DISABLED');
			} else if (data.status == 'BadCredentials') {
				$scope.msg = $translate('LOGIN_BAD_CREDENTIALS');
			}
			
			if (data.url != null) {
				// redirect if user is authenticated
				if (isExternalFlow) {
					$window.location.replace(data.url);					
				} else {
					$window.location.href = data.url;
				}
			}
		})
		.error(function(data, status, headers, config) {
			$scope.msg = data.message;
		});
	};
	
	$scope.resetPassword = function() {
		$scope.processingResetPassword = true;
		$http.post("/api/v1/users/resetPassword", $scope.user)
			.success(function(data, status, headers, config) {
				$scope.processingResetPassword = false;
				if (status == 200) {
					swal({
						title: $translate('HDR_SUCCESS'), html: $translate('MSG_RESET_PASSWORD_SUCCESS'), type: 'success', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					}).then(function() {
						$timeout(function() {
							$scope.showLogin();
						}, 100);
					});
				};
			})
			.error(function(data, status, headers, config) {
				$scope.processingResetPassword = false;
				if (status == 404) {
					swal({
						title: $translate('HDR_WARNING'), html: $translate('MSG_RESET_PASSWORD_NOT_FOUND'), type: 'warning', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					});
				} else {
					$log.error(data);
					swal({
						title: $translate('HDR_ERROR'), html: $translate('MSG_RESET_PASSWORD_ERROR'), type: 'error', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					});
				}
			});
	};
	
	$scope.register = function() {
		if ($scope.processingRegister) {
			// if register is already is process, ignore it
			return;
		}
		$scope.processingRegister = true;
		$http.post("/api/v1/users/register", $scope.user)
			.success(function(data, status, headers, config) {
				$scope.processingRegister = false;
				if (status == 200) {
					swal({
						title: $translate('HDR_SUCCESS'), html: $translate('MSG_REGISTRATION_SUCCESS'), type: 'success', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					}).then(function() {
						$timeout(function() {
							$scope.showLogin();
						}, 100);
					});
				};
			})
			.error(function(data, status, headers, config) {
				$scope.processingRegister = false;
				if (status == 409) {
					swal({
						title: $translate('HDR_WARNING'), html: $translate('MSG_REGISTRATION_CONFLICT'), type: 'warning', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					});
				} else {
					$log.error(data);
					swal({
						title: $translate('HDR_ERROR'), html: $translate('MSG_REGISTRATION_ERROR'), type: 'error', timer: 30000, width: 800, confirmButtonText: $translate('BTN_CLOSE') 
					});
				}
			});
	};
	
	$scope.showLanding = function() {
		$scope.landingVisible = true;
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		$scope.termsOfUseVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
		
		$window.scrollTo(0, 0);
	};
	
	$scope.showWelcome = function() {
		$scope.landingVisible = false;
		$scope.welcomeVisible = true;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		$scope.termsOfUseVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
	};
	
	$scope.showLogin = function() {
		$scope.landingVisible = false;
		$scope.welcomeVisible = false;
		$scope.loginVisible = true;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		$scope.termsOfUseVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
	};
	
	$scope.showLoginDemo = function() {
		$scope.username = "demo";
		$scope.password = "demo";
		$scope.login();
	};
	
	$scope.showResetPassword = function() {
		$scope.landingVisible = false;
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = true;
		$scope.registrationVisible = false;
		$scope.termsOfUseVisible = false;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showRegistration = function() {
		$scope.landingVisible = false;
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = true;
		$scope.termsOfUseVisible = false;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showTermsOfUse = function() {
		$scope.landingVisible = false;
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		$scope.termsOfUseVisible = true;
		
		$scope.username = undefined;
		$scope.password = undefined;
		
		$window.scrollTo(0, 0);
	};
	
	// initial
	$scope.unauthorize = false;
	var authId = $location.search().authId;
	if (authId) {
		$scope.showWelcome();
		
		// external flow authorization
		$http.get("/e/api/v1/externalflow/authorize?authId=" + authId)
			.success(function(data, status, headers, config) {
				$scope.unauthorize = false;
				// to indicate that this is external flow authorization we create username as concatenation of ___ and userId
				$scope.username = "___" + data.id;
				$scope.password = data.password;
				$scope.login(true);
			})
			.error(function(data, status, headers, config) {
				$scope.unauthorize = true;
				$scope.status = status;
				if (status == 403) {
					$scope.externalFlowFailedMsg = $translate('LOGIN_DISABLED');
					$scope.externalFlowUnauthorizedMsg = undefined;	
				} else if (status == 409) {
					$scope.externalFlowFailedMsg = $translate('LBL_USER_ALREADY_REGISTERED');
					$scope.externalFlowUnauthorizedMsg = $translate('LBL_UNAUTHORIZED');	
				}
			});
	} else {
		$scope.showLanding();		
	}
});

$(function() {
	  $('.nav-link').click(function() {
	    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
	      var target = $(this.hash);
	      target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
	      if (target.length) {
	        $('html, body').animate({
	          scrollTop: target.offset().top
	        }, 1000);
	        return false;
	      }
	    }
	  });
	});

$(document).ready(function () {
	$('.panel-title').click(function(e) {

	    $('a').removeClass('active');

	    var $parent = $(this).parent();
	    if (!$parent.hasClass('active')) {
	        $parent.addClass('active');
	    }
	    e.preventDefault();
	  })
	  
	  $('.screen-changer').click(function() {
		  var thisLink = $(this).attr('data-imagelink');
		  $('#screen').attr('src', thisLink);
		});
	
	/*Cookie bar*/
	
	(function ($) {
		$.cookie = function (key, value, options) {
			if (arguments.length > 1 && (!/Object/.test(Object.prototype.toString.call(value)) || value === null || value === undefined)) {
				options = $.extend({}, options);

				if (value === null || value === undefined) {
					options.expires = -1;
				}

				if (typeof options.expires === 'number') {
					var days = options.expires, t = options.expires = new Date();
					t.setDate(t.getDate() + days);
				}

				value = String(value);

				return (document.cookie = [
					encodeURIComponent(key), '=', options.raw ? value : encodeURIComponent(value),
					options.expires ? '; expires=' + options.expires.toUTCString() : '', // max-age is not supported by IE
					options.path ? '; path=' + options.path : '',
					options.domain ? '; domain=' + options.domain : '',
					options.secure ? '; secure' : ''
				].join(''));
			}
			options = value || {};
			var decode = options.raw ? function (s) { return s; } : decodeURIComponent;

			var pairs = document.cookie.split('; ');
			for (var i = 0, pair; pair = pairs[i] && pairs[i].split('='); i++) {
				// IE
				if (decode(pair[0]) === key) return decode(pair[1] || '');
			}
			return null;
		};

		$.fn.cookieBar = function (options) {
			var settings = $.extend({
				'closeButton': 'none',
				'hideOnClose': true,
				'secure': false,
				'path': '/',
				'domain': ''
			}, options);

			return this.each(function () {
				var cookiebar = $(this);

				// just in case they didnt hide it by default.
				cookiebar.hide();

				// if close button not defined. define it!
				if (settings.closeButton == 'none') {
					cookiebar.append('<a class="cookiebar-close"><i class="icon-close" aria-hidden="true"></i></a>');
					$.extend(settings, { 'closeButton': '.cookiebar-close' });
				}

				if ($.cookie('cookiebar') != 'hide') {
					cookiebar.show();
				}

				cookiebar.find(settings.closeButton).click(function () {
					if (settings.hideOnClose) {
						cookiebar.hide();
					}
					$.cookie('cookiebar', 'hide', { path: settings.path, secure: settings.secure, domain: settings.domain, expires: 30 });
					cookiebar.trigger('cookieBar-close');
					return false;
				});
			});
		};

		// self injection init
		$.cookieBar = function (options) {
			$('body').prepend('<div class="ui-widget"><div class="container"><div style="display: none;" class="cookie-message cookie-widget-header blue"><p>Stranice <a href="www.zaba.hr/mojeufond">www.zaba.hr/mojeufond</a> koriste kolačiće (cookies). Nastavak upotrebe ovih stranica podrazumijeva Vaš pristanak na pohranu i pristup kolačićima. Više o kolačićima i mogućnosti vlastitih postavki povezanih s njima potražite u <a href="https://www.zaba.hr/home/footer/izjava-o-privatnosti" target="_blank"> Izjavi o zaštiti privatnosti na internetskim stranicama Zagrebačke banke</a>.</p></div></div></div>');
			$('.cookie-message').cookieBar(options);
		};
	})(jQuery);
	
	$.cookieBar();
	
	});


	
