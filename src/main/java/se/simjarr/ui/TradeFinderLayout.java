package se.simjarr.ui;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FileResource;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.*;
import se.simjarr.global.Currency;
import se.simjarr.model.TradeFinder;
import se.simjarr.model.TradeOffer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TradeFinderLayout extends VerticalLayout {

    private Panel tradeDisplayLayout;
    private Accordion tradeDisplayList;
    private TextField minProfitInput;
    private Map<Currency, String> currencyId;
    private Registration textAreaListener;

    public TradeFinderLayout() {
        tradeDisplayLayout = new Panel();
        tradeDisplayList = new Accordion();
        currencyId = new HashMap<>();

        addHeader();
        addCurrencySelection();
        addTradeDisplaySection();
    }

    private void addHeader() {
        Label header = new Label("find multiple trades based on your available currency");
        this.addComponent(header);
    }

    private void addCurrencySelection() {
        HorizontalLayout formLayout = new HorizontalLayout();
        for (int i = 0; i < Currency.values().length; i++) {
            Currency current = Currency.fromValue(i + 1);
            FileResource icon = current.getFileResource();
            Slider slider = new Slider();
            slider.setOrientation(SliderOrientation.VERTICAL);
            slider.setIcon(icon);
            slider.setWidth(40, Unit.PIXELS);
            slider.setId(UUID.randomUUID().toString());
            currencyId.put(current, slider.getId());
            formLayout.addComponent(slider);
        }

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        //TODO: fix alignment
        Label label = new Label();
        label.setValue("MIN PROFIT PER TRADE:");
        label.setDescription("profit in reference currency");

        minProfitInput = new TextField();
        minProfitInput.setWidth(50, Unit.PIXELS);
        minProfitInput.setDescription("default value 0.1");

        Button sendButton = new Button("Send");
        sendButton.addClickListener(clickEvent -> {
            Map<Currency, Integer> myCurrency = new HashMap<>();
            currencyId.forEach((k, v) -> {
                Slider slider = (Slider) findComponentById(this, v);
                assert slider != null;
                int sliderValue = slider.getValue().intValue();
                myCurrency.put(k, sliderValue);
            });
            TradeFinder tradeFinder = new TradeFinder();
            tradeFinder.setAvailableCurrency(myCurrency);
            double minProfitPerTrade;
            try {
                minProfitPerTrade = Double.parseDouble(minProfitInput.getValue());
            } catch (NumberFormatException e) {
                minProfitPerTrade = 0.1;
            }
            List<List<TradeOffer>> listList = tradeFinder.tradeChainer(minProfitPerTrade);
            List<TradeOffer> list = new ArrayList<>();
            listList.forEach(list::addAll);
            addTradeChainDisplay(list);
        });

        horizontalLayout.addComponents(label, minProfitInput, sendButton);
        horizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        this.addComponents(formLayout, horizontalLayout);
    }

    private void addTradeDisplaySection() {
        tradeDisplayLayout.setHeight("550px");
        tradeDisplayLayout.addStyleName("borderless");
        this.addComponent(tradeDisplayLayout);
    }

    private void addTradeChainDisplay(List<TradeOffer> trades) {
        tradeDisplayList.removeAllComponents();
        if (textAreaListener != null) textAreaListener.remove();

        AtomicInteger counter = new AtomicInteger(1);
        trades.forEach(trade -> {
            TextArea textArea = new TextArea();
            textArea.setEnabled(false);
            textArea.setWidth(100, Unit.PERCENTAGE);
            textArea.setValue(trade.toString());
            textArea.setId(String.valueOf(trades.indexOf(trade)));
            String imgTagStart = "<img class=\"v-icon\" src=\"";
            String imgTagEnd = "\">";
            String baseImgPath = "VAADIN/themes/valo/images/";
            String sellPicture = imgTagStart + baseImgPath + Currency.fromValue(trade.getSellCurrency()).getImgPath() + imgTagEnd;
            String buyPicture = imgTagStart + baseImgPath + Currency.fromValue(trade.getBuyCurrency()).getImgPath() + imgTagEnd;
            tradeDisplayList.setTabCaptionsAsHtml(true);
            tradeDisplayList.addTab(textArea, "TRADE " + counter.getAndIncrement() + ": " + sellPicture + "<b> " + trade.getSellValue() + " ⇐ " + trade.getBuyValue() + " </b>" + buyPicture);
        });

        textAreaListener = this.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent -> {
            if (layoutClickEvent.getClickedComponent() instanceof TextArea){
                TextArea clickedComponent = (TextArea) layoutClickEvent.getClickedComponent();
                TradeOffer currentTrade = trades.get(Integer.parseInt(clickedComponent.getId()));

                TextArea windowText = new TextArea("open mid");
                VerticalLayout windowContent = new VerticalLayout();
                Window window = new Window();

                windowText.setSizeFull();
                windowText.setValue(currentTrade.getBuyInGameMessage());
                windowText.setReadOnly(true);
                windowText.focus();
                windowText.addBlurListener(blurEvent -> window.close());

                windowContent.setWidth("800px");
                windowContent.setHeight("86px");
                windowContent.addComponent(windowText);
                windowContent.addLayoutClickListener((LayoutEvents.LayoutClickListener) clickEvent -> {
                    if (clickEvent.getClickedComponent() instanceof TextArea) {
                        TextArea textArea = (TextArea) clickEvent.getClickedComponent();
                        textArea.setSelection(0, textArea.getValue().length());
                    }
                });

                window.setContent(windowContent);
                window.setModal(true);
                window.setResizable(false);
                window.setClosable(false);
                window.center();
                this.getUI().addWindow(window);
            }
        });

        if (trades.size() > 0)
            tradeDisplayLayout.setContent(tradeDisplayList);
        else if (tradeDisplayLayout.getContent() != null)
            tradeDisplayLayout.setContent(new Label(""));
    }

    private static Component findComponentById(HasComponents root, String id) {
        for (Component child : root) {
            if (id.equals(child.getId())) {
                return child;
            } else if (child instanceof HasComponents) {
                Component result = findComponentById((HasComponents) child, id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
