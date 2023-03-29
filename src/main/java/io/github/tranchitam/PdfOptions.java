package io.github.tranchitam;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter.ICUBidiSplitterFactory;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.TextDirection;
import io.github.tranchitam.AutoFont.CssFont;
import java.util.List;

public class PdfOptions {

  private List<CssFont> fonts;
  private ICUBidiSplitterFactory bidiSplitterFactory;
  private ICUBidiReorderer bidiReorderer;
  private TextDirection textDirection;

  public PdfOptions() {
  }

  public List<CssFont> getFonts() {
    return fonts;
  }

  public void setFonts(List<CssFont> fonts) {
    this.fonts = fonts;
  }

  public ICUBidiSplitterFactory getBidiSplitterFactory() {
    return bidiSplitterFactory;
  }

  public void setBidiSplitterFactory(ICUBidiSplitterFactory bidiSplitterFactory) {
    this.bidiSplitterFactory = bidiSplitterFactory;
  }

  public ICUBidiReorderer getBidiReorderer() {
    return bidiReorderer;
  }

  public void setBidiReorderer(ICUBidiReorderer bidiReorderer) {
    this.bidiReorderer = bidiReorderer;
  }

  public TextDirection getTextDirection() {
    return textDirection;
  }

  public void setTextDirection(TextDirection textDirection) {
    this.textDirection = textDirection;
  }
}
