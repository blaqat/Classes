return {
	theme = "tccs",
	ui = "dark",
	font = {
		family = "Operator Mono Book",
		size = 15,
		fallbacks = { "Mononoki Nerd Font" },
	},
	-- line_spacing = 5,
	on_load = {
		"lua vim.cmd('colorscheme gruvchad46')",
	},
}
