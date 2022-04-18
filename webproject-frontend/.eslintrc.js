/*
 * Webproject NG
 * Copyright © 2021 - 2022 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International
 * Public License ("Public License").
 *
 * Webproject NG is non-free software: you can redistribute
 * it and/or modify it under the terms of the Public License.
 *
 * You should have received a copy of the Public License along
 * with Webproject NG. If not, see
 * https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
 */
module.exports = {
  root: true,
  env: {
    node: true,
    "vue/setup-compiler-macros": true,
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
    //"plugin:@typescript-eslint/recommended-requiring-type-checking",
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
    "@typescript-eslint/typedef": [
      "error",
      {
        arrayDestructuring: true,
        arrowParameter: true,
        memberVariableDeclaration: true,
        objectDestructuring: true,
        parameter: true,
        propertyDeclaration: true,
        variableDeclaration: true,
        variableDeclarationIgnoreFunction: true,
      },
    ],
    "@typescript-eslint/no-inferrable-types": "off",
  },
};
