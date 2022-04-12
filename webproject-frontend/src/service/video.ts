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

import axios, { AxiosError } from "axios";
import { Page, PageResponse, Video, VideoFull } from "@/types";

export const loadVideo = (id: string): Promise<VideoFull> => {
  return new Promise<VideoFull>((resolve, reject) => {
    axios
      .get<VideoFull>(`/api/videos/${id}`)
      .then((response) => {
        resolve(VideoFull.fromResponse(response));
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const loadVideos = (page: Page): Promise<PageResponse<Video>> => {
  return new Promise<PageResponse<Video>>((resolve, reject) => {
    axios
      .get(`/api/videos?size=${page.size}&page=${page.page}&sort=${page.sort}`)
      .then((response) => {
        const pageResponse: PageResponse<Video> =
          response.data as PageResponse<Video>;
        resolve(pageResponse);
      })
      .catch((error: AxiosError) => {
        reject(error);
      });
  });
};

export const videoService = {
  loadVideo,
  loadVideos,
};

export default videoService;
