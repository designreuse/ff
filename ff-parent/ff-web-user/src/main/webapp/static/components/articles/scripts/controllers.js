angular.module('FundFinder')
	.controller('ArticlesController', ArticlesController)
	.controller('ArticlesDetailsController', ArticlesDetailsController);

// ========================================================================
//	OVERVIEW
// ========================================================================
function ArticlesController($rootScope, $scope, $state, $log, $timeout, $filter, ArticlesService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.findAll = function() {
		ArticlesService.findAll()
			.success(function(data, status) {
				if (status == 200) {
					$scope.articles = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.showDetails = function(article) {
		$state.go('articles.details', { 'id' : article.id });
	};
	
	// initial load
	$scope.findAll();
};

// ========================================================================
//	DETAILS
// ========================================================================
function ArticlesDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, ArticlesService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.back = function() {
		$state.go('articles.overview');
	};
	
	$scope.find = function(id) {
		ArticlesService.find(id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.article = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	// initial load
	$scope.find($stateParams.id);
};
