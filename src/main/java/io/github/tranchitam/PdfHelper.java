package io.github.tranchitam;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.github.tranchitam.AutoFont.CssFont;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

public final class PdfHelper {

  public static ByteArrayOutputStream getPdfOutputStream(String pdfContent, PdfOptions options) {
    try {
      HtmlCleaner cleaner = new HtmlCleaner();
      TagNode rootTagNode = cleaner.clean(pdfContent);
      CleanerProperties cleanerProperties = cleaner.getProperties();
      XmlSerializer xmlSerializer = new PrettyXmlSerializer(cleanerProperties);
      String cleanedHtml = xmlSerializer.getAsString(rootTagNode);
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(cleanedHtml, null);
      ByteArrayOutputStream pdfStream = new ByteArrayOutputStream(pdfContent.length());
      builder.toStream(pdfStream);
      configurePdfOptions(builder, options);
      builder.run();
      return pdfStream;
    } catch (Exception e) {
      return null;
    }
  }

  private static void configurePdfOptions(PdfRendererBuilder builder, PdfOptions options) {
    if (Objects.isNull(options)) {
      return;
    }
    if (options.getFonts() != null && options.getFonts().size() > 0) {
      for (CssFont font : options.getFonts()) {
        builder.useFont(font.getPath().toFile(), font.getFamily(), font.getWeight(),
            font.getStyle(), true);
      }
    }

    if (Objects.nonNull(options.getBidiSplitterFactory())) {
      builder.useUnicodeBidiSplitter(options.getBidiSplitterFactory());
    }
    if (Objects.nonNull(options.getBidiReorderer())) {
      builder.useUnicodeBidiReorderer(options.getBidiReorderer());
    }

    if (Objects.nonNull(options.getTextDirection())) {
      builder.defaultTextDirection(options.getTextDirection());
    }
  }
}
