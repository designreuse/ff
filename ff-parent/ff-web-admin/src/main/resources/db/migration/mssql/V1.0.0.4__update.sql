CREATE TABLE [dbo].[organizational_unit]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [created_by] [varchar](128)  NULL,
  [creation_date] [datetime2](7)  NOT NULL,
  [last_modified_by] [varchar](128)  NULL,
  [last_modified_date] [datetime2](7)  NOT NULL,
  [code] [varchar](8)  NOT NULL,
  [name] [nvarchar](255)  NOT NULL
)
ALTER TABLE [dbo].[organizational_unit] ADD CONSTRAINT PK__organiza__3213E83FD59B47C2 PRIMARY KEY  ([id])

ALTER TABLE [dbo].[business_relationship_manager] DROP COLUMN org_unit

ALTER TABLE [dbo].[business_relationship_manager] ADD organizational_unit [int]  NULL