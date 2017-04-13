package se.simjarr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import se.simjarr.global.GlobalVariables;

@SpringUI
@Theme("valo")
public class ApplicationUI extends UI {

    private SettingsLayout settingsLayout;
    private TestPanelLayout testPanelLayout;
    private TradeFinderLayout tradeFinderLayout;
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
        GlobalVariables.reset();
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
        testPanelLayout = new TestPanelLayout();
        tradeFinderLayout = new TradeFinderLayout();
        settingsLayout = new SettingsLayout();
        tabSheet.addTab(testPanelLayout, GlobalVariables.TESTPANELAYOUT_INDEX);
        tabSheet.getTab(testPanelLayout).setCaption("Test Panel");
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
}
