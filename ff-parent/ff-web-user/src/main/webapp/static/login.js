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
.config(function($httpProvider, $translateProvider, $locationProvider, constants) {  
	
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
			LBL_WELCOME: 'Fund Finder prijava',
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
			LBL_LOADING: 'UČITAVAM FUND FINDER',
			LBL_UNAUTHORIZE: 'NEAUTORIZIRAN PRISTUP',
			LBL_USER_ALREADY_REGISTERED: 'Korisnik je već registriran',
			
			BTN_LOGIN: 'Prijava',
			BTN_REGISTER: 'Registriraj',
			BTN_SEND: 'Pošalji',
			BTN_CLOSE: 'Zatvori',
			
			HDR_SUCCESS: 'Uspjeh',
			HDR_WARNING: 'Upozorenje',
			HDR_ERROR: 'Greška',
			
			MSG_REGISTRATION_SUCCESS: 'Vaš zahtijev za registracijom je uspješno zaprimljen.<p>Za nekoliko minuta primiti ćete konfirmacijski e-mail. Molimo kliknite na poveznicu iz e-maila da dovršite registraciju. Nakon toga ćete se moći prijaviti u Find Finder.',
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
			$scope.msg = data.message;
			$scope.msgClass = "text-danger";
			if (data.url != null) {
				$scope.msgClass = "text-success";
				$window.location.href = data.url;
			}
		})
		.error(function(data, status, headers, config) {
			$scope.msg = data.message;
			$scope.msgClass = "text-danger";
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
	
	$scope.showWelcome = function() {
		$scope.welcomeVisible = true;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
	};
	
	$scope.showLogin = function() {
		$scope.welcomeVisible = false;
		$scope.loginVisible = true;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
	};
	
	$scope.showResetPassword = function() {
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = true;
		$scope.registrationVisible = false;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showRegistration = function() {
		$scope.welcomeVisible = false;
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = true;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showTermsOfUse = function() {
		$window.open("http://google.hr", "_blank");
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
				$scope.username = data.email;
				$scope.password = data.password;
				$scope.login();
			})
			.error(function(data, status, headers, config) {
				$scope.unauthorize = true;
				$scope.status = status;
			});
	} else {
		$scope.showLogin();		
	}
});