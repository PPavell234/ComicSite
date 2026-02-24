/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}"  // <-- важно, чтобы включало все vue файлы
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}