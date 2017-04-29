/****** Object:  Table [dbo].[activity]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[activity](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[algorithm_item]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[algorithm_item](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[code] [varchar](8) NOT NULL,
	[conditional_item_code] [varchar](8) NULL,
	[operator] [varchar](16) NOT NULL,
	[status] [varchar](16) NOT NULL,
	[type] [varchar](16) NOT NULL,
	[company_item] [int] NULL,
	[tender_item] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[article]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[article](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[status] [varchar](16) NOT NULL,
	[text] [ntext] NULL,
	[image] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[business_relationship_manager]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[business_relationship_manager](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[email] [nvarchar](255) NULL,
	[first_name] [nvarchar](128) NULL,
	[last_name] [nvarchar](128) NULL,
	[mobile] [nvarchar](128) NULL,
	[phone] [nvarchar](128) NULL,
	[organizational_unit] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[company]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[branch_office_number] [nvarchar](255) NULL,
	[code] [nvarchar](64) NULL,
	[company_number] [nvarchar](255) NULL,
	[name] [nvarchar](255) NULL,
	[other_business] [ntext] NULL,
	[primary_business] [nvarchar](255) NULL,
	[registration_number] [nvarchar](255) NULL,
	[user] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[company_item]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company_item](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[currency] [nvarchar](8) NULL,
	[value] [ntext] NULL,
	[company] [int] NULL,
	[item] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[contact]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[contact](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[company_code] [nvarchar](255) NULL,
	[company_name] [nvarchar](255) NULL,
	[email] [nvarchar](255) NULL,
	[location] [ntext] NULL,
	[name] [nvarchar](255) NULL,
	[phone] [nvarchar](255) NULL,
	[text] [ntext] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[email]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[email](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[subject] [ntext] NULL,
	[text] [ntext] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[image]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[image](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[base64] [varchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[impression]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[impression](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[entity_id] [int] NOT NULL,
	[entity_type] [varchar](16) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[investment]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[investment](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[status] [varchar](16) NOT NULL,
	[text] [ntext] NULL,
	[image] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[item]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[item](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[code] [varchar](8) NOT NULL,
	[entity_type] [varchar](16) NOT NULL,
	[help] [ntext] NULL,
	[mandatory] [bit] NOT NULL,
	[meta_tag] [varchar](64) NULL,
	[position] [int] NOT NULL,
	[status] [varchar](16) NOT NULL,
	[summary_item] [bit] NULL,
	[text] [ntext] NOT NULL,
	[type] [varchar](32) NOT NULL,
	[widget_item] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[item_option]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[item_option](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[position] [int] NOT NULL,
	[text] [ntext] NULL,
	[item] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[organizational_unit]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[organizational_unit](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[code] [varchar](32) NOT NULL,
	[name] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[permission]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[permission](
	[id] [int] NOT NULL,
	[name] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[project]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[project](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[description] [ntext] NULL,
	[name] [nvarchar](255) NOT NULL,
	[company] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[project_investment]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[project_investment](
	[project_id] [int] NOT NULL,
	[investment_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[project_id] ASC,
	[investment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[project_item]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[project_item](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[currency] [nvarchar](8) NULL,
	[value] [ntext] NULL,
	[item] [int] NULL,
	[project] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[role]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[role_permission]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role_permission](
	[role_id] [int] NOT NULL,
	[permission_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[role_id] ASC,
	[permission_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[subdivision1]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subdivision1](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[development_index] [varchar](32) NULL,
	[name] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[subdivision2]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[subdivision2](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[development_index] [varchar](32) NULL,
	[name] [nvarchar](255) NOT NULL,
	[subdivision1] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tender]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tender](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[status] [varchar](16) NOT NULL,
	[text] [ntext] NULL,
	[image] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tender_item]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tender_item](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[currency] [nvarchar](8) NULL,
	[value] [ntext] NULL,
	[item] [int] NULL,
	[tender] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[user]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[demo_user] [bit] NULL,
	[email] [nvarchar](255) NULL,
	[email2] [nvarchar](255) NULL,
	[first_name] [nvarchar](128) NULL,
	[last_login] [datetime2](7) NULL,
	[last_name] [nvarchar](128) NULL,
	[password] [nvarchar](128) NULL,
	[registration_code] [varchar](255) NULL,
	[registration_code_confirmed] [datetime2](7) NULL,
	[registration_code_sent] [datetime2](7) NULL,
	[registration_type] [varchar](32) NULL,
	[status] [varchar](32) NULL,
	[business_relationship_manager] [int] NULL,
	[business_relationship_manager_substitute] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[user_email]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_email](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[email] [int] NULL,
	[tender] [int] NULL,
	[user] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[user_group]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_by] [varchar](128) NULL,
	[creation_date] [datetime2](7) NOT NULL,
	[last_modified_by] [varchar](128) NULL,
	[last_modified_date] [datetime2](7) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[status] [varchar](16) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[user_group_user]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_group_user](
	[user_group_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_group_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[zaba_mappings_location]    Script Date: 28.4.2017. 19:50:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[zaba_mappings_location](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[subdivision2] [nvarchar](128) NULL,
	[subdivision3] [nvarchar](128) NULL,
	[zip_code] [varchar](16) NULL,
	[subdivision2_development_index] [varchar](16) NULL,
	[subdivision1] [nvarchar](128) NULL,
	[subdivision1_development_index] [varchar](16) NULL
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[algorithm_item]  WITH CHECK ADD  CONSTRAINT [FK_4aagso9q0posdfwnkmjn6deka] FOREIGN KEY([tender_item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[algorithm_item] CHECK CONSTRAINT [FK_4aagso9q0posdfwnkmjn6deka]
GO
ALTER TABLE [dbo].[algorithm_item]  WITH CHECK ADD  CONSTRAINT [FK_6rl2vibxo61aj97ur6ieieks] FOREIGN KEY([company_item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[algorithm_item] CHECK CONSTRAINT [FK_6rl2vibxo61aj97ur6ieieks]
GO
ALTER TABLE [dbo].[article]  WITH CHECK ADD  CONSTRAINT [FK_jopb9qivnsxwrahkamrv98hcw] FOREIGN KEY([image])
REFERENCES [dbo].[image] ([id])
GO
ALTER TABLE [dbo].[article] CHECK CONSTRAINT [FK_jopb9qivnsxwrahkamrv98hcw]
GO
ALTER TABLE [dbo].[business_relationship_manager]  WITH CHECK ADD  CONSTRAINT [FK_5nw6y0obewou7dam5lvdw3muq] FOREIGN KEY([organizational_unit])
REFERENCES [dbo].[organizational_unit] ([id])
GO
ALTER TABLE [dbo].[business_relationship_manager] CHECK CONSTRAINT [FK_5nw6y0obewou7dam5lvdw3muq]
GO
ALTER TABLE [dbo].[company]  WITH CHECK ADD  CONSTRAINT [FK_7cs7aqu97p4ep7ocbylx8cchs] FOREIGN KEY([user])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[company] CHECK CONSTRAINT [FK_7cs7aqu97p4ep7ocbylx8cchs]
GO
ALTER TABLE [dbo].[company_item]  WITH CHECK ADD  CONSTRAINT [FK_iffgmimr917ylxr63thetmbfq] FOREIGN KEY([company])
REFERENCES [dbo].[company] ([id])
GO
ALTER TABLE [dbo].[company_item] CHECK CONSTRAINT [FK_iffgmimr917ylxr63thetmbfq]
GO
ALTER TABLE [dbo].[company_item]  WITH CHECK ADD  CONSTRAINT [FK_sejipjuelub5j2wd0ee898n41] FOREIGN KEY([item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[company_item] CHECK CONSTRAINT [FK_sejipjuelub5j2wd0ee898n41]
GO
ALTER TABLE [dbo].[investment]  WITH CHECK ADD  CONSTRAINT [FK_ci9hlt21hhk0kgtxom5768fks] FOREIGN KEY([image])
REFERENCES [dbo].[image] ([id])
GO
ALTER TABLE [dbo].[investment] CHECK CONSTRAINT [FK_ci9hlt21hhk0kgtxom5768fks]
GO
ALTER TABLE [dbo].[item_option]  WITH CHECK ADD  CONSTRAINT [FK_gq6nqdsaic1mj0bpql5xhu521] FOREIGN KEY([item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[item_option] CHECK CONSTRAINT [FK_gq6nqdsaic1mj0bpql5xhu521]
GO
ALTER TABLE [dbo].[project]  WITH CHECK ADD  CONSTRAINT [FK_i7cnlnqeieu2xsv9jh1od0t5n] FOREIGN KEY([company])
REFERENCES [dbo].[company] ([id])
GO
ALTER TABLE [dbo].[project] CHECK CONSTRAINT [FK_i7cnlnqeieu2xsv9jh1od0t5n]
GO
ALTER TABLE [dbo].[project_investment]  WITH CHECK ADD  CONSTRAINT [FK_qo5mmmxe5femovus3eyoksypy] FOREIGN KEY([project_id])
REFERENCES [dbo].[project] ([id])
GO
ALTER TABLE [dbo].[project_investment] CHECK CONSTRAINT [FK_qo5mmmxe5femovus3eyoksypy]
GO
ALTER TABLE [dbo].[project_investment]  WITH CHECK ADD  CONSTRAINT [FK_smxuuri2yegx8sc1xmk3afnd9] FOREIGN KEY([investment_id])
REFERENCES [dbo].[investment] ([id])
GO
ALTER TABLE [dbo].[project_investment] CHECK CONSTRAINT [FK_smxuuri2yegx8sc1xmk3afnd9]
GO
ALTER TABLE [dbo].[project_item]  WITH CHECK ADD  CONSTRAINT [FK_9wbtdxj19qmteif3v01hryu2c] FOREIGN KEY([item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[project_item] CHECK CONSTRAINT [FK_9wbtdxj19qmteif3v01hryu2c]
GO
ALTER TABLE [dbo].[project_item]  WITH CHECK ADD  CONSTRAINT [FK_foxoj24uhesknmmf77krud5sj] FOREIGN KEY([project])
REFERENCES [dbo].[project] ([id])
GO
ALTER TABLE [dbo].[project_item] CHECK CONSTRAINT [FK_foxoj24uhesknmmf77krud5sj]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FK_fn4pldu982p9u158rpk6nho5k] FOREIGN KEY([permission_id])
REFERENCES [dbo].[permission] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FK_fn4pldu982p9u158rpk6nho5k]
GO
ALTER TABLE [dbo].[role_permission]  WITH CHECK ADD  CONSTRAINT [FK_j89g87bvih4d6jbxjcssrybks] FOREIGN KEY([role_id])
REFERENCES [dbo].[role] ([id])
GO
ALTER TABLE [dbo].[role_permission] CHECK CONSTRAINT [FK_j89g87bvih4d6jbxjcssrybks]
GO
ALTER TABLE [dbo].[subdivision2]  WITH CHECK ADD  CONSTRAINT [FK_tonwsn904l6qsuisbu6ci8ehv] FOREIGN KEY([subdivision1])
REFERENCES [dbo].[subdivision1] ([id])
GO
ALTER TABLE [dbo].[subdivision2] CHECK CONSTRAINT [FK_tonwsn904l6qsuisbu6ci8ehv]
GO
ALTER TABLE [dbo].[tender]  WITH CHECK ADD  CONSTRAINT [FK_fund0tsyw5xr44hotbb3k86mr] FOREIGN KEY([image])
REFERENCES [dbo].[image] ([id])
GO
ALTER TABLE [dbo].[tender] CHECK CONSTRAINT [FK_fund0tsyw5xr44hotbb3k86mr]
GO
ALTER TABLE [dbo].[tender_item]  WITH CHECK ADD  CONSTRAINT [FK_h4vmvk4gxt2tsmb9qqwwkyaba] FOREIGN KEY([item])
REFERENCES [dbo].[item] ([id])
GO
ALTER TABLE [dbo].[tender_item] CHECK CONSTRAINT [FK_h4vmvk4gxt2tsmb9qqwwkyaba]
GO
ALTER TABLE [dbo].[tender_item]  WITH CHECK ADD  CONSTRAINT [FK_ohhjjdu4ltdkbxjkg3494ip2q] FOREIGN KEY([tender])
REFERENCES [dbo].[tender] ([id])
GO
ALTER TABLE [dbo].[tender_item] CHECK CONSTRAINT [FK_ohhjjdu4ltdkbxjkg3494ip2q]
GO
ALTER TABLE [dbo].[user]  WITH CHECK ADD  CONSTRAINT [FK_5t6qrhu2fappmelw8hc6bkiky] FOREIGN KEY([business_relationship_manager])
REFERENCES [dbo].[business_relationship_manager] ([id])
GO
ALTER TABLE [dbo].[user] CHECK CONSTRAINT [FK_5t6qrhu2fappmelw8hc6bkiky]
GO
ALTER TABLE [dbo].[user]  WITH CHECK ADD  CONSTRAINT [FK_5xd0dxshxc5f81dtl5r8cejg4] FOREIGN KEY([business_relationship_manager_substitute])
REFERENCES [dbo].[business_relationship_manager] ([id])
GO
ALTER TABLE [dbo].[user] CHECK CONSTRAINT [FK_5xd0dxshxc5f81dtl5r8cejg4]
GO
ALTER TABLE [dbo].[user_email]  WITH CHECK ADD  CONSTRAINT [FK_dy9lb8d1fjx5npkdnna1hlg3x] FOREIGN KEY([user])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[user_email] CHECK CONSTRAINT [FK_dy9lb8d1fjx5npkdnna1hlg3x]
GO
ALTER TABLE [dbo].[user_email]  WITH CHECK ADD  CONSTRAINT [FK_qm35y897bny0qjqutviap50yi] FOREIGN KEY([email])
REFERENCES [dbo].[email] ([id])
GO
ALTER TABLE [dbo].[user_email] CHECK CONSTRAINT [FK_qm35y897bny0qjqutviap50yi]
GO
ALTER TABLE [dbo].[user_email]  WITH CHECK ADD  CONSTRAINT [FK_t2qs5vh123oeev7ssllttar6e] FOREIGN KEY([tender])
REFERENCES [dbo].[tender] ([id])
GO
ALTER TABLE [dbo].[user_email] CHECK CONSTRAINT [FK_t2qs5vh123oeev7ssllttar6e]
GO
ALTER TABLE [dbo].[user_group_user]  WITH CHECK ADD  CONSTRAINT [FK_9nu8cvmmc0olpqdulhwhe3ni9] FOREIGN KEY([user_group_id])
REFERENCES [dbo].[user_group] ([id])
GO
ALTER TABLE [dbo].[user_group_user] CHECK CONSTRAINT [FK_9nu8cvmmc0olpqdulhwhe3ni9]
GO
ALTER TABLE [dbo].[user_group_user]  WITH CHECK ADD  CONSTRAINT [FK_phklr5y0o9nl2xt5v47gkwlko] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[user_group_user] CHECK CONSTRAINT [FK_phklr5y0o9nl2xt5v47gkwlko]
GO