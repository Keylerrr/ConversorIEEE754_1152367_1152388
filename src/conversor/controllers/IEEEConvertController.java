package conversor.controllers;

import conversor.model.Conversion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class IEEEConvertController {

    @FXML
    private Button btnConvertirDecimal;

    @FXML
    private Button btnConvertirIEEE;

    @FXML
    private ChoiceBox<String> choicePrecision;

    @FXML
    private TextField txtExponente;

    @FXML
    private TextField txtMantisa;

    @FXML
    private TextField txtNumeroDecimal;

    @FXML
    private TextField txtNumeroIEEE;

    @FXML
    private TextField txtRepresentacion;

    @FXML
    private TextField txtResultadoDecimal;

    @FXML
    private TextField txtSigno;
    
    private Conversion conversion = new Conversion();

    @FXML
    public void initialize() {
    	ObservableList<String> precisiones = FXCollections.observableArrayList("Sencilla", "Doble");
        choicePrecision.setItems(precisiones);
        choicePrecision.setValue("Precisión");
    }
    
    @FXML
    void handleConvertirIEEEBtn(ActionEvent event) {
    	
    	String input = txtNumeroDecimal.getText().trim();
    	String precision = choicePrecision.getValue();
    	
    	if (input.isEmpty() || precision == null || precision == "Precisión") {
            mostrarAlerta("Error", "Todos los campos deben estar llenos.");
            return;
        }

        if (!esDouble(input)) {
            mostrarAlerta("Entrada inválida", "El número ingresado no es válido.");
            return;
        }
        
        double valor = Double.parseDouble(txtNumeroDecimal.getText().trim());
        
        txtRepresentacion.setText(conversion.convIEEE754(valor, precision));
        txtSigno.setText(conversion.getSigno());
        txtExponente.setText(conversion.getExponente_IEEE());
        txtMantisa.setText(conversion.getMantisa());
    }

    @FXML
    void handleConvertirDecimalBtn(ActionEvent event) {
    	
    	String input = txtNumeroIEEE.getText().trim();
    	
    	if (input.isEmpty()) {
            mostrarAlerta("Error", "Primero debe ingresar el número.");
            return;
        }

        if (!esBinario(input)) {
            mostrarAlerta("Entrada inválida", "El número ingresado no es válido.");
            return;
        }
        
        String result;
        
        try {
        	result = String.valueOf(conversion.convDecimal(input));
        } catch (Exception e) {
        	mostrarAlerta("Entrada inválida", e.getMessage());
        	return;
        }
        
        double numero = conversion.getNumero_decimal();
        int exponente = conversion.getExponente_decimal();
        
        if(numero == 0 && exponente == 0) {
        	txtResultadoDecimal.setText(result);
        }
        else {
        	txtResultadoDecimal.setText(String.valueOf(numero) + " x 2^" + String.valueOf(exponente) + " = " + result);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private boolean esDouble(String num) {
        try {
            Double.parseDouble(num.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean esBinario(String entrada) {
        return entrada.matches("[01]+");
    }

}
