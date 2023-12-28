package com.example.nova_marketproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;



public class Main_interface extends Application {

    private TextField searchField = new TextField();
    private Button searchButton = new Button("Search");

    private List<Product> productList;
    private TilePane productTilePane;
    private ImageView selectedProductImage;

    private ComboBox<String> sortingComboBox = new ComboBox<>();


    Database_of_users d1 = new Database_of_users();
    Font f1 = Font.loadFont("file:src/main/resources/inkfree.ttf", 30);
    Font hf1 = Font.loadFont("file:src/main/resources/inkfree.ttf", 29);
    Font f2 = Font.loadFont("file:src/main/resources/inkfree.ttf", 10);
    LinearGradient lgrad = new LinearGradient(
            0, 0, 1280, 720, false,                      //sizing
            CycleMethod.NO_CYCLE,                  //cycling
            new Stop(0, Color.web("#000000"/*"#81c483"*/)),     //colors
            new Stop(1, Color.web("#FF5733"/*"#fcc200")*/)
            ));

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent; -fx-border-color: white";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color:black; -fx-opacity: 50";

    private static final String DATA_FILE_PATH = "product_data.txt";

    VBox mainHolder = new VBox();
    ImageView logoImage = new ImageView("E:\\Coding files\\OOP_Project\\Logo\\logo1.png");

    Button signInButton = new Button("Sign In for Sellers");
    Button registerButton = new Button("Create an Account");

    HBox buttonHolder = new HBox(10);

