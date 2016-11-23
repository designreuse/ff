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
ALTER TABLE [dbo].[algorithm_item] ADD CONSTRAINT PK__algorith__3213E83F955CB2F6 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[article] ADD CONSTRAINT PK__article__3213E83F80F56EAE PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[business_relationship_manager] ADD CONSTRAINT PK__business__3213E83FF4BB5ED8 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[city]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [development_index] [varchar](32)  NULL,
  [name] [nvarchar](255)  NOT NULL,
  [county] [int]  NULL
)
ALTER TABLE [dbo].[city] ADD CONSTRAINT PK__city__3213E83F0CD983FE PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[company] ADD CONSTRAINT PK__company__3213E83F0C7724AA PRIMARY KEY  ([id])

CREATE TABLE [dbo].[company_investment]
(
  [company_id] [int]  NOT NULL,
  [investment_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[company_investment] ADD CONSTRAINT PK__company___1C2F4E1DA9AAE8F3 PRIMARY KEY  ([company_id], [investment_id])

CREATE TABLE [dbo].[company_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [value] [ntext]  NULL,
  [company] [int]  NULL,
  [item] [int]  NULL
)
ALTER TABLE [dbo].[company_item] ADD CONSTRAINT PK__company___3213E83F361382A2 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[county]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[county] ADD CONSTRAINT PK__county__3213E83F2BCBDE7F PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[email] ADD CONSTRAINT PK__email__3213E83F4CDD50C8 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[image]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [base64] [varchar](MAX)  NULL
)
ALTER TABLE [dbo].[image] ADD CONSTRAINT PK__image__3213E83F53CE1B8B PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[impression] ADD CONSTRAINT PK__impressi__3213E83FC0B8A3D4 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[investment] ADD CONSTRAINT PK__investme__3213E83F7C8660B8 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [code] [varchar](8)  NOT NULL,
  [entity_id] [int]  NULL,
  [entity_type] [varchar](16)  NOT NULL,
  [help] [ntext]  NULL,
  [mandatory] [bit]  NOT NULL,
  [meta_tag] [varchar](64)  NULL,
  [position] [int]  NOT NULL,
  [status] [varchar](16)  NOT NULL,
  [text] [ntext]  NOT NULL,
  [type] [varchar](32)  NOT NULL,
  [widget_item] [bit]  NULL
)
ALTER TABLE [dbo].[item] ADD CONSTRAINT PK__item__3213E83F1B2E2A75 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[item_option] ADD CONSTRAINT PK__item_opt__3213E83FC1D8816E PRIMARY KEY  ([id])

CREATE TABLE [dbo].[nkd]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [activity] [nvarchar](8)  NOT NULL,
  [activity_name] [nvarchar](512)  NOT NULL,
  [area] [nvarchar](8)  NOT NULL,
  [sector] [nvarchar](8)  NOT NULL,
  [sector_name] [nvarchar](512)  NOT NULL
)
ALTER TABLE [dbo].[nkd] ADD CONSTRAINT PK__nkd__3213E83F846B546C PRIMARY KEY  ([id])

CREATE TABLE [dbo].[permission]
(
  [id] [int]  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[permission] ADD CONSTRAINT PK__permissi__3213E83F1FC2957B PRIMARY KEY  ([id])

CREATE TABLE [dbo].[role]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[role] ADD CONSTRAINT PK__role__3213E83FFDA8A8C3 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[role_permission]
(
  [role_id] [int]  NOT NULL,
  [permission_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[role_permission] ADD CONSTRAINT PK__role_per__C85A5463369847BC PRIMARY KEY  ([role_id], [permission_id])

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
ALTER TABLE [dbo].[tender] ADD CONSTRAINT PK__tender__3213E83F18690051 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[tender_item]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [value] [ntext]  NULL,
  [item] [int]  NULL,
  [tender] [int]  NULL
)
ALTER TABLE [dbo].[tender_item] ADD CONSTRAINT PK__tender_i__3213E83F8FFDADE2 PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[user] ADD CONSTRAINT PK__user__3213E83F7539751E PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[user_email] ADD CONSTRAINT PK__user_ema__3213E83F48FCFAAD PRIMARY KEY  ([id])

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
ALTER TABLE [dbo].[user_group] ADD CONSTRAINT PK__user_gro__3213E83FEA076821 PRIMARY KEY  ([id])

CREATE TABLE [dbo].[user_group_user]
(
  [user_group_id] [int]  NOT NULL,
  [user_id] [int]  NOT NULL
)
ALTER TABLE [dbo].[user_group_user] ADD CONSTRAINT PK__user_gro__308DB4F1F1AA7B3C PRIMARY KEY  ([user_group_id], [user_id])