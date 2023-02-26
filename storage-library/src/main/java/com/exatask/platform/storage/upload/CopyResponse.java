package com.exatask.platform.storage.upload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CopyResponse {

    private final String fileUrl;

    private final String fileUri;
}
