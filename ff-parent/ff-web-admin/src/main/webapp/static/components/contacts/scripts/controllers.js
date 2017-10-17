angular.module('FundFinder')
	.controller('ContactsOverviewController', ContactsOverviewController)
	.controller('ContactsDetailsController', ContactsDetailsController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function ContactsOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, uiGridExporterConstants, uiGridExporterService, constants, ContactsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	var calculateWidth = function() {
		var width = 0;
		var padding = 4;
		var cnt = 0;
		
		if ($rootScope.hasPermission(['contacts.read'])) {
			width = width + 30;
			cnt++;
		}
		if ($rootScope.hasPermission(['contacts.delete'])) {
			width = width + 26;
			cnt++;
		}
		if ($rootScope.hasPermission(['contacts.export'])) {
			width = width + 30;
			cnt++;
		}

		if (cnt == 1) {
			width = width + padding;
		} else if (cnt > 1) {
			width = width + padding + (cnt * 2) ;
		}
		
		return width;
	};
	
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
			exporterMenuCsv: false,
			exporterMenuPdf: false,
			exporterOlderExcelCompatibility: true,
			exporterCsvFilename: 'kontakti.csv',
			exporterPdfMaxGridWidth: $rootScope.exporterPdfMaxGridWidth,
			exporterPdfDefaultStyle: $rootScope.exporterPdfDefaultStyle,
			exporterPdfTableStyle: $rootScope.exporterPdfTableStyle,
		    exporterPdfTableHeaderStyle: $rootScope.exporterPdfTableHeaderStyle,
		    exporterPdfHeader: $rootScope.exporterPdfHeader($translate('MENU_CONTACTS')),
		    exporterPdfFooter: $rootScope.exporterPdfFooter,
		    exporterPdfCustomFormatter: $rootScope.exporterPdfCustomFormatter,
		    exporterFieldCallback: function (grid, row, col, value) {
		    	if (col.name === 'creationDate') {
		    		return (value) ? ($filter('date')(value, $rootScope.dateTimeFormat)) : '';
		    	}
		    	return value;
		    },
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
					width: 175
				},
				{
					displayName: $translate('COLUMN_COMPANY'),
					field: 'companyName',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_COMPANY_CODE'),
					field: 'companyCode',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 125,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_FIRST_AND_LAST_NAME'),
					field: 'name',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 150,
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
					width: 200,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_PHONE'),
					field: 'phone',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					visible: false,
					width: 150,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_CONTACT_LOCATION'),
					field: 'location',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 200,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					name: ' ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: calculateWidth(),
					exporterSuppressExport: true,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button ng-if="grid.appScope.hasPermission([\'contacts.read\'])" uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-search-plus"></i></button>' +
							'<a href="{{ grid.appScope.contextPath }}/api/v1/contacts/{{row.entity.id}}/export/pdf" target="_blank" ng-if="grid.appScope.hasPermission([\'contacts.export\'])" class="m-l-xs m-r-xs" uib-tooltip="{{\'ACTION_TOOLTIP_EXPORT\' | translate}} {{\'TO_PDF\' | translate}}" tooltip-append-to-body="true"><i class="fa fa-2x fa-download"></i></a>' +
							'<button ng-if="grid.appScope.hasPermission([\'contacts.delete\'])" uib-tooltip="{{\'ACTION_TOOLTIP_DELETE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.deleteEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-times"></i></button>' + 
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
	
	$scope.exportAll = function(format) {
		$('#ExportingModal').modal('show');
		var uiGridResource = { "pagination" : { "page" : 0, "size" : 100000 }, "sort" : $scope.sortArray, "filter" : $scope.filterArray };

		ContactsService.getPage(uiGridResource)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					$scope.gridOptions.data = data.data;
					$scope.gridOptions.totalItems = data.total;
					
					$timeout(function() {
						if (format == 'CSV') {
							$scope.gridApi.exporter.csvExport(uiGridExporterConstants.VISIBLE, uiGridExporterConstants.VISIBLE);
						} else if (format == 'PDF') {
							var rows = new Array();
							
							var gridColumns = uiGridExporterService.getColumnHeaders($scope.gridApi.grid, uiGridExporterConstants.VISIBLE);
							var tmpArray = new Array();
							for (var i=0; i<gridColumns.length; i++) {
								tmpArray.push(
										{ text: gridColumns[i].displayName, style: 'tableHeader', alignment: 'left', fillColor: '#f0f0f0' }
								);
							}
							rows.push(tmpArray);
							
							var gridRows = uiGridExporterService.getData($scope.gridApi.grid, uiGridExporterConstants.VISIBLE, uiGridExporterConstants.VISIBLE, true);
							for (var i=0; i<gridRows.length; i++) {
								tmpArray = new Array();
								for (var j=0; j<gridColumns.length; j++) {
									var value = gridRows[i][j].value;
									tmpArray.push(
											{ text: (value) ? value : "", style: 'tableRow', alignment: 'left' }
									);
								}
								rows.push(tmpArray);
							}
							
							var pdfMakeStyles = {
								header: {
									fontSize: 14,
									bold: true,
									margin: [0, 0, 0, 0]
								},
								tableContent: {
									margin: [0, 10, 0, 0]
								},
								tableHeader: {
									bold: true,
									fontSize: 8
								},
								tableRow: {
									fontSize: 8
								}	
							};
							
							var docDefinition = {
								pageSize: 'A4',
								content: [
									{ text: $translate('MENU_CONTACTS'), style: 'header' },
									{
								    	style: 'tableContent',
								    	table: {
							        		headerRows: 1,
							        		body: rows
							        	}
								    }
								],
								styles: pdfMakeStyles
							};
							
							pdfMake.createPdf(docDefinition).download("kontakti.pdf");
						}
						
						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
						$('#ExportingModal').modal('hide');
					}, 5000);
				} else {
					$log.error(data);
					$('#ExportingModal').modal('hide');
				}
			})
			.error(function(data, status, headers, config) {
				$log.error(data);
				$('#ExportingModal').modal('hide');
			});
	};
	
	$scope.exportSelected = function(format) {
		if (format == 'CSV') {
			$scope.gridApi.exporter.csvExport(uiGridExporterConstants.SELECTED, uiGridExporterConstants.SELECTED);
		} else if (format == 'PDF') {
			var rows = new Array();
			
			var gridColumns = uiGridExporterService.getColumnHeaders($scope.gridApi.grid, uiGridExporterConstants.VISIBLE);
			var tmpArray = new Array();
			for (var i=0; i<gridColumns.length; i++) {
				tmpArray.push(
						{ text: gridColumns[i].displayName, style: 'tableHeader', alignment: 'left', fillColor: '#f0f0f0' }
				);
			}
			rows.push(tmpArray);
			
			var gridRows = uiGridExporterService.getData($scope.gridApi.grid, uiGridExporterConstants.SELECTED, uiGridExporterConstants.SELECTED, true);
			for (var i=0; i<gridRows.length; i++) {
				tmpArray = new Array();
				for (var j=0; j<gridColumns.length; j++) {
					var value = gridRows[i][j].value;
					tmpArray.push(
							{ text: (value) ? value : "", style: 'tableRow', alignment: 'left' }
					);
				}
				rows.push(tmpArray);
			}
			
			var pdfMakeStyles = {
				header: {
					fontSize: 14,
					bold: true,
					margin: [0, 0, 0, 0]
				},
				tableContent: {
					margin: [0, 10, 0, 0]
				},
				tableHeader: {
					bold: true,
					fontSize: 8
				},
				tableRow: {
					fontSize: 8
				}	
			};
			
			var docDefinition = {
				pageSize: 'A4',
				content: [
					{ text: $translate('MENU_CONTACTS'), style: 'header' },
					{
				    	style: 'tableContent',
				    	table: {
			        		headerRows: 1,
			        		body: rows
			        	}
				    }
				],
				styles: pdfMakeStyles
			};
			
			pdfMake.createPdf(docDefinition).download("kontakti.pdf");
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
		
		ContactsService.getPage(uiGridResource)
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
		$state.go('contacts.details', { 'id' : entity.id });
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
	                	ContactsService.deleteEntity(entity.id)
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
			daysOfWeek: $rootScope.dpDaysOfWeek,
			monthNames: $rootScope.dpMonthNames,
			applyLabel: $translate('DATETIMEPICKER_APPLY'),
	        cancelLabel: $translate('DATETIMEPICKER_CANCEL'),
	        fromLabel: $translate('DATETIMEPICKER_FROM'),
	        toLabel: $translate('DATETIMEPICKER_TO'),
	        customRangeLabel: $translate('DATETIMEPICKER_CUSTOM'),
			firstDay: 1
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
			sortArray.push({ name: "creationDate", priority: 0, direction: uiGridConstants.DESC });
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
function ContactsDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, constants, ContactsService) {
	var $translate = $filter('translate');
	
	$scope.getEntity = function(id) {
		ContactsService.getEntity(id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.entity = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.back = function() {
		$state.go('contacts.overview');
	};
	
	// initial load
	$scope.getEntity($stateParams.id);
};