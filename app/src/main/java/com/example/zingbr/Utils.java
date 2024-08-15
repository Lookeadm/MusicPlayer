package com.example.zingbr;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.IOException;

public class Utils {
        public static String truncate(String text, int maxLenght){
            if(text == null || text.length() <= maxLenght){
                return text;
            }
            return text.substring(0, maxLenght - 3) + "...";
        }

        public static String getDuration(Context context, Uri uri) throws IOException {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(context, uri);
                String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long durationMs = Long.parseLong(duration);
                return formatDuration(durationMs);
            } catch (Exception e) {
                e.printStackTrace();
                return "00:00"; // Trả về giá trị mặc định nếu có lỗi
            } finally {
                retriever.release();
            }
        }
        private static String formatDuration(long durationMs){
            int seconds = (int) (durationMs / 1000) % 60;
            int minutes = (int) (durationMs / (1000 * 60)) % 60;
            int hours = (int) (durationMs / (1000 * 60 * 60));
            if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } else {
                return String.format("%02d:%02d", minutes, seconds);
            }
        }

    }

