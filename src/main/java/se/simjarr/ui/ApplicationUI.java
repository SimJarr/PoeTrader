package se.simjarr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI
@Theme("valo")
public class ApplicationUI extends UI {

    private TestLayout testLayout;
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
        testLayout = new TestLayout();
        tabSheet.addTab(testPanelLayout, "Test Panel");
        tabSheet.addTab(tradeFinderLayout, "Find Trades");
        tabSheet.addTab(testLayout, "Work in Progress");

        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 4);
    }

    private void addEstimatedValuesPanel(){
        estimatedValuesContainer = new EstimatedValuesContainer();
        layout.addComponent(estimatedValuesContainer);
        layout.setExpandRatio(estimatedValuesContainer, 2);
    }
}
