angular.module('FundFinder')
	.controller('UsersOverviewController', UsersOverviewController)
	.controller('UsersDetailsController', UsersDetailsController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function UsersOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, UsersService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.gridOptions = {
			rowHeight: $rootScope.rowHeight,
			paginationPageSize: $rootScope.paginationPageSize,
			paginationPageSizes: $rootScope.paginationPageSizes,
			enableFiltering: true,
			useExternalFiltering: true,
			useExternalSorting: true,
			useExternalPagination: true,
			enableColumnMenus: false,
			enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			enableGridMenu: true,
			columnDefs: [
				{
					displayName: $translate('COLUMN_ID'),
					field: 'id',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<input type="number" min="0" class="ui-grid-filter-input ui-grid-filter-input-0 ng-dirty ng-valid-parse ng-touched ui-grid-filter-input-port" ng-model="colFilter.term" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" aria-label="Filter for column" placeholder="">' + 
						'</div>', 
					enableHiding: true,
					visible: false,
					width: 60
				},
				{
					displayName: $translate('COLUMN_FIRST_NAME'),
					field: 'firstName',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_LAST_NAME'),
					field: 'lastName',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_EMAIL'),
					field: 'email',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					visible: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_COMPANY'),
					field: 'company.name',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_COMPANY_CODE'),
					field: 'company.code',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					visible: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_STATUS'),
					field: 'status',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
							{ value: 'ACTIVE', label: $translate('STATUS_ACTIVE') },
							{ value: 'INACTIVE', label: $translate('STATUS_INACTIVE') }]
					},
					enableHiding: false,
					width: 100,
					cellTemplate:
						'<div ng-show="row.entity.status == \'ACTIVE\'" class="ui-grid-cell-contents"><span class="badge badge-primary">{{\'STATUS_ACTIVE\' | translate}}</span></div>' + 
						'<div ng-show="row.entity.status == \'INACTIVE\'" class="ui-grid-cell-contents"><span class="badge badge-danger">{{\'STATUS_INACTIVE\' | translate}}</span></div>'
				},
				{
					displayName: $translate('COLUMN_CREATION_DATE'),
					field: 'creationDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterCreationDate" date-range-picker options="grid.appScope.dpOptions" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpCreationDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilter(\'filterCreationDate\')"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					enableHiding: true,
					visible: false,
					width: 175
				},
				{
					displayName: $translate('COLUMN_LAST_MODIFIED_DATE'),
					field: 'lastModifiedDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterLastModifiedDate" date-range-picker options="grid.appScope.dpOptions" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpLastModifiedDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilter(\'filterLastModifiedDate\')"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					enableHiding: true,
					width: 175
				},
				{
					name: ' ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: 100,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-search-plus"></i></button>' +	
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_ACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'INACTIVE\'" ng-click="grid.appScope.activateEntity(row.entity)" class=" ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DEACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'ACTIVE\'" ng-click="grid.appScope.deactivateEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-on"></i></button>' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DELETE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.deleteEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-times"></i></button>' + 
						'</div>'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				
				// register pagination changed handler
				$scope.gridApi.pagination.on.paginationChanged($scope, function(currentPage, pageSize) {
					$scope.getPage(currentPage, pageSize);
				});
				
				// register sort changed handler 
				$scope.gridApi.core.on.sortChanged($scope, $scope.sortChanged);
				
				// register filter changed handler
				$scope.gridApi.core.on.filterChanged($scope, function() {
					var grid = this.grid;
					
					var filterArray = new Array();
					for (var i=0; i<grid.columns.length; i++) {
						var name = grid.columns[i].field;
						var term = grid.columns[i].filters[0].term;
						if (name && term) {
							filterArray.push({
								"name" : name,
								"term" : term
							});
						}
					}
					
					// date filters (e.g. creationDate, lastModifiedDate)
					$rootScope.processDateFilters($('#filterCreationDate'), $('#filterLastModifiedDate'), filterArray);
					
					$scope.filterArray = filterArray;
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				});
				
				$scope.gridApi.core.on.rowsRendered($scope, function(b, f, i) {
					var newHeight = ($scope.gridApi.core.getVisibleRows($scope.gridApi.grid).length * $rootScope.rowHeight) + (($scope.gridOptions.totalItems == 0) ? $rootScope.heightNoData : $rootScope.heightCorrectionFactor);
					angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
				});
				
				$scope.gridApi.core.on.columnVisibilityChanged($scope, function() {
					$rootScope.saveGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
				});
				
				$scope.gridApi.grid.clearAllFilters = function() {
					this.columns.forEach(function(column) {
						column.filters.forEach(function(filter) {
							filter.term = undefined;
						});
					});
					$scope.clearDateFilter('filterCreationDate');
					$scope.clearDateFilter('filterLastModifiedDate');
				};
			}
		};
	
	$scope.getPage = function(page, size) {
		// save current state
		$rootScope.saveGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
		
		var uiGridResource = {
			"pagination" : {
				"page" : page - 1,
				"size" : size
			},
			"sort" : $scope.sortArray,
			"filter" : $scope.filterArray
		};
		
		UsersService.getPage(uiGridResource)
			.success(function(data, status, headers, config) {
				$scope.loading = false;
				if (status == 200) {
					$scope.gridOptions.data = data.data;
					$scope.gridOptions.totalItems = data.total;
				} else {
					$log.error(data);
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status, headers, config) {
				$scope.loading = false;
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.sortChanged = function (grid, sortColumns) {
		var sortArray = new Array();
		for (var i=0; i<sortColumns.length; i++) {
			sortArray.push({
				"name" : sortColumns[i].field,
				"priority" : sortColumns[i].sort.priority,
				"direction" : sortColumns[i].sort.direction
			});
		}
		$scope.sortArray = sortArray;
		$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
	};
	
	$scope.showEntity = function (entity) {
		$state.go('users.details', { 'id' : entity.id });
	}
	
	$scope.activateEntity = function(entity) {
		UsersService.activateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_ACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_USER') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
			});
	}
	
	$scope.deactivateEntity = function(entity) {
		UsersService.deactivateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_DEACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_USER') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
			});
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DIALOG_DELETE_HEADER', { entity: $lowercase($translate('ENTITY_USER')) }),
            message: $translate('DIALOG_DELETE_MESSAGE', { entity: $lowercase($translate('ENTITY_USER')) }),
            buttons: [
				{
					label: $translate('BUTTON_NO'),
					icon: 'fa fa-times',
				    cssClass: 'btn-white',
				    action: function(dialog) {
				        dialog.close();
				    }
				},
            	{
            		label: $translate('BUTTON_YES'),
	            	icon: 'fa fa-check',
	                cssClass: 'btn-primary',
	                action: function(dialog) {
	                	UsersService.deleteEntity(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_USER') }));
		    						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					} else {
		    						toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
		    					}
		    				})
		    				.error(function(data, status) {
		    					toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_USER') }));
		    				});
	        			dialog.close();
	                }	                
            	}
            ]
        });
	}
	
	// date picker options
	var rangesJSON = {};
	rangesJSON[$translate('DATETIMEPICKER_TODAY')] = [moment(), moment()];
	rangesJSON[$translate('DATETIMEPICKER_YESTERDAY')] = [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	rangesJSON[$translate('DATETIMEPICKER_LAST_7_DAYS')] = [moment().subtract(6, 'days'), moment()],
	rangesJSON[$translate('DATETIMEPICKER_LAST_30_DAYS')] = [moment().subtract(29, 'days'), moment()],
	rangesJSON[$translate('DATETIMEPICKER_THIS_MONTH')] = [moment().startOf('month'), moment().endOf('month')],
	rangesJSON[$translate('DATETIMEPICKER_LAST_MONTH')] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
	
	$scope.dpOptions = {
		opens: 'left',
		format: $rootScope.dateFormat.toUpperCase(),
		ranges: rangesJSON,
		locale: { 
			customRangeLabel: $translate('DATETIMEPICKER_CUSTOM') 
		}, 
		showDropdowns: true,
		eventHandlers: {
			'apply.daterangepicker': function(ev, picker) {
				$scope.applyDateFilter();
			}
		}
	};
	
	/**
	 * Function clears filter of the given date range picker field.
	 */
	$scope.clearDateFilter = function(field) {
		$('#' + field).val(null);
		$scope.applyDateFilter();	
	};
	
	/**
	 * Function applies date filters.
	 */
	$scope.applyDateFilter = function() {
		setTimeout(function () {
			$scope.$apply(function() {
				$scope.gridApi.core.raise.filterChanged();
			});
		}, 100);
	};
	
	/**
	 * Function sets initial sorting.
	 */
	$scope.setInitialSorting = function() {
		if (!$scope.sortArray) {
			var sortArray = new Array();
			sortArray.push({ name: "company.name", priority: 0, direction: uiGridConstants.ASC });
			$scope.sortArray = sortArray;			
		}
	}
	
	// initial load
	$scope.loading = true;
		
	$timeout(function() {
		// restore saved grid state
		$rootScope.restoreGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
		$scope.applyDateFilter();
		
		// initial sorting
		$scope.setInitialSorting();
		
		// watch 'cntUsers' variable, so if it changes we can refresh grid
		$scope.$watch('cntUsers', function(newValue, oldValue) {
			if (newValue != oldValue) {
				$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			}
		});
	}, 1000);
};

