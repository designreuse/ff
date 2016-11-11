function config($translateProvider) {
	$translateProvider
		.translations('en', {
			LANGUAGE: 'Language',
			ENGLISH: 'English',
			CROATIAN: 'Croatian',
			
			LOGOUT: 'Logout',
			
			MENU_USERS: 'Users',
			MENU_TENDERS: 'Tenders',
			MENU_INVESTMENTS: 'Investments',
			MENU_ARTICLES: 'Articles',
			MENU_SETTINGS: 'Settings',
			MENU_TENDER_ITEMS: 'Tender items',
			MENU_COMPANY_ITEMS: 'Company items',
			MENU_ALGORITHM_ITEMS: 'Algorithm items',
			MENU_NKD: 'NKD',
			MENU_CITIES: 'Cities',
			MENU_COUNTIES: 'Counties',
			MENU_DASHBOARD: 'Dashboard',
			MENU_USER_GROUPS: 'User groups',
			MENU_STATISTICS: 'Statistics',
			MENU_BUSINESSRELATIONSHIPMANAGERS: 'Business relationship managers',

			HEADER_MAIN: 'Administration console',
			HEADER_USER_DETAILS: 'User details',
			HEADER_TENDER_DETAILS: 'Tender details',
			HEADER_TENDER_ADD: 'Add tender',
			HEADER_TENDER_EDIT: 'Edit tender',
			HEADER_ARTICLE_DETAILS: 'Article details',
			HEADER_ARTICLE_ADD: 'Add article',
			HEADER_ARTICLE_EDIT: 'Edit article',
			HEADER_INVESTMENT_DETAILS: 'Investment details',
			HEADER_INVESTMENT_ADD: 'Add investment',
			HEADER_INVESTMENT_EDIT: 'Edit investment',
			HEADER_TENDER_ITEM_DETAILS: 'Tender item details',
			HEADER_TENDER_ITEM_ADD: 'Add tender item',
			HEADER_TENDER_ITEM_EDIT: 'Edit tender item',
			HEADER_COMPANY_ITEM_DETAILS: 'Company item details',
			HEADER_COMPANY_ITEM_ADD: 'Add company item',
			HEADER_COMPANY_ITEM_EDIT: 'Edit company item',
			HEADER_NKD_ADD: 'Add NKD',
			HEADER_NKD_EDIT: 'Edit NKD',
			HEADER_COUNTY_ADD: 'Add county',
			HEADER_COUNTY_EDIT: 'Edit county',
			HEADER_CITY_ADD: 'Add city',
			HEADER_CITY_EDIT: 'Edit city',
			HEADER_ALGORITHM_ITEM_ADD: 'Add algorithm item',
			HEADER_ALGORITHM_ITEM_EDIT: 'Edit algorithm item',
			HEADER_EMAIL_SEND: 'Send e-mail',
			HEADER_EMAIL_HISTORY: 'History',
			HEADER_USER_GROUP_ADD: 'Add user group',
			HEADER_USER_GROUP_EDIT: 'Edit user group',
			HEADER_BRM_ADD: 'Add business relationship manager',
			HEADER_BRM_EDIT: 'Edit business relationship manager',
			
			TAB_PREVIEW: 'Preview',
			TAB_STATISTICS: 'Statistics',
			TAB_BASIC_DATA: 'Basic data',
			TAB_USER_DATA: 'User data',
			TAB_COMPANY_DATA: 'Company data',
			TAB_COMMON_ITEMS: 'Common items',
			TAB_CUSTOM_ITEMS: 'Custom items',
			TAB_INVESTMENTS: 'Investments',
			TAB_EMAIL: 'E-mails',
			TAB_MATCHING_USERS: 'Matching users',
			TAB_MATCHING_TENDERS: 'Matching tenders',
			TAB_BRM: 'Business relationship manager',
			
			TAB_STATS_COMPANIES_BY_COUNTIES: 'Companies by counties',
			TAB_STATS_COMPANIES_BY_INVESTMENTS: 'Companies by investments',
			TAB_STATS_COMPANIES_BY_REVENUES: 'Companies by revenues',
			TAB_STATS_COMPANIES_BY_SECTORS: 'Companies by sectors',
			
			LABEL_NAME: 'Name',
			LABEL_CODE: 'Code',
			LABEL_TEXT: 'Text',
			LABEL_TYPE: 'Type',
			LABEL_IMAGE: 'Image',
			LABEL_USER: 'User',
			LABEL_USERS: 'Users',
			LABEL_ENTITY_TYPE: 'Enitity type',
			LABEL_ENTITY_ATTRIBUTES: 'Entity attributes',
			LABEL_STATISTICS_FOR: 'Statistics for',
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_POSITION: 'Position',
			LABEL_MANDATORY: 'Mandatory',
			LABEL_OPTIONS: 'Options',
			LABEL_COUNTY: 'County',
			LABEL_FIRST_NAME: 'First name',
			LABEL_LAST_NAME: 'Last name',
			LABEL_PHONE: 'Phone',
			LABEL_MOBILE: 'Mobile',
			LABEL_EMAIL: 'Email',
			LABEL_CREATION_DATE: 'Registered',
			LABEL_LAST_MODIFIED_DATE: 'Last modified',
			LABEL_LAST_LOGIN_DATE: 'Last login',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_COMPANY_CODE: 'Identification code',
			LABEL_EDIT: 'EDIT',
			LABEL_EDIT_POSTSCRIPT: 'Indicates how item looks like when edited (e.g. edit tender)',
			LABEL_DISPLAY: 'DISPLAY',
			LABEL_DISPLAY_POSTSCRIPT: 'Indicates how item looks like when displayed to user',
			LABEL_SECTOR: 'Sector',
			LABEL_SECTOR_NAME: 'Sector name',
			LABEL_AREA: 'Area',
			LABEL_ACTIVITY: 'Activity',
			LABEL_ACTIVITY_NAME: 'Activity name',
			LABEL_COMPANY_ITEM: 'Company item',
			LABEL_TENDER_ITEM: 'Tender item',
			LABEL_OPERATOR: 'Operator',
			LABEL_OPERATOR_NOTE: 'Operator is automatically set by the system based on the type of selected company and tender items',
			LABEL_CONDITIONAL_ITEM: 'Conditional item',
			LABEL_DEVELOPMENT_INDEX: 'Development index',
			LABEL_SEND_EMAIL_TO: 'To',
			LABEL_SEND_EMAIL_SUBJECT: 'Subject',
			LABEL_SEND_EMAIL_TEXT: 'Text',
			LABEL_SEND_EMAIL_TO_PLACEHOLDER: 'Please select...',
			LABEL_METATAG: 'Meta tag',
			LABEL_WIDGET_ITEM: 'Show as widget item',
			LABEL_HELP: 'Help',
			
			UI_GRID_LOADING_DATA: 'Loading data...',
			UI_GRID_NO_DATA: 'No data',
			
			COLUMN_ID: 'ID',
			COLUMN_NAME: 'Name',
			COLUMN_FIRST_NAME: 'First name',
			COLUMN_LAST_NAME: 'Last name',
			COLUMN_EMAIL: 'Email',
			COLUMN_COMPANY: 'Company',
			COLUMN_COMPANY_CODE: 'Company code',
			COLUMN_CODE: 'Code',
			COLUMN_STATUS: 'Status',
			COLUMN_TYPE: 'Type',
			COLUMN_POSITION: 'Position',
			COLUMN_MANDATORY: 'Mandatory',
			COLUMN_TEXT: 'Text',
			COLUMN_CREATION_DATE: 'Creation date',
			COLUMN_LAST_MODIFIED_DATE: 'Last modified date',
			COLUMN_SECTOR: 'Sector',
			COLUMN_SECTOR_NAME: 'Sector name',
			COLUMN_AREA: 'Area',
			COLUMN_ACTIVITY: 'Activity',
			COLUMN_ACTIVITY_NAME: 'Activity name',
			COLUMN_COUNTY: 'County',
			COLUMN_COMPANY_ITEM_CODE: 'Company item code',
			COLUMN_COMPANY_ITEM_TEXT: 'Company item text',
			COLUMN_TENDER_ITEM_CODE: 'Tender item code',
			COLUMN_TENDER_ITEM_TEXT: 'Tender item text',
			COLUMN_OPERATOR: 'Operator',
			COLUMN_DEVELOPMENT_INDEX: 'Development index',
			COLUMN_SEND_DATE: 'Send date',
			COLUMN_TO: 'To',
			COLUMN_SUBJECT: 'Subject',
			COLUMN_INVESTMENT: 'Investment',
			COLUMN_REVENUE: 'Revenue',
			COLUMN_SECTOR: 'Sector',
			COLUMN_NO_OF_COMPANIES: 'Number of companies',
			COLUMN_PHONE: 'Phone',
			COLUMN_MOBILE: 'Mobile',

			DATETIMEPICKER_TODAY: 'Today',
			DATETIMEPICKER_YESTERDAY: 'Yesterday',
			DATETIMEPICKER_LAST_7_DAYS: 'Last 7 days',
			DATETIMEPICKER_LAST_30_DAYS: 'Last 30 days',
			DATETIMEPICKER_THIS_MONTH: 'This month',
			DATETIMEPICKER_LAST_MONTH: 'Last month',
			DATETIMEPICKER_CUSTOM: 'Custom period',
			
			STATISTICS_PERIOD_LAST_7_DAYS: 'Last 7 days',
			STATISTICS_PERIOD_LAST_6_MONTHS: 'Last 6 months',
			
			STATUS_ACTIVE: 'Active',
			STATUS_INACTIVE: 'Inactive',
			
			ITEM_TYPE_TEXT: 'Text',
			ITEM_TYPE_TEXT_DESCRIPTION: 'Rendered as input text field. Maximum length is 512 characters.',
			ITEM_TYPE_TEXT_AREA: 'Text area',
			ITEM_TYPE_TEXT_AREA_DESCRIPTION: 'Rendered as text area that enables some primitive formatting features (e.g. new line, new paragraph). Maximum length is 4096 characters.',
			ITEM_TYPE_TEXT_EDITOR: 'Text editor',
			ITEM_TYPE_NUMBER: 'Number',
			ITEM_TYPE_NUMBER_DESCRIPTION: 'Rendered as input field that accepts numbers only.',
			ITEM_TYPE_DATE: 'Date',
			ITEM_TYPE_DATE_DESCRIPTION: 'Rendered as date picker component.',
			ITEM_TYPE_DATE_TIME: 'Date time',
			ITEM_TYPE_RADIO: 'Radio',
			ITEM_TYPE_RADIO_DESCRIPTION: 'Rendered as radio button group where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_CHECKBOX: 'Check box',
			ITEM_TYPE_CHECKBOX_DESCRIPTION: 'Rendered as checkbox(es) where none, one or many option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_SELECT: 'Select',
			ITEM_TYPE_SELECT_DESCRIPTION: 'Rendered as select component where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_MULTISELECT: 'Multiselect',
			ITEM_TYPE_MULTISELECT_DESCRIPTION: 'Rendered as select component where none (optional), one or many (mandatory) option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_HYPERLINK: 'Hyperlink',
			ITEM_TYPE_HYPERLINK_DESCRIPTION: 'Rendered as clickable hyperlink that will open entered URL in new browser window.',
			ITEM_TYPE_CITY: 'City',
			ITEM_TYPE_CITY_DESCRIPTION: 'Rendered as select component where none (optional) or one (mandatory) option can be selected. Options are predefined and cannot be changed.',
			ITEM_TYPE_COUNTIES: 'Counties',
			ITEM_TYPE_COUNTIES_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are predefined and cannot be changed.',
			ITEM_TYPE_NKD: 'NKD',
			ITEM_TYPE_NKD_DESCRIPTION: 'Rendered as select component where none (optional) or one (mandatory) option can be selected. Options are predefined and cannot be changed.',
			ITEM_TYPE_NKDS: 'NKDs',
			ITEM_TYPE_NKDS_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are predefined and cannot be changed.',
			ITEM_TYPE_INVESTMENTS: 'Investments',
			ITEM_TYPE_INVESTMENTS_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Only active investments are displayed as option(s).',
			
			ALGORITHM_ITEM_TYPE_MANDATORY: 'Mandatory',
			ALGORITHM_ITEM_TYPE_OPTIONAL: 'Optional',
			ALGORITHM_ITEM_TYPE_CONDITIONAL: 'Conditional',
			
			OPERATOR_EQUALS: 'Equals',
			OPERATOR_IN: 'In',
			
			BOOLEAN_TRUE: 'Yes',
			BOOLEAN_FALSE: 'No',
			
			USER_GROUP_METATAG_MATCHING_USERS: 'Matching users',
			
			ENTITY_USER: 'User',
			ENTITY_USER_GROUP: 'User group',
			ENTITY_TENDER: 'Tender',
			ENTITY_ARTICLE: 'Article',
			ENTITY_INVESTMENT: 'Investment',
			ENTITY_ALGORITHM_ITEM: 'Algorithm item',
			ENTITY_ITEM: 'Item',
			ENTITY_NKD: 'NKD',
			ENTITY_COUNTY: 'County',
			ENTITY_CITY: 'City',
			ENTITY_BRM: 'Business relationship manager',
			
			ROLE_ADMIN: 'Administrator',
			ROLE_OPERATOR_L1: 'Operator L1',
			ROLE_OPERATOR_L2: 'Operator L2',
			ROLE_OPERATOR_L3: 'Operator L3',
			ROLE_UNKNOWN: 'Unknown',
			
			ACTION_TOOLTIP_ACTIVATE: 'Activate',
			ACTION_TOOLTIP_DEACTIVATE: 'Deactivate',
			ACTION_TOOLTIP_EDIT: 'Edit',
			ACTION_TOOLTIP_DELETE: 'Delete',
			ACTION_TOOLTIP_DETAILS: 'Details',
			ACTION_TOOLTIP_EXPAND: 'Expand',
			ACTION_TOOLTIP_COLLAPSE: 'Collapse',
			
			TOOLTIP_TOTAL_IMPRESSIONS: 'Total impressions',
			TOOLTIP_UNIQUE_IMPRESSIONS: 'Unique impressions',
			TOOLTIP_CHART_VIEW: 'Chart view',
			TOOLTIP_TABLE_VIEW: 'Table view',
			
			ACTION_ACTIVATE_SUCCESS_MESSAGE: '{{entity}} successfully activated',
			ACTION_ACTIVATE_FAILURE_MESSAGE: 'Activating {{entity}} failed',
			ACTION_DEACTIVATE_SUCCESS_MESSAGE: '{{entity}} successfully deactivated',
			ACTION_DEACTIVATE_FAILURE_MESSAGE: 'Deactivating {{entity}} failed',
			ACTION_SAVE_FAILURE_MESSAGE: 'Saving data failed',
			ACTION_SAVE_SUCCESS_MESSAGE: 'Saving data successful',
			ACTION_DELETE_SUCCESS_MESSAGE: '{{entity}} successfully deleted',
			ACTION_DELETE_FAILURE_MESSAGE: 'Deleting {{entity}} failed',
			ACTION_LOAD_FAILURE_MESSAGE: 'Loading data failed',
			ACTION_SEND_EMAIL_SUCCESS_MESSAGE: 'Sending email successful',
			ACTION_SEND_EMAIL_FAILURE_MESSAGE: 'Sending email failed',
			ACTION_SEND_EMAIL_HTTP_STATUS_202_MESSAGE: 'Sending email aborted as no eligible recipient found',
			
			DIALOG_DELETE_HEADER: 'Delete {{entity}}',
			DIALOG_DELETE_MESSAGE: 'Do you really want to delete {{entity}}?',
			
			POSTSCRIPT_CODE: 'Identificator for cross-referencing algorithm',
			POSTSCRIPT_ITEM_POSITION: 'Value must be between 1 and 1000',
			
			VALIDATION_FAILED_HEADER: 'Validation failed',
			
			ERR_MSG_001: 'Company item with meta tag COMPANY_LOCATION not found',
			ERR_MSG_002: 'Meta tag COMPANY_LOCATION found in more then one company item',
			
			BUTTON_YES: 'Yes',
			BUTTON_NO: 'No',
			BUTTON_BACK: 'Back',
			BUTTON_SAVE: 'Save',
			BUTTON_ADD: 'Add',
			BUTTON_EDIT: 'Edit',
			BUTTON_CLOSE: 'Close',
			BUTTON_SELECT: 'Select...',
			BUTTON_DELETE: 'Delete',
			BUTTON_SEND: 'Send',
			BUTTON_RESET: 'Reset',
			
			DUALLIST_FILTER_CLEAR: 'Clear',
			DUALLIST_FILTER_PLACEHOLDER: 'Filter',
			DUALLIST_MOVE_ALL: 'Move all',
			DUALLIST_MOVE_SELECTED: 'Move selected',
			DUALLIST_REMOVE_ALL: 'Remove all',
			DUALLIST_REMOVE_SELECTED: 'Remove selected',
			DUALLIST_SELECTED_LIST_LABEL: 'Assigned',
			DUALLIST_UNSELECTED_LIST_LABEL: 'Unassigned',
			DUALLIST_ACCESS_NOT_ALLOWED_LIST_LABEL: 'Access not allowed',
			DUALLIST_ACCESS_ALLOWED_LIST_LABEL: 'Access allowed',
			DUALLIST_INFO_ALL: 'Showing all {0}',
			DUALLIST_INFO_EMPTY: 'Empty list',
			DUALLIST_INFO_FILTERED: '<span class="label label-warning">Filtered</span> {0} from {1}',
		});
	
	$translateProvider.preferredLanguage("en");
};

angular.module('FundFinder').config(config);
