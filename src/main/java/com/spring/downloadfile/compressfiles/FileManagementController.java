package com.spring.downloadfile.compressfiles;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileManagementController {
  @GetMapping(value = "/donwloadFile" , produces = "application/zip")
  public ResponseEntity<Resource> downloadFile() throws FileNotFoundException, IOException {
    //FileInputStream file = new FileInputStream("test.txt");

    Resource resource = new ClassPathResource("compress.zip");

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.zip")
            //.contentLength(file.read())
            .contentType(MediaType.parseMediaType("application/zip"))
            .body(resource);

  }
  @GetMapping(value = "/donwload/compressedFile")
  public ResponseEntity<?> downloadCompressedFile() throws FileNotFoundException, IOException {
    BinaryOutputWrapper binaryOutputWrapper = this.prepDownloadAsZIP(Arrays.asList("test.txt", "test2.txt"));
    return ResponseEntity.ok()
            .headers(binaryOutputWrapper.getHeaders())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(binaryOutputWrapper.getBytes());
  }

  public BinaryOutputWrapper prepDownloadAsZIP(List<String> filenames) throws IOException {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/zip"));
    String outputFilename = "output.zip";
    headers.setContentDispositionFormData(outputFilename, outputFilename);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream);

    for(String filename: filenames) {
      //File file = new File(filename);
      Resource resource = new ClassPathResource(filename);
      zipOutputStream.putNextEntry(new ZipEntry(filename));
      FileInputStream fileInputStream = new FileInputStream(resource.getFile());
      IOUtils.copy(fileInputStream, zipOutputStream);
      fileInputStream.close();
      zipOutputStream.closeEntry();
    }
    zipOutputStream.close();
    return new BinaryOutputWrapper(byteOutputStream.toByteArray(), headers);
  }
}
