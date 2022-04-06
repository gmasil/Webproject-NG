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

import { AxiosResponse } from "axios";

import { VideoFile } from "@/types/videofile";
import { Artist } from "@/types/artist";
import { Comment } from "@/types/comment";
import { Scene } from "@/types/scene";

export class VideoFull {
  id?: number;
  title?: string;
  description?: string;
  length?: number;
  thumbnail?: string;
  thumbnailPreview?: string;
  files?: VideoFile[];
  artists?: Artist[];
  categories?: string[];
  comments?: Comment[];
  rating?: number;
  scenes?: Scene[];

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  public static fromResponse(response: AxiosResponse<any, any>): VideoFull {
    const video = Object.assign(new VideoFull(), response.data) as VideoFull;
    video.scenes?.sort((a: Scene, b: Scene) => {
      if (a.time < b.time) {
        return -1;
      }
      if (a.time > b.time) {
        return 1;
      }
      return 0;
    });
    return video;
  }
}
