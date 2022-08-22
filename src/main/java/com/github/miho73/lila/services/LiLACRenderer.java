package com.github.miho73.lila.services;

import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LiLACRenderer {
    private static final String bold_n_italic   = "\\*{3}(.*?)\\*{3}";
    private static final String italic          = "\\*{2}(.*?)\\*{2}";
    private static final String bold            = "\\*(.*?)\\*";
    private static final String underline       = "_{2}(.*?)_{2}";
    private static final String strike          = "-{2}(.*?)-{2}";
    private static final String superscript     = "\\^{2}(.*?)\\^{2}";
    private static final String subscript       = ",{2}(.*?),{2}";
    private static final String link            = "\\[{2}(.*?)\\|(.*?)]{2}";
    private static final String ext_image       = "!\\[(.*?)]";
    private static final String loc_image       = "!\\((.*?)\\)";
    private static final String ordered_list    = "^-\\s(.*?)$";
    private static final String unordered_list  = "^\\*\\s(.*?)$";
    private static final Pattern font_size      = Pattern.compile("\\[\\(([+-]\\d+)\\)(.*?)]");
    private final static Pattern section        = Pattern.compile("^==\\s+(.*?)\\s+==$", Pattern.MULTILINE);
    private final static Pattern box_open       = Pattern.compile("^div$", Pattern.MULTILINE);
    private final static Pattern box_close      = Pattern.compile("^/div$", Pattern.MULTILINE);
    private final static Pattern fold_open      = Pattern.compile("^fold\\((.*?)/(.*?)\\)$", Pattern.MULTILINE);
    private final static Pattern fold_close     = Pattern.compile("^/fold$", Pattern.MULTILINE);
    private final static Pattern table_capture  = Pattern.compile("\\|(\\[[/-]\\d+])?([^|]*)");
    private final static String table_line      = "\\|(.*?)\\|";

    public static String render(String lilac) throws LiLACParsingException {
        // 인라인 요소 치환
        lilac = lilac
                // HTML Injection 방지
                .replaceAll("<", "&#60;")
                .replaceAll(">", "&#62;")
                // 매크로 치환
                .replaceAll("\\[lf]", "<br>")
                .replaceAll("---",    "<hr class=\\\"lilac-hr\\\">")
                // 텍스트 데코레이션
                .replaceAll(bold_n_italic, "<span class=\"lilac-bold lilac-italic\">$1</span>")
                .replaceAll(italic,        "<span class=\"lilac-italic\">$1</span>")
                .replaceAll(bold,          "<span class=\"lilac-bold\">$1</span>")
                .replaceAll(underline,     "<span class=\"lilac-underline\">$1</span>")
                .replaceAll(strike,        "<span class=\"lilac-strike\">$1</span>")
                .replaceAll(superscript,   "<sup class=\"lilac-super\">$1</sup>")
                .replaceAll(subscript,     "<sub class=\"lilac-sub\">$1</sub>")
                // 하이퍼링크 적용
                .replaceAll(link,          "<a href=\"$1\" class=\"lilac-link\" target=\"_blank\">$2</a>")
                // 이미지 적용
                .replaceAll(ext_image,     "<img src=\"$1\">")
                .replaceAll(loc_image,     "<img src=\"/problems/resources/get/$1\">");

        // 박스 적용
        lilac = box_open.matcher(lilac)  .replaceAll("\0<div class=\"lilac-box\">");
        lilac = box_close.matcher(lilac) .replaceAll("\0</div>");
        lilac = fold_open.matcher(lilac) .replaceAll("\0<details open-text=\"$2\"><summary>$1</summary>");
        lilac = fold_close.matcher(lilac).replaceAll("\0</details>");

        // 섹션 적용
        lilac = section.matcher(lilac).replaceAll("\0<h1 class=\"lilac-section\">$1</h1>");

        // 텍스트 크기 적용
        lilac = font_size.matcher(lilac).replaceAll(matchResult -> {
            return "<span style=\"font-size: "+Math.floor(Integer.parseInt(matchResult.group(1))+11)/10+"em;\">"+matchResult.group(2)+"</span>";
        });

        String[] lines = lilac.split("\n");
        ArrayList<String> html = new ArrayList<>();
        int lineNo = 0;

        int multilineStatus = 0; // bitmask variable

        for (String line : lines) {
            lineNo++;

            // 다중문 처리 중단 체크포인트
            if(multilineStatus == 1 && !line.startsWith("&#62; ")) {
                html.add("</blockquote>");
                multilineStatus = 0;
            }
            if(multilineStatus >> 1 == 1 && !line.startsWith("- ")) {
                html.add("</ol>");
                multilineStatus = multilineStatus ^ 0b10;
            }
            if(multilineStatus >> 2 == 1 && !line.startsWith("* ")) {
                html.add("</ul>");
                multilineStatus = multilineStatus ^ 0b100;
            }
            if(multilineStatus >> 3 == 1 && !line.matches(table_line)) {
                html.add("</table>");
                multilineStatus = multilineStatus ^ 0b1000;
            }

            // 빈 줄은 무시
            if(line.equals(""));
            // 시작이 널문자면 p로 감싸기 금지
            else if(line.startsWith("\0")) {
                html.add(line.substring(1));
            }
            // 인용문(2^0)
            else if(line.startsWith("&#62; ")) {
                if(multilineStatus == 0) {
                    multilineStatus = multilineStatus ^ 0b1;
                    html.add("<blockquote class=\"lilac-quote\">");
                }

                // 인용문 처리가 진행중이면 추가
                if(multilineStatus == 1) {
                    html.add("<p>"+line.substring(6)+"</p>");
                }
                // 내 것이 아닌 다중문 처리가 진행중이면 오류
                else if(multilineStatus != 0b1) {
                    throw new LiLACParsingException("Cannot overlap multiline object(while processing quote). Line "+lineNo);
                }
            }
            // 순서있는 리스트
            else if(line.matches(ordered_list)) {
                if(multilineStatus == 0) {
                    multilineStatus = multilineStatus ^ 0b10;
                    html.add("<ol class=\"lilac-ol\">");
                }

                // 처리중 > 추가
                if(multilineStatus >> 1 == 1) {
                    html.add("<li>"+line.substring(2)+"</li>");
                }
                else if(multilineStatus != 0b10) {
                    throw new LiLACParsingException("Cannot overlap multiline object(while processing ordered list). Line "+lineNo);
                }
            }
            // 순서없는 리스트
            else if(line.matches(unordered_list)) {
                if(multilineStatus == 0) {
                    multilineStatus = multilineStatus ^ 0b100;
                    html.add("<ul class=\"lilac-ul\">");
                }

                // 처리중 > 추가
                if(multilineStatus >> 2 == 1) {
                    html.add("<li>"+line.substring(2)+"</li>");
                }
                else if(multilineStatus != 0b100) {
                    throw new LiLACParsingException("Cannot overlap multiline object(while processing unordered list). Line "+lineNo);
                }
            }
            // 테이블
            else if(line.matches(table_line)) {
                if(multilineStatus == 0) {
                    multilineStatus = multilineStatus ^ 0b1000;
                    html.add("<table class=\"lilac-table\">");
                }

                // 처리중 > 추가
                if(multilineStatus >> 3 == 1) {
                    String lineBody = table_capture.matcher(StringUtils.chop(line)).replaceAll(matchResult -> {
                       if(matchResult.group(1) == null) {
                           String contentEscape = Matcher.quoteReplacement(matchResult.group(2));
                           return "<td class=\"lila-td\">"+contentEscape+"</td>";
                       }
                       else {
                           String director = matchResult.group(1);
                           String contentEscape = Matcher.quoteReplacement(matchResult.group(2));
                           return "<td class=\"lila-td\" "+
                                   (director.charAt(1) == '-' ? "colspan=\"" : "rowspan=\"")+
                                   director.substring(2, director.length()-1)+
                                   "\">"+
                                   contentEscape+
                                   "</td>";
                       }
                    });
                    html.add("<tr class=\"lila-tr\">"+lineBody+"</tr>");
                }
                else if(multilineStatus != 0b1000) {
                    throw new LiLACParsingException("Cannot overlap multiline object(while processing table). Line "+lineNo);
                }
            }
            //아무것도 해당되지 않으면 일반 텍스트 라인으로 간주. p태그 걸어줌
            else html.add("<p>"+line+"</p>");
        }

        StringBuilder finalHtml = new StringBuilder();
        html.forEach(finalHtml::append);
        return finalHtml.toString();
    }
}
