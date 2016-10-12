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
.config(function($httpProvider, $translateProvider, constants) {  
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
		.translations('en', {
			LBL_WELCOME: 'Welcome to Fund Finder',
			LBL_USERNAME: 'Username',
			LBL_PASSWORD: 'Password',
			LBL_CONFIRM_PASSWORD: 'Confirm password',
			LBL_RESET_PASSWORD: 'Reset password',
			LBL_REGISTRATION: 'Registration',
			LBL_CANCEL: 'Cancel',
			LBL_TERMS_OF_USE: 'Terms of use',
			LBL_FIRST_NAME: 'First name',
			LBL_LAST_NAME: 'Last name',
			LBL_EMAIL: 'Email',
			LBL_COMPANY_NAME: 'Company name',
			
			BTN_LOGIN: 'Login',
			BTN_REGISTER: 'Register',
			BTN_SEND: 'Send',
			BTN_CLOSE: 'Close',
			
			HDR_SUCCESS: 'Success',
			HDR_WARNING: 'Warning',
			HDR_ERROR: 'Error',
			
			MSG_REGISTRATION_SUCCESS: 'Your registration form has been successfully submitted.<p>In a few minutes you should receive confirmation email. Please click the URL from email to complete registration. After that you can login to Fund Finder.',
			MSG_REGISTRATION_ERROR: 'We are sorry, but action has failed.<p>For more information please contact customer support service.',
			MSG_REGISTRATION_CONFLICT: 'User with entered email already exists in Fund Finder system.',
				
			MSG_RESET_PASSWORD_SUCCESS: 'New password has been sent to your email.',
			MSG_RESET_PASSWORD_ERROR: 'We are sorry, but action has failed.<p>For more information please contact customer support service.',
			MSG_RESET_PASSWORD_NOT_FOUND: 'User with entered email not found in Fund Finder system.',
		});
	$translateProvider.preferredLanguage("en");
})

// ================================================================================
//	CONTROLLER
// ================================================================================
.controller('Controller', function($rootScope, $scope, $http, $window, $filter, $timeout, $log) {
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
	
	$scope.showLogin = function() {
		$scope.loginVisible = true;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = false;
		
		$scope.username = undefined;
		$scope.password = undefined;
	};
	
	$scope.showResetPassword = function() {
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = true;
		$scope.registrationVisible = false;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showRegistration = function() {
		$scope.loginVisible = false;
		$scope.resetPasswordVisible = false;
		$scope.registrationVisible = true;
		
		$scope.user = { 'firstName' : undefined, 'lastName' : undefined, 'email' : undefined, 'password' : undefined, 'confirmPassword' : undefined, 'status' : 'WAITING_CONFIRMATION', 'company' : { 'name' : undefined } };
	};
	
	$scope.showTermsOfUse = function() {
		$window.open("http://google.hr", "_blank");
	};
	
	// initial
	$scope.showLogin();
});