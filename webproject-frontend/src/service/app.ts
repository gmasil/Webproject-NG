///
/// Webproject NG
/// Copyright © 2021 - 2022 Gmasil
///
/// This file is part of Webproject NG.
///
/// Webproject NG is licensed under the Creative Commons
/// Attribution-NonCommercial-ShareAlike 4.0 International
/// Public License ("Public License").
///
/// Webproject NG is non-free software: you can redistribute
/// it and/or modify it under the terms of the Public License.
///
/// You should have received a copy of the Public License along
/// with Webproject NG. If not, see
/// https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
///

import { useStore } from "@/store/pinia";
import axios, { AxiosError } from "axios";
import { AppProperties } from "@/types";

export const loadAppProperties = (): Promise<AppProperties> => {
  return new Promise<AppProperties>((resolve, reject) => {
    axios
      .get("/api/app/config")
      .then((response) => {
        const props: AppProperties = response.data as AppProperties;
        const store = useStore();
        store.appProperties = props;
        resolve(props);
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const appService = {
  loadAppProperties,
};

export default appService;