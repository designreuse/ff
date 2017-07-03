angular.module('FundFinderUnsecured', ['pascalprecht.translate', 'ui.router', 'angular-ladda'])

// ================================================================================
//	CONSTANT
// ================================================================================
.constant("constants", {
    "contextPath": "/fundfinder"
})

// ================================================================================
//	CONFIG
// ================================================================================
.config(function($httpProvider, $translateProvider, $locationProvider, $sceProvider, constants) {  
	
	// disable SCE (Strict Contextual Escaping)
	$sceProvider.enabled(false);
	
	$locationProvider.html5Mode({ enabled: true, requireBase: false });
	
	// configure interceptors
	$httpProvider.interceptors.push(function($location) {  
		var path = {
				request: function(config) {
					config.url = constants.contextPath + config.url;
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
			LBL_UNAUTHORIZE: 'Neautoriziran pristup',
			LBL_USER_ALREADY_REGISTERED: 'Korisnik je već registriran',
			
			BTN_LOGIN: 'Prijava',
			BTN_REGISTER: 'Registriraj',
			BTN_SEND: 'Pošalji',
			BTN_CLOSE: 'Zatvori',
			
			HDR_SUCCESS: 'Uspjeh',
			HDR_WARNING: 'Upozorenje',
			HDR_ERROR: 'Greška',
			
			LOGIN_BAD_CREDENTIALS: 'Prijava neuspješna, molimo pokušajte ponovo.',
			LOGIN_DISABLED: 'Vaš profil je deaktiviran. Za ponovnu aktivaciju obratite se na e-mail adresu: <a href="mailto:mojEUfond@unicreditgroup.zaba.hr?Subject=Aktivacija korisnika" target="_top">mojEUfond@unicreditgroup.zaba.hr</a>',
			
			MSG_REGISTRATION_SUCCESS: 'Vaš zahtijev za registracijom je uspješno zaprimljen.<p>Za nekoliko minuta primiti ćete konfirmacijski e-mail. Molimo kliknite na poveznicu iz e-maila da dovršite registraciju. Nakon toga ćete se moći prijaviti u MojEUfond.',
			MSG_REGISTRATION_ERROR: 'Nažalost, došlo je do pogreške.<p>Molimo kontaktirajte službu za korisnike.',
			MSG_REGISTRATION_CONFLICT: 'Korisnik sa upisanim e-mailom već postoji u sustavu.',
				
			MSG_RESET_PASSWORD_SUCCESS: 'Nova zaporka je poslana na vaš e-mail.',
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
	
	$scope.login = function() {
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
				$window.location.href = data.url;
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
				$scope.login();
			})
			.error(function(data, status, headers, config) {
				$scope.unauthorize = true;
				$scope.status = status;
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
	});
	
