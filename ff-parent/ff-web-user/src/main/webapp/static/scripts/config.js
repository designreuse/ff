angular.module('FundFinder')

.constant("constants", {
    "contextPath": "/fundfinder"
})

.config(function config($provide, $stateProvider, $httpProvider, $urlRouterProvider, cfpLoadingBarProvider, constants) {
	// configure interceptor
	$httpProvider.interceptors.push(function($location) {  
		var path = {
				request: function(config) {
					if (config.url.indexOf("template/datepicker") != -1 || config.url.indexOf("uib") != -1) {
						// workaround for problem with loading datepicker templates
					} else {
						config.url = constants.contextPath + config.url;
					}
					return config;
				},
				response: function(response) {
					return response;
				}
			};
			return path;
		});
	
	// by default, the loading bar will only display after it has been waiting for a response for over 500ms
	cfpLoadingBarProvider.latencyThreshold = 500;
	cfpLoadingBarProvider.includeBar = false;
	cfpLoadingBarProvider.includeSpinner = false;
		
	// 'getPrincipal' function
	var getPrincipal = function($rootScope, $http, $log) {
		if (!$rootScope.principal) {
			$log.info('Getting principal');
			
			// get data synchronously
			var request = new XMLHttpRequest();
			request.open('GET', constants.contextPath + '/principal', false);
			request.send(null);
			
			if (request.status == 200) {
				$rootScope.principal = jQuery.parseJSON(request.response);
			} else {
				$log.error('Error occurred while getting principal!', status, data);
			}
		}
	};
	
	$stateProvider
	    // SETTINGS
		.state('settings', {
	        abstract: true,
	        url: "/settings",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/settings/scripts/controllers.js',
	        			        'components/settings/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.overview', {
	        url: "/overview",
	        templateUrl: "/components/settings/views/overview.html",
	        controller: 'SettingsController'
	    })
	    
		// TENDERS
		.state('tenders', {
	        abstract: true,
	        url: "/tenders",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/tenders/scripts/controllers.js',
	        			        'components/tenders/scripts/services.js',
	        			        'components/images/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('tenders.overview', {
	        url: "/overview",
	        templateUrl: "/components/tenders/views/overview.html",
	        controller: 'TendersOverviewController'
	    })
	    .state('tenders.details', {
	        url: "/details/:id",
	        templateUrl: "/components/tenders/views/details.html",
	        controller: 'TendersDetailsController',
	        params: { 'id' : null }
	    })
	    
	    // COMPANY
		.state('company', {
	        abstract: true,
	        url: "/company",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/company/scripts/controllers.js',
	        			        'components/company/scripts/services.js',
	        			        'components/currencies/scripts/services.js',
	        			        'components/subdivisions1/scripts/services.js',
	        			        'components/subdivisions2/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('company.edit', {
	        url: "/edit",
	        templateUrl: "/components/company/views/edit.html",
	        controller: 'CompanyEditController'
	    })
	    
		// PROJECTS
		.state('projects', {
	        abstract: true,
	        url: "/projects",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/projects/scripts/controllers.js',
	        			        'components/projects/scripts/services.js',
	        			        'components/company/scripts/services.js',
	        			        'components/currencies/scripts/services.js',
	        			        'components/activities/scripts/services.js',
	        			        'components/subdivisions1/scripts/services.js',
	        			        'components/subdivisions2/scripts/services.js',
	        			        'components/images/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('projects.overview', {
	        url: "/overview",
	        templateUrl: "/components/projects/views/overview.html",
	        controller: 'ProjectsController'
	    })
	    .state('projects.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/projects/views/edit.html",
	        controller: 'ProjectsEditController',
	        params: { 'id' : null }
	    })
	    
	    // ARTICLES
		.state('articles', {
	        abstract: true,
	        url: "/articles",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/articles/scripts/controllers.js',
	        			        'components/articles/scripts/services.js',
	        			        'components/images/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('articles.overview', {
	        url: "/overview",
	        templateUrl: "/components/articles/views/overview.html",
	        controller: 'ArticlesController'
	    })
	    .state('articles.details', {
	        url: "/details/:id",
	        templateUrl: "/components/articles/views/details.html",
	        controller: 'ArticlesDetailsController',
	        params: { 'id' : null }
	    })
	    
	    // CONTACT
		.state('contact', {
	        abstract: true,
	        url: "/contact",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/contact/scripts/controllers.js',
	        			        'components/contact/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('contact.submit', {
	        url: "/submit",
	        templateUrl: "/components/contact/views/overview.html",
	        controller: 'ContactController'
	    })
	    
	    // DASHBOARD
		.state('dashboard', {
	        abstract: true,
	        url: "/dashboard",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/dashboard/scripts/controllers.js',
	        			        'components/dashboard/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('dashboard.overview', {
	        url: "/overview",
	        templateUrl: "/components/dashboard/views/overview.html",
	        controller: 'DashboardController'
	    })
})
	
