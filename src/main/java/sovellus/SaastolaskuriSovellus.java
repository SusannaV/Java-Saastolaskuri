package sovellus;

import java.util.HashMap;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaastolaskuriSovellus extends Application {

    @Override
    public void start(Stage ikkuna) {
        BorderPane alusta = new BorderPane();
        VBox laatikko = new VBox();
        BorderPane ylaykkonen = new BorderPane();
        BorderPane ylakakkonen = new BorderPane();

        //Viivakaavion pohja
        NumberAxis xAkseli = new NumberAxis(0, 30, 1);
        NumberAxis yAkseli = new NumberAxis();
        LineChart<Number, Number> viivakaavio = new LineChart<>(xAkseli, yAkseli);
        viivakaavio.setTitle("Säästölaskuri");

        XYChart.Series data = new XYChart.Series();
        data.setName("Talletus");

        XYChart.Series korkodata = new XYChart.Series();
        korkodata.setName("Korko");
        //yläosan eka BorderPane

        ylaykkonen.setLeft(new Label("Kuukausittainen tallennus"));

        Slider slider = new Slider();
        slider.setMin(25);
        slider.setMax(250);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(500);
        ylaykkonen.setCenter(slider);
        Label summaArvo = new Label(Double.toString(slider.getValue()));
        ylaykkonen.setRight(summaArvo);

        //yläosan toka BorderPane
        ylakakkonen.setLeft(new Label("Vuosittainen korko"));
        Slider korkoslider = new Slider();
        korkoslider.setMin(0);
        korkoslider.setMax(10);
        korkoslider.setValue(0);
        korkoslider.setShowTickLabels(true);
        korkoslider.setShowTickMarks(true);
        korkoslider.setBlockIncrement(500);
        ylakakkonen.setCenter(korkoslider);
        Label korkoArvo = new Label(Double.toString(korkoslider.getValue()));
        ylakakkonen.setRight(korkoArvo);

        //talletuskäyrä
        for (int i = 0; i <= 30; i++) {
            double saasto = slider.getValue() * i * 12;
            data.getData().add(new XYChart.Data(i, saasto));
        }

        //korkokäyrä
        for (int i = 0; i <= 30; i++) {
            
            double korko = 0;
            if (korkoslider.getValue() > 0) {
                korko = (slider.getValue() * i * 12) * (korkoslider.getValue() / 100 + 1);
            }

            korkodata.getData().add(new XYChart.Data(i, korko));
        }

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                summaArvo.setText(String.format("%.2f", new_val));
                data.getData().clear();

                for (int i = 0; i <= 30; i++) {
                    double saasto = slider.getValue() * i * 12;
                    data.getData().add(new XYChart.Data(i, saasto));
                }
                
                
                korkodata.getData().clear();
                               
                double vuositalletus = slider.getValue() * 12;
                double korkoprosentti = 0;
                if (korkoslider.getValue() > 0) {
                    korkoprosentti = korkoslider.getValue() / 100 + 1;
                }
                double summa = 0;
                for (int i = 0; i <= 30; i++) {
                    if (i==0){
                        korkodata.getData().add(new XYChart.Data(i, summa));
                        continue;
                    }
                    summa = (summa + vuositalletus) * korkoprosentti;
                    korkodata.getData().add(new XYChart.Data(i, summa));
                }
            }

        });

        korkoslider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                korkoArvo.setText(String.format("%.2f", new_val));
                korkodata.getData().clear();
                data.getData().clear();
                for (int i = 0; i <= 30; i++) {
                    double saasto = slider.getValue() * i * 12;
                    data.getData().add(new XYChart.Data(i, saasto));
                }
                korkodata.getData().clear();
                               
                double vuositalletus = slider.getValue() * 12;
                double korkoprosentti = 0;
                if (korkoslider.getValue() > 0) {
                    korkoprosentti = korkoslider.getValue() / 100 + 1;
                }
                double summa = 0;
                for (int i = 0; i <= 30; i++) {
                    if (i==0){
                        korkodata.getData().add(new XYChart.Data(i, summa));
                        continue;
                    }
                    summa = (summa + vuositalletus) * korkoprosentti;
                    korkodata.getData().add(new XYChart.Data(i, summa));
                }
            }

        });

        viivakaavio.getData().add(data);
        viivakaavio.getData().add(korkodata);

        laatikko.getChildren().addAll(ylaykkonen, ylakakkonen);

        //alustan luonti
        alusta.setCenter(viivakaavio);
        alusta.setTop(laatikko);

        Scene nakyma = new Scene(alusta, 640, 480);
        ikkuna.setScene(nakyma);
        ikkuna.show();

    }

    public static void main(String[] args) {
        launch(SaastolaskuriSovellus.class);
    }

}
