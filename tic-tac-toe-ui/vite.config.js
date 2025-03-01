import { defineConfig } from 'vite';
import tailwindcss from '@tailwindcss/vite';
export default defineConfig({
	plugins: [tailwindcss()],
	server: {
		port: 3000, // Change Vite's port to 3000
	},
});
