package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.services.PostService;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.validation.FileSchema;
import com.mtm.bulletin_board.utils.validation.NumberSchema;
import com.mtm.bulletin_board.utils.validation.RequestValidator;
import com.mtm.bulletin_board.utils.validation.StringSchema;
import com.mtm.bulletin_board.utils.validation.ValidationSchema;
import com.mtm.bulletin_board.utils.validation.Validator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@WebServlet("/posts/upload-csv")
@MultipartConfig
public class PostCsvUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostService postService;
    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        postService = new PostService();
        userService = new UserService();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward the request to the JSP page
        request.getRequestDispatcher("/views/post/upload-csv.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> validate = validateRequest(request);

        if (validate == null || validate.isEmpty()) {
            try {
                // Get the file part from the request
                Part filePart = request.getPart("file");

                // Get the InputStream from the file part
                InputStream fileContent = filePart.getInputStream();

                // Process the CSV data (e.g., using a CSV parser)
                List<String[]> csvData = parseCSV(fileContent);

                List<String[]> vCsvData = validatedCsvData(csvData);

                if (vCsvData != null && vCsvData.size() > 0) {
                    // TODO:: Display Success Row Count Alert
                    List<Post> uploadedPosts = convertToPostList(vCsvData);

                    int successRow = postService.savePostsFromCsvUpload(uploadedPosts);
                    System.out.println("successRow:"+successRow);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid CSV file");
                    return;
                }

                response.sendRedirect(request.getContextPath() + "/home");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
            }

        } else {
            String validationError = gson.toJson(validate);

            request.setAttribute("error", validationError);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/upload-csv.jsp");
            dispatcher.forward(request, response);
        }
    }

    private Map<String, String> validateRequest(HttpServletRequest request) {
        RequestValidator reqValidator = new RequestValidator(request);

        reqValidator.addSchema("file", new FileSchema().required());

        return reqValidator.validate();
    }

    private List<String[]> parseCSV(InputStream fileContent) {
        List<String[]> csvData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(fileContent))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                csvData.add(line);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return csvData;
    }

    private List<String[]> validatedCsvData(List<String[]> csvData) {
        List<String[]> validatedData = new ArrayList<>();

        List<Integer> userIds = userService.getAllUserIds();
        String[] actualHeaders = csvData.get(0);

        if (userIds == null) {
            return null;
        }

        String[] expectedHeaders = ("Title,Description,Created User Id").split(",");

        // Validate headers
        if (!validateHeaders(actualHeaders, expectedHeaders)) {
            return null;
        }

        if (!(csvData.get(1).length > 0)) {
            return null;
        }

        // Assuming the first row contains headers and should be skipped
        for (int i = 1; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            Integer createdUserId = (row.length > 2) ? Integer.parseInt(row[2]) : null;
            // Validate input field
            if (validateRow(row, actualHeaders.length) && userIds.contains(createdUserId)) {
                validatedData.add(row);
            } else {
                System.out.println("Invalid row: " + String.join(",", row));
            }
        }
        return validatedData;
    }

    private boolean validateHeaders(String[] actualHeaders, String[] expectedHeaders) {

        if (actualHeaders == null || expectedHeaders == null || actualHeaders.length != expectedHeaders.length) {
            return false;
        }

        for (int i = 0; i < actualHeaders.length; i++) {
            if (!actualHeaders[i].equals(expectedHeaders[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean validateRow(String[] row, int expectedSize) {
        if (row.length != expectedSize) {
            return false;
        }

        String title = (row.length > 0) ? row[0] : "";
        String description = (row.length > 1) ? row[1] : "";
        Integer createdUserId = (row.length > 2) ? Integer.parseInt(row[2]) : null;

        Map<String, String> validate = validateRowField(title, description, createdUserId);

        if (validate == null || validate.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, String> validateRowField(String title, String description, Integer createdUserId) {
        Map<String, ValidationSchema> schema = new HashMap<>();

        schema.put("title", new StringSchema(title, "title").required().min(5).max(255));
        schema.put("description", new StringSchema(description, "description").required().max(255));
        schema.put("createdUserId", new NumberSchema(createdUserId, "createdUserId").required().min(1));

        return new Validator(schema).validate();
    }

    private List<Post> convertToPostList(List<String[]> csvData) {
        List<Post> postList = new ArrayList<>();

        for (int i = 0; i < csvData.size(); i++) {
            String[] row = csvData.get(i);

            // Assuming row[0] is the title and row[1] is the description
            String title = (row.length > 0) ? row[0] : "";
            String description = (row.length > 1) ? row[1] : "";
            Integer createdUserId = (row.length > 2) ? Integer.parseInt(row[2]) : null;

            Post post = new Post();
            post.setTitle(title);
            post.setDescription(description);
            post.setCreatedUserId(createdUserId);
            postList.add(post);
        }

        return postList;
    }
}
