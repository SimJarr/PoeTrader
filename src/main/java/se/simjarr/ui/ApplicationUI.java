package se.simjarr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class ApplicationUI extends UI {

    private TradeFinderUI tradeFinderUI;
    private EstimatedValuesContainer estimatedValuesContainer;
    private HorizontalLayout layout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupLayout();
        addLeftSide();
        addTabs();
        addEstimatedValuesPanel();
    }

    private void setupLayout() {
        layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setHeight("100%");
        layout.setMargin(true);
        setContent(layout);
    }

    private void addLeftSide(){
        VerticalLayout emptySide = new VerticalLayout();
        layout.addComponent(emptySide);
        layout.setExpandRatio(emptySide, 1);
    }

    private void addTabs() {
        TabSheet tabSheet = new TabSheet();
        tradeFinderUI = new TradeFinderUI();
        tabSheet.addTab(tradeFinderUI, "Hot Deals");
        tabSheet.addTab(new VerticalLayout(), "My Offers");
        tabSheet.addTab(new VerticalLayout(), "Simon Knows");

        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 4);
    }

    private void addEstimatedValuesPanel(){
        estimatedValuesContainer = new EstimatedValuesContainer();
        layout.addComponent(estimatedValuesContainer);
        layout.setExpandRatio(estimatedValuesContainer, 2);
    }
}
