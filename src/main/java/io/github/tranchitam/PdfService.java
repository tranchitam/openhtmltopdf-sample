package io.github.tranchitam;

import java.io.ByteArrayOutputStream;

public interface PdfService {

  ByteArrayOutputStream createPdfStream();

  String getPdfContentBase64(String pdfContent, String language);
}
