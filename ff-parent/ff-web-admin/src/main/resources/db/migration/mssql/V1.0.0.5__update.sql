ALTER TABLE [dbo].[contact] DROP COLUMN [channel]
ALTER TABLE [dbo].[contact] DROP COLUMN [topic]
ALTER TABLE [dbo].[contact] DROP COLUMN [type]

ALTER TABLE [dbo].[contact] ADD [location] [ntext]  NULL