package io.github.tranchitam;

public class Main {

  public static void main(String[] args) throws Exception {
    String pathToFontDirectory = "./configs/fonts";
    PdfService service = new PdfServiceImpl(pathToFontDirectory);
    String html = "<html><body>Hello World</body></html>";
    String pdfBase64 = service.getPdfContentBase64(html, "en");
    System.out.println(pdfBase64);
  }
}