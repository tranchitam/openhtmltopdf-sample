package io.github.tranchitam;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class AutoFont {

  private AutoFont() {
  }

  public static List<CssFont> findFontsInDirectory(Path directory, List<String> validFileExtensions,
      boolean recurse, boolean followLinks) throws IOException {

    FontFileProcessor processor = new FontFileProcessor(validFileExtensions);

    int maxDepth = recurse ? Integer.MAX_VALUE : 1;
    Set<FileVisitOption> options = followLinks
        ? EnumSet.of(FileVisitOption.FOLLOW_LINKS)
        : EnumSet.noneOf(FileVisitOption.class);

    Files.walkFileTree(directory, options, maxDepth, processor);

    return processor.getFontsAdded();
  }

  public static List<CssFont> findFontsInDirectory(Path directory) throws IOException {
    return findFontsInDirectory(directory, Collections.singletonList("ttf"), true, true);
  }

  public static String toCssEscapedFontFamily(List<CssFont> fontsList) {
    return fontsList
        .stream()
        .map(fnt -> '\'' + fnt.familyCssEscaped() + '\'')
        .distinct()
        .collect(Collectors.joining(", "));
  }

  public static void toBuilder(PdfRendererBuilder builder, List<CssFont> fonts) {
    for (CssFont font : fonts) {
      builder.useFont(font.path.toFile(), font.family, font.weight, font.style, true);
    }
  }

  public static class CssFont {

    private final Path path;
    private final String family;
    private final int weight;
    private final FontStyle style;

    public CssFont(Path path, String family, int weight, FontStyle style) {
      this.path = path;
      this.family = family;
      this.weight = weight;
      this.style = style;
    }

    public Path getPath() {
      return path;
    }

    public String getFamily() {
      return family;
    }

    public int getWeight() {
      return weight;
    }

    public FontStyle getStyle() {
      return style;
    }

    public String familyCssEscaped() {
      return this.family.replace("'", "\\'");
    }

    @Override
    public int hashCode() {
      return Objects.hash(path, family, weight, style);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) {
        return true;
      }

      if (other == null || other.getClass() != this.getClass()) {
        return false;
      }

      CssFont b = (CssFont) other;

      return Objects.equals(this.path, b.path) && Objects.equals(this.family, b.family)
          && this.weight == b.weight && this.style == b.style;
    }
  }

  public static class FontFileProcessor extends SimpleFileVisitor<Path> {

    private final List<String> validFileExtensions;
    private final List<CssFont> fontsAdded = new ArrayList<>();

    public FontFileProcessor(List<String> validFileExtensions) {
      this.validFileExtensions = new ArrayList<>(validFileExtensions);
    }

    public List<CssFont> getFontsAdded() {
      return this.fontsAdded;
    }

    @Override
    public FileVisitResult visitFile(Path font, BasicFileAttributes attrs) throws IOException {
      if (attrs.isRegularFile() && isValidFont(font)) {
        try {
          Font f = Font.createFont(Font.TRUETYPE_FONT, font.toFile());
          String family = f.getFamily();
          String name = f.getFontName(Locale.US).toLowerCase(Locale.US);
          int weight = name.contains("bold") ? 700 : 400;
          FontStyle style = name.contains("italic") ? FontStyle.ITALIC : FontStyle.NORMAL;
          CssFont fnt = new CssFont(font, family, weight, style);
          onValidFont(fnt);
          fontsAdded.add(fnt);
        } catch (FontFormatException ffe) {
          onInvalidFont(font, ffe);
        }
      }

      return FileVisitResult.CONTINUE;
    }

    protected void onValidFont(CssFont font) {

    }

    protected void onInvalidFont(Path font, FontFormatException ffe) {

    }

    protected boolean isValidFont(Path font) {
      return this.validFileExtensions
          .stream()
          .anyMatch(ext -> font.toString().endsWith(ext));
    }
  }
}
