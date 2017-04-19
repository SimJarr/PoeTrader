package se.simjarr.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import se.simjarr.global.ThreadLocalVariables;

import java.io.IOException;

public class TestPanelLayout extends VerticalLayout {

    private ThreadLocalVariables threadLocalVariables;

    public TestPanelLayout() {
        threadLocalVariables = ((ApplicationUI) UI.getCurrent()).getThreadLocalVariables();

        addButton();
    }

    private void addButton() {
        Button button = new Button("Test");
        button.addClickListener(clickEvent ->  {
            try {
                Jsoup.connect("http://currency.poe.trade/shop?league=Hardcore+Legacy")
                        .timeout(60000)
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .requestBody("league=Hardcore+Legacy&apikey=unitegasasiona&sell_currency=Orb+of+Fusing&sell_value=35&buy_value=121&buy_currency=Orb+of+Alchemy")
                        .execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.addComponent(button);
    }
}




