    @Override
    public void start(Stage stage) throws IOException {
        logoImage.setFitHeight(200.0);
        logoImage.setFitWidth(200.0);
        logoImage.setTranslateX(20);
        logoImage.setTranslateY(2);
        KeyFrame startFadeOut6 = new KeyFrame(Duration.seconds(0), new KeyValue(logoImage.opacityProperty(), 0.0));
        KeyFrame endFadeOut7 = new KeyFrame(Duration.seconds(1.8), new KeyValue(logoImage.opacityProperty(), 1.0));

        signInButton.setTranslateX(450);
        signInButton.setTranslateY(20);
        signInButton.setTextFill(Color.WHITE);
        KeyFrame startFadeOut8 = new KeyFrame(Duration.seconds(0), new KeyValue(signInButton.opacityProperty(), 0.0));
        KeyFrame endFadeOut9 = new KeyFrame(Duration.seconds(1.8), new KeyValue(signInButton.opacityProperty(), 1.0));
        signInButton.setStyle(IDLE_BUTTON_STYLE);
        signInButton.setOnMouseEntered(e -> signInButton.setStyle(HOVERED_BUTTON_STYLE));
        signInButton.setOnMouseExited(e -> signInButton.setStyle(IDLE_BUTTON_STYLE));
        signInButton.setOnAction(event -> {
            Login login = new Login();
            login.start();
            stage.close();
        });


        registerButton.setTranslateX(450);
        registerButton.setTranslateY(20);
        registerButton.setTextFill(Color.WHITE);
        KeyFrame startFadeOut7 = new KeyFrame(Duration.seconds(0), new KeyValue(registerButton.opacityProperty(), 0.0));
        KeyFrame endFadeOut8 = new KeyFrame(Duration.seconds(1.8), new KeyValue(registerButton.opacityProperty(), 1.0));
        registerButton.setStyle(IDLE_BUTTON_STYLE);
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(HOVERED_BUTTON_STYLE));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(IDLE_BUTTON_STYLE));
        registerButton.setOnAction(event -> {
            Registration registration = new Registration();
            registration.registration_Start();
            stage.close();
        });

        sortingComboBox.getItems().addAll("Sort by Newest","Sort by Name", "Sort by Price");
        sortingComboBox.setValue("Sort by Newest"); // Default sorting option
        sortingComboBox.setTranslateX(100);
        sortingComboBox.setTranslateY(20);
        sortingComboBox.setOnAction(event -> {
            String selectedSortOption = sortingComboBox.getValue();
            if ("Sort by Name".equals(selectedSortOption)) {
                mergeSort(productList, Comparator.comparing(Product::getName));
            } else if ("Sort by Price".equals(selectedSortOption)) {
                insertionSort(productList, Comparator.comparingDouble(product -> Double.parseDouble(product.getPrice())));
            }
            else if ("Sort by Newest".equals(selectedSortOption)) {
                productList=loadProductData();
                updateProductTiles();
            }

            updateProductTiles();
        });


        mainHolder.setBackground(new Background(new BackgroundFill(lgrad, null, null)));

        productList = loadProductData();

        productTilePane = new TilePane();
        productTilePane.setPrefColumns(4);
        productTilePane.setHgap(20);
        productTilePane.setVgap(20);

        for (Product product : productList) {
            productTilePane.getChildren().add(createProductTile(product));
        }

        HBox buyBox = new HBox(10);
        Button cartButton = new Button("View Cart");
        cartButton.setTextFill(Color.WHITE);
        KeyFrame startFadeOut11 = new KeyFrame(Duration.seconds(0), new KeyValue(cartButton.opacityProperty(), 0.0));
        KeyFrame endFadeOut12 = new KeyFrame(Duration.seconds(1.8), new KeyValue(cartButton.opacityProperty(), 1.0));
        cartButton.setStyle(IDLE_BUTTON_STYLE);
        cartButton.setOnMouseEntered(e -> cartButton.setStyle(HOVERED_BUTTON_STYLE));
        cartButton.setOnMouseExited(e -> cartButton.setStyle(IDLE_BUTTON_STYLE));
        cartButton.setTranslateY(100);
        cartButton.setTranslateX(300);
        cartButton.setOnAction(e -> {
            try {
                Cart_System c1 = new Cart_System();
                c1.start4();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        searchField.setPromptText("Enter product name");
        searchButton.setOnAction(event -> searchProduct(searchField.getText()));

        searchButton.setTextFill(Color.WHITE);
        KeyFrame startFadeOut13 = new KeyFrame(Duration.seconds(0), new KeyValue(searchButton.opacityProperty(), 0.0));
        KeyFrame endFadeOut14 = new KeyFrame(Duration.seconds(1.8), new KeyValue(searchButton.opacityProperty(), 1.0));
        searchButton.setStyle(IDLE_BUTTON_STYLE);
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(HOVERED_BUTTON_STYLE));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(IDLE_BUTTON_STYLE));
        searchButton.setTranslateY(20);
        searchField.setTranslateY(20);
        searchField.setTranslateX(100);
        searchButton.setTranslateX(100);

        buttonHolder.getChildren().addAll(logoImage,searchField,searchButton, sortingComboBox,  signInButton, registerButton, cartButton);


        mainHolder.getChildren().addAll(buttonHolder, productTilePane);

        Scene logged = new Scene(mainHolder, 1280, 720);
        stage.setTitle("Nova Marketplace - Seller Dashboard");
        stage.setScene(logged);
        stage.setMaximized(true);
        stage.show();
        Timeline timelineOn = new Timeline(startFadeOut11,endFadeOut12,startFadeOut6, endFadeOut7, startFadeOut7, startFadeOut8, endFadeOut9, endFadeOut8);
        timelineOn.play();
    }


    private Button createProductTile(Product product) {
        Button productButton = new Button();
        productButton.setGraphic(createProductGraphic(product));
        productButton.setStyle("-fx-background-color: transparent; -fx-border-color: white");
        productButton.setOnMouseEntered(e -> productButton.setStyle("-fx-background-color:black; -fx-opacity: 50"));
        productButton.setOnMouseExited(e -> productButton.setStyle("-fx-background-color: transparent; -fx-border-color: white"));
        productButton.setOnAction(event -> displayProductDetails(product));

        return productButton;
    }

    private VBox createProductGraphic(Product product) {
        VBox productGraphic = new VBox();
        productGraphic.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView productImage = new ImageView(new Image("file:" + product.getImagePath()));
        productImage.setFitWidth(150);
        productImage.setFitHeight(150);

        Label productNameLabel = new Label(product.getName());
        productNameLabel.setFont(Font.loadFont("file:src/main/resources/inkfree.ttf", 14));
        productNameLabel.setTextFill(Color.WHITE);

        Label productPriceLabel = new Label("$" + product.getPrice());
        productPriceLabel.setFont(Font.loadFont("file:src/main/resources/inkfree.ttf", 12));
        productPriceLabel.setTextFill(Color.WHITE);

        productGraphic.getChildren().addAll(productImage, productNameLabel, productPriceLabel);

        return productGraphic;
    }

    private void searchProduct(String productName) {
        List<Product> temp = productList;
        temp.sort(Comparator.comparing(Product::getName));


        int index = binarySearch(productName, temp);

        // Check if the index is valid and if the product at that index has the correct name
        if (index >= 0 && index < temp.size() && temp.get(index).getName().equals(productName)) {
            displayProductDetails(temp.get(index));
        } else {
            // Product not found
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Product not found.");
        }
        }


    private int binarySearch(String productName, List<Product> temp) {
        int low = 0;
        int high = temp.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Product midProduct = temp.get(mid);
            int comparisonResult = midProduct.getName().compareTo(productName);

            if (comparisonResult == 0) {
                return mid;
            } else if (comparisonResult < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -(low + 1);
    }

        private void updateProductTiles() {
            productTilePane.getChildren().clear();
            for (Product product : productList) {
                productTilePane.getChildren().add(createProductTile(product));
            }
        }

        private void mergeSort(List<Product> list, Comparator<Product> comparator) {
            if (list.size() > 1) {
                int mid = list.size() / 2;
                List<Product> left = new ArrayList<>(list.subList(0, mid));
                List<Product> right = new ArrayList<>(list.subList(mid, list.size()));

                mergeSort(left, comparator);
                mergeSort(right, comparator);

                merge(list, left, right, comparator);
            }
        }

        private void merge(List<Product> list, List<Product> left, List<Product> right, Comparator<Product> comparator) {
            int i = 0, j = 0, k = 0;

            while (i < left.size() && j < right.size()) {
                if (comparator.compare(left.get(i), right.get(j)) < 0) {
                    list.set(k++, left.get(i++));
                } else {
                    list.set(k++, right.get(j++));
                }
            }

            while (i < left.size()) {
                list.set(k++, left.get(i++));
            }

            while (j < right.size()) {
                list.set(k++, right.get(j++));
            }
        }
        private void insertionSort(List<Product> list, Comparator<Product> comparator) {
            for (int i = 1; i < list.size(); i++) {
                Product key = list.get(i);
                int j = i - 1;

                while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                    list.set(j + 1, list.get(j));
                    j--;
                }

                list.set(j + 1, key);
            }
        }


    private void displayProductDetails(Product product) {
        Dialog<Void> detailsDialog = new Dialog<>();
        Window window = detailsDialog.getDialogPane().getScene().getWindow();
        detailsDialog.setTitle("Product Details");

        VBox detailsVBox = new VBox(10);

        Label nameLabel = new Label("Name: " + product.getName());
        Label priceLabel = new Label("Price: $" + product.getPrice());
        Label sellerLabel = new Label("Seller: " + product.getSeller());

        ImageView productImage = new ImageView(new Image("file:" + product.getImagePath()));
        productImage.setFitWidth(200);
        productImage.setFitHeight(200);
        window.setOnCloseRequest(event -> window.hide());

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(event -> {
            try {
                detailsDialog.close();
                window.hide();
                buyProduct(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        detailsVBox.getChildren().addAll(nameLabel, priceLabel, sellerLabel, productImage, addToCartButton);
        detailsDialog.getDialogPane().setContent(detailsVBox);
        detailsDialog.showAndWait();
    }

    private void buyProduct(Product selectedProduct) throws IOException {
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a product to buy.");
            return;
        }

        FileWriter myWriter2 = new FileWriter("Cartitems.txt", true);
        myWriter2.write("\n" + selectedProduct.getName() + "," + selectedProduct.getPrice() + "," + selectedProduct.getSeller() + "," + selectedProduct.getImagePath());
        myWriter2.close();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Product added to cart successfully!");
    }

    private List<Product> loadProductData() {
        List<Product> products = new ArrayList<>();
        Stack<Product> pd1 = new Stack<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String price = parts[1];
                    String seller = parts[2];
                    String imagePath = parts[3];
                    pd1.push(new Product(name, price, seller, new File(imagePath)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!pd1.isEmpty())
        {
            products.add(pd1.pop());
        }
        return products;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }
}

