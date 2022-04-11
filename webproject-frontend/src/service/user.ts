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

import { store } from "@/store";
import axios from "axios";
import { User, ChangePasswordData } from "@/types";

export const loadCurrentUser = (): Promise<User | null> => {
  return new Promise<User | null>((resolve, reject) => {
    axios
      .get("/api/users/current")
      .then((response) => {
        if (response.data !== "") {
          const user: User = response.data as User;
          store.commit("setCurrentUser", user);
          store.commit("setCurrentUser", user);
          resolve(user);
        } else {
          store.commit("setCurrentUser", null);
          resolve(null);
        }
      })
      .catch((error: Error) => {
        reject(error);
      });
  });
};

export const changePassword = (data: ChangePasswordData): Promise<void> => {
  const config = { headers: { "Content-Type": "application/json" } };
  return new Promise<void>((resolve, reject) => {
    axios
      .put("/api/users/updatepassword", data, config)
      .then(() => {
        resolve();
      })
      .catch((error: Error) => {
        reject(error);
      });
  });
};

export const mapUserFunctions = () => {
  return {
    loadCurrentUser,
    changePassword,
  };
};