// ========================================================================
//	DETAILS CONTROLLER
// ========================================================================
function UsersDetailsController($rootScope, $scope, $state, $stateParams, $sce, $log, $timeout, $filter, uiGridConstants, UsersService, UserEmailsService, EmailsService) {
	var $translate = $filter('translate');
	
	// ========================================================================================================================
	//	E-mails grid [start]
	// ========================================================================================================================
	
	$scope.gridOptions4Emails = {
			rowHeight: $rootScope.rowHeight,
			paginationPageSize: $rootScope.paginationPageSize,
			paginationPageSizes: $rootScope.paginationPageSizes,
			enableFiltering: true,
			useExternalFiltering: true,
			useExternalSorting: true,
			useExternalPagination: true,
			enableColumnMenus: false,
			enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			expandableRowTemplate: 'email-exp.html',
	        expandableRowHeight: 125,
	        expandableRowScope: { 
	        	toTrusted: function(html) {
	        		console.log(html);
	        		return $sce.trustAsHtml(html);
	        	}
	        },
	        enableExpandableRowHeader: false,
			columnDefs: [
				{
					name: '  ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					exporterSuppressExport: true,
					width: 32,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button ng-if="!row.isExpanded" uib-tooltip="{{\'ACTION_TOOLTIP_EXPAND\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.expandRow4Emails(row)" class="btn-xs btn-white m-l-xs m-t-xxs ff-grid-button"><i class="fa fa-plus"></i></button>' +
							'<button ng-if="row.isExpanded" uib-tooltip="{{\'ACTION_TOOLTIP_COLLAPSE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.collapseRow4Emails(row)" class="btn-xs btn-white m-l-xs m-t-xxs ff-grid-button"><i class="fa fa-minus"></i></button>' +
						'</div>'
				},
				{
					displayName: $translate('COLUMN_SEND_DATE'),
					field: 'creationDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterCreationDate" date-range-picker options="grid.appScope.dpOptions4Emails" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpCreationDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilterGlobal([\'filterCreationDate\'], grid.appScope.gridApi4Emails)"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					width: 175
				},
				{
					displayName: $translate('COLUMN_TO'),
					field: 'user.email',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss',
					width: 225
				},
				{
					displayName: $translate('COLUMN_SUBJECT'),
					field: 'email.subject',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi4Emails = gridApi;
				
				$scope.gridApi4Emails.expandable.on.rowExpandedStateChanged($scope, function(row) {
					if (row.isExpanded) {
						EmailsService.getEntity(row.entity.email.id)
							.success(function(data, status) {
								if (status == 200) {
									row.entity.email = data;
								} else {
									toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
								}
							})
							.error(function(data, status) {
								toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
							});
					}
					
					// refresh grid (to update the height)
					$scope.gridApi4Emails.core.queueGridRefresh();
				});
				
				// register pagination changed handler
				$scope.gridApi4Emails.pagination.on.paginationChanged($scope, function(currentPage, pageSize) {
					$scope.getPage4Emails(currentPage, pageSize);
				});
				
				// register sort changed handler 
				$scope.gridApi4Emails.core.on.sortChanged($scope, $scope.sortChanged4Emails);
				
				// register filter changed handler
				$scope.gridApi4Emails.core.on.filterChanged($scope, function() {
					var grid = this.grid;
					
					var filterArray = new Array();
					for (var i=0; i<grid.columns.length; i++) {
						var name = grid.columns[i].field;
						var term = grid.columns[i].filters[0].term;
						if (name && term) {
							filterArray.push({
								"name" : name,
								"term" : term
							});
						}
					}
					
					// date filters (e.g. creationDate, lastModifiedDate)
					$rootScope.processDateFilters($('#filterCreationDate'), null, filterArray);
					
					$scope.filterArray4Emails = filterArray;
					$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
				});
				
				$scope.gridApi4Emails.core.on.rowsRendered($scope, function(b, f, i) {
					var cntExpandedRows = $scope.gridApi4Emails.expandable.getExpandedRows($scope.gridApi4Emails.grid).length;
					var newHeight = ($scope.gridApi4Emails.core.getVisibleRows($scope.gridApi4Emails.grid).length * $rootScope.rowHeight) 
						+ (cntExpandedRows * ($scope.gridApi4Emails.grid.options.expandableRowHeight + 2)) 
						+ (($scope.gridOptions4Emails.totalItems == 0) ? $rootScope.heightNoData : $rootScope.heightCorrectionFactor);
					angular.element(document.getElementsByClassName('grid4Emails')[0]).css('height', newHeight + 'px');
				});
				
				// set initial sort
				if (!$scope.sortArray4Emails) {
					var sortArray = new Array();
					sortArray.push({ name: "creationDate", priority: 0, direction: uiGridConstants.DESC });
					$scope.sortArray4Emails = sortArray;			
				}
				
				// initial load
				$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
			}
		};
	
	$scope.expandRow4Emails = function(row) {
		$scope.gridApi4Emails.expandable.expandRow(row.entity);
	};
	
	$scope.collapseRow4Emails = function(row) {
		$scope.gridApi4Emails.expandable.collapseRow(row.entity);
	};
	
	$scope.getPage4Emails = function(page, size) {
		$scope.loading4Emails = true;
		
		if (!$scope.filterArray4Emails) {
			$scope.filterArray4Emails = new Array();
		}
		$scope.filterArray4Emails.push({ "name" : "user.id", "term" : $stateParams.id });
		
		var uiGridResource = {
			"pagination" : { "page" : page - 1, "size" : size },
			"sort" : $scope.sortArray4Emails,
			"filter" : $scope.filterArray4Emails
		};
		
		UserEmailsService.getPage(uiGridResource)
			.success(function(data, status, headers, config) {
				$scope.loading4Emails = false;
				$scope.gridOptions4Emails.data = data.data;
				$scope.gridOptions4Emails.totalItems = data.total;
			})
			.error(function(data, status, headers, config) {
				$scope.loading4Emails = false;
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.sortChanged4Emails = function (grid, sortColumns) {
		var sortArray = new Array();
		for (var i=0; i<sortColumns.length; i++) {
			sortArray.push({
				"name" : sortColumns[i].field,
				"priority" : sortColumns[i].sort.priority,
				"direction" : sortColumns[i].sort.direction
			});
		}
		$scope.sortArray4Emails = sortArray;
		$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
	};
	
	$scope.dpOptions4Emails = {
		opens: 'left',
		format: $rootScope.dateFormat.toUpperCase(),
		ranges: $rootScope.datePickerRanges,
		locale: { 
			customRangeLabel: $translate('DATETIMEPICKER_CUSTOM') 
		}, 
		showDropdowns: true,
		eventHandlers: {
			'apply.daterangepicker': function(ev, picker) {
				$rootScope.applyDateFilterGlobal($scope.gridApi4Emails);
			}
		}
	};
	
	// ========================================================================================================================
	//	E-mails grid [end]
	// ========================================================================================================================
	
	$scope.back = function() {
		$state.go('users.overview');
	};
	
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return $sce.trustAsHtml(html);
	}
	
	$scope.getEntity = function() {
		UsersService.getEntity($stateParams.id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.entity = data;
					$scope.email = { "subject" : null, "text" : null, "users" : [$scope.entity] };
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});	
	};
	
	$scope.sendEmail = function() {
		$scope.sendingEmail = true;
		
		UsersService.sendEmail($scope.email)
			.success(function(data, status) {
				toastr.success($translate('ACTION_SEND_EMAIL_SUCCESS_MESSAGE'));
				$scope.sendingEmail = false;
				$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_SEND_EMAIL_FAILURE_MESSAGE'));
				$scope.sendingEmail = false;
			});
	};
	
	$scope.resetEmail = function() {
		$scope.email = { "subject" : null, "text" : null, "users" : [$scope.entity] };
	}
	
	// initial load
	$scope.getEntity();
};