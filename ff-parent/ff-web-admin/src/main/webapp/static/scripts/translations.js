function config($translateProvider) {
	$translateProvider
		.translations('en', {
			LANGUAGE: 'Language',
			ENGLISH: 'English',
			CROATIAN: 'Croatian',
			
			LOGOUT: 'Logout',
			
			MENU_ACTIVITIES: 'Activities',
			MENU_ALGORITHM_ITEMS: 'Algorithm items',
			MENU_ARTICLES: 'Articles',
			MENU_BUSINESSRELATIONSHIPMANAGERS: 'Business relationship managers',
			MENU_COMPANY_ITEMS: 'Company items',
			MENU_DASHBOARD: 'Dashboard',
			MENU_INVESTMENTS: 'Investments',
			MENU_ROLES: 'Roles',
			MENU_SETTINGS: 'Settings',
			MENU_STATISTICS: 'Statistics',
			MENU_SUBDIVISIONS1: 'Counties',
			MENU_SUBDIVISIONS2: 'Municipalities / cities',
			MENU_TENDERS: 'Tenders',
			MENU_TENDER_ITEMS: 'Tender items',
			MENU_USERS: 'Users',
			MENU_USER_GROUPS: 'User groups',

			HEADER_ACTIVITY_ADD: 'Add activity',
			HEADER_ACTIVITY_EDIT: 'Edit activity',
			HEADER_ALGORITHM_ITEM_ADD: 'Add algorithm item',
			HEADER_ALGORITHM_ITEM_EDIT: 'Edit algorithm item',
			HEADER_ARTICLE_ADD: 'Add article',
			HEADER_ARTICLE_DETAILS: 'Article details',
			HEADER_ARTICLE_EDIT: 'Edit article',
			HEADER_BRM_ADD: 'Add business relationship manager',
			HEADER_BRM_EDIT: 'Edit business relationship manager',
			HEADER_COMPANY_ITEM_ADD: 'Add company item',
			HEADER_COMPANY_ITEM_DETAILS: 'Company item details',
			HEADER_COMPANY_ITEM_EDIT: 'Edit company item',
			HEADER_EMAIL_HISTORY: 'History',
			HEADER_EMAIL_SEND: 'Send e-mail',
			HEADER_INVESTMENT_ADD: 'Add investment',
			HEADER_INVESTMENT_DETAILS: 'Investment details',
			HEADER_INVESTMENT_EDIT: 'Edit investment',
			HEADER_MAIN: 'Administration console',
			HEADER_ROLE_ADD: 'Add role',
			HEADER_ROLE_EDIT: 'Edit role',
			HEADER_SUBDIVISION1_ADD: 'Add county',
			HEADER_SUBDIVISION1_EDIT: 'Edit county',
			HEADER_SUBDIVISION2_ADD: 'Add municipality / city',
			HEADER_SUBDIVISION2_EDIT: 'Edit municipality / city',
			HEADER_TENDER_ADD: 'Add tender',
			HEADER_TENDER_DETAILS: 'Tender details',
			HEADER_TENDER_EDIT: 'Edit tender',
			HEADER_TENDER_ITEM_ADD: 'Add tender item',
			HEADER_TENDER_ITEM_DETAILS: 'Tender item details',
			HEADER_TENDER_ITEM_EDIT: 'Edit tender item',
			HEADER_USER_DETAILS: 'User details',
			HEADER_USER_GROUP_ADD: 'Add user group',
			HEADER_USER_GROUP_EDIT: 'Edit user group',
			
			TAB_BASIC_DATA: 'Basic data',
			TAB_BRM: 'Business relationship manager',
			TAB_COMMON_ITEMS: 'Common items',
			TAB_COMPANY_DATA: 'Company data',
			TAB_CUSTOM_ITEMS: 'Custom items',
			TAB_EMAIL: 'E-mails',
			TAB_PROJECTS: 'Projects',
			TAB_MATCHING_TENDERS: 'Matching tenders',
			TAB_MATCHING_USERS: 'Matching users',
			TAB_PREVIEW: 'Preview',
			TAB_STATISTICS: 'Statistics',
			TAB_USER_DATA: 'User data',
			
			TAB_STATS_COMPANIES_BY_COUNTIES: 'Companies by counties',
			TAB_STATS_COMPANIES_BY_INVESTMENTS: 'Companies by investments',
			TAB_STATS_COMPANIES_BY_REVENUES: 'Companies by revenues',
			TAB_STATS_COMPANIES_BY_SECTORS: 'Companies by sectors',
			
			LABEL_ACTIVITY: 'Activity',
			LABEL_ACTIVITY_NAME: 'Activity name',
			LABEL_AREA: 'Area',
			LABEL_CODE: 'Code',
			LABEL_COMPANY_CODE: 'Identification code',
			LABEL_COMPANY_ITEM: 'Company item',
			LABEL_COMPANY_NAME: 'Name',
			LABEL_CONDITIONAL_ITEM: 'Conditional item',
			LABEL_CREATION_DATE: 'Registered',
			LABEL_DESCRIPTION: 'Description',
			LABEL_DEVELOPMENT_INDEX: 'Development index',
			LABEL_DISPLAY: 'DISPLAY',
			LABEL_DISPLAY_POSTSCRIPT: 'Indicates how item looks like when displayed to user',
			LABEL_EDIT: 'EDIT',
			LABEL_EDIT_POSTSCRIPT: 'Indicates how item looks like when edited (e.g. edit tender)',
			LABEL_EMAIL: 'Email',
			LABEL_ENTITY_ATTRIBUTES: 'Entity attributes',
			LABEL_ENTITY_TYPE: 'Enitity type',
			LABEL_FIRST_NAME: 'First name',
			LABEL_HELP: 'Help',
			LABEL_IMAGE: 'Image',
			LABEL_INVESTMENT: 'Investment',
			LABEL_LAST_LOGIN_DATE: 'Last login',
			LABEL_LAST_MODIFIED: 'Last modified',
			LABEL_LAST_MODIFIED_DATE: 'Last modified',
			LABEL_LAST_NAME: 'Last name',
			LABEL_MAIN: 'Main',
			LABEL_MANDATORY: 'Mandatory',
			LABEL_MANDATORY_ITEMS_ONLY: 'Mandatory items only',
			LABEL_METATAG: 'Meta tag',
			LABEL_MOBILE: 'Mobile',
			LABEL_NAME: 'Name',
			LABEL_OPERATOR: 'Operator',
			LABEL_OPTIONS: 'Options',
			LABEL_PERMISSIONS: 'Permissions',
			LABEL_PHONE: 'Phone',
			LABEL_POSITION: 'Position',
			LABEL_READ_MORE: 'Read more',
			LABEL_REGISTERED_USERS: 'Registered users',
			LABEL_REGISTRATION_TYPE: 'Registration type',
			LABEL_SECTOR: 'Sector',
			LABEL_SECTOR_NAME: 'Sector name',
			LABEL_SEND_EMAIL_SUBJECT: 'Subject',
			LABEL_SEND_EMAIL_TEXT: 'Text',
			LABEL_SEND_EMAIL_TO: 'To',
			LABEL_SEND_EMAIL_TO_PLACEHOLDER: 'Please select...',
			LABEL_SUBDIVISION1: 'County',
			LABEL_SUBSTITUTE: 'Substitute',
			LABEL_SUMMARY_ITEM: 'Summary item',
			LABEL_STATISTICS_FOR: 'Statistics for',
			LABEL_TENDER_ITEM: 'Tender item',
			LABEL_TEXT: 'Text',
			LABEL_TYPE: 'Type',
			LABEL_UNIQUE_VISITS: 'Unique visits',
			LABEL_USER: 'User',
			LABEL_USERS: 'Users',
			LABEL_VISITS: 'Visits',
			LABEL_WIDGET_ITEM: 'Widget item',
			
			COLUMN_ACTIVITY: 'Activity',
			COLUMN_ACTIVITY_NAME: 'Activity name',
			COLUMN_AREA: 'Area',
			COLUMN_CODE: 'Code',
			COLUMN_COMPANY: 'Company',
			COLUMN_COMPANY_CODE: 'Company code',
			COLUMN_COMPANY_ITEM_CODE: 'Company item code',
			COLUMN_COMPANY_ITEM_TEXT: 'Company item text',
			COLUMN_CREATION_DATE: 'Creation date',
			COLUMN_DEVELOPMENT_INDEX: 'Development index',
			COLUMN_EMAIL: 'Email',
			COLUMN_FIRST_NAME: 'First name',
			COLUMN_ID: 'ID',
			COLUMN_INVESTMENT: 'Investment',
			COLUMN_LAST_MODIFIED_DATE: 'Last modified date',
			COLUMN_LAST_NAME: 'Last name',
			COLUMN_MANDATORY: 'Mandatory',
			COLUMN_METATAG: 'Meta tag',
			COLUMN_MOBILE: 'Mobile',
			COLUMN_NAME: 'Name',
			COLUMN_NO_OF_COMPANIES: 'Number of companies',
			COLUMN_OPERATOR: 'Operator',
			COLUMN_PHONE: 'Phone',
			COLUMN_POSITION: 'Position',
			COLUMN_REGISTRATION_TYPE: 'Registration type',
			COLUMN_REVENUE: 'Revenue',
			COLUMN_SECTOR: 'Sector',
			COLUMN_SECTOR: 'Sector',
			COLUMN_SECTOR_NAME: 'Sector name',
			COLUMN_SEND_DATE: 'Send date',
			COLUMN_SUMMARY_ITEM: 'Summary item',
			COLUMN_STATUS: 'Status',
			COLUMN_SUBDIVISION1: 'County',
			COLUMN_SUBJECT: 'Subject',
			COLUMN_TENDER_ITEM_CODE: 'Tender item code',
			COLUMN_TENDER_ITEM_TEXT: 'Tender item text',
			COLUMN_TEXT: 'Text',
			COLUMN_TO: 'To',
			COLUMN_TYPE: 'Type',
			COLUMN_WIDGET_ITEM: 'Widget item',

			BUTTON_ADD: 'Add',
			BUTTON_BACK: 'Back',
			BUTTON_CLOSE: 'Close',
			BUTTON_DELETE: 'Delete',
			BUTTON_EDIT: 'Edit',
			BUTTON_NO: 'No',
			BUTTON_RESET: 'Reset',
			BUTTON_SAVE: 'Save',
			BUTTON_SELECT: 'Select...',
			BUTTON_SEND: 'Send',
			BUTTON_YES: 'Yes',
			BUTTON_EXPORT_PDF: 'Export to PDF',
			
			DATETIMEPICKER_TODAY: 'Today',
			DATETIMEPICKER_YESTERDAY: 'Yesterday',
			DATETIMEPICKER_LAST_7_DAYS: 'Last 7 days',
			DATETIMEPICKER_LAST_30_DAYS: 'Last 30 days',
			DATETIMEPICKER_THIS_MONTH: 'This month',
			DATETIMEPICKER_LAST_MONTH: 'Last month',
			DATETIMEPICKER_CUSTOM: 'Custom period',
			
			STATISTICS_PERIOD_LAST_7_DAYS: 'Last 7 days',
			STATISTICS_PERIOD_LAST_6_MONTHS: 'Last 6 months',
			
			PERIOD_TODAY: 'Today',
			PERIOD_LAST_7_DAYS: 'In last 7 days',
			PERIOD_LAST_6_MONTHS: 'In last 6 months',
			
			STATUS_ACTIVE: 'Active',
			STATUS_INACTIVE: 'Inactive',
			STATUS_INCOMPLETE: 'Incomplete',
			
			REGISTRATION_TYPE_INTERNAL: 'Registration form',
			REGISTRATION_TYPE_EXTERNAL: 'e-ZaBa',
			
			ITEM_TYPE_ACTIVITY: 'Activity',
			ITEM_TYPE_ACTIVITY_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Activities.',
			ITEM_TYPE_ACTIVITIES: 'Activities',
			ITEM_TYPE_ACTIVITIES_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Activities.',
			ITEM_TYPE_CHECKBOX: 'Check box',
			ITEM_TYPE_CHECKBOX_DESCRIPTION: 'Rendered as checkbox(es) where none, one or many option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_CURRENCY: 'Currency',
			ITEM_TYPE_CURRENCY_DESCRIPTION: 'Rendered as combination of input field that accepts numbers only and select where currency can be selected.',
			ITEM_TYPE_DATE: 'Date',
			ITEM_TYPE_DATE_DESCRIPTION: 'Rendered as date picker component.',
			ITEM_TYPE_DATE_TIME: 'Date time',
			ITEM_TYPE_HYPERLINK: 'Hyperlink',
			ITEM_TYPE_HYPERLINK_DESCRIPTION: 'Rendered as clickable hyperlink that will open entered URL in new browser window.',
			ITEM_TYPE_INVESTMENTS_PRIMARY: 'Primary investments',
			ITEM_TYPE_INVESTMENTS_SECONDARY: 'Secondary investments',
			ITEM_TYPE_INVESTMENTS_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Only active investments are displayed as option(s).',
			ITEM_TYPE_MULTISELECT: 'Multiselect',
			ITEM_TYPE_MULTISELECT_DESCRIPTION: 'Rendered as select component where none (optional), one or many (mandatory) option(s) can be selected. Options can be defined below.',
			ITEM_TYPE_NUMBER: 'Number',
			ITEM_TYPE_NUMBER_DESCRIPTION: 'Rendered as input field that accepts numbers only.',
			ITEM_TYPE_PERCENTAGE: 'Percentage',
			ITEM_TYPE_PERCENTAGE_DESCRIPTION: 'Rendered as input field that accepts numbers in range 0-100.',
			ITEM_TYPE_RADIO: 'Radio',
			ITEM_TYPE_RADIO_DESCRIPTION: 'Rendered as radio button group where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_SELECT: 'Select',
			ITEM_TYPE_SELECT_DESCRIPTION: 'Rendered as select component where none (optional) or one (mandatory) option can be selected. Options can be defined below.',
			ITEM_TYPE_SUBDIVISION1: 'County',
			ITEM_TYPE_SUBDIVISION1_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Counties.',
			ITEM_TYPE_SUBDIVISION2: 'Municipality / city',
			ITEM_TYPE_SUBDIVISION2_DESCRIPTION: 'Rendered as select component where none or one option can be selected. Options are defined in Settings - Municipalities / cities.',
			ITEM_TYPE_SUBDIVISIONS1: 'Counties',
			ITEM_TYPE_SUBDIVISIONS1_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Counties.',
			ITEM_TYPE_SUBDIVISIONS2: 'Municipalities / cities',
			ITEM_TYPE_SUBDIVISIONS2_DESCRIPTION: 'Rendered as select component where none, one or many option(s) can be selected. Options are defined in Settings - Municipalities / cities.',
			ITEM_TYPE_TEXT: 'Text',
			ITEM_TYPE_TEXT_AREA: 'Text area',
			ITEM_TYPE_TEXT_AREA_DESCRIPTION: 'Rendered as text area that enables some primitive formatting features (e.g. new line, new paragraph).',
			ITEM_TYPE_TEXT_DESCRIPTION: 'Rendered as input text field. Maximum length is 512 characters.',
			ITEM_TYPE_TEXT_EDITOR: 'Text editor',
			
			ALGORITHM_ITEM_TYPE_MANDATORY: 'Mandatory',
			ALGORITHM_ITEM_TYPE_OPTIONAL: 'Optional',
			ALGORITHM_ITEM_TYPE_CONDITIONAL: 'Conditional',
			
			OPERATOR_EQUAL: 'Equal',
			OPERATOR_GREATER_OR_EQUAL: 'Greater or equal',
			OPERATOR_LESS_OR_EQUAL: 'Less or equal',
			OPERATOR_IN: 'In',
			
			BOOLEAN_TRUE: 'Yes',
			BOOLEAN_FALSE: 'No',
			
			USER_GROUP_METATAG_MATCHING_USERS: 'Matching users',
			
			ENTITY_ACTIVITY: 'Activity',
			ENTITY_ALGORITHM_ITEM: 'Algorithm item',
			ENTITY_ARTICLE: 'Article',
			ENTITY_BRM: 'Business relationship manager',
			ENTITY_INVESTMENT: 'Investment',
			ENTITY_ITEM: 'Item',
			ENTITY_ROLE: 'Role',
			ENTITY_SUBDIVISION1: 'County',
			ENTITY_SUBDIVISION2: 'Municipality / city',
			ENTITY_TENDER: 'Tender',
			ENTITY_USER: 'User',
			ENTITY_USER_GROUP: 'User group',
			
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
			
			POSTSCRIPT_CODE: 'Unique identificator',
			POSTSCRIPT_ITEM_POSITION: 'Value must be between 1 and 1000',
			
			VALIDATION_FAILED_HEADER: 'Validation failed',
			
			ERR_MSG_001: 'Company item with meta tag COMPANY_LOCATION not found',
			ERR_MSG_002: 'Meta tag COMPANY_LOCATION found in more then one company item',
			
			UI_GRID_LOADING_DATA: 'Loading data...',
			UI_GRID_NO_DATA: 'No data',
			
			ACCESS_DENIED_HEADER: 'Access denied',
			ACCESS_DENIED_MESSAGE: 'You are not allowed to access selected resource',
			
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
			
			DASHBOARD_USERS: 'Users',
			DASHBOARD_TENDERS: 'Tenders',
			DASHBOARD_VISITS: 'Visits',
			DASHBOARD_TOTAL: 'Total',
			DASHBOARD_UNIQUE: 'Unique',
			DASHBOARD_ACTIVE: 'Active',
			DASHBOARD_INACTIVE: 'Inactive',
			DASHBOARD_CHART_DIMENSION: 'Dimension',
			DASHBOARD_CHART_PERIOD: 'Period',
			DASHBOARD_REGISTRATION_TYPE_INTERNAL: 'Registered via Fund Finder',
			DASHBOARD_REGISTRATION_TYPE_EXTERNAL: 'Registered via e-Zaba',
		});
	
	$translateProvider.preferredLanguage("en");
};

angular.module('FundFinder').config(config);
