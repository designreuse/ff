CREATE TABLE [dbo].[activity]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[activity] ADD CONSTRAINT PK__activity__3213E83FB7AC2B71 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[algorithm_item] ADD CONSTRAINT PK__algorith__3213E83F6F96512F PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[article] ADD CONSTRAINT PK__article__3213E83F8F6B333E PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[business_relationship_manager] ADD CONSTRAINT PK__business__3213E83F269DF4D1 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[company]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [code] [nvarchar](64)  NULL,
  [name] [nvarchar](255)  NOT NULL,
  [user] [int]  NULL
)
ALTER TABLE [dbo].[company] ADD CONSTRAINT PK__company__3213E83FD03B8ABA PRIMARY KEY  ([id])

CREATE TABLE [dbo].[company_investment]
(
  [company_id] [int]  NOT NULL,
  [investment_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[company_investment] ADD CONSTRAINT PK__company___1C2F4E1D76134EE0 PRIMARY KEY  ([company_id], [investment_id])

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
ALTER TABLE [dbo].[company_item] ADD CONSTRAINT PK__company___3213E83F320BB86A PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[email] ADD CONSTRAINT PK__email__3213E83F53533A1F PRIMARY KEY  ([id])

CREATE TABLE [dbo].[image]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [base64] [varchar](MAX)  NULL
)
ALTER TABLE [dbo].[image] ADD CONSTRAINT PK__image__3213E83FC431DBD1 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[impression] ADD CONSTRAINT PK__impressi__3213E83F66A4F643 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[investment] ADD CONSTRAINT PK__investme__3213E83FCE093BCE PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[item] ADD CONSTRAINT PK__item__3213E83FB3AE05D7 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[item_option] ADD CONSTRAINT PK__item_opt__3213E83FB5CCF644 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[permission]
(
  [id] [int]  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[permission] ADD CONSTRAINT PK__permissi__3213E83FDF8E7EC1 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[role]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[role] ADD CONSTRAINT PK__role__3213E83FCA40408C PRIMARY KEY  ([id])

CREATE TABLE [dbo].[role_permission]
(
  [role_id] [int]  NOT NULL,
  [permission_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT PK__role_per__C85A546339871089 PRIMARY KEY  ([role_id], [permission_id])

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
ALTER TABLE [dbo].[subdivision1] ADD CONSTRAINT PK__subdivis__3213E83F4988A2F7 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[subdivision2] ADD CONSTRAINT PK__subdivis__3213E83F3F8F8C54 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[tender] ADD CONSTRAINT PK__tender__3213E83FE9110C56 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[tender_item] ADD CONSTRAINT PK__tender_i__3213E83F3ECDF539 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[user]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [demo_user] [bit]  NULL,
  [email] [nvarchar](255)  NOT NULL,
  [first_name] [nvarchar](128)  NOT NULL,
  [last_login] [datetime2](7)  NULL,
  [last_name] [nvarchar](128)  NOT NULL,
  [password] [nvarchar](128)  NOT NULL,
  [registration_code] [varchar](255)  NULL,
  [registration_code_confirmed] [datetime2](7)  NULL,
  [registration_code_sent] [datetime2](7)  NULL,
  [status] [varchar](32)  NOT NULL,
  [business_relationship_manager] [int]  NULL
)
ALTER TABLE [dbo].[user] ADD CONSTRAINT PK__user__3213E83F6DCCB062 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[user_email] ADD CONSTRAINT PK__user_ema__3213E83F78E4855F PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[user_group] ADD CONSTRAINT PK__user_gro__3213E83F13065731 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[user_group_user]
(
  [user_group_id] [int]  NOT NULL,
  [user_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[user_group_user] ADD CONSTRAINT PK__user_gro__308DB4F1B6DC2E1B PRIMARY KEY  ([user_group_id], [user_id])