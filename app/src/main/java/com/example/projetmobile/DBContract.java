package com.example.projetmobile;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract() {}

    public static class DogEntry implements BaseColumns {
        public static final String TABLE_NAME = "dogs";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE_URL = "image_url";
    }
}
