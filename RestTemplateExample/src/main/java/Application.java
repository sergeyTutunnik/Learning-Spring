
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class Application {

    private static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        String url = "https://test-pass.rzd.ru/selfcare/j_security_check/ru/?";

        //Prepare headers
        HttpHeaders headers = new HttpHeaders();

        headers.add("Connection", "keep-alive");
        headers.add("Cache-Control", "max-age=0");
        headers.add("Origin", "https://test-pass.rzd.ru");
        headers.add("Upgrade-Insecure-Requests", "1");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.add("Referer", "https://test-pass.rzd.ru/timetable/logon/ru");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,bg;q=0.6");
        headers.add("Cookie", "klang=ru; _POSTED_GENDER_ID=2; JSESSIONID=00001s6pVswvGEmlwEKiUrfVuMj:17obq26s3; AuthFlag=false; WASReqURL=https:///timetable/secure/ru; accessible=false");

        //Prepare request data
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("j_username", "user4task1");
        map.add("j_password", "1");

        //Execute request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);


        //Parse response
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> setCookieList = responseHeaders.getValuesAsList("Set-Cookie");

        setCookieList.forEach((p) -> {
            if (p.startsWith("LtpaToken2")) {
                LOGGER.info("Hi!");
                LOGGER.info("The returned token value is {}", p);
            }
        });
    }
}
