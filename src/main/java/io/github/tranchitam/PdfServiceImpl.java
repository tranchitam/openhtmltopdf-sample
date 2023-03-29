package io.github.tranchitam;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.TextDirection;
import io.github.tranchitam.AutoFont.CssFont;
import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class PdfServiceImpl implements PdfService {

  private List<CssFont> fonts;

  public PdfServiceImpl(String fontConfigDirectory) {
    try {
      this.fonts = AutoFont.findFontsInDirectory(Paths.get(fontConfigDirectory));
    } catch (Exception e) {
      this.fonts = Collections.emptyList();
    }
  }

  @Override
  public ByteArrayOutputStream createPdfStream() {
    String language = "en";
    PdfOptions options = getPdfOptions(language);
    String body = "<html><body>Hello world</body></html>";
    return PdfHelper.getPdfOutputStream(body, options);
  }

  @Override
  public String getPdfContentBase64(String pdfContent, String language) {
    PdfOptions options = getPdfOptions(language);
    ByteArrayOutputStream pdfOutputStream = PdfHelper.getPdfOutputStream(pdfContent, options);
    return Base64.getEncoder().encodeToString(pdfOutputStream.toByteArray());
  }

  private PdfOptions getPdfOptions(String language) {
    boolean rightToLeft = language.equals("ar");
    PdfOptions options = new PdfOptions();
    options.setFonts(fonts);
    if (rightToLeft) {
      options.setBidiReorderer(new ICUBidiReorderer());
      options.setBidiSplitterFactory(new ICUBidiSplitter.ICUBidiSplitterFactory());
      options.setTextDirection(TextDirection.RTL);
    }
    return options;
  }
}
