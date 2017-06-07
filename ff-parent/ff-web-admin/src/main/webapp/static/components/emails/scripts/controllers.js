angular.module('FundFinder')
	.controller('EmailsSendController', EmailsSendController);

// ========================================================================
//	SEND CONTROLLER
// ========================================================================
function EmailsSendController($rootScope, $scope, $state, $log, $timeout, $filter, EmailsService, UserGroupsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.getUserGroups = function() {
		UserGroupsService.getEntities()
			.success(function(data, status) {
				$scope.userGroups = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.send = function() {
		$scope.sendingEmail = true;
		
		EmailsService.send($scope.email)
			.success(function(data, status) {
				$scope.sendingEmail = false;
				toastr.success($translate('ACTION_SEND_EMAIL_SUCCESS_MESSAGE'));
			})
			.error(function(data, status) {
				$scope.sendingEmail = false;
				toastr.error($translate('ACTION_SEND_EMAIL_FAILURE_MESSAGE'));
			});
	};
	
	$scope.reset = function() {
		$scope.email = {};
	}
	
	// initial load
	$scope.summernoteOptions = {
	    height: 300,
	    focus: false,
	    airMode: false,
	    toolbar: [
	            ['edit', ['undo','redo']],
	            ['style', ['bold', 'italic', 'underline']],
	            ['alignment', ['ul', 'ol', 'paragraph']],
	            ['insert', ['link','hr']]
	    ]
	};
	$scope.sendingEmail = false;
	$scope.email = {};
	$scope.getUserGroups();
};