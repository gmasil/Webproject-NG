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
import { VideoSeekPreviewFile } from "@/types/videoseekpreviewfile";

export class VideoFull {
  id?: number;
  title?: string;
  description?: string;
  duration?: number;
  thumbnail?: string;
  thumbnailPreview?: string;
  releaseDate?: Date;
  files?: VideoFile[];
  artists?: Artist[];
  categories?: string[];
  comments?: Comment[];
  rating?: number;
  scenes?: Scene[];
  seekPreviewFile?: VideoSeekPreviewFile;

  public static fromResponse(
    response: AxiosResponse<VideoFull, unknown>
  ): VideoFull {
    const video: VideoFull = Object.assign(new VideoFull(), response.data);
    video.scenes?.sort(compareScenes);
    return video;
  }
}

function compareScenes(a: Scene, b: Scene): number {
  return a.time - b.time;
}
