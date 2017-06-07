angular.module('FundFinder')
	.controller('SettingsController', SettingsController)
	.controller('RegistrationController', RegistrationController);

function SettingsController($rootScope, $scope, $state, $log, $timeout, $http, $window, $filter, SettingsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.find = function() {
		SettingsService.find()
			.success(function(data, status) {
				$scope.settings = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.save = function() {
		SettingsService.save($scope.settings)
			.success(function(data, status, headers, config) {
				toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
				$rootScope.principal.firstName = data.user.firstName;
				$rootScope.principal.lastName = data.user.lastName;
				$rootScope.principal.username = data.user.email;
			})
			.error(function(data, status, headers, config) {
				if (data.exception.indexOf("ValidationFailedException") != -1) {
					toastr.warning(data.message);
				} else {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.deactivate = function() {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DLG_DEACTIVATE_HDR'),
            message: $translate('DLG_DEACTIVATE_MSG'),
            buttons: [
				{
					label: $translate('BUTTON_NO'),
				    cssClass: 'btn-white',
				    action: function(dialog) {
				        dialog.close();
				    }
				},
            	{
            		label: $translate('BUTTON_YES'),
	                cssClass: 'btn-primary',
	                action: function(dialog) {
	                	SettingsService.deactivate()
		        			.success(function(data, status, headers, config) {
		        				if (status == 200) {
		        					$scope.settings = data;
		        					$scope.logout();
		        				} else {
		        					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
		        				}
		        			})
		        			.error(function(data, status, headers, config) {
		        				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
		        			});	
	        			dialog.close();
	                }	                
            	}
            ]
        });
	};
	
	$scope.logout = function() {
		$http.post('/logout', {})
			.success(function() {
				$window.location.href = '';
			})
			.error(function(data) {
				$log.error(data);
			});
	};
	
	// initial load
	$scope.find();
};

function RegistrationController($rootScope, $scope, $state, $log, $timeout, $http, $window, $filter, SessionStorage, SettingsService) {
	var $translate = $filter('translate');
	
	$scope.user = {};
	
	$scope.register = function() {
		$scope.saving = true;
		
		var user = $scope.user
		user.company = SessionStorage.getSession("company");
		user.projects = SessionStorage.getSession("projects");
		
		if (user && user.company) {
			SettingsService.registerDemo(user)
				.success(function(data, status, headers, config) {
					$scope.saving = false;
					BootstrapDialog.show({
						type: BootstrapDialog.TYPE_DEFAULT,
						closable: false,
			            title: $translate('DLG_REGISTRATION_HDR'),
			            message: $translate('DLG_REGISTRATION_MSG'),
			            buttons: [
							{
								label: $translate('BUTTON_CLOSE_AND_LOGOUT'),
							    cssClass: 'btn-white',
							    action: function(dialog) {
							    	$http.post('/logout', {})
										.success(function() {
											$window.location.href = '';
										})
										.error(function(data) {
											$log.error(data);
										});
							    }
							}
			            ]
			        });
				})
				.error(function(data, status, headers, config) {
					$scope.saving = false;
					if (data == 100) {
						toastr.warning($translate('MSG_REGISTRATION_CONFLICT_USER'));
					} else if (data == 200) {
						toastr.warning($translate('MSG_REGISTRATION_CONFLICT_COMPANY'));
					} else {
						toastr.error($translate('MSG_REGISTRATION_ERROR'));
					}
				});
		}
	};
};