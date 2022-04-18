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

import { Store, useStore } from "@/store/pinia";
import axios, { AxiosError } from "axios";
import { User, ChangePasswordData, Response, Resolve, Reject } from "@/types";

export const loadCurrentUser = (): Promise<User | null> => {
  return new Promise<User | null>(
    (resolve: Resolve<User | null>, reject: Reject) => {
      axios
        .get("/api/users/current")
        .then((response: Response<User | string>) => {
          const store: Store = useStore();
          if (response.data !== "") {
            const user: User = response.data as User;
            store.currentUser = user;
            store.initializedUser = true;
            resolve(user);
          } else {
            store.currentUser = null;
            store.initializedUser = true;
            resolve(null);
          }
        })
        .catch((error: AxiosError) => {
          reject(error);
        });
    }
  );
};

export const changePassword = (data: ChangePasswordData): Promise<void> => {
  // eslint-disable-next-line @typescript-eslint/typedef
  const config = { headers: { "Content-Type": "application/json" } };
  return new Promise<void>((resolve: Resolve<void>, reject: Reject) => {
    axios
      .put("/api/users/updatepassword", data, config)
      .then(() => {
        resolve();
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

// eslint-disable-next-line @typescript-eslint/typedef
export const userService = {
  loadCurrentUser,
  changePassword,
};

export default userService;
