package se.simjarr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import se.simjarr.global.GlobalVariables;
import se.simjarr.global.ThreadLocalVariables;

@SpringUI
@Theme("valo")
public class ApplicationUI extends UI {

    private SettingsLayout settingsLayout;
    private TradePosterLayout tradePosterLayout;
    private TradeFinderLayout tradeFinderLayout;
    private EstimatedValuesContainer estimatedValuesContainer;
    private HorizontalLayout layout;
    private ThreadLocalVariables threadLocalVariables;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        threadLocalVariables = new ThreadLocalVariables();
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
        tradePosterLayout = new TradePosterLayout();
        tradeFinderLayout = new TradeFinderLayout();
        settingsLayout = new SettingsLayout();
        tabSheet.addTab(tradePosterLayout, GlobalVariables.TESTPANELAYOUT_INDEX);
        tabSheet.getTab(tradePosterLayout).setCaption("Test Panel");
        tabSheet.addTab(tradeFinderLayout, GlobalVariables.TRADEFINDERLAYOUT_INDEX);
        tabSheet.getTab(tradeFinderLayout).setCaption("Find Trades");
        tabSheet.addTab(settingsLayout, GlobalVariables.SETTINGSLAYOUT_INDEX);
        tabSheet.getTab(settingsLayout).setCaption("Work in Progress");

        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 4);
    }

    private void addEstimatedValuesPanel(){
        estimatedValuesContainer = new EstimatedValuesContainer();
        layout.addComponent(estimatedValuesContainer);
        layout.setExpandRatio(estimatedValuesContainer, 2);
    }

    public ThreadLocalVariables getThreadLocalVariables() {
        return threadLocalVariables;
    }
}
