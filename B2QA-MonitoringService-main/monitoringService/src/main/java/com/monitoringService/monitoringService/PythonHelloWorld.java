package com.monitoringService.monitoringService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PythonHelloWorld {

        public static void main(String[] args) {
            try {
                // Set the working directory to the directory containing the Python script
                String workingDirectory = Paths.get("src/main/resources").toAbsolutePath().toString();

                // Launch the Python server as a separate process
                ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\aishwarya\\Downloads\\monitoringService (1)\\monitoringService\\src\\main\\resources\\pythonScript.py");
                processBuilder.directory(new File(workingDirectory));
                Process process = processBuilder.start();

                // Wait for the server to start up
                Thread.sleep(5000);

                // Make a request to the Python server's API endpoint
                String response = makeRequest("http://localhost:5000/api/data");

                // Print the response data
                System.out.println(response);

                // Stop the Python server process
                process.destroy();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static String makeRequest(String url) throws IOException {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    return EntityUtils.toString(response.getEntity());
                }
            }
        }
}



