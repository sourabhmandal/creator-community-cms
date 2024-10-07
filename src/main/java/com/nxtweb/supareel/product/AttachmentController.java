package com.nxtweb.supareel.product;

import com.nxtweb.supareel.common.MessageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(name = "file")
@RequiredArgsConstructor
@Tag(name = "Uploads")
public class AttachmentController {
    private final AttachmentService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        try {
            byte[] content = service.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(content);
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Could not download file", e);
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<MessageResponse> deleteFile(@PathVariable String fileName) {

        if(service.deleteFile(fileName)) {
            return ResponseEntity.ok(MessageResponse.builder()
                    .status(MessageResponse.Status.SUCCESS)
                    .message("File deleted successfully")
                    .build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                MessageResponse.builder()
                        .status(MessageResponse.Status.ERROR)
                        .message("file does not exist")
                        .build());
    }
}