.run(function ($rootScope, $state, $stateParams, $log, $localStorage, $sce, ModalService, CommonService) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	
	$rootScope.version = "v1.0.4";
	$rootScope.helpEnabled = false; // indicates if help feature is enabled or not
	
	$rootScope.dateFormat = "yyyy-MM-dd";
	$rootScope.dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	
	$rootScope.rowHeight = 30;
	$rootScope.paginationPageSize = 10;
	$rootScope.paginationPageSizes = [10, 25, 50, 100];
	$rootScope.heightNoData = 300;
	$rootScope.heightNoDataRevisions = 88;
	$rootScope.heightCorrectionFactor = 90;
	$rootScope.heightCorrectionFactorRevisions = 60;
	
	// toastr default settings
	toastr.options.preventDuplicates = true;
	toastr.options.closeButton = true;
	
	// keep track of previous and current state via root scope variables
	$rootScope.previousState;
	$rootScope.currentState;
	$rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
	    $rootScope.previousState = from.name;
	    $rootScope.previousStateParams = fromParams;
	    $rootScope.currentState = to.name;
	    $rootScope.currentStateParams = toParams;
	});
	
	$rootScope.getRandom = function () {
		return random = $.now() + "" + Math.floor((Math.random() * 1000) + 1);
	}
	
	$rootScope.processDateFilters = function (creationDateEl, lastModifiedDateEl, filterArray) {
		if (creationDateEl && creationDateEl.val() && creationDateEl.val().length > 0) {
			filterArray.push({ "name" : "creationDate", "term" : creationDateEl.val() });
		}

		if (lastModifiedDateEl && lastModifiedDateEl.val() && lastModifiedDateEl.val().length > 0) {
			filterArray.push({ "name" : "lastModifiedDate", "term" :  lastModifiedDateEl.val() });
		}
	}
	
	$rootScope.setDateFilters = function (creationDateEl, lastModifiedDateEl, filterArray) {
		for(var i in filterArray) {
			var filter = filterArray[i];
			
			if(filter.name == "creationDate" && creationDateEl != undefined) {
				creationDateEl.val(filter.term);
			}
			if(filter.name == "lastModifiedDate" && lastModifiedDateEl != undefined) {
				lastModifiedDateEl.val(filter.term);
			}
		}
	}

	$rootScope.saveGridState = function (view, gridApi, creationDateEl, lastModifiedDateEl) {
		if(view != undefined && gridApi != undefined && gridApi.saveState != undefined) {
			if($localStorage.gridStates == undefined) {
				$localStorage.gridStates = new Array();
			}
			var gridState = gridApi.saveState.save();
			var dateFilters = new Array();
			$rootScope.processDateFilters(creationDateEl, lastModifiedDateEl, dateFilters);
			
			$localStorage.gridStates[view] = {
				gridState: gridState,
				dateFilters: dateFilters
			};
		}
	}
	
	$rootScope.restoreGridState = function (view, gridApi, creationDateEl, lastModifiedDateEl) {
		if(view != undefined && $localStorage.gridStates != undefined && $localStorage.gridStates[view] != undefined
				&& gridApi != undefined && gridApi.saveState != undefined) {
			var state = $localStorage.gridStates[view];
			
			gridApi.saveState.restore(null, state.gridState);
			$rootScope.setDateFilters(creationDateEl, lastModifiedDateEl, state.dateFilters);
		}
	}
	
	$rootScope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
	/**
	 * Function validates company profile.
	 * Profile is valid if all mandatory items are entered.
	 */
	$rootScope.validateProfile = function() {
		CommonService.validateProfile()
			.success(function(data, status) {
				var profileCompleteness = parseFloat(data);
				$rootScope.profileCompleteness = profileCompleteness;
				$rootScope.profileIncomplete = (profileCompleteness == 100) ? false : true;
			})
			.error(function(data, status) {
				$log.error(data);
			});
	}
	
	$rootScope.profileIncomplete = false;
	$rootScope.validateProfile();
});