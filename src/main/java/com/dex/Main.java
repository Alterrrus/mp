import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static String regexp = ":\"(\\\\.|[^\"\\\\])*\"";
  public static final String[] ITEMS = {"address", "city", "email", "firstName", "lastName",
      "phone"};

  public static void main(String[] args) throws InterruptedException, IOException {
    if (args.length < 3) {
      System.out.println("java Main url start stop [timeout ms]");
      System.exit(1);
    }
    System.out.println(
        "address," + "city," + "email," + "firstName," + "lastName," + "phone,");
    String url = args[0];
    int start = 0, end = 0, timeout = 0;
    try {
      start = Integer.parseInt(args[1]);
      end = Integer.parseInt(args[2]);
      timeout = args.length > 3 ? Integer.parseInt(args[3]) : 500;
    } catch (Exception e) {
      System.out.println("ошибка ввода параметра (start/end/timeout). параметр должен быть числом");
    }
    for (int i = start; i <= end; i++) {
      String out = new Scanner(new URL(url + i).openStream(),
          String.valueOf(StandardCharsets.UTF_8)).useDelimiter("\\A").next();

      for (String item : ITEMS) {
        parse(item, out);
      }
      System.out.println();
      Thread.sleep(timeout);
    }
  }

  static void parse(String tag, String out) {
    Matcher matcher = Pattern
        .compile("\"" + tag + "\"" + regexp, Pattern.UNICODE_CHARACTER_CLASS).matcher(out);
    matcher.find();
    String[] s = out.substring(matcher.start(), matcher.end()).split(":");
    if (s[1].length() == 2) {
      System.out.print(",");
    } else {
      System.out.print(s[1].substring(1, s[1].length() - 1) + ",");
    }
  }
}
