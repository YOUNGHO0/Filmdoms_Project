package com.filmdoms.community.testentityprovider;

import com.filmdoms.community.file.data.entity.File;

public class TestFileProvider {

    public static int count;

    public static File get() {
        count++;
        File file = File.builder()
                .originalFileName("original_file_name_" + count)
                .uuidFileName("uuid_file_name_" + count)
                .build();

        return file;
    }
}
