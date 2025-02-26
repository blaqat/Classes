return {
	-- theme = "rose-pine-dawn",
	theme = {
		"nordfox",
		"dawnfox",
		-- { "paper", "light" },
		-- { "gruvbox-minor", "dark" },
	},
	font = {
		family = "PragmataPro Mono Liga",
		size = 16,
		fallbacks = { "Mononoki Nerd Font" },
	},
	-- highlight = {
	-- 	misc = { ["@lsp.type.xmlDocCommentName"] = "inverse" },
	-- },
	run = "dotnet run",
	line_spacing = 7,

	on_load = "TSDisable highlight",
}
