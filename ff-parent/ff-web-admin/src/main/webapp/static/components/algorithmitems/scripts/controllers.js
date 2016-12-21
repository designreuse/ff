angular.module('FundFinder')
	.controller('AlgorithmItemsOverviewController', AlgorithmItemsOverviewController)
	.controller('AlgorithmItemsEditController', AlgorithmItemsEditController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function AlgorithmItemsOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, AlgorithmItemsService) {
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
					displayName: $translate('COLUMN_CODE'),
					field: 'code',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 75,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_TYPE'),
					field: 'type',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
						    { value: 'MANDATORY', label: $translate('ALGORITHM_ITEM_TYPE_MANDATORY') },
						    { value: 'OPTIONAL', label: $translate('ALGORITHM_ITEM_TYPE_OPTIONAL') },
						    { value: 'CONDITIONAL', label: $translate('ALGORITHM_ITEM_TYPE_CONDITIONAL') }
						]
					},
					width: 100,
					cellTemplate: '<div class="ui-grid-cell-contents">{{ (\'ALGORITHM_ITEM_TYPE_\' + row.entity.type) | translate }}</div>' 
				},
				{
					displayName: $translate('COLUMN_COMPANY_ITEM_CODE'),
					field: 'companyItem.code',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 150,
					visible: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_COMPANY_ITEM_TEXT'),
					field: 'companyItem.text',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_OPERATOR'),
					field: 'operator.value',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
						    { value: 'EQUAL', label: $translate('OPERATOR_EQUAL') },
						    { value: 'GREATER_OR_EQUAL', label: $translate('OPERATOR_GREATER_OR_EQUAL') },
						    { value: 'LESS_OR_EQUAL', label: $translate('OPERATOR_LESS_OR_EQUAL') },
						    { value: 'IN', label: $translate('OPERATOR_IN') }
						]
					},
					width: 100,
					cellTemplate: '<div class="ui-grid-cell-contents">{{ (\'OPERATOR_\' + row.entity.operator.value) | translate }}</div>' 
				},
				{
					displayName: $translate('COLUMN_TENDER_ITEM_CODE'),
					field: 'tenderItem.code',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 150,
					visible: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_TENDER_ITEM_TEXT'),
					field: 'tenderItem.text',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
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
					visible: false,
					width: 175
				},
				{
					name: ' ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: 102,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_ACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'INACTIVE\'" ng-click="grid.appScope.activateEntity(row.entity)" class=" ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DEACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'ACTIVE\'" ng-click="grid.appScope.deactivateEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-on"></i></button>' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_EDIT\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.editEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-edit"></i></button>' +
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
		
		AlgorithmItemsService.getPage(uiGridResource)
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
	
	$scope.addEntity = function (entity) {
		$state.go('settings.algorithmitems_edit', { 'id' : 0 });
	}
	
	$scope.editEntity = function (entity) {
		$state.go('settings.algorithmitems_edit', { 'id' : entity.id });
	}
	
	$scope.activateEntity = function(entity) {
		AlgorithmItemsService.activateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_ACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
			});
	}
	
	$scope.deactivateEntity = function(entity) {
		AlgorithmItemsService.deactivateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_DEACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
			});
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DIALOG_DELETE_HEADER', { entity: $lowercase($translate('ENTITY_ALGORITHM_ITEM')) }),
            message: $translate('DIALOG_DELETE_MESSAGE', { entity: $lowercase($translate('ENTITY_ALGORITHM_ITEM')) }),
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
	                	AlgorithmItemsService.deleteEntity(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
		    						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					} else {
		    						toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
		    					}
		    				})
		    				.error(function(data, status) {
		    					toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_ALGORITHM_ITEM') }));
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
			sortArray.push({ name: "id", priority: 0, direction: uiGridConstants.ASC });
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
	}, 1000);
};

// ========================================================================
//	EDIT CONTROLLER
// ========================================================================
function AlgorithmItemsEditController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, uiGridConstants, AlgorithmItemsService, ItemsService) {
	var $translate = $filter('translate');
	
	$scope.getCompanyItems = function() {
		ItemsService.getEntities('COMPANY')
			.success(function(data, status) {
				if (status == 200) {
					$scope.companyItems = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getTenderItems = function() {
		ItemsService.getEntities('TENDER')
			.success(function(data, status) {
				if (status == 200) {
					$scope.tenderItems = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getConditionalItems = function() {
		AlgorithmItemsService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					var conditionalItemCodes = new Array();
					$.each(data, function(index, value) {
						if (value.id != $stateParams.id) {
							conditionalItemCodes.push(value.code);
						}
					});
					$scope.conditionalItemCodes = conditionalItemCodes;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getEntity = function(id) {
		AlgorithmItemsService.getEntity(id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.entity = data;
					$scope.updateOperator();
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.saveEntity = function() {
		AlgorithmItemsService.saveEntity($scope.entity)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					$state.go('settings.algorithmitems.edit', { 'id' : data.id });
				} else {
					if (data.exception.indexOf("ValidationFailedException") != -1) {
						toastr.warning(data.message, $translate('VALIDATION_FAILED_HEADER'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				}
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
			});		
	};
	
	$scope.back = function() {
		$state.go('settings.algorithmitems_overview');
	};
	
	$scope.updateOperator = function() {
		if ($scope.entity.companyItem && $scope.entity.tenderItem) {
			if ($scope.entity.companyItem.type == 'SELECT' && $scope.entity.tenderItem.type == 'MULTISELECT') {
				$scope.operators = [{ 'value': 'IN', 'name': $translate('OPERATOR_IN') }];
			} else if ($scope.entity.companyItem.type == 'SELECT' && $scope.entity.tenderItem.type == 'SELECT') {
				$scope.operators = [{ 'value': 'EQUAL', 'name': $translate('OPERATOR_EQUAL') }];
			} else if ($scope.entity.companyItem.type == 'NUMBER' && $scope.entity.tenderItem.type == 'NUMBER') {
				$scope.operators = [{ 'value': 'GREATER_OR_EQUAL', 'name': $translate('OPERATOR_GREATER_OR_EQUAL') }];
			} else if ($scope.entity.companyItem.type == 'DATE' && $scope.entity.tenderItem.type == 'NUMBER') {
				$scope.operators = [{ 'value': 'GREATER_OR_EQUAL', 'name': $translate('OPERATOR_GREATER_OR_EQUAL') }, { 'value': 'LESS_OR_EQUAL', 'name': $translate('OPERATOR_LESS_OR_EQUAL') }];
			} else if ($scope.entity.companyItem.type == 'SUBDIVISIONS1' && $scope.entity.tenderItem.type == 'MULTISELECT') {
				$scope.operators = [{ 'value': 'IN', 'name': $translate('OPERATOR_IN') }];
			} else if ($scope.entity.companyItem.type == 'SUBDIVISIONS2' && $scope.entity.tenderItem.type == 'MULTISELECT') {
				$scope.operators = [{ 'value': 'IN', 'name': $translate('OPERATOR_IN') }];
			} else if ($scope.entity.companyItem.type == 'ACTIVITIES' && $scope.entity.tenderItem.type == 'ACTIVITIES') {
				$scope.operators = [{ 'value': 'IN', 'name': $translate('OPERATOR_IN') }];
			} else {
				$scope.operators = [];
				$scope.entity.operator = null;
			}
		} else {
			$scope.entity.operator = null;
		}
	};
	
	// initial load
	$scope.operators = [];
	$scope.getCompanyItems();
	$scope.getTenderItems();
	$scope.getConditionalItems();
	$scope.getEntity($stateParams.id);
};