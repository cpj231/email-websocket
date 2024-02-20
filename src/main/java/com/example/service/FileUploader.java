package com.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUploader {

    public static void main(String[] args) {
        String filePath = "large_file.bin"; // 假设大型二进制文件的路径
        int numThreads = 4; // 指定线程数量

        File file = new File(filePath);
        long fileSize = file.length();
        long chunkSize = fileSize / numThreads; // 计算每个线程处理的文件块大小

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int i = 0; i < numThreads; i++) {
                long startOffset = i * chunkSize;
                long endOffset = (i == numThreads - 1) ? fileSize : (startOffset + chunkSize);

                executorService.execute(new UploadTask(fileInputStream, startOffset, endOffset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    static class UploadTask implements Runnable {
        private FileInputStream inputStream;
        private long startOffset;
        private long endOffset;

        public UploadTask(FileInputStream inputStream, long startOffset, long endOffset) {
            this.inputStream = inputStream;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        @Override
        public void run() {
            try {
                // 创建连接
                URL url = new URL("http://example.com/upload");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // 设置范围
                connection.setRequestProperty("Content-Range", "bytes " + startOffset + "-" + (endOffset - 1) + "/" + endOffset);

                // 上传文件块
                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    inputStream.skip(startOffset);
                    long bytesToWrite = endOffset - startOffset;
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) > 0 && bytesToWrite > 0) {
                        int bytesWritten = Math.min(bytesRead, (int) bytesToWrite);
                        outputStream.write(buffer, 0, bytesWritten);
                        bytesToWrite -= bytesWritten;
                    }
                }

                // 获取响应
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("File chunk uploaded successfully.");
                } else {
                    System.err.println("Failed to upload file chunk. Response code: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
