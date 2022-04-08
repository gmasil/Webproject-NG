module.exports = {
  content: ["./src/**/*.html", "./src/**/*.vue", "./src/**/*.jsx"],
  theme: {
    extend: {
      colors: {
        "theme-background": "var(--theme-background)",
        "theme-background-highlight": "var(--theme-background-highlight)",
        "theme-primary": "var(--theme-primary)",
        "theme-secondary": "var(--theme-secondary)",
        "theme-text": "var(--theme-text)",
      },
      gridTemplateColumns: {
        fit: "fit-content(100%) fit-content(100%)",
      },
    },
  },
  plugins: [require("@tailwindcss/line-clamp")],
};
