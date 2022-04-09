module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "plugin:vue/vue3-essential",
    "plugin:vue/vue3-strongly-recommended",
    "plugin:vue/vue3-recommended",
    "@vue/typescript",
    "@vue/typescript/recommended",
    "@vue/prettier",
    "eslint:recommended",
    "plugin:@typescript-eslint/eslint-recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:@typescript-eslint/recommended-requiring-type-checking",
    "plugin:sonarjs/recommended",
  ],
  parser: "vue-eslint-parser",
  parserOptions: {
    parser: "@typescript-eslint/parser",
    project: "./tsconfig.json",
    ecmaVersion: 2020,
  },
  plugins: ["@typescript-eslint", "sonarjs"],
  rules: {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
    "no-console": process.env.NODE_ENV === "production" ? "error" : "warn",
    // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "warn",
    "vue/multi-word-component-names": "off",
  },
};
