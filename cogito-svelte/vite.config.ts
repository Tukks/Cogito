import { sveltekit } from "@sveltejs/kit/vite";
import type { UserConfig } from "vite";

const config: UserConfig = {
	plugins: [sveltekit()],
	server: {
		proxy: {
			"/api": "http://localhost:9191"
		}
	}
};

export default config;
