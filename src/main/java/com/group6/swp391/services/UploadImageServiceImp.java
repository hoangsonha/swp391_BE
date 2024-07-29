package com.group6.swp391.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Service
public class UploadImageServiceImp implements UploadImageService {

    // Firebase

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.content.type}")
    private String contentType;

    @Value("${firebase.get.stream}")
    private String fileConfigFirebase;

    @Value("${firebase.get.url}")
    private String urlFirebase;

    @Value("${firebase,get.folder}")
    private String folderContainImage;

    @Value("${firebase.file.format}")
    private String fileFormat;

    // BufferImage

    @Value("${bufferImage.type}")
    private String bufferImageType;

    @Value("${bufferImage.fillRect.width}")
    private int bufferImageWidth;

    @Value("${bufferImage.fillRect.height}")
    private int bufferImageHeight;

    @Value("${bufferImage.fillRect.color.background}")
    private String bufferImageColorBackground;

    @Value("${bufferImage.fillRect.color.text}")
    private String bufferImageColorText;

    @Value("${bufferImage.fillRect.font.text}")
    private String bufferImageFontText;

    @Value("${bufferImage.fillRect.size.text}")
    private int bufferImageSizeText;

    @Value("${bufferImage.fillRect.x}")
    private int bufferImageX;

    @Value("${bufferImage.fillRect.y}")
    private int bufferImageY;

    @Value("${bufferImage.devide}")
    private int bufferImageDevide;


    private String uploadFile(File file, String fileName) throws IOException {  // file vs fileName is equal
        String folder = folderContainImage + "/" + fileName;  // 1 is folder and fileName is "randomString + "extension""
        BlobId blobId = BlobId.of(bucketName, folder); // blodId is a path to file in firebase
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();  // blodInfo contains blodID and more
        InputStream inputStream = UploadImageServiceImp.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        // saved image on firebase
        String DOWNLOAD_URL = urlFirebase;
        return String.format(DOWNLOAD_URL, URLEncoder.encode(folder, StandardCharsets.UTF_8));
    }

    @Override
    public boolean deleteImageOnFireBase(String urlImage) throws IOException {
        String folder = folderContainImage + "/" + urlImage;
        BlobId blobId = BlobId.of(bucketName, folder); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = UploadImageServiceImp.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.delete(blobId);
    }

    @Override
    public String uploadFileBase64(String base64Image) throws IOException {

        String fileName = UUID.randomUUID().toString() + fileFormat;  // Generate a random file name
        String folder = folderContainImage + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, folder); // Replace with your bucket name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();    // type media does not see the picture on firebase

        InputStream inputStream = UploadImageServiceImp.class.getClassLoader().getResourceAsStream(fileConfigFirebase); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        storage.create(blobInfo, imageBytes);

        String DOWNLOAD_URL = urlFirebase;
        return String.format(DOWNLOAD_URL, URLEncoder.encode(folder, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);          // create newFile ưith String of fileName (random String + "extension") and save to Current Working Directory or Java Virtual Machine (JVM)
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get file name .jpg, .png, ...
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name and plus + "extension".
            File file1 = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadFile(file1, fileName);                                   // to get uploaded file link
            file1.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    @Override
    public String upload(MultipartFile[] multipartFile) {
        try {

            for (MultipartFile file : multipartFile) {
                String fileName = file.getOriginalFilename();                        // to get original file name
                fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

                File file1 = this.convertToFile(file, fileName);                      // to convert multipartFile to File
                String URL = this.uploadFile(file1, fileName);                                   // to get uploaded file link
                file1.delete();
            }
//            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
//            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.
//
//            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
//            String URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
//            file.delete();
//            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
        return null;
    }

    @Override
    public String generateImageWithInitial(String userName) throws IOException {

        char initial = Character.toUpperCase(userName.trim().charAt(0));

        // create width and height of image
        int width = bufferImageWidth;
        int height = bufferImageHeight;

        // BufferedImage to process image in memory, it can be drawing, edit, insert things into image
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // insert character into image
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.decode(bufferImageColorBackground));
        graphics.fillRect(bufferImageX, bufferImageY, width, height);  // x, y is the conner on the top left of rectangle
        graphics.setFont(new Font(bufferImageFontText, Font.BOLD, bufferImageSizeText));
        graphics.setColor(Color.decode(bufferImageColorText));
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = (width - fontMetrics.charWidth(initial)) / bufferImageDevide;
        int y = ((height - fontMetrics.getHeight()) / bufferImageDevide) + fontMetrics.getAscent();
        graphics.drawString(String.valueOf(initial), x, y);
        graphics.dispose();

        // change image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, bufferImageType, baos);

        byte[] imageBytes = baos.toByteArray();

        // Encode the byte array to a Base64 string
        String base64String = Base64.getEncoder().encodeToString(imageBytes);

        return base64String;

    }


}
