package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;

public class TransactionTest {

    @BeforeEach
    public void shouldLogin() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferFromFirstCardToSecond() {
        var dashboardPage = new DashboardPage();
        var initialBalanceFirst = dashboardPage.getFirstCardBalance();
        var initialBalanceSecond = dashboardPage.getSecondCardBalance();
        int destinationCardIndex = 1;
        int amount = 42;
        dashboardPage.transferTo(destinationCardIndex)
                .transfer(amount, DataHelper.getFirstCardNumber().getNumber());
        var currentBalanceFirst = dashboardPage.getFirstCardBalance();
        var currentBalanceSecond = dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(initialBalanceFirst - amount, currentBalanceFirst);
        Assertions.assertEquals(initialBalanceSecond + amount, currentBalanceSecond);
    }

    @Test
    public void shouldTransferFromSecondCardToFirst() {
        var dashboardPage = new DashboardPage();
        var initialBalanceFirst = dashboardPage.getFirstCardBalance();
        var initialBalanceSecond = dashboardPage.getSecondCardBalance();
        int destinationCardIndex = 0;
        int amount = 42;
        dashboardPage.transferTo(destinationCardIndex)
                .transfer(amount, DataHelper.getSecondCardNumber().getNumber());
        var currentBalanceFirst = dashboardPage.getFirstCardBalance();
        var currentBalanceSecond = dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(initialBalanceFirst + amount, currentBalanceFirst);
        Assertions.assertEquals(initialBalanceSecond - amount, currentBalanceSecond);
    }

    @Test
    public void shouldReturnErrorWhenTransferFromNonexistentCard() {
        var dashboardPage = new DashboardPage();
        var initialBalanceFirst = dashboardPage.getFirstCardBalance();
        var initialBalanceSecond = dashboardPage.getSecondCardBalance();
        int destinationCardIndex = 0;
        int amount = 42;
        var transferPage = new TransferPage();

        dashboardPage.transferTo(destinationCardIndex)
                .transfer(amount, "5589000000000003");
        transferPage.errorNotificationAssert("????????????\n" +
                "????????????! ?????????????????? ????????????");
    }

    @Test
    public void shouldReturnErrorWhenTransferAboveLimit() {
        var dashboardPage = new DashboardPage();
        var initialBalanceFirst = dashboardPage.getFirstCardBalance();
        var initialBalanceSecond = dashboardPage.getSecondCardBalance();
        int destinationCardIndex = 0;
        int amount = 100000;
        var transferPage = new TransferPage();
        dashboardPage.transferTo(destinationCardIndex)
                .transfer(amount, DataHelper.getSecondCardNumber().getNumber());
        var currentBalanceFirst = dashboardPage.getFirstCardBalance();
        var currentBalanceSecond = dashboardPage.getSecondCardBalance();
        Assertions.assertEquals(initialBalanceFirst, currentBalanceFirst);
        Assertions.assertEquals(initialBalanceSecond, currentBalanceSecond);
        transferPage.errorNotificationAssert("????????????\n" +
                "????????????! ???? ?????????? ???????????????? ???????????????????????? ??????????????");
    }
}
