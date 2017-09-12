CREATE TABLE [dbo].[gfi_sync]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [cnt_nok] [int]  NULL,
  [cnt_ok] [int]  NULL,
  [cnt_total] [int]  NULL,
  [end_time] [datetime2](7)  NOT NULL,
  [start_time] [datetime2](7)  NOT NULL
)
ALTER TABLE [dbo].[gfi_sync] ADD CONSTRAINT PK__gfi_sync__3213E83FF1E8D78E PRIMARY KEY  ([id])

-- Add 0 rows for gfi_sync.
CREATE TABLE [dbo].[gfi_sync_error]
(
  [id] [int] IDENTITY(1,1) NOT NULL,
  [company_data] [ntext]  NULL,
  [error] [ntext]  NULL,
  [gfi_sync] [int]  NULL,
  [user] [int]  NULL
)
ALTER TABLE [dbo].[gfi_sync_error] ADD CONSTRAINT PK__gfi_sync__3213E83F579B789E PRIMARY KEY  ([id])