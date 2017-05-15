ALTER TABLE [dbo].[company] ADD [sync_data] [bit] NOT NULL DEFAULT 0;
ALTER TABLE [dbo].[company] ADD [hide_sync_data_warning] [bit] NOT NULL DEFAULT 0;

ALTER TABLE [dbo].[company_item] ADD [value_ext] [ntext] NULL;

ALTER TABLE [dbo].[item] ADD [emphasize] [bit] NOT NULL DEFAULT 0;

ALTER TABLE [dbo].[item_option] ADD [url] [ntext] NULL;