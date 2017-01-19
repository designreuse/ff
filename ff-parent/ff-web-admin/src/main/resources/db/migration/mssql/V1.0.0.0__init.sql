CREATE TABLE [dbo].[activity]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[activity] ADD CONSTRAINT PK__activity__3213E83FC37EB4F5 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[activity].
CREATE TABLE [dbo].[algorithm_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [code] [varchar](8)  NOT NULL,
  [conditional_item_code] [varchar](8)  NULL,
  [operator] [varchar](16)  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [type] [varchar](16)  NOT NULL,
  [company_item] [int]  NULL,
  [tender_item] [int]  NULL
)
ALTER TABLE [dbo].[algorithm_item] ADD CONSTRAINT PK__algorith__3213E83F9D5F2FC4 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[algorithm_item].
CREATE TABLE [dbo].[article]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [text] [ntext]  NULL,
  [image] [int]  NULL
)
ALTER TABLE [dbo].[article] ADD CONSTRAINT PK__article__3213E83FEF8B2FE7 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[article].
CREATE TABLE [dbo].[business_relationship_manager]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [email] [nvarchar](255)  NULL,
  [first_name] [nvarchar](128)  NULL,
  [last_name] [nvarchar](128)  NULL,
  [mobile] [nvarchar](128)  NULL,
  [phone] [nvarchar](128)  NULL
)
ALTER TABLE [dbo].[business_relationship_manager] ADD CONSTRAINT PK__business__3213E83FB4CC83D7 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[business_relationship_manager].
CREATE TABLE [dbo].[company]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [branch_office_number] [nvarchar](255)  NULL,
  [code] [nvarchar](64)  NULL,
  [company_number] [nvarchar](255)  NULL,
  [name] [nvarchar](255)  NOT NULL,
  [other_business] [ntext]  NULL,
  [primary_business] [nvarchar](255)  NULL,
  [registration_number] [nvarchar](255)  NULL,
  [user] [int]  NULL
)
ALTER TABLE [dbo].[company] ADD CONSTRAINT PK__company__3213E83FB651B03E PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[company].
CREATE TABLE [dbo].[company_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [currency] [nvarchar](8)  NULL,
  [value] [ntext]  NULL,
  [company] [int]  NULL,
  [item] [int]  NULL
)
ALTER TABLE [dbo].[company_item] ADD CONSTRAINT PK__company___3213E83FE1C87E14 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[company_item].
CREATE TABLE [dbo].[contact]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [channel] [ntext]  NULL,
  [company_code] [nvarchar](255)  NULL,
  [company_name] [nvarchar](255)  NULL,
  [email] [nvarchar](255)  NULL,
  [name] [nvarchar](255)  NULL,
  [phone] [nvarchar](255)  NULL,
  [text] [ntext]  NULL,
  [topic] [ntext]  NULL,
  [type] [ntext]  NULL
)
ALTER TABLE [dbo].[contact] ADD CONSTRAINT PK__contact__3213E83F0F970CE9 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[contact].
CREATE TABLE [dbo].[email]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [subject] [ntext]  NULL,
  [text] [ntext]  NULL
)
ALTER TABLE [dbo].[email] ADD CONSTRAINT PK__email__3213E83F284C2308 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[email].
CREATE TABLE [dbo].[image]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [base64] [varchar](MAX)  NULL
)
ALTER TABLE [dbo].[image] ADD CONSTRAINT PK__image__3213E83FEF7640CC PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[image].
CREATE TABLE [dbo].[impression]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [entity_id] [int]  NOT NULL,
  [entity_type] [varchar](16)  NOT NULL
)
ALTER TABLE [dbo].[impression] ADD CONSTRAINT PK__impressi__3213E83F751B1114 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[impression].
CREATE TABLE [dbo].[investment]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [text] [ntext]  NULL,
  [image] [int]  NULL
)
ALTER TABLE [dbo].[investment] ADD CONSTRAINT PK__investme__3213E83F3F29A0B4 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[investment].
CREATE TABLE [dbo].[item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [code] [varchar](8)  NOT NULL,
  [entity_type] [varchar](16)  NOT NULL,
  [help] [ntext]  NULL,
  [mandatory] [bit]  NOT NULL,
  [meta_tag] [varchar](64)  NULL,
  [position] [int]  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [summary_item] [bit]  NULL,
  [text] [ntext]  NOT NULL,
  [type] [varchar](32)  NOT NULL,
  [widget_item] [bit]  NULL
)
ALTER TABLE [dbo].[item] ADD CONSTRAINT PK__item__3213E83F266F3415 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[item].
CREATE TABLE [dbo].[item_option]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [position] [int]  NOT NULL,
  [text] [ntext]  NULL,
  [item] [int]  NULL
)
ALTER TABLE [dbo].[item_option] ADD CONSTRAINT PK__item_opt__3213E83F9A7B397B PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[item_option].
CREATE TABLE [dbo].[permission]
(
  [id] [int]  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[permission] ADD CONSTRAINT PK__permissi__3213E83F3796FC54 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[permission].
CREATE TABLE [dbo].[project]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [description] [ntext]  NULL,
  [name] [nvarchar](255)  NOT NULL,
  [company] [int]  NULL
)
ALTER TABLE [dbo].[project] ADD CONSTRAINT PK__project__3213E83F8E2A2BB8 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[project].
CREATE TABLE [dbo].[project_investment]
(
  [project_id] [int]  NOT NULL,
  [investment_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[project_investment] ADD CONSTRAINT PK__project___9E70A2374DA66F3B PRIMARY KEY  ([project_id], [investment_id])

-- Add 0 rows for [dbo].[project_investment].
CREATE TABLE [dbo].[project_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [currency] [nvarchar](8)  NULL,
  [value] [ntext]  NULL,
  [item] [int]  NULL,
  [project] [int]  NULL
)
ALTER TABLE [dbo].[project_item] ADD CONSTRAINT PK__project___3213E83FB063B906 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[project_item].
CREATE TABLE [dbo].[role]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[role] ADD CONSTRAINT PK__role__3213E83F06D5DC52 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[role].
CREATE TABLE [dbo].[role_permission]
(
  [role_id] [int]  NOT NULL,
  [permission_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT PK__role_per__C85A546309B9F07B PRIMARY KEY  ([role_id], [permission_id])

-- Add 0 rows for [dbo].[role_permission].
CREATE TABLE [dbo].[subdivision1]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [development_index] [varchar](32)  NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[subdivision1] ADD CONSTRAINT PK__subdivis__3213E83FA477BC95 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[subdivision1].
CREATE TABLE [dbo].[subdivision2]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [development_index] [varchar](32)  NULL,
  [name] [nvarchar](255)  NOT NULL,
  [subdivision1] [int]  NULL
)
ALTER TABLE [dbo].[subdivision2] ADD CONSTRAINT PK__subdivis__3213E83F9FEFCE96 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[subdivision2].
CREATE TABLE [dbo].[tender]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [text] [ntext]  NULL,
  [image] [int]  NULL
)
ALTER TABLE [dbo].[tender] ADD CONSTRAINT PK__tender__3213E83F60E690A0 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[tender].
CREATE TABLE [dbo].[tender_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [currency] [nvarchar](8)  NULL,
  [value] [ntext]  NULL,
  [item] [int]  NULL,
  [tender] [int]  NULL
)
ALTER TABLE [dbo].[tender_item] ADD CONSTRAINT PK__tender_i__3213E83FBDBCAE59 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[tender_item].
CREATE TABLE [dbo].[user]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [demo_user] [bit]  NULL,
  [email] [nvarchar](255)  NULL,
  [first_name] [nvarchar](128)  NULL,
  [last_login] [datetime2](7)  NULL,
  [last_name] [nvarchar](128)  NULL,
  [password] [nvarchar](128)  NULL,
  [registration_code] [varchar](255)  NULL,
  [registration_code_confirmed] [datetime2](7)  NULL,
  [registration_code_sent] [datetime2](7)  NULL,
  [registration_type] [varchar](32)  NULL,
  [status] [varchar](32)  NULL,
  [business_relationship_manager] [int]  NULL,
  [business_relationship_manager_substitute] [int]  NULL
)
ALTER TABLE [dbo].[user] ADD CONSTRAINT PK__user__3213E83FED02827F PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[user].
CREATE TABLE [dbo].[user_email]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [email] [int]  NULL,
  [tender] [int]  NULL,
  [user] [int]  NULL
)
ALTER TABLE [dbo].[user_email] ADD CONSTRAINT PK__user_ema__3213E83FA28A5701 PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[user_email].
CREATE TABLE [dbo].[user_group]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL,
  [status] [varchar](16)  NOT NULL
)
ALTER TABLE [dbo].[user_group] ADD CONSTRAINT PK__user_gro__3213E83FDB3F0BAE PRIMARY KEY  ([id])

-- Add 0 rows for [dbo].[user_group].
CREATE TABLE [dbo].[user_group_user]
(
  [user_group_id] [int]  NOT NULL,
  [user_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[user_group_user] ADD CONSTRAINT PK__user_gro__308DB4F1E659AB21 PRIMARY KEY  ([user_group_id], [user_id])

-- Add 0 rows for [dbo].[user_group_user].
