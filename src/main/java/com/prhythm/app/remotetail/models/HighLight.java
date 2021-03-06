package com.prhythm.app.remotetail.models;

import com.prhythm.core.generic.util.Strings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.regex.Pattern;

/**
 * 顯著標示項目
 * Created by nanashi07 on 16/1/5.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HighLight {

    /**
     * 不分大小寫
     */
    boolean ignoreCase;
    /**
     * 符合的 {@link java.util.regex.Pattern} 字串
     */
    String pattern;

    /**
     * 文字顏色
     */
    String foreground;
    /**
     * 背景顏色
     */
    String background;
    /**
     * 粗體
     */
    boolean bold;
    /**
     * 斜體
     */
    boolean italic;
    /**
     * 排序
     */
    int order;

    transient Pattern expression;

    public HighLight() {
    }

    public HighLight(boolean ignoreCase, String pattern, String foreground, String background, boolean bold, boolean italic) {
        this.ignoreCase = ignoreCase;
        this.pattern = pattern;
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.italic = italic;
    }

    /**
     * 取得目前設定的 {@link Pattern}
     *
     * @return
     */
    public Pattern getExpression() {
        if (Strings.isNullOrWhiteSpace(pattern)) {
            return null;
        } else {
            if (expression == null) {
                return expression = Pattern.compile(pattern, isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
            } else {
                if (pattern.equals(expression.pattern()) && ignoreCase == ((expression.flags() & Pattern.CASE_INSENSITIVE) == Pattern.CASE_INSENSITIVE)) {
                    return expression;
                } else {
                    return expression = Pattern.compile(pattern, isIgnoreCase() ? Pattern.CASE_INSENSITIVE : 0);
                }
            }
        }
    }

    // getter & setter

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HighLight highLight = (HighLight) o;

        return pattern.equals(highLight.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
