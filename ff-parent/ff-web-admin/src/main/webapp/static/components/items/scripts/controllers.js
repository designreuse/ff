angular.module('FundFinder')
	.controller('ItemsOverviewController', ItemsOverviewController)
	.controller('ItemsDetailsController', ItemsDetailsController)
	.controller('ItemsEditController', ItemsEditController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function ItemsOverviewController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, uiGridConstants, Upload, ItemsService, FileSaver, Blob) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.entityType = $stateParams.entityType;
	
	var selectOptions4Type = new Array();
	selectOptions4Type.push({ value: 'TEXT', label: $translate('ITEM_TYPE_TEXT') });
	selectOptions4Type.push({ value: 'TEXT_AREA', label: $translate('ITEM_TYPE_TEXT_AREA') });
	selectOptions4Type.push({ value: 'NUMBER', label: $translate('ITEM_TYPE_NUMBER') });
	selectOptions4Type.push({ value: 'CURRENCY', label: $translate('ITEM_TYPE_CURRENCY') });
	if ($scope.entityType == 'tender') {
		selectOptions4Type.push({ value: 'PERCENTAGE', label: $translate('ITEM_TYPE_PERCENTAGE') });
	}
	selectOptions4Type.push({ value: 'DATE', label: $translate('ITEM_TYPE_DATE') });
