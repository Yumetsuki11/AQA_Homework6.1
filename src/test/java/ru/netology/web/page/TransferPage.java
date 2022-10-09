package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public DashboardPage transfer(int amount, String cardNumber) {
        amountField.setValue(Integer.toString(amount));
        fromField.setValue(cardNumber);
        transferButton.click();
        return new DashboardPage();
    }

    public void errorNotificationAssert(String message) {
        errorNotification.shouldBe(Condition.visible);
        errorNotification.shouldHave(Condition.exactText(message));
    }
}
