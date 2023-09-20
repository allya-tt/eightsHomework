import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EightsHomework {
    @BeforeEach
    public void setCookies() {
        Response responseLogin = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .body("return=%2Fmantisbt%2Fmy_view_page.php& username=admin&password=admin20&secure_session=on")
                .post("https://academ-it.ru/mantisbt/login.php")
                .andReturn();
        PHPSESSID = responseLogin.cookie("PHPSESSID");
        MANTIS_secure_session = responseLogin.cookie("MANTIS_secure_session");
        MANTIS_STRING_COOKIE = responseLogin.cookie("MANTIS_STRING_COOKIE");
        MANTIS_BUG_LIST_COOKIE = responseLogin.cookie("MANTIS_BUG_LIST_COOKIE");
        cookies.put("PHPSESSID", PHPSESSID);
        cookies.put("MANTIS_secure_session", MANTIS_secure_session);
        cookies.put("MANTIS_STRING_COOKIE", MANTIS_STRING_COOKIE);
        cookies.put("MANTIS_BUG_LIST_COOKIE", MANTIS_BUG_LIST_COOKIE);
    }

    private String PHPSESSID;
    private String MANTIS_secure_session;
    private String MANTIS_STRING_COOKIE;
    private String MANTIS_BUG_LIST_COOKIE;
    private Map<String, String> cookies = new HashMap<>();

    @Test
    public void getRequest() {
        String url = "https://academ-it.ru/mantisbt/account_page.php";
        Response response = RestAssured
                .given()
                .cookies(cookies)
                .get(url)
                .andReturn();
        System.out.println("\nResponse");
        response.prettyPrint();
        assertTrue(response.body().asString().contains("Real Name"));
        System.out.println(response.getCookies());
    }

    @Test
    public void updateRealNameTest() {
        String realName = "real name";
        Response responseUpdateRealName = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .cookies(cookies)
                .body("password_current=&password=&password_confirm=&email=rov55an3014%40mail.ru&realname=" + realName)
                .post("https://academ-it.ru/mantisbt/account_update.php")
                .andReturn();
//        System.out.println("\nResponse");
//        responseUpdateRealName.prettyPrint();
        assertEquals(200, responseUpdateRealName.statusCode(), "Response status code is not as expected");

        Response response = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/account_update.php")
                .andReturn();
        assertEquals(200, response.statusCode(), "Response status code is not as expected");
        org.junit.jupiter.api.Assertions.assertTrue(response.body().asString().contains("Real name successfully updated"));

        Response checkUpdatedRealName = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/account_page.php")
                .andReturn();
        assertEquals(200, checkUpdatedRealName.statusCode(), "Response status code is not as expected");
        System.out.println(checkUpdatedRealName.body().asString());
        assertTrue(checkUpdatedRealName.body().asString().contains("real name"));
        org.junit.jupiter.api.Assertions.assertTrue(checkUpdatedRealName.body().asString().contains("name=\"realname\" value=\"real name\""));


        Response checkUpdatedRealNameVersionTwo = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/my_view_page.php")
                .andReturn();
        assertEquals(200, checkUpdatedRealNameVersionTwo.statusCode(), "Response status code is not as expected");
        System.out.println(checkUpdatedRealNameVersionTwo.body().asString());
        org.junit.jupiter.api.Assertions.assertTrue(checkUpdatedRealNameVersionTwo.body().asString().contains("admin ( real name )"));
    }

    @Test
    public void checkUpdatedRealName() {
        Response checkUpdatedRealName = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/my_view_page.php")
                .andReturn();
        assertEquals(200, checkUpdatedRealName.statusCode(), "Response status code is not as expected");
        System.out.println(checkUpdatedRealName.body().asString());
    }
}
