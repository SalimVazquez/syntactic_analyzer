package Controllers;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Controller {

    private static Stack<String> pila;
    private static Pattern p;
    private static Matcher m;
    private static String inputAux;
    private static String[] arrays;
    private static int contador = 0;
    private static String auxPrint;
    private static boolean aux = false;

    @FXML
    private TextArea cadena;

    @FXML
    private Button btn_start;

    @FXML
    private TextArea process;

    @FXML
    private Button reset;

    @FXML
    private Button btn_out;

    @FXML
    void initialize() {
        pila = new Stack<>();
    }

    @FXML
    void OnMouseClickedExit() {
        System.exit(1);
    }

    @FXML
    void OnMouseClickedHiperLink() {
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("https://github.com/SalimVazquez"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnMouseClickedRestart() {
        process.setText("");
        for (int i = 0; i < pila.size(); i++) {
            pila.pop();
        }
    }

    @FXML
    void OnMouseClickedStart() throws ArrayIndexOutOfBoundsException {
        pila.add("I");
        inputAux = cadena.getText();
        arrays = inputAux.split(" ");
        contador = 0;
        addParentRules();
        if (pila.peek().equals(arrays[contador])) {
            auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
            preparatePrint();
            contador++;
            addConditionRules();
            addPrintRules();
            contador++;
            addRestoRules();
            addConditionRules();
            addPrintRules();
            contador++;
            addElseRule();
            addPrintRules();
            contador++;
            addElseRule();
            addPrintRules();
            if (pila.isEmpty())
                showMessageSuccessfully();
        } else
            showMessageError(arrays[contador], pila.peek());
        write();
    }

    void addParentRules() {
        for (int i = 0; i < pila.size(); i++) {
            auxPrint = "[" + pila.get(i) + "]\n";
        }
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        pila.push("impresion");
        pila.push("else:");
        pila.push("R");
        pila.push("impresion");
        pila.push("condition:");
        pila.push("if");
        preparatePrint();
    }

    void addConditionRules() {
        try {
            p = Pattern.compile("[a-zA-z]+(!=|==|<|<=|>|>=)[a-zA-z]+:*");
            m = p.matcher(arrays[contador]);
            if (m.find()) {
                if (aux == false) {
                    auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                    preparatePrint();
                    aux = true;
                }
                addRulesConditionVar();
                contador++;
                p = Pattern.compile("(and|or)");
                m = p.matcher(arrays[contador]);
                if (m.find()) {
                    contador++;
                    addConditionRules();
                }
            } else {
                p = Pattern.compile("true:*");
                m = p.matcher(arrays[contador]);
                if (m.find()) {
                    if (aux == false) {
                        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                        preparatePrint();
                        aux = true;
                    }
                    addRulesConditionTrue();
                    contador++;
                    p = Pattern.compile("(and|or)");
                    m = p.matcher(arrays[contador]);
                    if (m.find()) {
                        contador++;
                        addConditionRules();
                    }
                } else
                    showMessageError(arrays[contador], pila.peek());
            }
        } catch (Exception arrayIndexOutOfBoundsException) {
            showMessageError(arrays[contador - 1], pila.peek());
        }
    }

    void addPrintRules() {
        try {
            String[] arrayAux = arrays[contador].split("'");
            p = Pattern.compile("print\\(");
            m = p.matcher(arrayAux[0]);
            if (m.find()) {
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                preparatePrint();
                pila.push("alfabeto");
                preparatePrint();
                checkAlfabeto(arrayAux[1]);
            } else
                showMessageError(arrayAux[0], pila.peek());
        } catch (Exception arrayIndexOutOfBoundsException) {
            showMessageError(arrays[contador - 1], pila.peek());
        }
    }

    void addRestoRules() {
        try {
            p = Pattern.compile("if");
            m = p.matcher(arrays[contador]);
            if (m.find()) {
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                pila.push("impresion");
                pila.push("else:");
                pila.push("impresion");
                pila.push("condition:");
                pila.push("if");
                preparatePrint();
                contador++;
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                preparatePrint();
            } else
                showMessageError(arrays[contador], pila.peek());
        } catch (Exception arrayIndexOutOfBoundsException) {
            showMessageError(arrays[contador - 1], pila.peek());
        }
    }

    void addElseRule() {
        try {
            p = Pattern.compile("else:");
            m = p.matcher(arrays[contador]);
            if (m.find()) {
                contador++;
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                preparatePrint();
            }
        } catch (Exception arrayIndexOutOfBoundsException) {
            showMessageError(arrays[contador - 1], pila.peek());
        }
    }

    void addRulesConditionVar() {
        pila.push("Rcondition");
        pila.push("var");
        pila.push("operator");
        pila.push("var");
        preparatePrint();
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        preparatePrint();
    }

    void addRulesConditionTrue() {
        pila.push("Rcondition");
        pila.push("true");
        preparatePrint();
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        preparatePrint();
    }

    void checkAlfabeto(String array) {
        p = Pattern.compile("\\p{Alpha}+");
        m = p.matcher(array);
        Pattern p2 = Pattern.compile("\\p{Digit}+");
        Matcher m2 = p2.matcher(array);
        Pattern p3 = Pattern.compile("\\p{Punct}+");
        Matcher m3 = p3.matcher(array);
        if (m.find())
            checkLetters(array);
        else if (m2.find())
            checkDigits(array);
        else if (m3.find())
            checkSimbols(array);
        else
            showMessageError(array, pila.peek());
    }

    void checkLetters(String array) {
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        p = Pattern.compile("\\p{Alpha}");
        for (int i = 0; i < array.length(); i++) {
            m = p.matcher(String.valueOf(array.charAt(i)));
            if (m.find()) {
                pila.push("Rletra");
                pila.push("letra");
                preparatePrint();
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
            }
        }
        preparatePrint();
    }

    void checkDigits(String array) {
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        p = Pattern.compile("\\p{Digit}");
        for (int i = 0; i < array.length(); i++) {
            m = p.matcher(String.valueOf(array.charAt(i)));
            if (m.find()) {
                pila.push("Rnumero");
                pila.push("numero");
                preparatePrint();
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
            }
        }
        preparatePrint();
    }

    void checkSimbols(String array) {
        auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
        p = Pattern.compile("\\p{Punct}");
        for (int i = 0; i < array.length(); i++) {
            m = p.matcher(String.valueOf(array.charAt(i)));
            if (m.find()) {
                pila.push("Rcaracter");
                pila.push("caracter");
                preparatePrint();
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
                auxPrint = auxPrint + "Sale: " + pila.pop() + "\n";
            }
        }
        preparatePrint();
    }

    void preparatePrint() {
        auxPrint = auxPrint + "[";
        for (int i = 0; i < pila.size(); i++) {
            if (i == pila.size() - 1)
                auxPrint = auxPrint + pila.get(i);
            else
                auxPrint = auxPrint + pila.get(i) + ", ";
        }
        auxPrint = auxPrint + "]\n";
    }

    void write() {
        process.setFont(Font.font("Book antiqua", 13));
        process.setText(auxPrint);
    }

    void showMessageError(String data, String expectedString) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Error sintactico");
        alert.setResizable(true);
        alert.setContentText("Detectamos el error en " + data + "\nCuando se esperaba: " + expectedString);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    void showMessageSuccessfully() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("La cadena es valida");
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}