//	selectOptions4Type.push({ value: 'RADIO', label: $translate('ITEM_TYPE_RADIO') });
//	selectOptions4Type.push({ value: 'CHECKBOX', label: $translate('ITEM_TYPE_CHECKBOX') });
	selectOptions4Type.push({ value: 'SELECT', label: $translate('ITEM_TYPE_SELECT') });
	selectOptions4Type.push({ value: 'MULTISELECT', label: $translate('ITEM_TYPE_MULTISELECT') });
	if ($scope.entityType == 'tender') {
		selectOptions4Type.push({ value: 'HYPERLINK', label: $translate('ITEM_TYPE_HYPERLINK') });
	}
	if ($scope.entityType == 'company') {
		selectOptions4Type.push({ value: 'ACTIVITY', label: $translate('ITEM_TYPE_ACTIVITY') });
		selectOptions4Type.push({ value: 'SUBDIVISION1', label: $translate('ITEM_TYPE_SUBDIVISION1') });
		selectOptions4Type.push({ value: 'SUBDIVISION2', label: $translate('ITEM_TYPE_SUBDIVISION2') });
	}
	if ($scope.entityType == 'tender') {
		selectOptions4Type.push({ value: 'SUBDIVISIONS1', label: $translate('ITEM_TYPE_SUBDIVISIONS1') });
		selectOptions4Type.push({ value: 'SUBDIVISIONS2', label: $translate('ITEM_TYPE_SUBDIVISIONS2') });
		selectOptions4Type.push({ value: 'INVESTMENTS_PRIMARY', label: $translate('ITEM_TYPE_INVESTMENTS_PRIMARY') });
		selectOptions4Type.push({ value: 'INVESTMENTS_SECONDARY', label: $translate('ITEM_TYPE_INVESTMENTS_SECONDARY') });
	}
	
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
					displayName: $translate('COLUMN_POSITION'),
					field: 'position',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<input type="number" min="0" class="ui-grid-filter-input ui-grid-filter-input-0 ng-dirty ng-valid-parse ng-touched ui-grid-filter-input-port" ng-model="colFilter.term" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" aria-label="Filter for column" placeholder="">' + 
						'</div>', 
					enableHiding: false,
					width: 75
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
					displayName: $translate('COLUMN_TEXT'),
					field: 'text',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					cellTemplate: '<div class="ui-grid-cell-contents"><a class="ff-a" ng-click="grid.appScope.showEntity(row.entity)">{{row.entity.text}}</a></div>',
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_TYPE'),
					field: 'type',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: selectOptions4Type
					},
					enableHiding: true,
					width: 150,
					cellTemplate: '<div class="ui-grid-cell-contents">{{ (\'ITEM_TYPE_\' + row.entity.type) | translate }}</div>' 
				},
				{
					displayName: $translate('COLUMN_MANDATORY'),
					field: 'mandatory',
					type: 'boolean',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
							{ value: 'true', label: $translate('BOOLEAN_TRUE') },
							{ value: 'false', label: $translate('BOOLEAN_FALSE') }]
					},
					width: 100,
					cellTemplate:'<div ng-show="row.entity.mandatory" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-check-square-o"></span></div><div ng-show="!row.entity.mandatory" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-square-o"></span></div>'
				},
				{
					displayName: $translate('COLUMN_SUMMARY_ITEM'),
					field: 'summaryItem',
					type: 'boolean',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: ($scope.entityType == 'tender') ? true : false,
					visible: ($scope.entityType == 'tender') ? true : false,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
							{ value: 'true', label: $translate('BOOLEAN_TRUE') },
							{ value: 'false', label: $translate('BOOLEAN_FALSE') }]
					},
					width: 100,
					cellTemplate:'<div ng-show="row.entity.summaryItem" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-check-square-o"></span></div><div ng-show="!row.entity.summaryItem" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-square-o"></span></div>'
				},
				{
					displayName: $translate('COLUMN_WIDGET_ITEM'),
					field: 'widgetItem',
					type: 'boolean',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: ($scope.entityType == 'tender') ? true : false,
					visible: ($scope.entityType == 'tender') ? true : false,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
							{ value: 'true', label: $translate('BOOLEAN_TRUE') },
							{ value: 'false', label: $translate('BOOLEAN_FALSE') }]
					},
					width: 100,
					cellTemplate:'<div ng-show="row.entity.widgetItem" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-check-square-o"></span></div><div ng-show="!row.entity.widgetItem" class="ui-grid-cell-contents" style="text-align: center;"><span class="fa fa-square-o"></span></div>'
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
					enableHiding: true,
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
					width: 134,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-search-plus"></i></button>' +	
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
		if (!$scope.filterArray) {
			$scope.filterArray = new Array();
		}
		$scope.filterArray.push({
			"name" : "entityType",
			"term" : $stateParams.entityType
		});
		
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
		
		$stateParams.entityType;
		
		ItemsService.getPage(uiGridResource)
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
		$state.go('settings.items_details_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType, 'id' : entity.id });
	}
	
	$scope.addEntity = function (entity) {
		$state.go('settings.items_edit_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType, 'id' : 0 });
	}
	
	$scope.editEntity = function (entity) {
		$state.go('settings.items_edit_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType, 'id' : entity.id });
	}
	
	$scope.importData = function(files) {
		if (files && files.length) {
			Upload.upload({
		        url: '/api/v1/items/import',
		        method: 'POST',
		        file: files[0]
		    }).success(function (data, status, headers, config) {
		    	toastr.success($translate('ACTION_IMPORT_SUCCESS_MESSAGE'));
		    	$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    }).error(function(data, status, headers, config) {
		    	toastr.error($translate('ACTION_IMPORT_FAILURE_MESSAGE'));
		    });
		}
	};
	
	$scope.exportData = function() {
		ItemsService.exportData($stateParams.entityType)
			.success(function(data, status) {
				var blob = new Blob([angular.toJson(data)], { type : "application/json;charset=utf-8;" });	
				FileSaver.saveAs(blob, "ff_" + $lowercase($stateParams.entityType)  + "_items.json");
				toastr.success($translate('ACTION_EXPORT_SUCCESS_MESSAGE'));
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_EXPORT_FAILURE_MESSAGE'));
			});
	};
	
	$scope.activateEntity = function(entity) {
		ItemsService.activateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_ACTIVATE_SUCCESS_MESSAGE'));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE'));
			});
	}
	
	$scope.deactivateEntity = function(entity) {
		ItemsService.deactivateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_DEACTIVATE_SUCCESS_MESSAGE'));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
			});
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DIALOG_DELETE_HEADER'),
            message: $translate('DIALOG_DELETE_MESSAGE'),
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
	                	ItemsService.deleteEntity(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE'));
		    						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					} else {
		    						toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE'));
		    					}
		    				})
		    				.error(function(data, status) {
		    					toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE'));
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
			sortArray.push({ name: "position", priority: 0, direction: uiGridConstants.ASC });
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
//	DETAILS CONTROLLER
// ========================================================================
function ItemsDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, $sce, uiGridConstants, ItemsService, Subdivisions1Service, Subdivisions2Service, ActivitiesService, InvestmentsService, CurrenciesService) {
	var $translate = $filter('translate');
	
	$scope.entityType = $stateParams.entityType;
	
	var trusted = {};
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return trusted[html] || (trusted[html] = $sce.trustAsHtml(html)); 
	}
	
	$scope.editEntity = function() {
		$state.go('settings.items_edit_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType, 'id' : $stateParams.id });
	}
	
	$scope.back = function() {
		$state.go('settings.items_overview_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType });
	};
	
	$scope.getSubdivisions1 = function() {
		Subdivisions1Service.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions1 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getSubdivisions2 = function() {
		Subdivisions2Service.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions2 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.radioPreview = function(object) {
		var result = "";
		if (object) {
			$.each($scope.entity.options, function(key, value) {
				if (value.id == object) {
					result = result + value.text + "<br>";
					return;
				}
			});
		}
		return result;
	}
	
	$scope.checkboxPreview = function(object) {
		var result = "";
		if (object) {
			$.each(object, function(key1, value1) {
				if (value1 == true) {
					$.each($scope.entity.options, function(key2, value2) {
						if (value2.id == key1) {
							result = result + value2.text + "<br>";
							return;
						}
					});
				}
			});			
		}
		return result;
	}
	
	$scope.getActivities = function() {
		ActivitiesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.activities = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getInvestments = function() {
		InvestmentsService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.investments = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getCurrencies = function() {
		CurrenciesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.currencies = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};

	$scope.dictPopupDate = { opened: false };
	$scope.openPopupDate = function(index) {
		$scope.dictPopupDate.opened = true;
	};
	
	// initial load
	$scope.getCurrencies();
	$scope.getInvestments();
	$scope.getSubdivisions1();
	$scope.getSubdivisions2();
	$scope.getActivities();
	
	ItemsService.getEntity($stateParams.entityType, $stateParams.id)
		.success(function(data, status) {
			if (status == 200) {
				$scope.entity = data;
				$scope.entity.currency = $scope.currencies[0];
			} else {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});	
};

// ========================================================================
//	EDIT CONTROLLER
// ========================================================================
function ItemsEditController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, uiGridConstants, ItemsService) {
	var $translate = $filter('translate');
	
	$scope.entityType = $stateParams.entityType;
	
	$scope.getEntity = function(entityType, id) {
		ItemsService.getEntity(entityType, id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.entity = data;
					$scope.typeChanged();
					$scope.getMetaTags($scope.entity.entityType);
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.saveEntity = function() {
		ItemsService.saveEntity($scope.entity)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					$scope.getEntity($stateParams.entityType, data.id);
					$state.go('settings.items_edit_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType, 'id' : data.id });
				} else {
					if (data.exception.indexOf("ValidationFailedException") != -1) {
						toastr.warning(data.message, $translate('VALIDATION_FAILED_HEADER'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				}
			})
			.error(function(data, status, headers, config) {
				if (data.exception.indexOf("ValidationFailedException") != -1) {
					toastr.warning(data.message, $translate('VALIDATION_FAILED_HEADER'));
				} else {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				}
			});		
	};
	
	$scope.getMetaTags = function(entityType) {
		ItemsService.getMetaTags(entityType)
			.success(function(data, status) {
				$scope.metaTags = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.typeChanged = function() {
		if ($scope.entity.type == 'TEXT') {
			$scope.typeDescription = $translate('ITEM_TYPE_TEXT_DESCRIPTION');
		} else if ($scope.entity.type == 'TEXT_AREA') {
			$scope.typeDescription = $translate('ITEM_TYPE_TEXT_AREA_DESCRIPTION');
		} else if ($scope.entity.type == 'NUMBER') {
			$scope.typeDescription = $translate('ITEM_TYPE_NUMBER_DESCRIPTION');
		} else if ($scope.entity.type == 'CURRENCY') {
			$scope.typeDescription = $translate('ITEM_TYPE_CURRENCY_DESCRIPTION');
		} else if ($scope.entity.type == 'PERCENTAGE') {
			$scope.typeDescription = $translate('ITEM_TYPE_PERCENTAGE_DESCRIPTION');
		} else if ($scope.entity.type == 'DATE') {
			$scope.typeDescription = $translate('ITEM_TYPE_DATE_DESCRIPTION');
		} else if ($scope.entity.type == 'RADIO') {
			$scope.typeDescription = $translate('ITEM_TYPE_RADIO_DESCRIPTION');
		} else if ($scope.entity.type == 'CHECKBOX') {
			$scope.typeDescription = $translate('ITEM_TYPE_CHECKBOX_DESCRIPTION');
		} else if ($scope.entity.type == 'SELECT') {
			$scope.typeDescription = $translate('ITEM_TYPE_SELECT_DESCRIPTION');
		} else if ($scope.entity.type == 'MULTISELECT') {
			$scope.typeDescription = $translate('ITEM_TYPE_MULTISELECT_DESCRIPTION');
		} else if ($scope.entity.type == 'HYPERLINK') {
			$scope.typeDescription = $translate('ITEM_TYPE_HYPERLINK_DESCRIPTION');
		} else if ($scope.entity.type == 'ACTIVITY') {
			$scope.typeDescription = $translate('ITEM_TYPE_ACTIVITY_DESCRIPTION');
		} else if ($scope.entity.type == 'ACTIVITIES') {
			$scope.typeDescription = $translate('ITEM_TYPE_ACTIVITIES_DESCRIPTION');
		} else if ($scope.entity.type == 'SUBDIVISION1') {
			$scope.typeDescription = $translate('ITEM_TYPE_SUBDIVISION1_DESCRIPTION');
		} else if ($scope.entity.type == 'SUBDIVISION2') {
			$scope.typeDescription = $translate('ITEM_TYPE_SUBDIVISION2_DESCRIPTION');
		} else if ($scope.entity.type == 'SUBDIVISIONS1') {
			$scope.typeDescription = $translate('ITEM_TYPE_SUBDIVISIONS1_DESCRIPTION');
		} else if ($scope.entity.type == 'SUBDIVISIONS2') {
			$scope.typeDescription = $translate('ITEM_TYPE_SUBDIVISIONS2_DESCRIPTION');
		} else if ($scope.entity.type == 'INVESTMENTS_PRIMARY' || $scope.entity.type == 'INVESTMENTS_SECONDARY') {
			$scope.typeDescription = $translate('ITEM_TYPE_INVESTMENTS_DESCRIPTION');
		}
	};
	
	$scope.addOption = function(index) {
		$scope.entity.options.splice(index + 1, 0, { });
	}
	
	$scope.removeOption = function(index) {
		$scope.entity.options.splice(index, 1);
	}
	
	$scope.back = function() {
		$state.go('settings.items_overview_' + $stateParams.entityType, { 'entityType' : $stateParams.entityType });
	};
	
	$scope.summernoteOptions = {
		    height: 300,
		    focus: false,
		    airMode: false,
		    toolbar: [
		            ['style', ['bold', 'italic', 'underline']],
		            ['alignment', ['ul', 'ol', 'paragraph']]
		    ]
		};
	
	// initial load
	$scope.getEntity($stateParams.entityType, $stateParams.id);
};