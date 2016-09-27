angular.module('FundFinder')
	.controller('CompanyEditController', CompanyEditController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function CompanyEditController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, CompanyService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
};
