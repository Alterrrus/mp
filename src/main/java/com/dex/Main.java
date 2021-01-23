package com.dex;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

  private final static String REGEXP = ":\"(\\\\.|[^\"\\\\])*\"";

  static void PARSE(String tag, String out) {
    Matcher matcher = Pattern
        .compile("\"" + tag + "\"" + REGEXP, Pattern.UNICODE_CHARACTER_CLASS).matcher(out);
    matcher.find();
    String[] s = out.substring(matcher.start(), matcher.end()).split(":");
    if (s[1].length() == 2) {
      System.out.print(",");
    } else {
      System.out.print(s[1].substring(1, s[1].length() - 1) + ",");
    }
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    if (args.length == 0) {
      System.out.println("при запуске программы необходимо ввести параметры.");
      System.out.println("для java9 и выше: ");
      System.out.println("java Main.java url start end [timeout]");
      System.out.println("где url - адрес парсинга вида http://somesite.com/someresource=");
      System.out.println("где start - id ресурса для начала парсинга (число)");
      System.out.println("где end - id ресурса для окончания парсинга (число)");
      System.out.println("где timeout - время в мс между парсингом страниц.по умолчанию 500 мс");
      System.exit(0);
    }
    System.out.println(
        "address," + " " + "city," + " " + "email," + " " + "firstName," + " " + "lastName," + " "
            + "phone,");
    String url = args[0];
    int start = 0, end = 0, timeout = 0;
    try {
      start = Integer.parseInt(args[1]);
      end = Integer.parseInt(args[2]);
      timeout = Integer.parseInt((args.length > 3) ? args[3] : "500");
    } catch (Exception e) {
      System.out.println("ошибка ввода параметра (start/end/timeout). параметр должен быть числом");
    }

    for (int i = start; i <= end; i++) {
      String out = new Scanner(new URL(url + i).openStream(),
          String.valueOf(StandardCharsets.UTF_8)).useDelimiter("\\A").next();
      PARSE("address", out);
      PARSE("city", out);
      PARSE("email", out);
      PARSE("firstName", out);
      PARSE("lastName", out);
      PARSE("phone", out);
      System.out.println();
      Thread.sleep(timeout);
    }
  }
}
