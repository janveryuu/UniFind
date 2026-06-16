package com.example.system1.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class QrCodeService {

    public String generateQrCode(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
            
            String filename = UUID.randomUUID().toString() + "-qr.png";
            Path path = FileSystems.getDefault().getPath("uploads/" + filename);
            
            // Ensure uploads directory exists
            java.io.File uploadDir = new java.io.File("uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return filename;
        } catch (Exception e) {
            System.err.println("Failed to generate QR Code: " + e.getMessage());
            return null;
        }
    }
}
