package se.simjarr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
@Theme("valo")
public class ApplicationUI extends UI {

    private TradeFinderUI tradeFinderUI;
    private HorizontalLayout layout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupLayout();
        addTabs();
    }

    private void setupLayout() {
        layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        setContent(layout);
    }

    private void addTabs() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidth("80%");
        tradeFinderUI = new TradeFinderUI();
        tabSheet.addTab(tradeFinderUI, "Hot Deals");
        tabSheet.addTab(new VerticalLayout(), "My Offers");
        tabSheet.addTab(new VerticalLayout(), "Simon Knows");

        layout.addComponent(tabSheet);
    }
}
