package com.example.demo;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HelloController {

    // Login & Main Layout Controls
    @FXML private VBox roleSelectionView;
    @FXML private VBox loginFormView;
    @FXML private VBox signUpFormView;
    @FXML private Label loginRoleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField; // For showing password
    @FXML private Button togglePasswordButton; // The eye icon button
    @FXML private Label loginStatusLabel;
    @FXML private Hyperlink signUpLink;
    
    // Sign Up Controls
    @FXML private Label signUpRoleLabel;
    @FXML private TextField signUpNameField;
    @FXML private TextField signUpUsernameField;
    @FXML private PasswordField signUpPasswordField;
    @FXML private TextField signUpPasswordTextField;
    @FXML private Button toggleSignUpPasswordButton;
    @FXML private PasswordField signUpConfirmPasswordField; // New field
    @FXML private TextField signUpSpecField;
    @FXML private Label signUpSpecLabel;
    @FXML private Label signUpStatusLabel;
    @FXML private DatePicker signUpDatePicker; // For manual date selection
    @FXML private VBox passwordValidationVBox;
    @FXML private Label lengthRuleLabel;
    @FXML private Label letterRuleLabel;
    @FXML private Label digitRuleLabel;
    @FXML private Label specialCharRuleLabel;
    @FXML private Button signUpSubmitButton;
    
    // Doctor Specific Controls
    @FXML private Label signUpDaysLabel;
    @FXML private CheckComboBox<String> signUpDaysPicker; // For selecting multiple days
    @FXML private Label signUpAvailableTimeLabel;
    @FXML private ComboBox<String> signUpAvailableTimePicker; // Changed to ComboBox
    
    // Hidden but required for compilation
    @FXML private TextField signUpHoursField;
    @FXML private TextField signUpTimeField;
    @FXML private Label signUpHoursLabel;
    @FXML private Label signUpTimeLabel;
    
    @FXML private BorderPane mainAppView;
    @FXML private Label currentUserLabel;
    @FXML private TabPane mainTabPane;
    @FXML private Tab adminTab;
    @FXML private Tab reportsTab;
    @FXML private Tab receptionTab;
    @FXML private Tab examinerTab;
    @FXML private Tab doctorTab;
    @FXML private Tab laboratoryTab;
    @FXML private Tab pharmacyTab; // New Tab for Pharmacy

    // Admin Controls
    @FXML private Label adminStatusLabel;
    @FXML private ListView<Doctor> adminDoctorsListView;
    @FXML private ListView<Examiner> adminExaminersListView;
    @FXML private ListView<Laboratory> adminLaboratoriesListView;
    @FXML private ListView<Pharmacy> adminPharmaciesListView;
    
    // Reports Controls
    @FXML private Label totalPatientsLabel;
    @FXML private Label treatedPatientsLabel;
    @FXML private TextArea doctorWorkloadArea;

    // Reception Controls
    @FXML private TextField firstNameField;
    @FXML private TextField secondNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField patientAgeField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField addressField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField patientIdField;
    @FXML private TextArea foundPatientDetailsArea;
    @FXML private Button sendToExaminerButton;
    @FXML private Label receptionStatusLabel;
    @FXML private ListView<Patient> registeredPatientsListView;

    // Examiner Controls
    @FXML private ComboBox<Patient> examinerPatientComboBox;
    @FXML private TextField weightField;
    @FXML private TextField bpField;
    @FXML private TextField temperatureField;
    @FXML private TextArea medicalHistoryField; // Changed from TextField to TextArea
    @FXML private TextArea painDescriptionField; // Changed from TextField to TextArea
    @FXML private ComboBox<Doctor> doctorComboBox;
    @FXML private Button submitVitalsButton;
    @FXML private Label examinerStatusLabel;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private TextField appointmentTimeField;
    @FXML private ComboBox<Doctor> appointmentDoctorComboBox;
    @FXML private Button createAppointmentButton;

    // Doctor Controls
    @FXML private Label doctorInfoLabel;
    @FXML private ListView<Patient> doctorPatientsListView;
    @FXML private TextArea patientDetailsArea;
    @FXML private TextArea prescriptionArea; // New field for prescription
    @FXML private ComboBox<Doctor> transferDoctorComboBox;
    @FXML private Label doctorStatusLabel;
    @FXML private TreeTableView<LabTest> labTestTreeTableView;
    @FXML private TreeTableColumn<LabTest, String> testNameTreeTableColumn;
    @FXML private TreeTableColumn<LabTest, Boolean> selectTestTreeTableColumn;
    @FXML private TextArea transferReasonArea;
    
    // Laboratory Controls
    @FXML private ListView<Patient> laboratoryPatientsListView;
    @FXML private TextField labResultsField;
    @FXML private Label labStatusLabel;
    @FXML private TextArea labTestsArea;
    
    // Pharmacy Controls
    @FXML private ListView<Patient> pharmacyPatientsListView;
    @FXML private TextArea pharmacyPrescriptionArea;
    @FXML private Label pharmacyStatusLabel;

    // Global Search
    @FXML private TextField globalSearchField;

    private ClinicManager clinicManager;
    private ObservableList<Patient> registeredPatientsList;
    private ObservableList<Doctor> doctorsList;
    private ObservableList<Examiner> examinersList;
    private ObservableList<Laboratory> laboratoriesList;
    private ObservableList<Pharmacy> pharmaciesList;
    private ObservableList<Patient> laboratoryPatientsList;
    private ObservableList<Patient> pharmacyPatientsList; // List for pharmacy patients
    
    private String currentRole;
    private Doctor loggedInDoctor;
    private Patient foundPatient;

    public void initialize() {
        System.out.println("Initializing HelloController...");
        clinicManager = new ClinicManager();
        registeredPatientsList = FXCollections.observableArrayList(clinicManager.getRegisteredPatients());
        doctorsList = FXCollections.observableArrayList(clinicManager.getDoctors());
        examinersList = FXCollections.observableArrayList(clinicManager.getExaminers());
        laboratoriesList = FXCollections.observableArrayList(clinicManager.getLaboratories());
        pharmaciesList = FXCollections.observableArrayList(clinicManager.getPharmacies());
        laboratoryPatientsList = FXCollections.observableArrayList();
        pharmacyPatientsList = FXCollections.observableArrayList();

        // Bind lists to UI
        registeredPatientsListView.setItems(registeredPatientsList);
        examinerPatientComboBox.setItems(registeredPatientsList);
        laboratoryPatientsListView.setItems(laboratoryPatientsList);
        pharmacyPatientsListView.setItems(pharmacyPatientsList);
        
        // Populate Gender ComboBox
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        
        // Populate Doctor Registration Pickers
        if (signUpDaysPicker != null) {
            signUpDaysPicker.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        }
        
        if (signUpAvailableTimePicker != null) {
            signUpAvailableTimePicker.getItems().addAll("Full Day", "Morning", "Afternoon");
        }
        
        // Only show active doctors in assignment dropdowns
        updateActiveDoctorsList();
        
        adminDoctorsListView.setItems(doctorsList);
        adminExaminersListView.setItems(examinersList);
        adminLaboratoriesListView.setItems(laboratoriesList);
        adminPharmaciesListView.setItems(pharmaciesList);

        // Setup ListViews to show names
        setupPatientListView(registeredPatientsListView);
        setupPatientListView(doctorPatientsListView);
        setupPatientListView(laboratoryPatientsListView);
        setupPatientListView(pharmacyPatientsListView);
        setupDoctorListView(adminDoctorsListView);
        setupExaminerListView(adminExaminersListView);
        setupLaboratoryListView(adminLaboratoriesListView);
        setupPharmacyListView(adminPharmaciesListView);
        setupLabTestTreeTableView();

        // Add listener for patient selection in doctor view to show details
        doctorPatientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showPatientDetails(newValue);
            } else {
                patientDetailsArea.clear();
            }
        });
        
        // Add listener for patient selection in pharmacy view to show prescription
        pharmacyPatientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                pharmacyPrescriptionArea.setText(newValue.getPrescription());
            } else {
                pharmacyPrescriptionArea.clear();
            }
        });
        
        // Add listener for patient selection in laboratory view to show ordered tests
        laboratoryPatientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                labTestsArea.setText(String.join("\n", newValue.getOrderedLabTests()));
            } else {
                labTestsArea.clear();
            }
        });
        
        // Bind submit button's disable property
        setupExaminerFormValidation();
        
        // Apply automatic validation to form fields
        applyTextFormatter(firstNameField, "[a-zA-Z]*");
        applyTextFormatter(secondNameField, "[a-zA-Z]*");
        applyTextFormatter(lastNameField, "[a-zA-Z]*");
        applyAgeValidation(patientAgeField);
        applyTextFormatter(addressField, "[a-zA-Z\\s]*");
        applyPhoneNumberValidation(phoneNumberField);
        applyPhoneNumberValidation(patientIdField); // Apply to existing patient search field as well
        
        // Apply validation to Examiner Dashboard fields
        applyNumericValidation(weightField);
        applyNumericValidation(temperatureField);
        applyBloodPressureValidation(bpField);
        applyTimeValidation(appointmentTimeField);
        applyTextFormatter(signUpNameField, "[a-zA-Z\\s]*");
        applyTextFormatter(signUpUsernameField, "[a-zA-Z0-9]*");
        setupPasswordValidation();
    }
    
    private void applyTextFormatter(TextField textField, String regex) {
        Pattern pattern = Pattern.compile(regex);
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        textField.setTextFormatter(formatter);
    }
    
    private void applyAgeValidation(TextField ageField) {
        Pattern pattern = Pattern.compile("[0-9]*");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                if (newText.isEmpty()) {
                    return change;
                }
                try {
                    int age = Integer.parseInt(newText);
                    if (age >= 0 && age <= 200) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    // Should not happen due to regex, but as a safeguard
                    return null;
                }
            }
            return null;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        ageField.setTextFormatter(formatter);
    }
    
    private void applyPhoneNumberValidation(TextField phoneField) {
        phoneField.setText("+251");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (!newText.startsWith("+251")) {
                return null; // Reject change if prefix is deleted
            }

            if (newText.length() > 13) {
                return null; // Reject change if length exceeds max
            }

            if (newText.length() > 4) {
                String userInput = newText.substring(4);
                if (!userInput.startsWith("9")) {
                    return null; // Must start with 9 after prefix
                }
                if (!userInput.matches("9[0-9]*")) {
                    return null; // The rest must be digits
                }
            }

            return change; // Accept the change
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        phoneField.setTextFormatter(formatter);

        phoneField.setOnMouseClicked(event -> {
            if (phoneField.getText().equals("+251")) {
                phoneField.positionCaret(phoneField.getText().length());
            }
        });
    }
    
    private void applyNumericValidation(TextField textField) {
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void applyBloodPressureValidation(TextField textField) {
        Pattern pattern = Pattern.compile("[0-9]{1,3}/[0-9]{1,3}");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*/?[0-9]*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void applyTimeValidation(TextField textField) {
        Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([01]?[0-9]|2[0-3])?(:[0-5]?[0-9]?)?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
    
    private void setupExaminerFormValidation() {
        submitVitalsButton.disableProperty().bind(
            Bindings.isNull(examinerPatientComboBox.valueProperty())
            .or(weightField.textProperty().isEmpty())
            .or(bpField.textProperty().isEmpty())
            .or(temperatureField.textProperty().isEmpty())
            .or(medicalHistoryField.textProperty().isEmpty())
            .or(painDescriptionField.textProperty().isEmpty())
            .or(Bindings.isNull(doctorComboBox.valueProperty()))
        );
        
        createAppointmentButton.disableProperty().bind(
            Bindings.isNull(examinerPatientComboBox.valueProperty())
            .or(Bindings.isNull(appointmentDatePicker.valueProperty()))
            .or(appointmentTimeField.textProperty().isEmpty())
            .or(Bindings.isNull(appointmentDoctorComboBox.valueProperty()))
        );
    }

    private void setupPasswordValidation() {
        signUpPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean hasLetter = newValue.matches(".*[a-zA-Z].*");
            boolean hasDigit = newValue.matches(".*[0-9].*");
            boolean hasSpecialChar = newValue.matches(".*[^a-zA-Z0-9].*");
            boolean isLongEnough = newValue.length() >= 8;

            updateValidationLabel(lengthRuleLabel, "At least 8 characters", isLongEnough);
            updateValidationLabel(letterRuleLabel, "At least one letter", hasLetter);
            updateValidationLabel(digitRuleLabel, "At least one digit", hasDigit);
            updateValidationLabel(specialCharRuleLabel, "At least one special character", hasSpecialChar);

            signUpSubmitButton.setDisable(!(hasLetter && hasDigit && hasSpecialChar && isLongEnough && signUpPasswordField.getText().equals(signUpConfirmPasswordField.getText())));
        });
        
        signUpConfirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            signUpSubmitButton.setDisable(!signUpPasswordField.getText().equals(newValue));
        });
    }

    private void updateValidationLabel(Label label, String ruleText, boolean isValid) {
        if (isValid) {
            label.setText("✓ " + ruleText);
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setText("✗ " + ruleText);
            label.setStyle("-fx-text-fill: red;");
        }
    }
    
    @FXML
    private void toggleSignUpPasswordVisibility() {
        if (signUpPasswordField.isVisible()) {
            signUpPasswordTextField.setText(signUpPasswordField.getText());
            signUpPasswordTextField.setVisible(true);
            signUpPasswordField.setVisible(false);
            toggleSignUpPasswordButton.setText("😉");
        } else {
            signUpPasswordField.setText(signUpPasswordTextField.getText());
            signUpPasswordField.setVisible(true);
            signUpPasswordTextField.setVisible(false);
            toggleSignUpPasswordButton.setText("👁");
        }
    }
    
    private void updateActiveDoctorsList() {
        ObservableList<Doctor> activeDoctors = FXCollections.observableArrayList(clinicManager.getActiveDoctors());
        doctorComboBox.setItems(activeDoctors);
        transferDoctorComboBox.setItems(activeDoctors);
        appointmentDoctorComboBox.setItems(activeDoctors);
    }

    private void setupPatientListView(ListView<Patient> listView) {
        listView.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (listView == laboratoryPatientsListView) {
                        setText(item.getFullName() + " - Tests: " + String.join(", ", item.getOrderedLabTests()));
                    } else {
                        setText(item.getFullName() + " (Card No: " + item.getId() + ")");
                    }
                }
            }
        });
    }

    private void setupDoctorListView(ListView<Doctor> listView) {
        listView.setCellFactory(param -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset style for empty cells
                } else {
                    String status = item.isActive() ? "[ACTIVE]" : "[PENDING APPROVAL]";
                    setText(item.getName() + " - " + item.getSpecialty() + " " + status);
                    
                    // Set style based on active status. Red for pending, black for active.
                    if (!item.isActive()) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }
    
    private void setupExaminerListView(ListView<Examiner> listView) {
        listView.setCellFactory(param -> new ListCell<Examiner>() {
            @Override
            protected void updateItem(Examiner item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset style for empty cells
                } else {
                    String status = item.isActive() ? "[ACTIVE]" : "[PENDING APPROVAL]";
                    setText(item.getName() + " " + status);
                    
                    // Set style based on active status. Red for pending, black for active.
                    if (!item.isActive()) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void setupLaboratoryListView(ListView<Laboratory> listView) {
        listView.setCellFactory(param -> new ListCell<Laboratory>() {
            @Override
            protected void updateItem(Laboratory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset style for empty cells
                } else {
                    String status = item.isActive() ? "[ACTIVE]" : "[PENDING APPROVAL]";
                    setText(item.getName() + " " + status);
                    
                    // Set style based on active status. Red for pending, black for active.
                    if (!item.isActive()) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void setupPharmacyListView(ListView<Pharmacy> listView) {
        listView.setCellFactory(param -> new ListCell<Pharmacy>() {
            @Override
            protected void updateItem(Pharmacy item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset style for empty cells
                } else {
                    String status = item.isActive() ? "[ACTIVE]" : "[PENDING APPROVAL]";
                    setText(item.getName() + " " + status);
                    
                    // Set style based on active status. Red for pending, black for active.
                    if (!item.isActive()) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void setupLabTestTreeTableView() {
        testNameTreeTableColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<LabTest, String> param) -> param.getValue().getValue().testNameProperty()
        );
    
        selectTestTreeTableColumn.setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<LabTest, Boolean> param) -> param.getValue().getValue().selectedProperty()
        );
    
        selectTestTreeTableColumn.setCellFactory(column -> new CheckBoxTreeTableCell<LabTest, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TreeItem<LabTest> treeItem = getTreeTableRow().getTreeItem();
                    if (treeItem != null && treeItem.isLeaf()) {
                        setGraphic(getGraphic());
                        setEditable(true);
                    } else {
                        setGraphic(null);
                        setEditable(false);
                    }
                }
            }
        });
    
        TreeItem<LabTest> root = new TreeItem<>(new LabTest("All Tests"));
        labTestTreeTableView.setRoot(root);
        labTestTreeTableView.setShowRoot(false);
        labTestTreeTableView.setEditable(true);
    
        Map<String, List<String>> categorizedTests = clinicManager.getCategorizedLabTests();
        for (String category : categorizedTests.keySet()) {
            TreeItem<LabTest> categoryItem = new TreeItem<>(new LabTest(category));
            root.getChildren().add(categoryItem);
            for (String testName : categorizedTests.get(category)) {
                categoryItem.getChildren().add(new TreeItem<>(new LabTest(testName)));
            }
        }
    }

    private void showPatientDetails(Patient patient) {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(patient.getFullName()).append("\n");
        details.append("Card No: ").append(patient.getId()).append("\n");
        details.append("Age: ").append(patient.getAge()).append("\n");
        details.append("Gender: ").append(patient.getGender()).append("\n");
        details.append("Address: ").append(patient.getAddress()).append("\n");
        details.append("Phone: ").append(patient.getPhoneNumber()).append("\n\n");
        details.append("--- Medical Vitals ---\n");
        details.append("Weight: ").append(patient.getWeight()).append(" kg\n");
        details.append("Blood Pressure: ").append(patient.getBloodPressure()).append("\n");
        details.append("Temperature: ").append(patient.getTemperature()).append(" °C\n");
        details.append("Medical History: ").append(patient.getMedicalHistory()).append("\n");
        details.append("Pain Description: ").append(patient.getPainDescription()).append("\n");
        
        if (patient.getLabResults() != null) {
            details.append("\n--- Lab Results ---\n");
            details.append(patient.getLabResults()).append("\n");
        }
        
        patientDetailsArea.setText(details.toString());
    }

    // Login Selection Actions
    @FXML
    public void onSelectAdminRole() {
        System.out.println("Admin role selected");
        showLoginForm("Administrator");
    }

    @FXML
    public void onSelectReceptionRole() {
        System.out.println("Reception role selected");
        showLoginForm("Receptionist");
    }

    @FXML
    public void onSelectExaminerRole() {
        System.out.println("Examiner role selected");
        showLoginForm("Examiner");
    }

    @FXML
    public void onSelectDoctorRole() {
        System.out.println("Doctor role selected");
        showLoginForm("Doctor");
    }
    
    @FXML
    public void onSelectLaboratoryRole() {
        System.out.println("Laboratory role selected");
        showLoginForm("Laboratory");
    }
    
    @FXML
    public void onSelectPharmacyRole() {
        System.out.println("Pharmacy role selected");
        showLoginForm("Pharmacy");
    }
    
    private void showLoginForm(String role) {
        currentRole = role;
        loginRoleLabel.setText("Login as " + role);
        roleSelectionView.setVisible(false);
        loginFormView.setVisible(true);
        loginStatusLabel.setText("");
        usernameField.clear();
        passwordField.clear();
        
        // Show Sign Up link only for Doctor and Examiner
        signUpLink.setVisible("Doctor".equals(role) || "Examiner".equals(role) || "Laboratory".equals(role) || "Pharmacy".equals(role));
    }
    
    @FXML
    public void onBackToHome() {
        loginFormView.setVisible(false);
        roleSelectionView.setVisible(true);
        currentRole = null;
    }
    
    // Sign Up Actions
    @FXML
    public void onSignUpClick() {
        loginFormView.setVisible(false);
        signUpFormView.setVisible(true);
        signUpRoleLabel.setText("Register as " + currentRole);
        signUpStatusLabel.setText("");
        
        // Clear fields
        signUpNameField.clear();
        signUpUsernameField.clear();
        signUpPasswordField.clear();
        signUpConfirmPasswordField.clear();
        signUpSpecField.clear();
        
        // Set current date and time
        signUpDatePicker.setValue(LocalDate.now());
        
        // Show/Hide doctor specific fields
        boolean isDoctor = "Doctor".equals(currentRole);
        signUpSpecField.setVisible(isDoctor);
        signUpSpecLabel.setVisible(isDoctor);
        
        if (signUpDaysPicker != null) {
            signUpDaysPicker.setVisible(isDoctor);
            signUpDaysLabel.setVisible(isDoctor);
            signUpDaysPicker.getCheckModel().clearChecks();
        }
        
        if (signUpAvailableTimePicker != null) {
            signUpAvailableTimePicker.setVisible(isDoctor);
            signUpAvailableTimeLabel.setVisible(isDoctor);
            signUpAvailableTimePicker.getSelectionModel().clearSelection();
        }
    }
    
    @FXML
    public void onSignUpCancel() {
        signUpFormView.setVisible(false);
        loginFormView.setVisible(true);
    }
    
    @FXML
    public void onSignUpSubmit() {
        String name = signUpNameField.getText();
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPassword = signUpConfirmPasswordField.getText();

        if (name.trim().length() <= 1) {
            signUpStatusLabel.setText("Full Name must be more than one character and contain only letters and spaces.");
            return;
        }
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signUpStatusLabel.setText("Please fill all required fields.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            signUpStatusLabel.setText("Passwords do not match.");
            return;
        }
        
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        if ("Doctor".equals(currentRole)) {
            String spec = signUpSpecField.getText();
            ObservableList<String> selectedDays = signUpDaysPicker.getCheckModel().getCheckedItems();
            String selectedTime = signUpAvailableTimePicker.getSelectionModel().getSelectedItem();
            
            if (spec.isEmpty()) {
                signUpStatusLabel.setText("Please fill all doctor details.");
                return;
            }
            
            if (selectedDays.isEmpty()) {
                signUpStatusLabel.setText("Please select at least one available day.");
                return;
            }
            
            if (selectedTime == null) {
                signUpStatusLabel.setText("Please select an available time option.");
                return;
            }
            
            List<String> days = new ArrayList<>(selectedDays);
            String timePerPatient = "30 mins"; // Default
            
            Doctor newDoctor = new Doctor(name, spec, days, selectedTime, timePerPatient, username, hashedPassword);
            clinicManager.addDoctor(newDoctor);
            doctorsList.add(newDoctor);
            
        } else if ("Examiner".equals(currentRole)) {
            Examiner newExaminer = new Examiner(name, username, hashedPassword);
            clinicManager.addExaminer(newExaminer);
            examinersList.add(newExaminer);
        } else if ("Laboratory".equals(currentRole)) {
            Laboratory newLab = new Laboratory(name, username, hashedPassword);
            clinicManager.addLaboratory(newLab);
            laboratoriesList.add(newLab);
        } else if ("Pharmacy".equals(currentRole)) {
            Pharmacy newPharmacy = new Pharmacy(name, username, hashedPassword);
            clinicManager.addPharmacy(newPharmacy);
            pharmaciesList.add(newPharmacy);
        }
        
        signUpStatusLabel.setText("Registration submitted! Pending approval.");
        // Optionally clear fields or redirect back to login after a delay
    }

    @FXML
    public void onLoginSubmit() {
        String username = usernameField.getText();
        String password = passwordTextField.isVisible() ? passwordTextField.getText() : passwordField.getText();
        
        boolean isAuthenticated = false;
        loggedInDoctor = null;

        if ("Administrator".equals(currentRole)) {
            isAuthenticated = clinicManager.authenticateAdmin(username, password);
            if (isAuthenticated) showDashboard(currentRole, adminTab, reportsTab);
        } else if ("Receptionist".equals(currentRole)) {
            isAuthenticated = clinicManager.authenticateReception(username, password);
            if (isAuthenticated) showDashboard(currentRole, receptionTab);
        } else if ("Examiner".equals(currentRole)) {
            Examiner examiner = clinicManager.authenticateExaminer(username, password);
            if (examiner != null) {
                isAuthenticated = true;
                showDashboard(currentRole, examinerTab);
            } else {
                // Check inactive
                for (Examiner e : clinicManager.getExaminers()) {
                    if (e.getUsername().equals(username) && e.getPassword().equals(password) && !e.isActive()) {
                        loginStatusLabel.setText("Account pending approval.");
                        return;
                    }
                }
            }
        } else if ("Doctor".equals(currentRole)) {
            Doctor doctor = clinicManager.authenticateDoctor(username, password);
            if (doctor != null) {
                isAuthenticated = true;
                loggedInDoctor = doctor;
                showDashboard(currentRole, doctorTab);
                updateDoctorView();
            } else {
                // Check inactive
                for (Doctor d : clinicManager.getDoctors()) {
                    if (d.getUsername().equals(username) && d.getPassword().equals(password) && !d.isActive()) {
                        loginStatusLabel.setText("Account pending approval.");
                        return;
                    }
                }
            }
        } else if ("Laboratory".equals(currentRole)) {
            Laboratory lab = clinicManager.authenticateLaboratory(username, password);
            if (lab != null) {
                isAuthenticated = true;
                showDashboard(currentRole, laboratoryTab);
            } else {
                // Check inactive
                for (Laboratory l : clinicManager.getLaboratories()) {
                    if (l.getUsername().equals(username) && l.getPassword().equals(password) && !l.isActive()) {
                        loginStatusLabel.setText("Account pending approval.");
                        return;
                    }
                }
            }
        } else if ("Pharmacy".equals(currentRole)) {
            Pharmacy pharm = clinicManager.authenticatePharmacy(username, password);
            if (pharm != null) {
                isAuthenticated = true;
                showDashboard(currentRole, pharmacyTab);
            } else {
                // Check inactive
                for (Pharmacy p : clinicManager.getPharmacies()) {
                    if (p.getUsername().equals(username) && p.getPassword().equals(password) && !p.isActive()) {
                        loginStatusLabel.setText("Account pending approval.");
                        return;
                    }
                }
            }
        }

        if (!isAuthenticated) {
            loginStatusLabel.setText("Invalid credentials.");
        }
    }

    private void showDashboard(String role, Tab... activeTabs) {
        roleSelectionView.setVisible(false);
        loginFormView.setVisible(false);
        mainAppView.setVisible(true);
        
        String userDisplay = role;
        if (loggedInDoctor != null) {
            userDisplay += " (" + loggedInDoctor.getName() + ")";
        }
        currentUserLabel.setText("Logged in as: " + userDisplay);
        
        // Show only the relevant tabs
        mainTabPane.getTabs().clear();
        mainTabPane.getTabs().addAll(activeTabs);
        
        if (role.equals("Administrator")) {
            onRefreshReportsClick();
        }
    }

    @FXML
    public void onLogout() {
        mainAppView.setVisible(false);
        roleSelectionView.setVisible(true);
        loginFormView.setVisible(false);
        currentUserLabel.setText("");
        loggedInDoctor = null;
        currentRole = null;
        
        // Reset tabs for next login
        mainTabPane.getTabs().clear();
        mainTabPane.getTabs().addAll(adminTab, reportsTab, receptionTab, examinerTab, doctorTab, laboratoryTab, pharmacyTab);
    }

    // Admin Actions
    @FXML
    public void onAdminApproveDoctorClick() {
        Doctor selectedDoctor = adminDoctorsListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null && !selectedDoctor.isActive()) {
            clinicManager.approveDoctor(selectedDoctor);
            adminStatusLabel.setText("Doctor " + selectedDoctor.getName() + " approved.");
            // The .refresh() call re-renders the list, and the cell factory will update the style.
            adminDoctorsListView.refresh();
            updateActiveDoctorsList();
        } else if (selectedDoctor != null) {
            adminStatusLabel.setText("Doctor is already active.");
        } else {
            adminStatusLabel.setText("Select a doctor to approve.");
        }
    }
    
    @FXML
    public void onAdminRemoveDoctorClick() {
        Doctor selectedDoctor = adminDoctorsListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            clinicManager.removeDoctor(selectedDoctor);
            doctorsList.remove(selectedDoctor);
            adminStatusLabel.setText("Doctor removed.");
            updateActiveDoctorsList();
        }
    }
    
    @FXML
    public void onAdminApproveExaminerClick() {
        Examiner selectedExaminer = adminExaminersListView.getSelectionModel().getSelectedItem();
        if (selectedExaminer != null && !selectedExaminer.isActive()) {
            clinicManager.approveExaminer(selectedExaminer);
            adminStatusLabel.setText("Examiner " + selectedExaminer.getName() + " approved.");
            // The .refresh() call re-renders the list, and the cell factory will update the style.
            adminExaminersListView.refresh();
        } else if (selectedExaminer != null) {
            adminStatusLabel.setText("Examiner is already active.");
        } else {
            adminStatusLabel.setText("Select an examiner to approve.");
        }
    }
    
    @FXML
    public void onAdminRemoveExaminerClick() {
        Examiner selectedExaminer = adminExaminersListView.getSelectionModel().getSelectedItem();
        if (selectedExaminer != null) {
            clinicManager.removeExaminer(selectedExaminer);
            examinersList.remove(selectedExaminer);
            adminStatusLabel.setText("Examiner removed.");
        }
    }

    @FXML
    public void onAdminApproveLaboratoryClick() {
        Laboratory selectedLab = adminLaboratoriesListView.getSelectionModel().getSelectedItem();
        if (selectedLab != null && !selectedLab.isActive()) {
            clinicManager.approveLaboratory(selectedLab);
            adminStatusLabel.setText("Laboratory staff " + selectedLab.getName() + " approved.");
            adminLaboratoriesListView.refresh();
        } else if (selectedLab != null) {
            adminStatusLabel.setText("Laboratory staff is already active.");
        } else {
            adminStatusLabel.setText("Select a laboratory staff to approve.");
        }
    }

    @FXML
    public void onAdminRemoveLaboratoryClick() {
        Laboratory selectedLab = adminLaboratoriesListView.getSelectionModel().getSelectedItem();
        if (selectedLab != null) {
            clinicManager.removeLaboratory(selectedLab);
            laboratoriesList.remove(selectedLab);
            adminStatusLabel.setText("Laboratory staff removed.");
        }
    }

    @FXML
    public void onAdminApprovePharmacyClick() {
        Pharmacy selectedPharmacy = adminPharmaciesListView.getSelectionModel().getSelectedItem();
        if (selectedPharmacy != null && !selectedPharmacy.isActive()) {
            clinicManager.approvePharmacy(selectedPharmacy);
            adminStatusLabel.setText("Pharmacy staff " + selectedPharmacy.getName() + " approved.");
            adminPharmaciesListView.refresh();
        } else if (selectedPharmacy != null) {
            adminStatusLabel.setText("Pharmacy staff is already active.");
        } else {
            adminStatusLabel.setText("Select a pharmacy staff to approve.");
        }
    }

    @FXML
    public void onAdminRemovePharmacyClick() {
        Pharmacy selectedPharmacy = adminPharmaciesListView.getSelectionModel().getSelectedItem();
        if (selectedPharmacy != null) {
            clinicManager.removePharmacy(selectedPharmacy);
            pharmaciesList.remove(selectedPharmacy);
            adminStatusLabel.setText("Pharmacy staff removed.");
        }
    }
    
    @FXML
    public void onRefreshReportsClick() {
        totalPatientsLabel.setText(String.valueOf(clinicManager.getTotalRegisteredPatients()));
        treatedPatientsLabel.setText(String.valueOf(clinicManager.getTotalTreatedPatients()));
        doctorWorkloadArea.setText(clinicManager.getDoctorWorkloadReport());
    }

    // Reception Actions
    @FXML
    public void onRegisterPatientClick() {
        String firstName = firstNameField.getText();
        String secondName = secondNameField.getText();
        String lastName = lastNameField.getText();
        String ageText = patientAgeField.getText();
        String gender = genderComboBox.getValue();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty() || gender == null || address.isEmpty() || phoneNumber.isEmpty()) {
            receptionStatusLabel.setText("Please fill all required patient details.");
            return;
        }
        
        if (firstName.length() <= 1) {
            receptionStatusLabel.setText("First Name must be more than one character.");
            return;
        }
        
        if (lastName.length() <= 1) {
            receptionStatusLabel.setText("Last Name must be more than one character.");
            return;
        }
        
        // Second name is optional, but if filled, it must be valid
        if (!secondName.isEmpty() && (secondName.length() <= 1)) {
            receptionStatusLabel.setText("Second Name must be more than one character.");
            return;
        }
        
        if (address.length() <= 1) {
            receptionStatusLabel.setText("Address must be more than one character.");
            return;
        }
        
        if (phoneNumber.length() != 13) {
            receptionStatusLabel.setText("Phone number must be in the format +2519XXXXXXXX.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0) {
                receptionStatusLabel.setText("Please enter a valid age.");
                return;
            }
            
            Patient patient = clinicManager.registerPatient(firstName, secondName, lastName, age, gender, address, phoneNumber);
            registeredPatientsList.add(patient);
            receptionStatusLabel.setText("Patient registered with Card No: " + patient.getId());
            
            // Clear fields
            firstNameField.clear();
            secondNameField.clear();
            lastNameField.clear();
            patientAgeField.clear();
            genderComboBox.getSelectionModel().clearSelection();
            addressField.clear();
            phoneNumberField.setText("+251");
            
        } catch (NumberFormatException e) {
            receptionStatusLabel.setText("Invalid age format.");
        }
    }
    
    @FXML
    public void onFindPatientClick() {
        String idOrPhone = patientIdField.getText();
        if (idOrPhone.isEmpty()) {
            receptionStatusLabel.setText("Please enter Patient Card Number or Phone Number.");
            return;
        }
        
        // Try finding by ID first
        foundPatient = clinicManager.findPatientById(idOrPhone);
        
        // If not found by ID, try finding by phone number
        if (foundPatient == null) {
            foundPatient = clinicManager.findPatientByPhoneNumber(idOrPhone);
        }
        
        if (foundPatient != null) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(foundPatient.getFullName()).append("\n");
            details.append("Card No: ").append(foundPatient.getId()).append("\n");
            details.append("Phone: ").append(foundPatient.getPhoneNumber()).append("\n");
            details.append("Last Medical History: ").append(foundPatient.getMedicalHistory() != null ? foundPatient.getMedicalHistory() : "N/A");
            foundPatientDetailsArea.setText(details.toString());
            sendToExaminerButton.setDisable(false);
            receptionStatusLabel.setText("Patient found. Review details and send to examiner.");
        } else {
            receptionStatusLabel.setText("Patient not found.");
            foundPatientDetailsArea.clear();
            sendToExaminerButton.setDisable(true);
            foundPatient = null;
        }
    }
    
    @FXML
    public void onSendToExaminerClick() {
        if (foundPatient != null) {
            if (!registeredPatientsList.contains(foundPatient)) {
                registeredPatientsList.add(foundPatient);
                receptionStatusLabel.setText("Patient " + foundPatient.getFullName() + " sent to examiner queue.");
            } else {
                receptionStatusLabel.setText("Patient is already in the queue.");
            }
            // Clear fields after sending
            patientIdField.clear();
            foundPatientDetailsArea.clear();
            sendToExaminerButton.setDisable(true);
            foundPatient = null;
        }
    }

    // Examiner Actions
    @FXML
    public void onSubmitVitalsClick() {
        Patient selectedPatient = examinerPatientComboBox.getValue();
        Doctor selectedDoctor = doctorComboBox.getValue();
        String weightText = weightField.getText();
        String bp = bpField.getText();
        String tempText = temperatureField.getText();
        String history = medicalHistoryField.getText();
        String pain = painDescriptionField.getText();

        if (selectedPatient == null || selectedDoctor == null || weightText.isEmpty() || bp.isEmpty() || tempText.isEmpty()) {
            examinerStatusLabel.setText("Please fill all required vitals.");
            return;
        }

        try {
            double weight = Double.parseDouble(weightText);
            double temp = Double.parseDouble(tempText);
            
            clinicManager.recordVitals(selectedPatient, weight, bp, temp, history, pain);
            clinicManager.assignDoctor(selectedPatient, selectedDoctor);
            
            // Remove from waiting list as they are now assigned
            registeredPatientsList.remove(selectedPatient);
            
            examinerStatusLabel.setText("Patient assigned to " + selectedDoctor.getName());
            weightField.clear();
            bpField.clear();
            temperatureField.clear();
            medicalHistoryField.clear();
            painDescriptionField.clear();
            examinerPatientComboBox.getSelectionModel().clearSelection();
            doctorComboBox.getSelectionModel().clearSelection();
            
            // Refresh doctor view if needed and select the new patient
            if (loggedInDoctor != null && loggedInDoctor.equals(selectedDoctor)) {
                updateDoctorView();
                doctorPatientsListView.getSelectionModel().select(selectedPatient);
            }
            
        } catch (NumberFormatException e) {
            examinerStatusLabel.setText("Invalid format for weight or temperature.");
        }
    }
    
    @FXML
    public void onCreateAppointmentClick() {
        Patient selectedPatient = examinerPatientComboBox.getValue();
        LocalDate date = appointmentDatePicker.getValue();
        String time = appointmentTimeField.getText();
        Doctor selectedDoctor = appointmentDoctorComboBox.getValue();

        if (selectedPatient == null || date == null || time.isEmpty() || selectedDoctor == null) {
            examinerStatusLabel.setText("Please select patient, date, time, and doctor.");
            return;
        }

        selectedPatient.setAppointmentDate(date);
        selectedPatient.setAppointmentTime(time);
        selectedPatient.setAssignedDoctor(selectedDoctor);
        examinerStatusLabel.setText("Appointment scheduled for " + selectedPatient.getFullName() + " with " + selectedDoctor.getName() + " on " + date + " at " + time);
        
        appointmentDatePicker.setValue(null);
        appointmentTimeField.clear();
        appointmentDoctorComboBox.getSelectionModel().clearSelection();
    }

    // Doctor Actions
    private void updateDoctorView() {
        if (loggedInDoctor != null) {
            ObservableList<Patient> assignedPatients = FXCollections.observableArrayList(loggedInDoctor.getAssignedPatients());
            doctorPatientsListView.setItems(assignedPatients);
            patientDetailsArea.clear();
            prescriptionArea.clear();
            
            doctorInfoLabel.setText(loggedInDoctor.getName() + " - " + loggedInDoctor.getSpecialty() + " | Hours: " + loggedInDoctor.getAvailableTime() + " | Time/Patient: " + loggedInDoctor.getTimePerPatient());
            
            // Update transfer list to exclude current doctor
            List<Doctor> otherDoctors = doctorsList.stream()
                    .filter(d -> !d.equals(loggedInDoctor) && d.isActive())
                    .collect(Collectors.toList());
            transferDoctorComboBox.setItems(FXCollections.observableArrayList(otherDoctors));
        }
    }

    @FXML
    public void onMarkTreatedClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            clinicManager.markPatientTreated(selectedPatient);
            doctorStatusLabel.setText("Patient " + selectedPatient.getFullName() + " marked as treated.");
            updateDoctorView(); // Refresh list
        } else {
            doctorStatusLabel.setText("Select a patient first.");
        }
    }

    @FXML
    public void onMarkAbsentClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            clinicManager.markPatientAbsent(selectedPatient);
            doctorStatusLabel.setText("Patient " + selectedPatient.getFullName() + " marked as absent.");
            updateDoctorView(); // Refresh list
        } else {
            doctorStatusLabel.setText("Select a patient first.");
        }
    }

    @FXML
    public void onRescheduleClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            clinicManager.reschedulePatient(selectedPatient);
            doctorStatusLabel.setText("Patient " + selectedPatient.getFullName() + " rescheduled to end of queue.");
            updateDoctorView(); // Refresh list
        } else {
            doctorStatusLabel.setText("Select a patient first.");
        }
    }

    @FXML
    public void onTransferClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        Doctor newDoctor = transferDoctorComboBox.getValue();
        String reason = transferReasonArea.getText();

        if (selectedPatient != null && newDoctor != null && !reason.isEmpty()) {
            clinicManager.transferPatient(selectedPatient, newDoctor);
            doctorStatusLabel.setText("Transferred " + selectedPatient.getFullName() + " to " + newDoctor.getName() + " for: " + reason);
            updateDoctorView(); // Refresh current list
            transferReasonArea.clear();
        } else {
            doctorStatusLabel.setText("Select patient, new doctor, and provide a reason.");
        }
    }
    
    @FXML
    public void onSendToLabClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            List<String> selectedTests = new ArrayList<>();
            TreeItem<LabTest> root = labTestTreeTableView.getRoot();
            if (root != null) {
                for (TreeItem<LabTest> categoryItem : root.getChildren()) {
                    for (TreeItem<LabTest> testItem : categoryItem.getChildren()) {
                        if (testItem.getValue().isSelected()) {
                            selectedTests.add(testItem.getValue().getTestName());
                        }
                    }
                }
            }
            
            if (selectedTests.isEmpty()) {
                doctorStatusLabel.setText("Please select at least one lab test.");
                return;
            }

            selectedPatient.setOrderedLabTests(selectedTests);
            clinicManager.sendPatientToLab(selectedPatient);
            laboratoryPatientsList.add(selectedPatient);
            doctorStatusLabel.setText("Patient " + selectedPatient.getFullName() + " sent to Laboratory.");
            updateDoctorView(); // Refresh list
        } else {
            doctorStatusLabel.setText("Select a patient first.");
        }
    }
    
    @FXML
    public void onSubmitLabResultsClick() {
        Patient selectedPatient = laboratoryPatientsListView.getSelectionModel().getSelectedItem();
        String results = labResultsField.getText();
        
        if (selectedPatient != null && !results.isEmpty()) {
            selectedPatient.setLabResults(results);
            clinicManager.returnPatientFromLab(selectedPatient); // Return to doctor
            laboratoryPatientsList.remove(selectedPatient);
            labResultsField.clear();
            labStatusLabel.setText("Results submitted. Patient returned to doctor.");
            
            // If the doctor is currently logged in, refresh their view
            if (loggedInDoctor != null && loggedInDoctor.equals(selectedPatient.getAssignedDoctor())) {
                updateDoctorView();
            }
        } else {
            labStatusLabel.setText("Select a patient and enter results.");
        }
    }
    
    @FXML
    public void onSendToPharmacyClick() {
        Patient selectedPatient = doctorPatientsListView.getSelectionModel().getSelectedItem();
        String prescription = prescriptionArea.getText();
        
        if (selectedPatient != null && !prescription.isEmpty()) {
            selectedPatient.setPrescription(prescription);
            selectedPatient.setInPharmacy(true);
            
            // Remove from doctor's list as they are done with the patient
            if (loggedInDoctor != null) {
                loggedInDoctor.removePatient(selectedPatient);
            }
            
            pharmacyPatientsList.add(selectedPatient);
            doctorStatusLabel.setText("Patient sent to Pharmacy.");
            updateDoctorView();
        } else {
            doctorStatusLabel.setText("Select a patient and enter prescription.");
        }
    }

    @FXML
    public void onGlobalPatientSearchClick() {
        String phoneNumber = globalSearchField.getText();
        if (phoneNumber.isEmpty()) {
            showAlert("Error", "Please enter a phone number to search.");
            return;
        }

        Patient patient = clinicManager.findPatientByPhoneNumber(phoneNumber);
        if (patient != null) {
            showPatientDetailsInDialog(patient);
        } else {
            showAlert("Not Found", "No patient found with the provided phone number.");
        }
    }

    private void showPatientDetailsInDialog(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Details");
        alert.setHeaderText("Details for " + patient.getFullName());

        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(patient.getFullName()).append("\n");
        details.append("Card No: ").append(patient.getId()).append("\n");
        details.append("Age: ").append(patient.getAge()).append("\n");
        details.append("Gender: ").append(patient.getGender()).append("\n");
        details.append("Address: ").append(patient.getAddress()).append("\n");
        details.append("Phone: ").append(patient.getPhoneNumber()).append("\n\n");
        details.append("--- Medical Vitals ---\n");
        details.append("Weight: ").append(patient.getWeight()).append(" kg\n");
        details.append("Blood Pressure: ").append(patient.getBloodPressure()).append("\n");
        details.append("Temperature: ").append(patient.getTemperature()).append(" °C\n");
        details.append("Medical History: ").append(patient.getMedicalHistory()).append("\n");
        details.append("Pain Description: ").append(patient.getPainDescription()).append("\n");

        if (patient.getOrderedLabTests() != null && !patient.getOrderedLabTests().isEmpty()) {
            details.append("\n--- Ordered Lab Tests ---\n");
            patient.getOrderedLabTests().forEach(test -> details.append("- ").append(test).append("\n"));
        }

        if (patient.getLabResults() != null) {
            details.append("\n--- Lab Results ---\n");
            details.append(patient.getLabResults()).append("\n");
        }

        if (patient.getPrescription() != null) {
            details.append("\n--- Prescription ---\n");
            details.append(patient.getPrescription()).append("\n");
        }

        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            // Show password
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            togglePasswordButton.setText("😉");
        } else {
            // Hide password
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            togglePasswordButton.setText("👁");
        }
    }
}
